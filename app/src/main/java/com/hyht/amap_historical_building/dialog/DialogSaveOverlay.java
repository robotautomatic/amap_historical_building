package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amap.api.maps.model.LatLng;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.adapter.DrawRecyclerAdapter;
import com.hyht.amap_historical_building.callback.SingleButtonCallbackSaveOrUpdate;
import com.hyht.amap_historical_building.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogSaveOverlay {
    private Context context;
    private List<LatLng> latLngList = new ArrayList<>();

    public DialogSaveOverlay(Context context, List<LatLng> latLngList) {
        this.context = context;
        this.latLngList = latLngList;
        getDialog();
    }

    public DialogSaveOverlay(Context context, LatLng latLng) {
        this.context = context;
        latLngList.add(latLng);
        getDialog();
    }

    private MaterialDialog getDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_save_overlay, true)
                .iconRes(R.drawable.ic_save)
                .title("建筑物信息保存")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new SingleButtonCallbackSaveOrUpdate(SingleButtonCallbackSaveOrUpdate.Type.SAVE))
                .show();

        String coordinates = "";
        EditText edit_position_coordinates = materialDialog.getCustomView().findViewById(R.id.edit_position_coordinates);
        for (LatLng latLng : latLngList
        ) {
            coordinates = coordinates + latLng.latitude + "," + latLng.longitude + "/";
        }
        edit_position_coordinates.setText(coordinates);

        RecyclerView recyclerView = materialDialog.getCustomView().findViewById(R.id.rv_draw);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<String> stringList = new ArrayList<>();
        stringList.add("111");
        stringList.add("2222");
        System.out.println("adddddddddddddd");
        DrawRecyclerAdapter adapter = new DrawRecyclerAdapter(stringList);
        recyclerView.setAdapter(adapter);

/*        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        System.out.println("pic ====="+ result);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });*/

        return materialDialog;
    }
}
