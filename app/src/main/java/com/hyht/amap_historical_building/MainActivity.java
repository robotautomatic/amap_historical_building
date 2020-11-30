package com.hyht.amap_historical_building;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
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
import com.hyht.amap_historical_building.dialog.*;
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
        new SimplePopupShowOverlaysImp(this, aMap, 0);
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
        new BottomSheetDrawBuilding(this, aMap).getBottomSheet().build().show();
    }

    void selectAllOverlays() {
/*        aMap.clear(true);
        aMap.setOnMarkerClickListener(null);
        aMap.setOnInfoWindowClickListener(null);
        aMap.setOnMapClickListener(null);
        LinearLayout linearLayout = findViewById(R.id.linear_bt);
        while (linearLayout.getChildCount() > 6) {
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
        }*/
        new DialogSelectAllOverlays(MainActivity.this, aMap);
    }

    void showOverlays(View v) {
        new SimplePopupShowOverlays(this, aMap).GetSimplePopup().showDown(v);
    }


    private void showTools(View view) {
        new SimplePopupTools(this, aMap).GetSimplePopup().showDown(view);
    }

}
