package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.HashMap;
import java.util.Map;

public class SingleButtonCallbackSaveOrUpdate implements MaterialDialog.SingleButtonCallback {
    public enum Type {SAVE, UPDATE}
    private Type type;
    private TBasic tBasic;
    private Context context;
    private AMap aMap;

    public SingleButtonCallbackSaveOrUpdate(Type type, TBasic tBasic, Context context, AMap aMap) {
        this.type = type;
        this.tBasic = tBasic;
        this.context = context;
        this.aMap = aMap;
    }

    public SingleButtonCallbackSaveOrUpdate(Type type) {
        this.type = type;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        View view = dialog.getCustomView();
        Map<String, String> mapBuilding = new HashMap<>();

        RadioGroup rgCityType = view.findViewById(R.id.rg_city_type);
        switch (rgCityType.getCheckedRadioButtonId()) {
            case R.id.radio_city_type_1: {
                mapBuilding.put("cityType", "国家历史文化名城");
            }
            break;
            case R.id.radio_city_type_2: {
                mapBuilding.put("cityType", "省级历史文化名城");
            }
            break;
            case R.id.radio_city_type_3: {
                mapBuilding.put("cityType", "非历史文化名城");
            }
            break;
        }

        EditText edit_build_number = view.findViewById(R.id.edit_build_number);
        mapBuilding.put("buildingNumber", String.valueOf(edit_build_number.getText()));

        EditText edit_build_name = view.findViewById(R.id.edit_build_name);
        mapBuilding.put("buildingName", String.valueOf(edit_build_name.getText()));

        EditText edit_build_address = view.findViewById(R.id.edit_build_address);
        mapBuilding.put("buildingAddress", String.valueOf(edit_build_address.getText()));


        EditText edit_position_coordinates = view.findViewById(R.id.edit_position_coordinates);
        mapBuilding.put("positionCoordinates", String.valueOf(edit_position_coordinates.getText()));

        RadioGroup rg_architectural_age = view.findViewById(R.id.rg_architectural_age);
        switch (rg_architectural_age.getCheckedRadioButtonId()) {
            case R.id.rg_architectural_age_1: {
                mapBuilding.put("architecturalAge", "清代以前（1644年以前）");
            }
            break;
            case R.id.rg_architectural_age_2: {
                mapBuilding.put("architecturalAge", "清代（1644-1911年）");
            }
            break;
            case R.id.rg_architectural_age_3: {
                mapBuilding.put("architecturalAge", "中华民国（1911-1949年）");
            }
            break;
            case R.id.rg_architectural_age_4: {
                mapBuilding.put("architecturalAge", "建国后（1949-1978年）");
            }
            break;
            case R.id.rg_architectural_age_5: {
                mapBuilding.put("architecturalAge", "改革开放后（1979年以后）");
            }
            break;
        }

        RadioGroup rg_building_category = view.findViewById(R.id.rg_building_category);
        switch (rg_building_category.getCheckedRadioButtonId()) {
            case R.id.rg_building_category_1: {
                mapBuilding.put("buildingCategory", "居住建筑");
            }
            break;
            case R.id.rg_building_category_2: {
                mapBuilding.put("buildingCategory", "公共建筑");
            }
            break;
            case R.id.rg_building_category_3: {
                mapBuilding.put("buildingCategory", "工业建筑");
            }
            break;
            case R.id.rg_building_category_4: {
                mapBuilding.put("buildingCategory", "构筑物");
            }
            break;
        }

        EditText edit_building_description = view.findViewById(R.id.edit_building_description);
        mapBuilding.put("buildingDescription", String.valueOf(edit_building_description.getText()));

        EditText edit_historical_evolution = view.findViewById(R.id.edit_historical_evolution);
        mapBuilding.put("historicalEvolution", String.valueOf(edit_historical_evolution.getText()));

        EditText edit_architect_name = view.findViewById(R.id.edit_architect_name);
        mapBuilding.put("architectName", String.valueOf(edit_architect_name.getText()));

        RadioGroup rg_value_elements = view.findViewById(R.id.rg_value_elements);
        switch (rg_value_elements.getCheckedRadioButtonId()) {
            case R.id.rg_value_elements_1: {
                mapBuilding.put("valueElements", "平面布局");
            }
            break;
            case R.id.rg_value_elements_2: {
                EditText rg_value_elements_2_et = view.findViewById(R.id.rg_value_elements_2_et);
                mapBuilding.put("valueElements", "主要立面," + rg_value_elements_2_et.getText());
            }
            break;
            case R.id.rg_value_elements_3: {
                mapBuilding.put("valueElements", "主体结构");
            }
            break;
            case R.id.rg_value_elements_4: {
                EditText rg_value_elements_4_et = view.findViewById(R.id.rg_value_elements_4_et);
                mapBuilding.put("valueElements", "特色材料装饰和部位," + rg_value_elements_4_et.getText());
            }
            break;
            case R.id.rg_value_elements_5: {
                EditText rg_value_elements_5_et = view.findViewById(R.id.rg_value_elements_5_et);
                mapBuilding.put("valueElements", "历史环境要素," + rg_value_elements_5_et.getText());
            }
            break;
        }

        RadioGroup rg_status_function = view.findViewById(R.id.rg_status_function);
        switch (rg_status_function.getCheckedRadioButtonId()) {
            case R.id.rg_status_function_1: {
                mapBuilding.put("statusFunction", "居住");
            }
            break;
            case R.id.rg_status_function_2: {
                mapBuilding.put("statusFunction", "商业");
            }
            break;
            case R.id.rg_status_function_3: {
                mapBuilding.put("statusFunction", "商住混合");
            }
            break;
            case R.id.rg_status_function_4: {
                mapBuilding.put("statusFunction", "办公");
            }
            break;
            case R.id.rg_status_function_5: {
                mapBuilding.put("statusFunction", "教育科研");
            }
            break;
            case R.id.rg_status_function_6: {
                mapBuilding.put("statusFunction", "文化展览");
            }
            break;
            case R.id.rg_status_function_7: {
                mapBuilding.put("statusFunction", "文娱设施");
            }
            break;
            case R.id.rg_status_function_8: {
                mapBuilding.put("statusFunction", "医疗卫生");
            }
            break;
            case R.id.rg_status_function_9: {
                mapBuilding.put("statusFunction", "宗教纪念");
            }
            break;
            case R.id.rg_status_function_10: {
                mapBuilding.put("statusFunction", "工业仓储");
            }
            break;
            case R.id.rg_status_function_11: {
                mapBuilding.put("statusFunction", "闲置空置");
            }
            break;
            case R.id.rg_status_function_12: {
                EditText rg_status_function_12_et = view.findViewById(R.id.rg_status_function_12_et);
                mapBuilding.put("statusFunction", "其他," + rg_status_function_12_et.getText());
            }
            break;
        }

        RadioGroup rg_structure_type = view.findViewById(R.id.rg_structure_type);
        switch (rg_structure_type.getCheckedRadioButtonId()) {
            case R.id.rg_structure_type_1: {
                mapBuilding.put("structureType", "木结构");
            }
            break;
            case R.id.rg_structure_type_2: {
                mapBuilding.put("structureType", "砖木结构");
            }
            break;
            case R.id.rg_structure_type_3: {
                mapBuilding.put("structureType", "砖混结构");
            }
            break;
            case R.id.rg_structure_type_4: {
                mapBuilding.put("structureType", "钢混结构");
            }
            break;
            case R.id.rg_structure_type_5: {
                EditText rg_structure_type_5_et = view.findViewById(R.id.rg_structure_type_5_et);
                mapBuilding.put("structureType", "其他结构," + rg_structure_type_5_et.getText());
            }
            break;
        }

        EditText edit_building_floors = view.findViewById(R.id.edit_historical_evolution);
        mapBuilding.put("buildingFloors", String.valueOf(edit_building_floors.getText()));

        EditText edit_building_area = view.findViewById(R.id.edit_building_area);
        mapBuilding.put("buildingArea", String.valueOf(edit_building_area.getText()));

        EditText edit_area_covered = view.findViewById(R.id.edit_area_covered);
        mapBuilding.put("areaCovered", String.valueOf(edit_area_covered.getText()));

        EditText edit_status_description = view.findViewById(R.id.edit_status_description);
        mapBuilding.put("statusDescription", String.valueOf(edit_status_description.getText()));

        RadioGroup rg_natural_factor = view.findViewById(R.id.rg_natural_factor);
        switch (rg_natural_factor.getCheckedRadioButtonId()) {
            case R.id.rg_natural_factor_1: {
                mapBuilding.put("naturalFactor", "地震");
            }
            break;
            case R.id.rg_natural_factor_2: {
                mapBuilding.put("naturalFactor", "水灾");
            }
            break;
            case R.id.rg_natural_factor_3: {
                mapBuilding.put("naturalFactor", "火灾");
            }
            break;
            case R.id.rg_natural_factor_4: {
                mapBuilding.put("naturalFactor", "生物破坏");
            }
            break;
            case R.id.rg_natural_factor_5: {
                mapBuilding.put("naturalFactor", "污染");
            }
            break;
            case R.id.rg_natural_factor_6: {
                mapBuilding.put("naturalFactor", "雷电");
            }
            break;
            case R.id.rg_natural_factor_7: {
                mapBuilding.put("naturalFactor", "风灾");
            }
            break;
            case R.id.rg_natural_factor_8: {
                mapBuilding.put("naturalFactor", "泥石流");
            }
            break;
            case R.id.rg_natural_factor_9: {
                mapBuilding.put("naturalFactor", "冰雹");
            }
            break;
            case R.id.rg_natural_factor_10: {
                mapBuilding.put("naturalFactor", "腐蚀");
            }
            break;
            case R.id.rg_natural_factor_11: {
                mapBuilding.put("naturalFactor", "沙漠化");
            }
            break;
            case R.id.rg_natural_factor_12: {
                EditText rg_natural_factor_12_et = view.findViewById(R.id.rg_natural_factor_12_et);
                mapBuilding.put("naturalFactor", "其他自然因素," + rg_natural_factor_12_et.getText());
            }
            break;
        }

        RadioGroup rg_human_factor = view.findViewById(R.id.rg_human_factor);
        switch (rg_human_factor.getCheckedRadioButtonId()) {
            case R.id.rg_human_factor_1: {
                mapBuilding.put("humanFactor", "战争动乱");
            }
            break;
            case R.id.rg_human_factor_2: {
                mapBuilding.put("humanFactor", "生产生活活动");
            }
            break;
            case R.id.rg_human_factor_3: {
                mapBuilding.put("humanFactor", "盗掘盗窃");
            }
            break;
            case R.id.rg_human_factor_4: {
                mapBuilding.put("humanFactor", "不合理利用");
            }
            break;
            case R.id.rg_human_factor_5: {
                mapBuilding.put("humanFactor", "违规修缮");
            }
            break;
            case R.id.rg_human_factor_6: {
                mapBuilding.put("humanFactor", "年久失修");
            }
            break;
            case R.id.rg_human_factor_7: {
                mapBuilding.put("humanFactor", "长期空置");
            }
            break;
            case R.id.rg_human_factor_8: {
                EditText rg_human_factor_8_et = view.findViewById(R.id.rg_human_factor_8_et);
                mapBuilding.put("humanFactor", "其他人为因素," + rg_human_factor_8_et.getText());
            }
            break;
        }

        CheckBox checkbox_property_type_1 = view.findViewById(R.id.checkbox_property_type_1);
        CheckBox checkbox_property_type_2 = view.findViewById(R.id.checkbox_property_type_2);
        CheckBox checkbox_property_type_3 = view.findViewById(R.id.checkbox_property_type_3);
        CheckBox checkbox_property_type_4 = view.findViewById(R.id.checkbox_property_type_4);
        String propertyType = "";
        if (checkbox_property_type_1.isChecked()) {
            propertyType = propertyType + "国有,";
        }
        if (checkbox_property_type_2.isChecked()) {
            propertyType = propertyType + "集体,";
        }
        if (checkbox_property_type_3.isChecked()) {
            propertyType = propertyType + "个人,";
        }
        if (checkbox_property_type_4.isChecked()) {
            EditText checkbox_property_type_et = view.findViewById(R.id.checkbox_property_type_et);
            propertyType = propertyType + "其他：" + checkbox_property_type_et.getText();
        }
        mapBuilding.put("propertyType", propertyType);

        EditText edit_property_name = view.findViewById(R.id.edit_property_name);
        mapBuilding.put("propertyName", String.valueOf(edit_property_name.getText()));

        EditText edit_user_name = view.findViewById(R.id.edit_user_name);
        mapBuilding.put("userName", String.valueOf(edit_user_name.getText()));

        EditText edit_property_description = view.findViewById(R.id.edit_property_description);
        mapBuilding.put("propertyDescription", String.valueOf(edit_property_description.getText()));

        volleyNetwork(context, mapBuilding);
    }

    private void volleyNetwork(Context context, Map mapBuilding){
        String url = null;
        switch (type){
            case SAVE:{
                url = Constant.TB_ADD;
            }
            break;
            case UPDATE:{
                url = Constant.TB_UPDATE;
                mapBuilding.put("basicId", String.valueOf(tBasic.getBasicId()));
            }
            break;
        }
        VolleyUtils.create(context).post(url, TBasic.class, new VolleyUtils.OnResponse<TBasic>() {
            @Override
            public void OnMap(Map<String, String> map) {
                map.putAll(mapBuilding);
                System.out.println(map + "/////" + map.size());
            }

            @Override
            public void onSuccess(TBasic response) {
                Log.e("TAG", "response---->" + response);
                XToast.normal(context,"保存成功").show();
                DialogOverlayDetail dialogOverlayDetail = new DialogOverlayDetail(context, tBasic, aMap);
            }

            @Override
            public void onError(String error) {
                Log.e("TAG", "error---->" + error);
                XToast.normal(context,"保存失败").show();
            }
        });

    }
}
