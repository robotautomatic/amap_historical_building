package com.hyht.amap_historical_building.callback;

import android.content.Context;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnInFoWindowClickListenerShowDetail;
import com.hyht.amap_historical_building.utils.EntityToOverlay;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.List;

public class SingleButtonCallBackShowOverlayOnMap implements MaterialDialog.SingleButtonCallback {
    private AMap aMap;
    private TBasic tBasic;
    Context context;

    public SingleButtonCallBackShowOverlayOnMap(AMap aMap, TBasic tBasic, Context context) {
        this.aMap = aMap;
        this.tBasic = tBasic;
        this.context = context;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        dialog.dismiss();
        String positionCoordinates = tBasic.getPositionCoordinates();
        if (positionCoordinates == null || positionCoordinates.isEmpty() || positionCoordinates.length() <20){
            XToast.normal(context,"没有坐标数据").show();
        }else {
            List<Marker> markers =  aMap.getMapScreenMarkers();
            Marker marker = new EntityToOverlay(aMap, tBasic,context).transform();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(marker.getPosition());
            aMap.animateCamera(cameraUpdate);
            aMap.setOnInfoWindowClickListener(new OnInFoWindowClickListenerShowDetail(context, aMap));
            marker.showInfoWindow();
            for (Marker marker1 : markers) {
                if (marker.getPosition().equals(marker1.getPosition())){
                    if (marker1.getObject() instanceof PolygonBasic){
                        ((PolygonBasic) marker1.getObject()).getPolygon().remove();
                    }
                    marker1.destroy();
                }
            }
        }
    }
}
