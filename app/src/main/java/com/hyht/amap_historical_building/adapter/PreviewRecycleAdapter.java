package com.hyht.amap_historical_building.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.entity.ImageViewInfo;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;

import java.util.ArrayList;
import java.util.List;

public class PreviewRecycleAdapter extends RecyclerView.Adapter<PreviewRecycleAdapter.ViewHolder>{
    private Context context;
    private List<Uri> uriList;
    private List<ImageViewInfo> previewInfos;

    public PreviewRecycleAdapter(Context context, List<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
        previewInfos = new ArrayList<>();
        for (Uri uri : uriList
             ) {
            ImageViewInfo imageViewInfo = new ImageViewInfo(uri.toString());
            previewInfos.add(imageViewInfo);
        }
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
        Glide.with(context).load(uriList.get(position)).into(holder.ivSelectPic);
        holder.ivSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewBuilder.from((Activity) context)
                        .setImgs(previewInfos)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(PreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSelectPic;
        LinearLayout llDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSelectPic = itemView.findViewById(R.id.iv_select_pic);
            llDelete = itemView.findViewById(R.id.ll_delete);
        }
    }
}
