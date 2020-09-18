package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.TBasic;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

public class DialogSingleButtonCallBackEditor implements MaterialDialog.SingleButtonCallback {
    private TBasic tBasic;
    private Context context;
    private AMap aMap;

    public DialogSingleButtonCallBackEditor(TBasic tBasic, Context context, AMap aMap) {
        this.tBasic = tBasic;
        this.context = context;
        this.aMap = aMap;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        MaterialDialog fatherDialog = dialog;
        dialog.dismiss();
        RadioButton radioButton;
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_save_overlay, true)
                .positiveText("确认")
                .onPositive(new SingleButtonCallbackSaveOrUpdate(SingleButtonCallbackSaveOrUpdate.Type.UPDATE, tBasic, context, aMap))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        fatherDialog.show();
                    }
                })
                .neutralText("删除")
                .onNeutral(new SingleButtonCallbackDelete(tBasic, context, aMap))
                .cancelable(false)
                .show();
        View view = materialDialog.getCustomView();

        switch (tBasic.getCityType()) {
            case "国家历史文化名城" :{
                radioButton = view.findViewById(R.id.radio_city_type_1);
                radioButton.setChecked(true);
            }
            break;
            case "省级历史文化名城" :{
                radioButton = view.findViewById(R.id.radio_city_type_2);
                radioButton.setChecked(true);
            }
            break;
            case "非历史文化名城" :{
                radioButton = view.findViewById(R.id.radio_city_type_3);
                radioButton.setChecked(true);
            }
            break;
        }

        EditText edit_build_number = view.findViewById(R.id.edit_build_number);
        edit_build_number.setText(tBasic.getBuildingNumber());

        EditText edit_build_name = view.findViewById(R.id.edit_build_name);
        edit_build_name.setText(tBasic.getBuildingName());

        EditText edit_build_address = view.findViewById(R.id.edit_build_address);
        edit_build_address.setText(tBasic.getBuildingAddress());

        EditText edit_position_coordinates = view.findViewById(R.id.edit_position_coordinates);
        edit_position_coordinates.setText(tBasic.getPositionCoordinates());

        switch (tBasic.getArchitecturalAge()) {
            case "清代以前（1644年以前）": {
                radioButton = view.findViewById(R.id.rg_architectural_age_1);
                radioButton.setChecked(true);
            }
            break;
            case "清代（1644-1911年）": {
                radioButton = view.findViewById(R.id.rg_architectural_age_2);
                radioButton.setChecked(true);
            }
            break;
            case "中华民国（1911-1949年）": {
                radioButton = view.findViewById(R.id.rg_architectural_age_3);
                radioButton.setChecked(true);
            }
            break;
            case "建国后（1949-1978年）": {
                radioButton = view.findViewById(R.id.rg_architectural_age_4);
                radioButton.setChecked(true);
            }
            break;
            case "改革开放后（1979年以后）": {
                radioButton = view.findViewById(R.id.rg_architectural_age_5);
                radioButton.setChecked(true);
            }
            break;
        }

        switch (tBasic.getBuildingCategory()) {
            case "居住建筑": {
                radioButton = view.findViewById(R.id.rg_building_category_1);
                radioButton.setChecked(true);
            }
            break;
            case "公共建筑": {
                radioButton = view.findViewById(R.id.rg_building_category_2);
                radioButton.setChecked(true);
            }
            break;
            case "工业建筑": {
                radioButton = view.findViewById(R.id.rg_building_category_3);
                radioButton.setChecked(true);
            }
            break;
            case "构筑物": {
                radioButton = view.findViewById(R.id.rg_building_category_4);
                radioButton.setChecked(true);
            }
            break;
        }

        EditText edit_building_description = view.findViewById(R.id.edit_building_description);
        edit_building_description.setText(tBasic.getBuildingDescription());

        EditText edit_historical_evolution = view.findViewById(R.id.edit_historical_evolution);
        edit_historical_evolution.setText(tBasic.getHistoricalEvolution());

        EditText edit_architect_name = view.findViewById(R.id.edit_architect_name);
        edit_architect_name.setText(tBasic.getArchitectName());


        String valueElements = tBasic.getValueElements();
        switch (valueElements.substring(0,4)) {
            case "平面布局": {
                radioButton = view.findViewById(R.id.rg_value_elements_1);
                radioButton.setChecked(true);
            }
            break;
            case "主要立面": {
                radioButton = view.findViewById(R.id.rg_value_elements_2);
                radioButton.setChecked(true);
                EditText rg_value_elements_2_et = view.findViewById(R.id.rg_value_elements_2_et);
                rg_value_elements_2_et.setText(valueElements.substring(5));
            }
            break;
            case "主体结构": {
                radioButton = view.findViewById(R.id.rg_value_elements_3);
                radioButton.setChecked(true);
            }
            break;
            case "特色材料": {
                radioButton = view.findViewById(R.id.rg_value_elements_4);
                radioButton.setChecked(true);
                EditText rg_value_elements_4_et = view.findViewById(R.id.rg_value_elements_4_et);
                rg_value_elements_4_et.setText(valueElements.substring(10));
            }
            break;
            case "历史环境": {
                radioButton = view.findViewById(R.id.rg_value_elements_5);
                radioButton.setChecked(true);
                EditText rg_value_elements_5_et = view.findViewById(R.id.rg_value_elements_5_et);
                rg_value_elements_5_et.setText(valueElements.substring(7));
            }
            break;
        }

        switch (tBasic.getStatusFunction()) {
            case "居住": {
                radioButton = view.findViewById(R.id.rg_status_function_1);
                radioButton.setChecked(true);
            }
            break;
            case "商业": {
                radioButton = view.findViewById(R.id.rg_status_function_2);
                radioButton.setChecked(true);
            }
            break;
            case "商住混合": {
                radioButton = view.findViewById(R.id.rg_status_function_3);
                radioButton.setChecked(true);
            }
            break;
            case "办公": {
                radioButton = view.findViewById(R.id.rg_status_function_4);
                radioButton.setChecked(true);
            }
            break;
            case "教育科研": {
                radioButton = view.findViewById(R.id.rg_status_function_5);
                radioButton.setChecked(true);
            }
            break;
            case "文化展览": {
                radioButton = view.findViewById(R.id.rg_status_function_6);
                radioButton.setChecked(true);
            }
            break;
            case "文娱设施": {
                radioButton = view.findViewById(R.id.rg_status_function_7);
                radioButton.setChecked(true);
            }
            break;
            case "医疗卫生": {
                radioButton = view.findViewById(R.id.rg_status_function_8);
                radioButton.setChecked(true);
            }
            break;
            case "宗教纪念": {
                radioButton = view.findViewById(R.id.rg_status_function_9);
                radioButton.setChecked(true);
            }
            break;
            case "工业仓储": {
                radioButton = view.findViewById(R.id.rg_status_function_10);
                radioButton.setChecked(true);
            }
            break;
            case "闲置空置": {
                radioButton = view.findViewById(R.id.rg_status_function_11);
                radioButton.setChecked(true);
            }
            break;
            default: {
                radioButton = view.findViewById(R.id.rg_status_function_12);
                radioButton.setChecked(true);
                EditText rg_status_function_12_et = view.findViewById(R.id.rg_status_function_12_et);
                rg_status_function_12_et.setText(tBasic.getStatusFunction().substring(3));
            }
            break;
        }

        switch (tBasic.getStructureType()) {
            case "木结构": {
                radioButton = view.findViewById(R.id.rg_structure_type_1);
                radioButton.setChecked(true);
            }
            break;
            case "砖木结构": {
                radioButton = view.findViewById(R.id.rg_structure_type_2);
                radioButton.setChecked(true);
            }
            break;
            case "砖混结构": {
                radioButton = view.findViewById(R.id.rg_structure_type_3);
                radioButton.setChecked(true);
            }
            break;
            case "钢混结构": {
                radioButton = view.findViewById(R.id.rg_structure_type_4);
                radioButton.setChecked(true);
            }
            break;
            default: {
                radioButton = view.findViewById(R.id.rg_structure_type_5);
                radioButton.setChecked(true);
                EditText rg_structure_type_5_et = view.findViewById(R.id.rg_structure_type_5_et);
                rg_structure_type_5_et.setText(tBasic.getStructureType().substring(5));
            }
            break;
        }

        EditText edit_building_floors = view.findViewById(R.id.edit_historical_evolution);
        edit_building_floors.setText(tBasic.getHistoricalEvolution());

        EditText edit_building_area = view.findViewById(R.id.edit_building_area);
        edit_building_area.setText(tBasic.getBuildingArea());

        EditText edit_area_covered = view.findViewById(R.id.edit_area_covered);
        edit_area_covered.setText(tBasic.getAreaCovered());

        EditText edit_status_description = view.findViewById(R.id.edit_status_description);
        edit_status_description.setText(tBasic.getStatusDescription());

        switch (tBasic.getNaturalFactor()) {
            case "地震": {
                radioButton = view.findViewById(R.id.rg_natural_factor_1);
                radioButton.setChecked(true);
            }
            break;
            case "水灾": {
                radioButton = view.findViewById(R.id.rg_natural_factor_2);
                radioButton.setChecked(true);
            }
            break;
            case "火灾": {
                radioButton = view.findViewById(R.id.rg_natural_factor_3);
                radioButton.setChecked(true);
            }
            break;
            case "生物破坏": {
                radioButton = view.findViewById(R.id.rg_natural_factor_4);
                radioButton.setChecked(true);
            }
            break;
            case "污染": {
                radioButton = view.findViewById(R.id.rg_natural_factor_5);
                radioButton.setChecked(true);
            }
            break;
            case "雷电": {
                radioButton = view.findViewById(R.id.rg_natural_factor_6);
                radioButton.setChecked(true);
            }
            break;
            case "风灾": {
                radioButton = view.findViewById(R.id.rg_natural_factor_7);
                radioButton.setChecked(true);
            }
            break;
            case "泥石流": {
                radioButton = view.findViewById(R.id.rg_natural_factor_8);
                radioButton.setChecked(true);
            }
            break;
            case "冰雹": {
                radioButton = view.findViewById(R.id.rg_natural_factor_9);
                radioButton.setChecked(true);
            }
            break;
            case "腐蚀": {
                radioButton = view.findViewById(R.id.rg_natural_factor_10);
                radioButton.setChecked(true);
            }
            break;
            case "沙漠化": {
                radioButton = view.findViewById(R.id.rg_natural_factor_11);
                radioButton.setChecked(true);
            }
            break;
            default: {
                radioButton = view.findViewById(R.id.rg_natural_factor_12);
                radioButton.setChecked(true);
                EditText rg_natural_factor_12_et = view.findViewById(R.id.rg_natural_factor_12_et);
                rg_natural_factor_12_et.setText(tBasic.getNaturalFactor().substring(7));
            }
            break;
        }

        switch (tBasic.getHumanFactor()) {
            case "战争动乱": {
                radioButton = view.findViewById(R.id.rg_human_factor_1);
                radioButton.setChecked(true);
            }
            break;
            case "生产生活活动": {
                radioButton = view.findViewById(R.id.rg_human_factor_2);
                radioButton.setChecked(true);
            }
            break;
            case "盗掘盗窃": {
                radioButton = view.findViewById(R.id.rg_human_factor_3);
                radioButton.setChecked(true);
            }
            break;
            case "不合理利用": {
                radioButton = view.findViewById(R.id.rg_human_factor_4);
                radioButton.setChecked(true);
            }
            break;
            case "违规修缮": {
                radioButton = view.findViewById(R.id.rg_human_factor_5);
                radioButton.setChecked(true);
            }
            break;
            case "年久失修": {
                radioButton = view.findViewById(R.id.rg_human_factor_6);
                radioButton.setChecked(true);
            }
            break;
            case "长期空置": {
                radioButton = view.findViewById(R.id.rg_human_factor_7);
                radioButton.setChecked(true);
            }
            break;
            default: {
                radioButton = view.findViewById(R.id.rg_human_factor_8);
                radioButton.setChecked(true);
                EditText rg_natural_factor_12_et = view.findViewById(R.id.rg_natural_factor_12_et);
                rg_natural_factor_12_et.setText(tBasic.getNaturalFactor().substring(7));
            }
            break;
        }

        String propertyType = tBasic.getPropertyType();
        String[] strings = propertyType.split(",");
        String s = propertyType;
        for (String string : strings) {
            switch (string) {
                case "国有": {
                    CheckBox checkbox_property_type_1 = view.findViewById(R.id.checkbox_property_type_1);
                    checkbox_property_type_1.setChecked(true);
                    s = s.substring(3);
                }
                break;
                case "集体": {
                    CheckBox checkbox_property_type_2 = view.findViewById(R.id.checkbox_property_type_2);
                    checkbox_property_type_2.setChecked(true);
                    s = s.substring(3);
                }
                break;
                case "个人": {
                    CheckBox checkbox_property_type_3 = view.findViewById(R.id.checkbox_property_type_3);
                    checkbox_property_type_3.setChecked(true);
                    s = s.substring(3);
                }
                break;
                default: {
                    CheckBox checkbox_property_type_4 = view.findViewById(R.id.checkbox_property_type_4);
                    checkbox_property_type_4.setChecked(true);
                    EditText checkbox_property_type_et = view.findViewById(R.id.checkbox_property_type_et);
                    s = s.substring(3);
                    checkbox_property_type_et.setText(s);
                }
            }
        }

        EditText edit_property_name = view.findViewById(R.id.edit_property_name);
        edit_property_name.setText(tBasic.getPropertyName());

        EditText edit_user_name = view.findViewById(R.id.edit_user_name);
        edit_user_name.setText(tBasic.getUserName());

        EditText edit_property_description = view.findViewById(R.id.edit_property_description);
        edit_property_description.setText(tBasic.getPropertyDescription());

    }
}
