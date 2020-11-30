package com.hyht.amap_historical_building.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.entity.TDraw;
import com.hyht.amap_historical_building.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.List;

public class SelectDrawRecyclerAdapter extends RecyclerView.Adapter<SelectDrawRecyclerAdapter.ViewHolder> {
    private List<LocalMedia> localMediaList;
    private Context context;

    public SelectDrawRecyclerAdapter(List<LocalMedia> localMediaList, Context context) {
        this.localMediaList = localMediaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draw_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.ivSelectPic.setImageResource(R.drawable.ic_add_image);
            holder.llDelete.setVisibility(View.INVISIBLE);
            holder.ivSelectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create((Activity) context)
                            .openGallery(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(99)
                            .selectionData(localMediaList)
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    // 结果回调
                                    localMediaList.clear();
                                    localMediaList.addAll(result);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancel() {
                                    // 取消
                                }
                            });
                }
            });
        } else {
            holder.llDelete.setVisibility(View.VISIBLE);
            holder.ivSelectPic.setImageURI(Uri.parse(localMediaList.get(position - 1).getRealPath()));
            holder.ivSelectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSelector.create((Activity) context).openGallery(PictureMimeType.ofImage()).openExternalPreview(position - 1, localMediaList);
                }
            });
            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = holder.getAdapterPosition();
                    if (index != RecyclerView.NO_POSITION) {
                        localMediaList.remove(index - 1);
                        notifyItemRemoved(index);
                        notifyItemRangeChanged(index, localMediaList.size());
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return localMediaList.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelectPic;
        LinearLayout llDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelectPic = itemView.findViewById(R.id.iv_select_pic);
            llDelete = itemView.findViewById(R.id.ll_delete);
        }
    }

}
