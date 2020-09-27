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
import android.widget.TextView;
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

public class DrawRecyclerAdapter extends RecyclerView.Adapter<DrawRecyclerAdapter.ViewHolder> {
    private List<TDraw> drawList;
    private Context context;

    public DrawRecyclerAdapter(List<TDraw> drawList, Context context) {
        this.drawList = drawList;
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
        ImageView delete = holder.itemView.findViewById(R.id.iv_delete);
        ImageView select = holder.itemView.findViewById(R.id.iv_select_pic);
        if (position == 0) {
            delete.setVisibility(View.INVISIBLE);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TDraw tDraw = new TDraw();
                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .customView(R.layout.dialog_save_draw, true)
                            .positiveText("确认")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    View view = dialog.getCustomView();
                                    EditText fileName = view.findViewById(R.id.edit_file_name);
                                    EditText drawProportion = view.findViewById(R.id.edit_draw_proportion);
                                    EditText drawName = view.findViewById(R.id.edit_draw_name);
                                    EditText drawDate = view.findViewById(R.id.edit_draw_date);
                                    tDraw.setFileName(String.valueOf(fileName.getText()));
                                    tDraw.setDrawProportion(String.valueOf(drawProportion.getText()));
                                    tDraw.setDrawName(String.valueOf(drawName.getText()));
                                    tDraw.setDrawDate(String.valueOf(drawDate.getText()));
                                    drawList.add(tDraw);
                                    notifyDataSetChanged();
                                }
                            })
                            .negativeText("取消")
                            .show();

                    View view = materialDialog.getCustomView();
                    ImageView getDraw = view.findViewById(R.id.iv_get_draw);
                    getDraw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PictureSelector.create((Activity) context)
                                    .openGallery(PictureMimeType.ofImage())
                                    .loadImageEngine(GlideEngine.createGlideEngine())
                                    .selectionMode(PictureConfig.SINGLE)
                                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                                        @Override
                                        public void onResult(List<LocalMedia> result) {
                                            // 结果回调
                                            tDraw.setDrawPath(result.get(0).getAndroidQToPath());
                                            getDraw.setImageURI(Uri.parse(tDraw.getDrawPath()));
                                        }

                                        @Override
                                        public void onCancel() {
                                            // 取消
                                        }
                                    });
                        }
                    });
                }
            });
        } else {
            TDraw tDraw = drawList.get(position - 1);
            ImageView imageView = select.findViewById(R.id.iv_select_pic);
            imageView.setImageURI(Uri.parse(tDraw.getDrawPath()));
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                            .customView(R.layout.dialog_save_draw, true)
                            .positiveText("确认")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    View view = dialog.getCustomView();
                                    EditText fileName = view.findViewById(R.id.edit_file_name);
                                    EditText drawProportion = view.findViewById(R.id.edit_draw_proportion);
                                    EditText drawName = view.findViewById(R.id.edit_draw_name);
                                    EditText drawDate = view.findViewById(R.id.edit_draw_date);
                                    tDraw.setFileName(String.valueOf(fileName.getText()));
                                    tDraw.setDrawProportion(String.valueOf(drawProportion.getText()));
                                    tDraw.setDrawName(String.valueOf(drawName.getText()));
                                    tDraw.setDrawDate(String.valueOf(drawDate.getText()));
                                    imageView.setImageURI(Uri.parse(tDraw.getDrawPath()));
                                }
                            })
                            .negativeText("取消")
                            .show();

                    ImageView getDraw = materialDialog.getCustomView().findViewById(R.id.iv_get_draw);
                    getDraw.setImageURI(Uri.parse(tDraw.getDrawPath()));

                    EditText fileName = materialDialog.getCustomView().findViewById(R.id.edit_file_name);
                    EditText drawProportion = materialDialog.getCustomView().findViewById(R.id.edit_draw_proportion);
                    EditText drawName = materialDialog.getCustomView().findViewById(R.id.edit_draw_name);
                    EditText drawDate = materialDialog.getCustomView().findViewById(R.id.edit_draw_date);
                    fileName.setText(tDraw.getFileName());
                    drawProportion.setText(tDraw.getDrawProportion());
                    drawName.setText(tDraw.getDrawName());
                    drawDate.setText(tDraw.getDrawDate());


                    getDraw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PictureSelector.create((Activity) context)
                                    .openGallery(PictureMimeType.ofImage())
                                    .loadImageEngine(GlideEngine.createGlideEngine())
                                    .selectionMode(PictureConfig.SINGLE)
                                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                                        @Override
                                        public void onResult(List<LocalMedia> result) {
                                            // 结果回调
                                            ImageView view = (ImageView) v;
                                            view.setImageURI(Uri.parse(result.get(0).getAndroidQToPath()));
                                            tDraw.setDrawPath(result.get(0).getAndroidQToPath());
                                        }

                                        @Override
                                        public void onCancel() {
                                            // 取消
                                        }
                                    });
                        }
                    });

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return drawList.size() + 1;
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
