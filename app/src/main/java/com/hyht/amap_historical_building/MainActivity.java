package com.hyht.amap_historical_building;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.*;
import com.hyht.amap_historical_building.dialog.DialogSaveOverlay;
import com.hyht.amap_historical_building.dialog.DialogSelectAllOverlays;
import com.hyht.amap_historical_building.utils.DefaultButton;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheetItemView;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xui.XUI.getContext;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.switchLayer)
    RoundButton switchLayer;
    @BindView(R.id.btn_draw)
    RoundButton btnDraw;
    @BindView(R.id.btn_select)
    RoundButton btnSelect;
    @BindView(R.id.btn_overlay)
    RoundButton btnOverlay;
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
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        addPermission();

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
    }

    public void addPermission() {
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @OnClick({R.id.switchLayer, R.id.btn_draw, R.id.btn_select, R.id.btn_overlay})
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
        }
    }

    void selectLayer(View v) {
        mListPopup = new XUISimplePopup(this, new String[]{
                "普通地图","卫星地图","导航地图","夜景地图",})
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
                .addItem(R.drawable.draw_point, "绘制点", 0, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.draw_polygon, "绘制多边形", 1, BottomSheet.BottomGridSheetBuilder.FIRST_LINE)
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

                        while(linearLayout.getChildCount() > 4){
                            linearLayout.removeViewAt(linearLayout.getChildCount()-1);
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
                                        if (marker.getPosition() == null){
                                            XToast.normal(getContext(), "请正确绘制").show();
                                        }else {
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
                            case 1 :{
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

                                        polygonDraw.setPoints(latLngs.subList(0,1));
                                        latLngs.clear();
                                        polygonDraw.setPoints(latLngs);
                                        aMap.runOnDrawFrame();
                                    }
                                });

                                linearLayout.addView(btn_save);
                                btn_save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (latLngs.size() < 3){
                                            XToast.normal(getContext(), "请正确绘制").show();
                                        }else {
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
                                        linearLayout.removeView(btn_exit);linearLayout.removeView(btn_clear);
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

    void selectAllOverlays(){
        aMap.clear(true);
        aMap.setOnMarkerClickListener(null);
        aMap.setOnInfoWindowClickListener(null);
        aMap.setOnMapClickListener(null);
        LinearLayout linearLayout = findViewById(R.id.linear_bt);
        while(linearLayout.getChildCount() > 4){
            linearLayout.removeViewAt(linearLayout.getChildCount()-1);
        }
        DialogSelectAllOverlays dialogSelectAllOverlays = new DialogSelectAllOverlays(MainActivity.this, aMap);
    }

    void showOverlays(View v){
        mListPopup = new XUISimplePopup(this, new String[]{
                "全部显示","点覆盖物","面覆盖物","取消显示",})
                .create(DensityUtils.dp2px(getContext(), 170), new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        aMap.clear(true);
                        aMap.setOnMarkerClickListener(null);
                        aMap.setOnInfoWindowClickListener(null);
                        aMap.setOnMapClickListener(null);
                        LinearLayout linearLayout = findViewById(R.id.linear_bt);
                        while(linearLayout.getChildCount() > 4){
                            linearLayout.removeViewAt(linearLayout.getChildCount()-1);
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
}
