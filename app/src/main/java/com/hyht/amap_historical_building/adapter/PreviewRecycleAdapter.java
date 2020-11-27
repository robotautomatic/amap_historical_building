package com.hyht.amap_historical_building.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.entity.ImageViewInfo;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.xuexiang.xui.widget.toast.XToast;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

public class PreviewRecycleAdapter extends RecyclerView.Adapter<PreviewRecycleAdapter.ViewHolder>{
    private Context context;
    private List<Uri> uriList;
    private List<ImageViewInfo> previewInfos;
    private Map<Integer, Boolean> successUrl;
    private OnAdapterItemClickListener viewClickListener;

    public void setViewClickListener(OnAdapterItemClickListener viewClickListener) {
        this.viewClickListener = viewClickListener;
    }

    public PreviewRecycleAdapter(Context context, List<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
        previewInfos = new ArrayList<>();
        successUrl = new HashMap<>();

        viewClickListener = new OnAdapterItemClickListener() {
            @Override
            public View.OnClickListener onItemClick(int i) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previewInfos.clear();
                        ImageView imageView = (ImageView) v;
                        for (int j = 0; j < uriList.size(); j++) {
                            if (successUrl.getOrDefault(j,false) == true){
                                ImageViewInfo imageViewInfo = new ImageViewInfo(uriList.get(j).toString());

                                Rect rect = new Rect();
                                imageView.getGlobalVisibleRect(rect);
                                int[] dialogLocation = new int[2];
                                imageView.getLocationOnScreen(dialogLocation);
                                rect.set(dialogLocation[0], dialogLocation[1], rect.right+dialogLocation[0]-rect.left, rect.bottom+dialogLocation[1]-rect.top);
                                imageViewInfo.setBounds(rect);

                                previewInfos.add(imageViewInfo);
                            }
                        }


                        PreviewBuilder.from((Activity) context)
                                .setImgs(previewInfos)
                                .setCurrentIndex(i)
                                .setSingleFling(true)
                                .setType(PreviewBuilder.IndicatorType.Number)
                                .start();
                    }
                };
            }
        };
    }

    @NonNull
    @Override
    public PreviewRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draw_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewRecycleAdapter.ViewHolder holder, int position) {
        holder.llDelete.setVisibility(View.INVISIBLE);
        holder.progressLoadingDraw.setVisibility(View.VISIBLE);

        RequestBuilder<Drawable> thumbnailRequest = Glide
                .with(context)
                .load(uriList.get(position)+"_small.png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        XToast.normal(context,"原图加载中").show();
                        return false;
                    }
                });

        ProgressManager.getInstance().addResponseListener(uriList.get(position).toString(), new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
//Todo 加上图片加载的进度显示
                holder.progressLoadingDraw.setProgress(progressInfo.getPercent());
            }

            @Override
            public void onError(long id, Exception e) {
                holder.progressLoadingDraw.setVisibility(View.GONE);
                System.out.println("progress error");
            }
        });

//.signature()
        Glide.with(context).load(uriList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.picture_icon_no_data)
                .error(R.drawable.picture_icon_data_error)
                .thumbnail(thumbnailRequest)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.ivSelectPic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                XToast.normal(context,"原图加载失败").show();
                            }
                        });
                        holder.progressLoadingDraw.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        successUrl.put(position, true);
                        System.out.println("load "+ position);
                        holder.ivSelectPic.setOnClickListener(viewClickListener.onItemClick(position));
                        holder.progressLoadingDraw.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivSelectPic);
        /*holder.ivSelectPic.setOnClickListener(viewClickListener.onItemClick(position));*/
    }

    public interface OnAdapterItemClickListener {
        View.OnClickListener onItemClick(int i);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelectPic;
        LinearLayout llDelete;
        HorizontalProgressView progressLoadingDraw;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelectPic = itemView.findViewById(R.id.iv_select_pic);
            ivSelectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XToast.normal(context,"图片加载中").show();
                }
            });
            llDelete = itemView.findViewById(R.id.ll_delete);
            progressLoadingDraw = itemView.findViewById(R.id.progress_loading_draw);
            progressLoadingDraw.setVisibility(View.GONE);
        }
    }
}
