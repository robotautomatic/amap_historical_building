package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.adapter.DrawRecyclerAdapter;
import com.hyht.amap_historical_building.adapter.SelectDrawRecyclerAdapter;
import com.hyht.amap_historical_building.callback.SingleButtonCallbackSaveOrUpdate;
import com.hyht.amap_historical_building.entity.TDraw;
import com.hyht.amap_historical_building.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogSaveOverlay {
    private Context context;
    private List<LatLng> latLngList = new ArrayList<>();
    private AMap aMap;

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
        List<LocalMedia> drawMediaList = new ArrayList<>();
        List<LocalMedia> imageMediaList = new ArrayList<>();
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_save_overlay, true)
                .iconRes(R.drawable.ic_save)
                .title("建筑物信息保存")
                .positiveText("确认")
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        System.out.println(drawMediaList);
                    }
                })
                .onPositive(new SingleButtonCallbackSaveOrUpdate(context, aMap, drawMediaList, imageMediaList))
                .show();

        String coordinates = "";
        EditText edit_position_coordinates = materialDialog.getCustomView().findViewById(R.id.edit_position_coordinates);
        for (LatLng latLng : latLngList
        ) {
            coordinates = coordinates + latLng.latitude + "," + latLng.longitude + "/";
        }
        edit_position_coordinates.setText(coordinates);

        RecyclerView drawRecyclerView = materialDialog.getCustomView().findViewById(R.id.rv_draw);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        drawRecyclerView.setLayoutManager(linearLayoutManager);
        SelectDrawRecyclerAdapter drawAdapter = new SelectDrawRecyclerAdapter(drawMediaList, context);
        drawRecyclerView.setAdapter(drawAdapter);

        RecyclerView imageRecyclerView = materialDialog.getCustomView().findViewById(R.id.rv_image);
        LinearLayoutManager imageLinearLayoutManager = new LinearLayoutManager(context);
        imageLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(imageLinearLayoutManager);
        SelectDrawRecyclerAdapter imageAdapter = new SelectDrawRecyclerAdapter(imageMediaList, context);
        imageRecyclerView.setAdapter(imageAdapter);

        return materialDialog;
    }
}
