package com.hyht.amap_historical_building.callback;

import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

public class SingleButtonCallBackHideOverlayOnMap implements MaterialDialog.SingleButtonCallback {
    private Marker marker;
    private AMap aMap;

    public SingleButtonCallBackHideOverlayOnMap(Marker marker, AMap aMap) {
        this.marker = marker;
        this.aMap = aMap;
    }


    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        dialog.dismiss();
        if (marker.getObject() instanceof PolygonBasic) {
            PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
            polygonBasic.getPolygon().remove();
        }
        marker.destroy();
    }
}
