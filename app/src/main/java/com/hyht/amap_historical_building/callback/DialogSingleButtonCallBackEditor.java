package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改编辑建筑物详情
 */
public class DialogSingleButtonCallBackEditor implements MaterialDialog.SingleButtonCallback {
    private TBasic tBasic;
    private Context context;
    private AMap aMap;
    private Marker marker = null;
    private List<LocalMedia> drawMediaList = null;
    private List<LocalMedia> imageMediaList = null;

    public DialogSingleButtonCallBackEditor(List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
    }

    public DialogSingleButtonCallBackEditor(Context context, AMap aMap, Marker marker, List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.context = context;
        this.aMap = aMap;
        this.marker = marker;
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
        if (marker.getObject() instanceof TBasic) {
            tBasic = (TBasic) marker.getObject();
        }else {
            PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
            tBasic = polygonBasic.getTBasic();
        }
    }

    public DialogSingleButtonCallBackEditor(TBasic tBasic, Context context, AMap aMap, List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.tBasic = tBasic;
        this.context = context;
        this.aMap = aMap;
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        MaterialDialog.SingleButtonCallback singleButtonCallback = null;
        if (marker == null){
            singleButtonCallback = new SingleButtonCallbackSaveOrUpdate(context, aMap, tBasic.getBasicId(), drawMediaList, imageMediaList);
        }else {
            singleButtonCallback = new SingleButtonCallbackSaveOrUpdate(context, aMap, marker, drawMediaList, imageMediaList);
        }
        MaterialDialog fatherDialog = dialog;
        fatherDialog.dismiss();
        RadioButton radioButton;
        CheckBox checkBox;
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_save_overlay, true)
                .positiveText("确认")
                .onPositive(singleButtonCallback)
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

        if (tBasic.getCityType() != null)
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

        if (tBasic.getArchitecturalAge() != null)
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

        if (!StringUtils.isEmpty(tBasic.getBuildingCategory())){
            String[] buildingCategory = tBasic.getBuildingCategory().split(";");
            for (String s : buildingCategory) {
                switch (s) {
                    case "居住建筑": {
                        checkBox = view.findViewById(R.id.checkbox_building_category_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "公共建筑": {
                        checkBox = view.findViewById(R.id.checkbox_building_category_2);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "工业建筑": {
                        checkBox = view.findViewById(R.id.checkbox_building_category_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "构筑物": {
                        checkBox = view.findViewById(R.id.checkbox_building_category_4);
                        checkBox.setChecked(true);
                    }
                    break;
                }
            }
        }

        EditText edit_building_description = view.findViewById(R.id.edit_building_description);
        edit_building_description.setText(tBasic.getBuildingDescription());

        EditText edit_historical_evolution = view.findViewById(R.id.edit_historical_evolution);
        edit_historical_evolution.setText(tBasic.getHistoricalEvolution());

        EditText edit_architect_name = view.findViewById(R.id.edit_architect_name);
        edit_architect_name.setText(tBasic.getArchitectName());


        if (!StringUtils.isEmpty(tBasic.getValueElements())){
            String[] getValueElements = tBasic.getValueElements().split(";");
            for (String s : getValueElements) {
                switch (s) {
                    case "平面布局": {
                        checkBox = view.findViewById(R.id.checkbox_value_elements_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "主体结构": {
                        checkBox = view.findViewById(R.id.checkbox_value_elements_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    default:{
                        String[] getValueElementsExtra = s.split(":");
                            switch (getValueElementsExtra[0]) {
                                case "主要立面": {
                                    checkBox = view.findViewById(R.id.checkbox_value_elements_2);
                                    checkBox.setChecked(true);
                                    EditText checkbox_value_elements_2_et = view.findViewById(R.id.checkbox_value_elements_2_et);
                                    checkbox_value_elements_2_et.setText(s.substring(5));
                                }
                                break;
                                case "特色材料装饰和部位": {
                                    checkBox = view.findViewById(R.id.checkbox_value_elements_4);
                                    checkBox.setChecked(true);
                                    EditText checkbox_value_elements_4_et = view.findViewById(R.id.checkbox_value_elements_4_et);
                                    checkbox_value_elements_4_et.setText(s.substring(10));
                                }
                                break;
                                case "历史环境": {
                                    checkBox = view.findViewById(R.id.checkbox_value_elements_5);
                                    checkBox.setChecked(true);
                                    EditText checkbox_value_elements_5_et = view.findViewById(R.id.checkbox_value_elements_5_et);
                                    checkbox_value_elements_5_et.setText(s.substring(5));
                                }
                                break;
                        }
                    }
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(tBasic.getStatusFunction())) {
            String[] getStatusFunction = tBasic.getStatusFunction().split(";");
            for (String s : getStatusFunction) {

                switch (s) {
                    case "居住": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "商业": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_2);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "商住混合": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "办公": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_4);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "教育科研": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_5);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "文化展览": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_6);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "文娱设施": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_7);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "医疗卫生": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_8);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "宗教纪念": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_9);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "工业仓储": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_10);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "闲置空置": {
                        checkBox = view.findViewById(R.id.checkbox_status_function_11);
                        checkBox.setChecked(true);
                    }
                    break;
                    default: {
                        checkBox = view.findViewById(R.id.checkbox_status_function_12);
                        checkBox.setChecked(true);
                        EditText checkbox_status_function_12_et = view.findViewById(R.id.checkbox_status_function_12_et);
                        checkbox_status_function_12_et.setText(s.substring(3));
                    }
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(tBasic.getStructureType())) {
            String[] getStructureType = tBasic.getStructureType().split(";");
            for (String s : getStructureType) {
                switch (s) {
                    case "木结构": {
                        checkBox = view.findViewById(R.id.checkbox_structure_type_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "砖木结构": {
                        checkBox = view.findViewById(R.id.checkbox_structure_type_2);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "砖混结构": {
                        checkBox = view.findViewById(R.id.checkbox_structure_type_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "钢混结构": {
                        checkBox = view.findViewById(R.id.checkbox_structure_type_4);
                        checkBox.setChecked(true);
                    }
                    break;
                    default: {
                        checkBox = view.findViewById(R.id.checkbox_structure_type_5);
                        checkBox.setChecked(true);
                        EditText checkbox_structure_type_5_et = view.findViewById(R.id.checkbox_structure_type_5_et);
                        checkbox_structure_type_5_et.setText(s.substring(5));
                    }
                    break;
                }
            }
        }

        EditText edit_building_floors = view.findViewById(R.id.edit_building_floors);
        edit_building_floors.setText(tBasic.getBuildingFloors());

        EditText edit_building_area = view.findViewById(R.id.edit_building_area);
        edit_building_area.setText(tBasic.getBuildingArea());

        EditText edit_area_covered = view.findViewById(R.id.edit_area_covered);
        edit_area_covered.setText(tBasic.getAreaCovered());

        EditText edit_status_description = view.findViewById(R.id.edit_status_description);
        edit_status_description.setText(tBasic.getStatusDescription());

        if (!StringUtils.isEmpty(tBasic.getNaturalFactor())) {
            String[] getNaturalFactor = tBasic.getNaturalFactor().split(";");
            for (String s : getNaturalFactor) {
                switch (s) {
                    case "地震": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "水灾": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_2);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "火灾": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "生物破坏": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_4);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "污染": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_5);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "雷电": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_6);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "风灾": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_7);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "泥石流": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_8);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "冰雹": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_9);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "腐蚀": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_10);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "沙漠化": {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_11);
                        checkBox.setChecked(true);
                    }
                    break;
                    default: {
                        checkBox = view.findViewById(R.id.checkbox_natural_factor_12);
                        checkBox.setChecked(true);
                        EditText checkbox_natural_factor_12_et = view.findViewById(R.id.checkbox_natural_factor_12_et);
                        checkbox_natural_factor_12_et.setText(s.substring(7));
                    }
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(tBasic.getHumanFactor())) {
            String[] getHumanFactor = tBasic.getHumanFactor().split(";");
            for (String s : getHumanFactor) {
                switch (s) {
                    case "战争动乱": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_1);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "生产生活活动": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_2);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "盗掘盗窃": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_3);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "不合理利用": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_4);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "违规修缮": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_5);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "年久失修": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_6);
                        checkBox.setChecked(true);
                    }
                    break;
                    case "长期空置": {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_7);
                        checkBox.setChecked(true);
                    }
                    break;
                    default: {
                        checkBox = view.findViewById(R.id.checkbox_human_factor_8);
                        checkBox.setChecked(true);
                        EditText checkbox_natural_factor_12_et = view.findViewById(R.id.checkbox_natural_factor_12_et);
                        checkbox_natural_factor_12_et.setText(s.substring(7));
                    }
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(tBasic.getPropertyType())) {
            String[] getPropertyType = tBasic.getPropertyType().split(";");
            for (String s : getPropertyType) {
                switch (s) {
                    case "国有": {
                        CheckBox checkbox_property_type_1 = view.findViewById(R.id.checkbox_property_type_1);
                        checkbox_property_type_1.setChecked(true);
                    }
                    break;
                    case "集体": {
                        CheckBox checkbox_property_type_2 = view.findViewById(R.id.checkbox_property_type_2);
                        checkbox_property_type_2.setChecked(true);
                    }
                    break;
                    case "个人": {
                        CheckBox checkbox_property_type_3 = view.findViewById(R.id.checkbox_property_type_3);
                        checkbox_property_type_3.setChecked(true);
                    }
                    break;
                    default: {
                        CheckBox checkbox_property_type_4 = view.findViewById(R.id.checkbox_property_type_4);
                        checkbox_property_type_4.setChecked(true);
                        EditText checkbox_property_type_et = view.findViewById(R.id.checkbox_property_type_et);
                        checkbox_property_type_et.setText(s.substring(3));
                    }
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
