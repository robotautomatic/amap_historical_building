package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.*;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.utils.DefaultButton;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xui.XUI.getContext;

public class BottomSheetDrawBuilding {
    BottomSheet.BottomGridSheetBuilder builder;
    Context context;
    AMap aMap;

    public BottomSheetDrawBuilding(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }
    private void GenerateBottomSheet(Context context, AMap aMap){
        builder = new BottomSheet.BottomGridSheetBuilder(context);
        builder
                .addItem(R.drawable.draw_point, "绘制建筑点", 0, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.draw_polygon, "绘制建筑范围", 1, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new BottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, BottomSheetItemView itemView) {
                        Activity activity = (Activity)context;

                        LinearLayout linearLayout = activity.findViewById(R.id.linear_bt);
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();

                        aMap.clear();
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);

                        Button btn_save = new DefaultButton(context).getDefaultButton();
                        btn_save.setText("保存");
                        Button btn_exit = new DefaultButton(context).getDefaultButton();
                        btn_exit.setText("退出");

                        while (linearLayout.getChildCount() > 6) {
                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                        }
                        DialogSaveOverlay dialogSaveOverlay;
                        switch (tag) {
                            case 0: {
                                Marker marker = aMap.addMarker(new MarkerOptions().position(null).title(null).snippet(null));
                                marker.setDraggable(true);
                                AMap.OnMapClickListener mapClickListener_point = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        marker.setPosition(latLng);
                                        System.out.println(marker.getId());
                                    }
                                };
                                aMap.setOnMapClickListener(mapClickListener_point);

                                linearLayout.addView(btn_save);
                                btn_save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (marker.getPosition() == null) {
                                            XToast.normal(getContext(), "请正确绘制").show();
                                        } else {
                                            DialogSaveOverlay dialogSaveOverlay = new DialogSaveOverlay(context, marker.getPosition());
                                        }
                                    }
                                });

                                linearLayout.addView(btn_exit);
                                btn_exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.clear(true);
                                        aMap.removeOnMapClickListener(mapClickListener_point);
                                        linearLayout.removeView(btn_save);
                                        linearLayout.removeView(btn_exit);
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
                                AMap.OnMapClickListener mapClickListener_point = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("删除").snippet(null));
                                        markers.add(marker);
                                        latLngs.add(latLng);
                                        polygonDraw.setPoints(latLngs);
                                        polygonDraw.setVisible(true);
                                    }
                                };
                                aMap.setOnMapClickListener(mapClickListener_point);
                                aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        markers.remove(marker);
                                        latLngs.remove(marker.getPosition());
                                        polygonDraw.setPoints(latLngs);
                                        marker.destroy();
                                    }
                                });

                                Button btn_rollback = new DefaultButton(context).getDefaultButton();
                                btn_rollback.setText("回退 ");
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
                                        polygonDraw.setVisible(false);
                                        aMap.runOnDrawFrame();
                                    }
                                });

                                linearLayout.addView(btn_save);
                                btn_save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (latLngs.size() < 3) {
                                            XToast.normal(getContext(), "请正确绘制").show();
                                        } else {
                                            DialogSaveOverlay dialogSaveOverlay = new DialogSaveOverlay(context, latLngs);
                                        }
                                    }
                                });

                                linearLayout.addView(btn_exit);
                                btn_exit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        aMap.clear(true);
                                        aMap.removeOnMapClickListener(mapClickListener_point);
                                        linearLayout.removeView(btn_save);
                                        linearLayout.removeView(btn_exit);
                                        linearLayout.removeView(btn_clear);
                                        linearLayout.removeView(btn_rollback);
                                    }
                                });
                            }
                            break;

                        }
                        XToast.normal(getContext(), "tag:" + tag + ", content:" + itemView.toString()).show();
                    }
                });
    }

    public BottomSheet.BottomGridSheetBuilder getBottomSheet(){
        GenerateBottomSheet(context, aMap);
        return builder;
    }
}
