package com.hyht.amap_historical_building;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.*;
import com.amap.api.maps.model.*;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackDialogSearch;
import com.hyht.amap_historical_building.dialog.DialogSaveOverlay;
import com.hyht.amap_historical_building.dialog.DialogSelectAllOverlays;
import com.hyht.amap_historical_building.entity.ImageViewInfo;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnColumnItemClickListener;
import com.hyht.amap_historical_building.listener.OnInFoWindowClickListenerShowDetail;
import com.hyht.amap_historical_building.utils.DefaultButton;
import com.hyht.amap_historical_building.utils.EntityToOverlay;
import com.hyht.amap_historical_building.utils.GetCoordinateUtil;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.luck.picture.lib.tools.ValueOf;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xui.widget.toast.XToast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xui.XUI.getContext;
import static java.util.stream.Collectors.toList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.switchLayer)
    SuperButton switchLayer;
    @BindView(R.id.btn_draw)
    SuperButton btnDraw;
    @BindView(R.id.btn_select)
    SuperButton btnSelect;
    @BindView(R.id.btn_overlay)
    SuperButton btnOverlay;
    @BindView(R.id.btn_tools)
    SuperButton btnTools;
    @BindView(R.id.title_main)
    TitleBar titleBar;

    private UiSettings mUiSettings;//定义一个UiSettings对象
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationListener mLocationListener;

    MapView mapView;
    AMap aMap;

    XUISimplePopup mListPopup;

    //在API23+以上，不仅要在AndroidManifest.xml里面添加权限 还要在JAVA代码中请求权限：
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPermission();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        //义乌市坐标
        LatLng centerYiWuPoint= new LatLng(29.306863,120.074911);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerYiWuPoint,10.5f));



        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //异步获取定位结果
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        //mUiSettings.setCompassEnabled(true);//指南针
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示

//        title.setText("historical building Geomatics");
        titleBar.disableLeftView();

        DistrictSearch search = new DistrictSearch(MainActivity.this);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("义乌市");//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        query.setShowChild(false);
        search.setQuery(query);
        search.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                ArrayList<DistrictItem> districtItems =districtResult.getDistrict();
                String[] boundary = districtItems.get(0).districtBoundary();
                String[] points = boundary[0].split(";");
                ArrayList<LatLng> latLngs = new ArrayList<>();
                for (String point : points
                     ) {
                    String[] latAndLng = point.split(",");
                    LatLng latLng = new LatLng(ValueOf.toDouble(latAndLng[1]), ValueOf.toDouble(latAndLng[0]));
                    latLngs.add(latLng);
                }
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.width(5).color(Color.BLUE);
                polylineOptions.addAll(latLngs);
                aMap.addPolyline(polylineOptions);
            }
        });//绑定监听器
        search.searchDistrictAnsy();//开始搜索
    }

    public void addPermission() {/*
        System.out.println(PermissionUtils.getPermissions());PermissionUtils.getPermissions();
        PermissionUtils.isGranted();
        PermissionUtils.permission(PERMISSIONS_STORAGE).request();*/
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


    @OnClick({R.id.switchLayer, R.id.btn_draw, R.id.btn_select, R.id.btn_overlay, R.id.btn_tools})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switchLayer:
                selectLayer(view);
                break;
            case R.id.btn_draw:
                drawBottomSheetGrid();
                break;
            case R.id.btn_select:
                selectAllOverlays();
                break;
            case R.id.btn_overlay:
                showOverlays(view);
                break;
            case R.id.btn_tools:
                showTools(view);
                break;
        }
    }

    void selectLayer(View v) {
        mListPopup = new XUISimplePopup(this, new String[]{
                "普通地图", "卫星地图", "导航地图", "夜景地图",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        switch (position) {
                            case 0: {
                                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 设置白昼地图（即普通地图）地图模式，aMap是地图控制器对象。
                            }
                            break;
                            case 1: {
                                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
                            }
                            break;
                            case 2: {
                                aMap.setMapType(AMap.MAP_TYPE_NAVI);// 设置导航地图模式，aMap是地图控制器对象。
                            }
                            break;
                            case 3: {
                                aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图，aMap是地图控制器对象。
                            }
                            break;
                        }
                    }
                })
                .setHasDivider(true);
        mListPopup.showDown(v);
    }

    void drawBottomSheetGrid() {
        final BottomSheet.BottomGridSheetBuilder builder = new BottomSheet.BottomGridSheetBuilder(this);
        builder
                .addItem(R.drawable.draw_point, "绘制建筑点", 0, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.draw_polygon, "绘制建筑范围", 1, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new BottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, BottomSheetItemView itemView) {
                        LinearLayout linearLayout = findViewById(R.id.linear_bt);
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();

                        aMap.clear();
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);

                        Button btn_save = new DefaultButton(MainActivity.this).getDefaultButton();
                        btn_save.setText("保存");
                        Button btn_exit = new DefaultButton(MainActivity.this).getDefaultButton();
                        btn_exit.setText("退出");

                        while (linearLayout.getChildCount() > 5) {
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
                                            DialogSaveOverlay dialogSaveOverlay = new DialogSaveOverlay(MainActivity.this, marker.getPosition());
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

                                Button btn_rollback = new DefaultButton(MainActivity.this).getDefaultButton();
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

                                Button btn_clear = new DefaultButton(MainActivity.this).getDefaultButton();
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
                                            DialogSaveOverlay dialogSaveOverlay = new DialogSaveOverlay(MainActivity.this, latLngs);
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
                }).build().show();
    }

    void selectAllOverlays() {
        aMap.clear(true);
        aMap.setOnMarkerClickListener(null);
        aMap.setOnInfoWindowClickListener(null);
        aMap.setOnMapClickListener(null);
        LinearLayout linearLayout = findViewById(R.id.linear_bt);
        while (linearLayout.getChildCount() > 5) {
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
        }
        DialogSelectAllOverlays dialogSelectAllOverlays = new DialogSelectAllOverlays(MainActivity.this, aMap);
    }

    void showOverlays(View v) {
        mListPopup = new XUISimplePopup(this, new String[]{
                "全部显示", "建筑点", "建筑范围", "取消显示",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        aMap.clear(true);
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);
                        aMap.setOnMapClickListener(null);
                        LinearLayout linearLayout = findViewById(R.id.linear_bt);
                        while (linearLayout.getChildCount() > 5) {
                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                        }
                        switch (position) {
                            case 0: {
                            }
                            case 1: {
                            }
                            case 2: {
                                SelectOverlays selectOverlays = new SelectOverlays(MainActivity.this, aMap, position);
                            }
                            break;
                            case 3: {
                            }
                            break;
                        }
                    }
                })
                .setHasDivider(true);
        mListPopup.showDown(v);
    }


    private void showTools(View view) {
        mListPopup = new XUISimplePopup(this, new String[]{
                "测距", "测面积", "范围统计", "模糊统计",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        aMap.clear(true);
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);
                        aMap.setOnMapClickListener(null);
                        LinearLayout linearLayout = findViewById(R.id.linear_bt);
                        while (linearLayout.getChildCount() > 5) {
                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                        }
                        switch (position) {
                            case 0: {
                                PolylineOptions polylineOptions = new PolylineOptions();
                                Polyline polyline = aMap.addPolyline(polylineOptions);
                                polyline.setWidth(5);
                                List<LatLng> latLngs = new ArrayList<>();
                                List<Marker> markers = new ArrayList<>();
                                Button btn_exit = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_exit.setText("退出");

                                AMap.OnMapClickListener mapClickListener_distance = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker;
                                        if (markers.size() > 0){
                                            BigDecimal distance = new BigDecimal(AMapUtils.calculateLineDistance(latLngs.get(latLngs.size() - 1),latLng));
                                            distance = distance.add((BigDecimal) markers.get(markers.size()-1).getObject());
                                            distance = distance.setScale(1,2);
                                            marker = aMap.addMarker(new MarkerOptions().position(latLng).title("点击删除").snippet(distance + "米"));
                                            marker.setObject(distance);
                                        }else {
                                            marker = aMap.addMarker(new MarkerOptions().position(latLng).title("点击删除").snippet("起点"));
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
                                        int markerIndex = markers.indexOf(marker);
                                        markers.remove(marker);
                                        latLngs.remove(marker.getPosition());
                                        polyline.setPoints(latLngs);
                                        marker.destroy();
                                        for (int i = markerIndex; i < markers.size(); i++) {
                                            if (i == 0){
                                                markers.get(i).setSnippet("起点");
                                                markers.get(i).setObject(new BigDecimal(0));
                                            }else {
                                                BigDecimal distance = new BigDecimal(AMapUtils.calculateLineDistance(markers.get(i).getPosition(),markers.get(i - 1).getPosition()));
                                                distance = distance.add((BigDecimal) markers.get(i - 1).getObject());
                                                distance = distance.setScale(1,2);
                                                markers.get(i).setSnippet(distance + "米");
                                                markers.get(i).setObject(distance);
                                            }
                                        }
                                    }
                                });

                                Button btn_rollback = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_rollback.setText("回退 ");
                                linearLayout.addView(btn_rollback);
                                btn_rollback.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (markers.size() > 0 && latLngs.size() > 0) {
                                            markers.get(latLngs.size() - 1).destroy();
                                            markers.remove(latLngs.size() - 1);
                                            latLngs.remove(latLngs.size() - 1);
                                            polyline.setPoints(latLngs);
                                        }
                                    }
                                });

                                Button btn_clear = new DefaultButton(MainActivity.this).getDefaultButton();
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
                                Button btn_exit = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_exit.setText("退出");

                                AMap.OnMapClickListener mapClickListener_distance = new AMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {
                                        Marker marker;
                                        latLngs.add(latLng);
                                        double area = AMapUtils.calculateArea(latLngs);
                                        BigDecimal polygonArea = new BigDecimal(area);
                                        polygonArea = polygonArea.setScale(1,2);
                                        marker = aMap.addMarker(new MarkerOptions().position(latLng).title("点击删除").snippet(polygonArea + "平方米"));
                                        marker.showInfoWindow();
                                        marker.setObject(area);
                                        for (int i = 0; i < markers.size(); i++) {
                                            markers.get(i).setObject(area);
                                            markers.get(i).setSnippet(polygonArea + "平方米");
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
                                        markers.remove(marker);
                                        latLngs.remove(marker.getPosition());
                                        marker.destroy();
                                        double area = AMapUtils.calculateArea(latLngs);
                                        BigDecimal polygonArea = new BigDecimal(area);
                                        polygonArea = polygonArea.setScale(1,2);
                                        for (int i = 0; i < markers.size(); i++) {
                                            markers.get(i).setObject(area);
                                            markers.get(i).setSnippet(polygonArea + "平方米");
                                        }
                                        polygonDraw.setPoints(latLngs);
                                    }
                                });

                                Button btn_rollback = new DefaultButton(MainActivity.this).getDefaultButton();
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
                                                markers.get(i).setObject(area);
                                                markers.get(i).setSnippet(polygonArea + "平方米");
                                            }
                                        }
                                    }
                                });

                                Button btn_clear = new DefaultButton(MainActivity.this).getDefaultButton();
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
                                Button btn_confirm = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_confirm.setText("确认");
                                Button btn_rollback = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_rollback.setText("回退");
                                Button btn_clear = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_clear.setText("清空");
                                Button btn_redraw = new DefaultButton(MainActivity.this).getDefaultButton();
                                btn_redraw.setText("重绘");
                                Button btn_exit = new DefaultButton(MainActivity.this).getDefaultButton();
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
                                        VolleyUtils.create(MainActivity.this).get(Constant.TB_GETAll, TBasic.class, new VolleyUtils.OnResponses<TBasic>() {
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
                                                MaterialDialog materialDialog = new MaterialDialog.Builder(MainActivity.this)
                                                        .customView(R.layout.dialog_select_overlay, false)
                                                        .iconRes(R.drawable.ic_build)
                                                        .title("范围查询结果")
                                                        .positiveText("确认")
                                                        .negativeText("全部显示在地图上")
                                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                for (TBasic tBasic : areaInner) {
                                                                    Marker marker = new EntityToOverlay(aMap, tBasic).transform();
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
                                                                aMap.setOnInfoWindowClickListener(new OnInFoWindowClickListenerShowDetail(MainActivity.this, aMap));
                                                            }
                                                        })
                                                        .cancelable(false)
                                                        .show();
                                                //普通列
                                                Column<String> column1 = new Column<>("建筑名称", "buildingName");
                                                column1.setOnColumnItemClickListener(new OnColumnItemClickListener(MainActivity.this, areaInner, materialDialog, aMap));
                                                Column<String> column2 = new Column<>("编号", "buildingNumber");
                                                column2.setOnColumnItemClickListener(new OnColumnItemClickListener(MainActivity.this, areaInner, materialDialog, aMap));
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
                                                XToast.normal(MainActivity.this,"数据查询错误").show();
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
                                        while (linearLayout.getChildCount() > 5) {
                                            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
                                        }
                                    }
                                });
                            }
                            break;
                            case 3: {
                                MaterialDialog materialDialog = new MaterialDialog.Builder(MainActivity.this)
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

                                                VolleyUtils.create(MainActivity.this).get(Constant.TB_fuzzy_select, TBasic.class, new VolleyUtils.OnResponses<TBasic>() {
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
                                                        MaterialDialog materialDialog = new MaterialDialog.Builder(MainActivity.this)
                                                                .customView(R.layout.dialog_select_overlay, false)
                                                                .iconRes(R.drawable.ic_build)
                                                                .title("模糊统计结果")
                                                                .positiveText("确认")
                                                                .cancelable(false)
                                                                .show();
                                                        //普通列
                                                        Column<String> column1 = new Column<>("建筑名称", "buildingName");
                                                        column1.setOnColumnItemClickListener(new OnColumnItemClickListener(MainActivity.this, response, materialDialog, aMap));
                                                        Column<String> column2 = new Column<>("编号", "buildingNumber");
                                                        column2.setOnColumnItemClickListener(new OnColumnItemClickListener(MainActivity.this, response, materialDialog, aMap));
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
                                                        XToast.normal(MainActivity.this, "模糊统计错误").show();
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
        mListPopup.showDown(view);
    }

}
