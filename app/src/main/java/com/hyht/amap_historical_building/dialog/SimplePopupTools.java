package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.*;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnColumnItemClickListener;
import com.hyht.amap_historical_building.listener.OnInFoWindowClickListenerShowDetail;
import com.hyht.amap_historical_building.utils.DefaultButton;
import com.hyht.amap_historical_building.utils.EntityToOverlay;
import com.hyht.amap_historical_building.utils.GetCoordinateUtil;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xui.widget.toast.XToast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xui.XUI.getContext;

public class SimplePopupTools {
    XUISimplePopup mListPopup;
    Context context;
    AMap aMap;

    public SimplePopupTools(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    private void GenerateSimplePopupShow(Context context, AMap aMap) {
        mListPopup = new XUISimplePopup(context, new String[]{
                "测距", "测面积", "范围统计", "模糊统计",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        /*aMap.clear(true);*/
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);
                        aMap.setOnMapClickListener(null);
                        Activity activity = (Activity) context;
                        LinearLayout linearLayout = activity.findViewById(R.id.linear_bt);
                        while (linearLayout.getChildCount() > 6) {
                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                        }
                        switch (position) {
                            case 0: {
                                List<LatLng> latLngs = new ArrayList<>();
                                List<Marker> markers = new ArrayList<>();
                                PolylineOptions polylineOptions = new PolylineOptions();
                                Polyline polyline = aMap.addPolyline(polylineOptions);
                                polyline.setWidth(5);
                                Button btn_exit = new DefaultButton(context).getDefaultButton();
                                btn_exit.setText("退出");


                                AMap.OnMapClickListener mapClickListener_distance = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker;
                                        if (markers.size() > 0){
                                            View view = View.inflate(context, R.layout.custom_marker, null);
                                            TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                            BigDecimal distance = new BigDecimal(AMapUtils.calculateLineDistance(latLngs.get(latLngs.size() - 1),latLng));
                                            distance = distance.add((BigDecimal) markers.get(markers.size()-1).getObject());
                                            distance = distance.setScale(1,2);
                                            textViewCustomMarker.setText(distance + "米");
                                            marker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                    .anchor(0.5f,0.6f)
                                                    .icon(BitmapDescriptorFactory.fromView(view))
                                                    .title("点击删除").snippet(distance + "米"));
                                            marker.setObject(distance);
                                        }else {
                                            View view = View.inflate(context, R.layout.custom_marker, null);
                                            TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                            textViewCustomMarker.setText("起点");
                                            marker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                    .anchor(0.5f,0.6f)
                                                    .icon(BitmapDescriptorFactory.fromView(view))
                                                    .title("点击删除").snippet("起点"));
                                            marker.setObject(new BigDecimal(0));
                                        }
                                        marker.showInfoWindow();
                                        markers.add(marker);
                                        latLngs.add(latLng);
                                        polyline.setPoints(latLngs);
                                    }
                                };
                                aMap.setOnMapClickListener(mapClickListener_distance);
                                aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        if (markers.contains(marker)) {
                                            int markerIndex = markers.indexOf(marker);
                                            markers.remove(marker);
                                            latLngs.remove(marker.getPosition());
                                            polyline.setPoints(latLngs);
                                            marker.destroy();
                                            for (int i = markerIndex; i < markers.size(); i++) {
                                                if (i == 0) {
                                                    View view = View.inflate(context, R.layout.custom_marker, null);
                                                    TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                                    textViewCustomMarker.setText("起点");
                                                    markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view));
                                                    markers.get(i).setSnippet("起点");
                                                    markers.get(i).setObject(new BigDecimal(0));
                                                } else {
                                                    View view = View.inflate(context, R.layout.custom_marker, null);
                                                    TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                                    BigDecimal distance = new BigDecimal(AMapUtils.calculateLineDistance(markers.get(i).getPosition(), markers.get(i - 1).getPosition()));
                                                    distance = distance.add((BigDecimal) markers.get(i - 1).getObject());
                                                    distance = distance.setScale(1, 2);
                                                    textViewCustomMarker.setText(distance + "米");
                                                    markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view));
                                                    markers.get(i).setSnippet(distance + "米");
                                                    markers.get(i).setObject(distance);
                                                }
                                            }
                                        }
                                    }
                                });
                                aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        if (markers.contains(marker)){
                                            marker.showInfoWindow();
                                        }
                                        else {
                                            Marker newMarker;
                                            LatLng latLng = marker.getPosition();
                                            if (markers.size() > 0){
                                                View view = View.inflate(context, R.layout.custom_marker, null);
                                                TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                                BigDecimal distance = new BigDecimal(AMapUtils.calculateLineDistance(latLngs.get(latLngs.size() - 1),latLng));
                                                distance = distance.add((BigDecimal) markers.get(markers.size()-1).getObject());
                                                distance = distance.setScale(1,2);
                                                textViewCustomMarker.setText(distance + "米");
                                                newMarker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                        .anchor(0.5f,0.6f)
                                                        .icon(BitmapDescriptorFactory.fromView(view))
                                                        .title("点击删除").snippet(distance + "米"));
                                                newMarker.setObject(distance);
                                            }else {
                                                View view = View.inflate(context, R.layout.custom_marker, null);
                                                TextView textViewCustomMarker1 = view.findViewById(R.id.tv_custom_marker);
                                                newMarker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                        .anchor(0.5f,0.6f)
                                                        .icon(BitmapDescriptorFactory.fromView(view))
                                                        .title("点击删除").snippet("起点"));
                                                newMarker.setObject(new BigDecimal(0));
                                            }
                                            newMarker.showInfoWindow();
                                            markers.add(newMarker);
                                            latLngs.add(latLng);
                                            polyline.setPoints(latLngs);
                                        }
                                        return true;
                                    }
                                });

                                Button btn_rollback = new DefaultButton(context).getDefaultButton();
                                btn_rollback.setText("回退");
                                linearLayout.addView(btn_rollback);
                                btn_rollback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (markers.size() > 0 && latLngs.size() > 0) {
                                            markers.get(latLngs.size() - 1).destroy();
                                            markers.remove(latLngs.size() - 1);
                                            latLngs.remove(latLngs.size() - 1);
                                            polyline.setPoints(latLngs);
                                            if (markers.size()>0)
                                                markers.get(markers.size()-1).showInfoWindow();
                                        }
                                    }
                                });

                                Button btn_clear = new DefaultButton(context).getDefaultButton();
                                btn_clear.setText("清空 ");
                                linearLayout.addView(btn_clear);
                                btn_clear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Marker marker : markers
                                        ) {
                                            marker.destroy();
                                        }
                                        markers.clear();
                                        latLngs.clear();
                                        polyline.setPoints(latLngs);
                                        aMap.runOnDrawFrame();
                                    }
                                });
                                linearLayout.addView(btn_exit);
                                btn_exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.clear(true);
                                        aMap.removeOnMapClickListener(mapClickListener_distance);
                                        linearLayout.removeView(btn_exit);
                                        linearLayout.removeView(btn_clear);
                                        linearLayout.removeView(btn_rollback);
                                    }
                                });
                            }
                            break;
                            case 1: {
                                PolygonOptions polygonOptions = new PolygonOptions();
                                polygonOptions.strokeWidth(5) // 多边形的边框
                                        .strokeColor(0xAA000000) // 边框颜色
                                        .fillColor(0xAAFF0000);   // 多边形的填充色
                                Polygon polygonDraw = aMap.addPolygon(polygonOptions);

                                List<LatLng> latLngs = new ArrayList<>();
                                List<Marker> markers = new ArrayList<>();
                                Button btn_exit = new DefaultButton(context).getDefaultButton();
                                btn_exit.setText("退出");

                                AMap.OnMapClickListener mapClickListener_distance = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker;
                                        latLngs.add(latLng);
                                        double area = AMapUtils.calculateArea(latLngs);
                                        BigDecimal polygonArea = new BigDecimal(area);
                                        polygonArea = polygonArea.setScale(1,2);
                                        View view = View.inflate(context, R.layout.custom_marker, null);
                                        TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                        textViewCustomMarker.setText(polygonArea + "平方米");
                                        marker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                .anchor(0.5f,0.6f)
                                                .icon(BitmapDescriptorFactory.fromView(view))
                                                .title("点击删除").snippet(polygonArea + "平方米"));
                                        marker.showInfoWindow();
                                        marker.setObject(area);
                                        for (int i = 0; i < markers.size(); i++) {
                                            View view1 = View.inflate(context, R.layout.custom_marker, null);
                                            TextView textViewCustomMarker1 = view.findViewById(R.id.tv_custom_marker);
                                            textViewCustomMarker.setText(polygonArea + "平方米");
                                            markers.get(i).setObject(area);
                                            markers.get(i).setSnippet(polygonArea + "平方米");
                                            markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view1));
                                        }
                                        markers.add(marker);
                                        polygonDraw.setPoints(latLngs);
                                        polygonDraw.setVisible(true);
                                    }
                                };
                                aMap.setOnMapClickListener(mapClickListener_distance);
                                aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        if (markers.contains(marker)) {
                                            markers.remove(marker);
                                            latLngs.remove(marker.getPosition());
                                            marker.destroy();
                                            double area = AMapUtils.calculateArea(latLngs);
                                            BigDecimal polygonArea = new BigDecimal(area);
                                            polygonArea = polygonArea.setScale(1, 2);
                                            for (int i = 0; i < markers.size(); i++) {
                                                markers.get(i).setObject(area);
                                                markers.get(i).setSnippet(polygonArea + "平方米");
                                                View view = View.inflate(context, R.layout.custom_marker, null);
                                                TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                                textViewCustomMarker.setText(polygonArea + "平方米");
                                                markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view));
                                            }
                                            polygonDraw.setPoints(latLngs);
                                        }
                                    }
                                });

                                aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        if (markers.contains(marker)){
                                            marker.showInfoWindow();
                                        }
                                        else {
                                            Marker newMarker;
                                            LatLng latLng = marker.getPosition();
                                            latLngs.add(latLng);
                                            double area = AMapUtils.calculateArea(latLngs);
                                            BigDecimal polygonArea = new BigDecimal(area);
                                            polygonArea = polygonArea.setScale(1,2);
                                            View view = View.inflate(context, R.layout.custom_marker, null);
                                            TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                            textViewCustomMarker.setText(polygonArea + "平方米");
                                            newMarker = aMap.addMarker(new MarkerOptions().position(latLng)
                                                    .anchor(0.5f,0.6f)
                                                    .icon(BitmapDescriptorFactory.fromView(view))
                                                    .title("点击删除").snippet(polygonArea + "平方米"));
                                            newMarker.showInfoWindow();
                                            newMarker.setObject(area);
                                            for (int i = 0; i < markers.size(); i++) {
                                                View view1 = View.inflate(context, R.layout.custom_marker, null);
                                                TextView textViewCustomMarker1 = view.findViewById(R.id.tv_custom_marker);
                                                textViewCustomMarker1.setText(polygonArea + "平方米");
                                                markers.get(i).setObject(area);
                                                markers.get(i).setSnippet(polygonArea + "平方米");
                                                markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view1));
                                            }
                                            markers.add(newMarker);
                                            polygonDraw.setPoints(latLngs);
                                            polygonDraw.setVisible(true);
                                        }
                                        return true;
                                    }
                                });

                                Button btn_rollback = new DefaultButton(context).getDefaultButton();
                                btn_rollback.setText("回退");
                                linearLayout.addView(btn_rollback);
                                btn_rollback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (markers.size() > 0 && latLngs.size() > 0) {
                                            markers.get(latLngs.size() - 1).destroy();
                                            markers.remove(latLngs.size() - 1);
                                            latLngs.remove(latLngs.size() - 1);
                                            polygonDraw.setPoints(latLngs);
                                            double area = AMapUtils.calculateArea(latLngs);
                                            BigDecimal polygonArea = new BigDecimal(area);
                                            polygonArea = polygonArea.setScale(1,2);
                                            for (int i = 0; i < markers.size(); i++) {
                                                View view = View.inflate(context, R.layout.custom_marker, null);
                                                TextView textViewCustomMarker = view.findViewById(R.id.tv_custom_marker);
                                                markers.get(i).setObject(area);
                                                markers.get(i).setSnippet(polygonArea + "平方米");
                                                textViewCustomMarker.setText(polygonArea + "平方米");
                                                markers.get(i).setIcon(BitmapDescriptorFactory.fromView(view));
                                            }
                                            if (markers.size()>0)
                                            markers.get(markers.size()-1).showInfoWindow();
                                        }
                                    }
                                });

                                Button btn_clear = new DefaultButton(context).getDefaultButton();
                                btn_clear.setText("清空");
                                linearLayout.addView(btn_clear);
                                btn_clear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Marker marker : markers
                                        ) {
                                            marker.destroy();
                                        }
                                        markers.clear();
                                        latLngs.clear();
                                        polygonDraw.setVisible(false);
                                        aMap.runOnDrawFrame();
                                    }
                                });
                                linearLayout.addView(btn_exit);
                                btn_exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.clear(true);
                                        aMap.removeOnMapClickListener(mapClickListener_distance);
                                        linearLayout.removeView(btn_exit);
                                        linearLayout.removeView(btn_clear);
                                        linearLayout.removeView(btn_rollback);
                                    }
                                });
                            }
                            break;
                            case 2: {
                                PolygonOptions polygonOptions = new PolygonOptions();
                                polygonOptions.strokeWidth(5) // 多边形的边框
                                        .strokeColor(0xAA000000) // 边框颜色
                                        .fillColor(0x00000000);   // 多边形的填充色
                                Polygon polygonDraw = aMap.addPolygon(polygonOptions);
                                List<LatLng> latLngs = new ArrayList<>();
                                List<Marker> markers = new ArrayList<>();
                                List<Marker> innerMarkers = new ArrayList<>();
                                Button btn_confirm = new DefaultButton(context).getDefaultButton();
                                btn_confirm.setText("确认");
                                Button btn_rollback = new DefaultButton(context).getDefaultButton();
                                btn_rollback.setText("回退");
                                Button btn_clear = new DefaultButton(context).getDefaultButton();
                                btn_clear.setText("清空");
                                Button btn_redraw = new DefaultButton(context).getDefaultButton();
                                btn_redraw.setText("重绘");
                                Button btn_exit = new DefaultButton(context).getDefaultButton();
                                btn_exit.setText("退出");

                                AMap.OnMapClickListener areaMapClickListener = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
                                        latLngs.add(latLng);
                                        marker.setClickable(false);
                                        markers.add(marker);
                                        polygonDraw.setPoints(latLngs);
                                        polygonDraw.setVisible(true);
                                    }
                                };
                                aMap.setOnMapClickListener(areaMapClickListener);

                                linearLayout.addView(btn_confirm);
                                btn_confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.setOnMapClickListener(null);
                                        btn_redraw.setVisibility(View.VISIBLE);
                                        btn_rollback.setVisibility(View.GONE);
                                        btn_clear.setVisibility(View.GONE);
                                        VolleyUtils.create(context).get(Constant.TB_GETAll, TBasic.class, new VolleyUtils.OnResponses<TBasic>() {
                                            @Override
                                            public void OnMap(Map<String, String> map) {

                                            }

                                            @Override
                                            public void onSuccess(List<TBasic> response) {
                                                List<TBasic> areaInner = new ArrayList<>();
                                                for (TBasic tBasic : response) {
                                                    List<LatLng> latLngList = new GetCoordinateUtil().getGeoPointList(tBasic);
                                                    for (LatLng latLng : latLngList) {
                                                        if (polygonDraw.contains(latLng) == true){
                                                            areaInner.add(tBasic);
                                                            break;
                                                        }
                                                    }
                                                }
                                                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                                        .customView(R.layout.dialog_select_overlay, false)
                                                        .iconRes(R.drawable.ic_build)
                                                        .title("范围查询结果")
                                                        .positiveText("确认")
                                                        .negativeText("全部显示在地图上")
                                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                for (TBasic tBasic : areaInner) {
                                                                    Marker marker = new EntityToOverlay(aMap, tBasic, context).transform();
                                                                    for (Marker innerMarker : innerMarkers) {
                                                                        if (innerMarker.getPosition() == null){
                                                                        }else if(innerMarker.getPosition().equals(marker.getPosition())){
                                                                            if (marker.getObject() instanceof PolygonBasic){
                                                                                PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
                                                                                polygonBasic.getPolygon().remove();
                                                                            }
                                                                            marker.remove();
                                                                        }
                                                                    }
                                                                    if (marker.isRemoved() == false){
                                                                        innerMarkers.add(marker);
                                                                    }
                                                                }
                                                                aMap.setOnInfoWindowClickListener(new OnInFoWindowClickListenerShowDetail(context, aMap));
                                                            }
                                                        })
                                                        .cancelable(false)
                                                        .show();
                                                //普通列
                                                Column<String> column1 = new Column<>("建筑名称", "buildingName");
                                                column1.setOnColumnItemClickListener(new OnColumnItemClickListener(context, areaInner, materialDialog, aMap));
                                                Column<String> column2 = new Column<>("编号", "buildingNumber");
                                                column2.setOnColumnItemClickListener(new OnColumnItemClickListener(context, areaInner, materialDialog, aMap));
                                                final TableData<TBasic> tableData = new TableData<TBasic>("建筑基本档案", areaInner, column1, column2);
                                                //设置数据
                                                SmartTable table = materialDialog.getCustomView().findViewById(R.id.table);
                                                table.getConfig().setShowXSequence(false).setShowYSequence(false);
                                                table.getConfig().setMinTableWidth(materialDialog.getWindow().getAttributes().width);
                                                //table.setZoom(true,3);是否缩放
                                                table.setTableData(tableData);
                                            }

                                            @Override
                                            public void onError(String error) {
                                                XToast.normal(context,"数据查询错误").show();
                                            }
                                        });
                                    }
                                });

                                linearLayout.addView(btn_rollback);
                                btn_rollback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (markers.size() > 0 && latLngs.size() > 0) {
                                            markers.get(latLngs.size() - 1).destroy();
                                            markers.remove(latLngs.size() - 1);
                                            latLngs.remove(latLngs.size() - 1);
                                            polygonDraw.setPoints(latLngs);
                                        }
                                    }
                                });

                                linearLayout.addView(btn_clear);
                                btn_clear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Marker marker : markers
                                        ) {
                                            marker.destroy();
                                        }
                                        markers.clear();
                                        latLngs.clear();
                                        polygonDraw.setVisible(false);
                                        aMap.runOnDrawFrame();

                                    }
                                });

                                btn_redraw.setVisibility(View.GONE);
                                linearLayout.addView(btn_redraw);
                                btn_redraw.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        for (Marker innerMarker : innerMarkers) {
                                            if (innerMarker.getObject() instanceof PolygonBasic) {
                                                PolygonBasic polygonBasic = (PolygonBasic) innerMarker.getObject();
                                                polygonBasic.getPolygon().remove();
                                            }
                                            innerMarker.destroy();
                                        }

                                        for (Marker marker : markers
                                        ) {
                                            marker.destroy();
                                        }

                                        markers.clear();
                                        latLngs.clear();
                                        btn_redraw.setVisibility(View.GONE);
                                        btn_confirm.setVisibility(View.VISIBLE);
                                        btn_rollback.setVisibility(View.VISIBLE);
                                        btn_clear.setVisibility(View.VISIBLE);
                                        polygonDraw.setVisible(false);
                                        aMap.setOnMapClickListener(areaMapClickListener);
                                    }
                                });

                                linearLayout.addView(btn_exit);
                                btn_exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.clear(true);
                                        aMap.removeOnMapClickListener(areaMapClickListener);
                                        while (linearLayout.getChildCount() > 6) {
                                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                                        }
                                    }
                                });
                            }
                            break;
                            case 3: {
                                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                        .customView(R.layout.fuzzy_select,true)
                                        .title("模糊统计")
                                        .iconRes(R.drawable.ic_select)
                                        .positiveText("搜索")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                View v = dialog.getCustomView();
                                                EditText editTextSelect = v.findViewById(R.id.et_fuzzy_select);
                                                EditText et_building_floor_1 = v.findViewById(R.id.et_building_floor_1);
                                                EditText et_building_floor_2 = v.findViewById(R.id.et_building_floor_2);
                                                EditText et_building_area_1 = v.findViewById(R.id.et_building_area_1);
                                                EditText et_building_area_2 = v.findViewById(R.id.et_building_area_2);
                                                EditText et_building_ground_1 = v.findViewById(R.id.et_building_ground_1);
                                                EditText et_building_ground_2 = v.findViewById(R.id.et_building_ground_2);

                                                VolleyUtils.create(context).get(Constant.TB_fuzzy_select, TBasic.class, new VolleyUtils.OnResponses<TBasic>() {
                                                    @Override
                                                    public void OnMap(Map<String, String> map) {
                                                        map.put("search", editTextSelect.getText().toString());
                                                        map.put("buildingFloorsBegin", et_building_floor_1.getText().toString());
                                                        map.put("buildingFloorsEnd", et_building_floor_2.getText().toString());
                                                        map.put("buildingAreaBegin", et_building_area_1.getText().toString());
                                                        map.put("buildingAreaEnd", et_building_area_2.getText().toString());
                                                        map.put("areaCoveredBegin", et_building_ground_1.getText().toString());
                                                        map.put("areaCoveredEnd", et_building_ground_2.getText().toString());
                                                    }

                                                    @Override
                                                    public void onSuccess(List<TBasic> response) {
                                                        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                                                .customView(R.layout.dialog_select_overlay, false)
                                                                .iconRes(R.drawable.ic_build)
                                                                .title("模糊统计结果")
                                                                .positiveText("确认")
                                                                .cancelable(false)
                                                                .show();
                                                        //普通列
                                                        Column<String> column1 = new Column<>("建筑名称", "buildingName");
                                                        column1.setOnColumnItemClickListener(new OnColumnItemClickListener(context, response, materialDialog, aMap));
                                                        Column<String> column2 = new Column<>("编号", "buildingNumber");
                                                        column2.setOnColumnItemClickListener(new OnColumnItemClickListener(context, response, materialDialog, aMap));
                                                        final TableData<TBasic> tableData = new TableData<TBasic>("建筑基本档案", response, column1, column2);
                                                        //设置数据
                                                        SmartTable table = materialDialog.getCustomView().findViewById(R.id.table);
                                                        table.getConfig().setMinTableWidth(materialDialog.getWindow().getAttributes().width);
                                                        table.getConfig().setShowXSequence(false).setShowYSequence(false);
                                                        //table.setZoom(true,3);是否缩放
                                                        table.setTableData(tableData);

                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        XToast.normal(context, "模糊统计错误").show();
                                                    }
                                                });
                                            }
                                        })
                                        .negativeText("取消")
                                        .show();
                            }
                            break;
                        }
                    }
                })
                .setHasDivider(true);
    }
    public XUISimplePopup GetSimplePopup(){
        GenerateSimplePopupShow(context, aMap);
        return mListPopup;
    }
}
