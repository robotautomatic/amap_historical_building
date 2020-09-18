package com.hyht.amap_historical_building.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.callback.DialogSingleButtonCallBackEditor;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackHideOverlayOnMap;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackShowOverlayOnMap;
import com.hyht.amap_historical_building.entity.TBasic;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

public class DialogOverlayDetail {
    private Context context;
    private TBasic tBasic;
    private AMap aMap;
    private Marker marker = null;
    String negativeText;

    public DialogOverlayDetail(Context context, TBasic tBasic, AMap aMap) {
        this.context = context;
        this.tBasic = tBasic;
        this.aMap = aMap;
        getDialog();
    }

    public DialogOverlayDetail(Context context, AMap aMap, Marker marker) {
        this.context = context;
        this.aMap = aMap;
        this.marker = marker;
        getDialog();
    }

    private void getDialog() {
        MaterialDialog.SingleButtonCallback singleButtonCallback = null;
        if (marker == null){
            negativeText = "显示在地图上";
            singleButtonCallback = new SingleButtonCallBackShowOverlayOnMap(aMap, tBasic, context);
        }else {
            negativeText = "在地图上隐藏";
            tBasic = (TBasic) marker.getObject();
            singleButtonCallback = new SingleButtonCallBackHideOverlayOnMap(marker, aMap);
        }
        assert singleButtonCallback != null;
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_overlay_detail, true)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .negativeText(negativeText)
                .onNegative(singleButtonCallback)
                .neutralText("编辑")
                .onNeutral(new DialogSingleButtonCallBackEditor(tBasic, context, aMap))
                .cancelable(false)
                .show();

        View view = materialDialog.getCustomView();

        TextView cityType = view.findViewById(R.id.text_city_type);
        cityType.setText(tBasic.getCityType());

        TextView buildingNumber = view.findViewById(R.id.text_build_number);
        buildingNumber.setText(tBasic.getBuildingNumber());

        TextView buildingName = view.findViewById(R.id.text_build_name);
        buildingName.setText(tBasic.getBuildingName());

        TextView buildingAddress = view.findViewById(R.id.text_build_address);
        buildingAddress.setText(tBasic.getBuildingAddress());

        TextView positionCoordinates = view.findViewById(R.id.text_position_coordinates);
        positionCoordinates.setText(tBasic.getPositionCoordinates());

        TextView architecturalAge = view.findViewById(R.id.text_architectural_age);
        architecturalAge.setText(tBasic.getArchitecturalAge());

        TextView buildingCategory = view.findViewById(R.id.text_building_category);
        buildingCategory.setText(tBasic.getBuildingCategory());

        TextView buildingDescription = view.findViewById(R.id.text_building_description);
        buildingDescription.setText(tBasic.getBuildingDescription());

        TextView historicalEvolution = view.findViewById(R.id.text_historical_evolution);
        historicalEvolution.setText(tBasic.getHistoricalEvolution());

        TextView architectName = view.findViewById(R.id.text_architect_name);
        architectName.setText(tBasic.getArchitectName());

        TextView valueElements = view.findViewById(R.id.text_value_elements);
        valueElements.setText(tBasic.getValueElements());

        TextView statusFunction = view.findViewById(R.id.text_status_function);
        statusFunction.setText(tBasic.getStatusFunction());

        TextView structureType = view.findViewById(R.id.text_structure_type);
        structureType.setText(tBasic.getStructureType());

        TextView buildingFloors = view.findViewById(R.id.text_building_floors);
        buildingFloors.setText(tBasic.getBuildingFloors());

        TextView buildingArea = view.findViewById(R.id.text_building_area);
        buildingArea.setText(tBasic.getBuildingArea());

        TextView areaCovered = view.findViewById(R.id.text_area_covered);
        areaCovered.setText(tBasic.getAreaCovered());

        TextView statusDescription = view.findViewById(R.id.text_status_description);
        statusDescription.setText(tBasic.getStatusDescription());

        TextView naturalFactor = view.findViewById(R.id.text_natural_factor);
        naturalFactor.setText(tBasic.getNaturalFactor());

        TextView humanFactor = view.findViewById(R.id.text_human_factor);
        humanFactor.setText(tBasic.getHumanFactor());

        TextView propertyType = view.findViewById(R.id.text_property_type);
        propertyType.setText(tBasic.getPropertyType());

        TextView propertyName = view.findViewById(R.id.text_property_name);
        propertyName.setText(tBasic.getPropertyName());

        TextView userName = view.findViewById(R.id.text_user_name);
        userName.setText(tBasic.getPropertyName());

        TextView propertyDescription = view.findViewById(R.id.text_property_description);
        propertyDescription.setText(tBasic.getPropertyDescription());
    }
}
