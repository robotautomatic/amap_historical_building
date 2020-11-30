package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.PolygonBasic;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.entity.TDraw;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xui.XUI.getContext;

public class SingleButtonCallbackSaveOrUpdate implements MaterialDialog.SingleButtonCallback {
    private Context context;
    private AMap aMap;
    private int getBasicId;
    private Marker marker = null;
    private List<LocalMedia> drawMediaList;
    private List<LocalMedia> imageMediaList;

    public SingleButtonCallbackSaveOrUpdate(Context context, AMap aMap, List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.context = context;
        this.aMap = aMap;
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
    }

    public SingleButtonCallbackSaveOrUpdate(Context context, AMap aMap, int getBasicId, List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.context = context;
        this.aMap = aMap;
        this.getBasicId = getBasicId;
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
    }

    public SingleButtonCallbackSaveOrUpdate(Context context, AMap aMap, Marker marker, List<LocalMedia> drawMediaList, List<LocalMedia> imageMediaList) {
        this.context = context;
        this.aMap = aMap;
        this.marker = marker;
        this.drawMediaList = drawMediaList;
        this.imageMediaList = imageMediaList;
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
            default:
                mapBuilding.put("cityType", "");
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
            default:
                mapBuilding.put("architecturalAge", "");
        }

        CheckBox checkbox_building_category_1 = view.findViewById(R.id.checkbox_building_category_1);
        CheckBox checkbox_building_category_2 = view.findViewById(R.id.checkbox_building_category_2);
        CheckBox checkbox_building_category_3 = view.findViewById(R.id.checkbox_building_category_3);
        CheckBox checkbox_building_category_4 = view.findViewById(R.id.checkbox_building_category_4);
        String buildingCategory = "";
        if (checkbox_building_category_1.isChecked()) {
            buildingCategory = buildingCategory + "居住建筑;";
        }
        if (checkbox_building_category_2.isChecked()) {
            buildingCategory = buildingCategory + "公共建筑;";
        }
        if (checkbox_building_category_3.isChecked()) {
            buildingCategory = buildingCategory + "工业建筑;";
        }
        if (checkbox_building_category_4.isChecked()) {
            buildingCategory = buildingCategory + "构筑物;";
        }
        mapBuilding.put("valueElements", buildingCategory);

        EditText edit_building_description = view.findViewById(R.id.edit_building_description);
        mapBuilding.put("buildingDescription", String.valueOf(edit_building_description.getText()));

        EditText edit_historical_evolution = view.findViewById(R.id.edit_historical_evolution);
        mapBuilding.put("historicalEvolution", String.valueOf(edit_historical_evolution.getText()));

        EditText edit_architect_name = view.findViewById(R.id.edit_architect_name);
        mapBuilding.put("architectName", String.valueOf(edit_architect_name.getText()));

        CheckBox checkbox_value_elements_1 = view.findViewById(R.id.checkbox_value_elements_1);
        CheckBox checkbox_value_elements_2 = view.findViewById(R.id.checkbox_value_elements_2);
        CheckBox checkbox_value_elements_3 = view.findViewById(R.id.checkbox_value_elements_3);
        CheckBox checkbox_value_elements_4 = view.findViewById(R.id.checkbox_value_elements_4);
        CheckBox checkbox_value_elements_5 = view.findViewById(R.id.checkbox_value_elements_5);
        String valueElements = "";
        if (checkbox_value_elements_1.isChecked()) {
            valueElements = valueElements + "平面布局;";
        }
        if (checkbox_value_elements_2.isChecked()) {
            EditText checkbox_value_elements_2_et = view.findViewById(R.id.checkbox_value_elements_2_et);
            valueElements = valueElements + "主要立面:" + checkbox_value_elements_2_et.getText().toString()+";";
        }
        if (checkbox_value_elements_3.isChecked()) {
            valueElements = valueElements + "主体结构;";
        }
        if (checkbox_value_elements_4.isChecked()) {
            EditText checkbox_value_elements_4_et = view.findViewById(R.id.checkbox_value_elements_4_et);
            valueElements = valueElements + "特色材料装饰和部位:" + checkbox_value_elements_4_et.getText().toString()+";";
        }
        if (checkbox_value_elements_5.isChecked()) {
            EditText checkbox_value_elements_5_et = view.findViewById(R.id.checkbox_value_elements_5_et);
            valueElements = valueElements + "历史环境要素:" + checkbox_value_elements_5_et.getText().toString()+";";
        }
        mapBuilding.put("valueElements", "");

        CheckBox checkbox_status_function_1 = view.findViewById(R.id.checkbox_status_function_1);
        CheckBox checkbox_status_function_2 = view.findViewById(R.id.checkbox_status_function_2);
        CheckBox checkbox_status_function_3 = view.findViewById(R.id.checkbox_status_function_3);
        CheckBox checkbox_status_function_4 = view.findViewById(R.id.checkbox_status_function_4);
        CheckBox checkbox_status_function_5 = view.findViewById(R.id.checkbox_status_function_5);
        CheckBox checkbox_status_function_6 = view.findViewById(R.id.checkbox_status_function_6);
        CheckBox checkbox_status_function_7 = view.findViewById(R.id.checkbox_status_function_7);
        CheckBox checkbox_status_function_8 = view.findViewById(R.id.checkbox_status_function_8);
        CheckBox checkbox_status_function_9 = view.findViewById(R.id.checkbox_status_function_9);
        CheckBox checkbox_status_function_10 = view.findViewById(R.id.checkbox_status_function_10);
        CheckBox checkbox_status_function_11 = view.findViewById(R.id.checkbox_status_function_11);
        CheckBox checkbox_status_function_12 = view.findViewById(R.id.checkbox_status_function_12);
        String statusFunction = "";
        if (checkbox_status_function_1.isChecked()) {
            statusFunction = statusFunction + "居住;";
        }
        if (checkbox_status_function_2.isChecked()) {
            statusFunction = statusFunction + "商业;";
        }
        if (checkbox_status_function_3.isChecked()) {
            statusFunction = statusFunction + "商住混合;";
        }
        if (checkbox_status_function_4.isChecked()) {
            statusFunction = statusFunction + "办公;";
        }
        if (checkbox_status_function_5.isChecked()) {
            statusFunction = statusFunction + "教育科研;";
        }
        if (checkbox_status_function_6.isChecked()) {
            statusFunction = statusFunction + "文化展览;";
        }
        if (checkbox_status_function_7.isChecked()) {
            statusFunction = statusFunction + "文娱设施;";
        }
        if (checkbox_status_function_8.isChecked()) {
            statusFunction = statusFunction + "医疗卫生;";
        }
        if (checkbox_status_function_9.isChecked()) {
            statusFunction = statusFunction + "宗教纪念;";
        }
        if (checkbox_status_function_10.isChecked()) {
            statusFunction = statusFunction + "工业仓储;";
        }
        if (checkbox_status_function_11.isChecked()) {
            statusFunction = statusFunction + "闲置空置;";
        }
        if (checkbox_status_function_12.isChecked()) {
            EditText checkbox_status_function_12_et = view.findViewById(R.id.checkbox_status_function_12_et);
            statusFunction = statusFunction + "其他:"+checkbox_status_function_12_et.getText().toString()+";";
        }

                mapBuilding.put("statusFunction", "");

        CheckBox checkbox_structure_type_1 = view.findViewById(R.id.checkbox_structure_type_1);
        CheckBox checkbox_structure_type_2 = view.findViewById(R.id.checkbox_structure_type_1);
        CheckBox checkbox_structure_type_3 = view.findViewById(R.id.checkbox_structure_type_3);
        CheckBox checkbox_structure_type_4 = view.findViewById(R.id.checkbox_structure_type_4);
        CheckBox checkbox_structure_type_5 = view.findViewById(R.id.checkbox_structure_type_5);
        String structureType = "";
        if (checkbox_structure_type_1.isChecked()) {
            structureType = structureType + "木结构;";
        }
        if (checkbox_structure_type_2.isChecked()) {
            structureType = structureType + "砖木结构;";
        }
        if (checkbox_structure_type_3.isChecked()) {
            structureType = structureType + "砖混结构;";
        }
        if (checkbox_structure_type_4.isChecked()) {
            structureType = structureType + "钢混结构;";
        }
        if (checkbox_structure_type_5.isChecked()) {
            EditText rg_structure_type_5_et = view.findViewById(R.id.checkbox_structure_type_5_et);
            structureType = structureType + "其他结构:"+rg_structure_type_5_et.getText().toString()+";";
        }
                mapBuilding.put("structureType", "");


        EditText edit_building_floors = view.findViewById(R.id.edit_building_floors);
        mapBuilding.put("buildingFloors", String.valueOf(edit_building_floors.getText()));

        EditText edit_building_area = view.findViewById(R.id.edit_building_area);
        mapBuilding.put("buildingArea", String.valueOf(edit_building_area.getText()));

        EditText edit_area_covered = view.findViewById(R.id.edit_area_covered);
        mapBuilding.put("areaCovered", String.valueOf(edit_area_covered.getText()));

        EditText edit_status_description = view.findViewById(R.id.edit_status_description);
        mapBuilding.put("statusDescription", String.valueOf(edit_status_description.getText()));


        CheckBox checkbox_natural_factor_1 = view.findViewById(R.id.checkbox_natural_factor_1);
        CheckBox checkbox_natural_factor_2 = view.findViewById(R.id.checkbox_natural_factor_2);
        CheckBox checkbox_natural_factor_3 = view.findViewById(R.id.checkbox_natural_factor_3);
        CheckBox checkbox_natural_factor_4 = view.findViewById(R.id.checkbox_natural_factor_4);
        CheckBox checkbox_natural_factor_5 = view.findViewById(R.id.checkbox_natural_factor_5);
        CheckBox checkbox_natural_factor_6 = view.findViewById(R.id.checkbox_natural_factor_6);
        CheckBox checkbox_natural_factor_7 = view.findViewById(R.id.checkbox_natural_factor_7);
        CheckBox checkbox_natural_factor_8 = view.findViewById(R.id.checkbox_natural_factor_8);
        CheckBox checkbox_natural_factor_9 = view.findViewById(R.id.checkbox_natural_factor_9);
        CheckBox checkbox_natural_factor_10 = view.findViewById(R.id.checkbox_natural_factor_10);
        CheckBox checkbox_natural_factor_11 = view.findViewById(R.id.checkbox_natural_factor_11);
        CheckBox checkbox_natural_factor_12 = view.findViewById(R.id.checkbox_natural_factor_12);
        String naturalFactor = "";
        if (checkbox_natural_factor_1.isChecked()) {
            naturalFactor = naturalFactor + "地震;";
        }
        if (checkbox_natural_factor_2.isChecked()) {
            naturalFactor = naturalFactor + "水灾;";
        }
        if (checkbox_natural_factor_3.isChecked()) {
            naturalFactor = naturalFactor + "火灾;";
        }
        if (checkbox_natural_factor_4.isChecked()) {
            naturalFactor = naturalFactor + "生物破坏;";
        }
        if (checkbox_natural_factor_5.isChecked()) {
            naturalFactor = naturalFactor + "污染;";
        }
        if (checkbox_natural_factor_6.isChecked()) {
            naturalFactor = naturalFactor + "雷电;";
        }
        if (checkbox_natural_factor_7.isChecked()) {
            naturalFactor = naturalFactor + "风灾;";
        }
        if (checkbox_natural_factor_8.isChecked()) {
            naturalFactor = naturalFactor + "泥石流;";
        }
        if (checkbox_natural_factor_9.isChecked()) {
            naturalFactor = naturalFactor + "冰雹;";
        }
        if (checkbox_natural_factor_10.isChecked()) {
            naturalFactor = naturalFactor + "腐蚀;";
        }
        if (checkbox_natural_factor_11.isChecked()) {
            naturalFactor = naturalFactor + "沙漠化;";
        }
        if (checkbox_natural_factor_12.isChecked()) {
            EditText checkbox_natural_factor_12_et = view.findViewById(R.id.checkbox_natural_factor_12_et);
            mapBuilding.put("naturalFactor", "其他自然因素," + checkbox_natural_factor_12_et.getText());
            naturalFactor = naturalFactor + "其他:"+ checkbox_natural_factor_12_et.getText().toString()+";";
        }

                mapBuilding.put("naturalFactor", "");

        CheckBox checkbox_human_factor_1 = view.findViewById(R.id.checkbox_human_factor_1);
        CheckBox checkbox_human_factor_2 = view.findViewById(R.id.checkbox_human_factor_2);
        CheckBox checkbox_human_factor_3 = view.findViewById(R.id.checkbox_human_factor_3);
        CheckBox checkbox_human_factor_4 = view.findViewById(R.id.checkbox_human_factor_4);
        CheckBox checkbox_human_factor_5 = view.findViewById(R.id.checkbox_human_factor_5);
        CheckBox checkbox_human_factor_6 = view.findViewById(R.id.checkbox_human_factor_6);
        CheckBox checkbox_human_factor_7 = view.findViewById(R.id.checkbox_human_factor_7);
        CheckBox checkbox_human_factor_8 = view.findViewById(R.id.checkbox_human_factor_8);
        String humanFactor = "";
        if (checkbox_human_factor_1.isChecked()) {
            humanFactor = humanFactor + "战争动乱;";
        }
        if (checkbox_human_factor_2.isChecked()) {
            humanFactor = humanFactor + "生产生活活动;";
        }
        if (checkbox_human_factor_3.isChecked()) {
            humanFactor = humanFactor + "盗掘盗窃;";
        }
        if (checkbox_human_factor_4.isChecked()) {
            humanFactor = humanFactor + "不合理利用;";
        }
        if (checkbox_human_factor_5.isChecked()) {
            humanFactor = humanFactor + "违规修缮;";
        }
        if (checkbox_human_factor_6.isChecked()) {
            humanFactor = humanFactor + "年久失修;";
        }
        if (checkbox_human_factor_7.isChecked()) {
            humanFactor = humanFactor + "长期空置;";
        }
        if (checkbox_human_factor_8.isChecked()) {
            EditText checkbox_human_factor_8_et = view.findViewById(R.id.checkbox_human_factor_8_et);
            humanFactor = humanFactor + "其他人为因素:"+ checkbox_human_factor_8.getText().toString()+";";
        }
        mapBuilding.put("humanFactor", "");

        CheckBox checkbox_property_type_1 = view.findViewById(R.id.checkbox_property_type_1);
        CheckBox checkbox_property_type_2 = view.findViewById(R.id.checkbox_property_type_2);
        CheckBox checkbox_property_type_3 = view.findViewById(R.id.checkbox_property_type_3);
        CheckBox checkbox_property_type_4 = view.findViewById(R.id.checkbox_property_type_4);
        String propertyType = "";
        if (checkbox_property_type_1.isChecked()) {
            propertyType = propertyType + "国有;";
        }
        if (checkbox_property_type_2.isChecked()) {
            propertyType = propertyType + "集体;";
        }
        if (checkbox_property_type_3.isChecked()) {
            propertyType = propertyType + "个人;";
        }
        if (checkbox_property_type_4.isChecked()) {
            EditText checkbox_property_type_et = view.findViewById(R.id.checkbox_property_type_et);
            propertyType = propertyType + "其他:"+checkbox_property_type_et.getText()+";";
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

    private void volleyNetwork(Context context, Map mapBuilding) {
        String url = "";
        if (getBasicId == 0) {
            url = Constant.TB_ADD;
        }else {
            url = Constant.TB_UPDATE;
            TBasic tBasic;
            if (marker != null) {
                if (marker.getObject() instanceof TBasic) {
                    tBasic = (TBasic) marker.getObject();
                } else {
                    PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
                    tBasic = polygonBasic.getTBasic();
                }
                mapBuilding.put("basicId", String.valueOf(tBasic.getBasicId()));
            }else {
                mapBuilding.put("basicId", String.valueOf(getBasicId));
            }
        }


        VolleyUtils.create(context).post(url, TBasic.class, new VolleyUtils.OnResponse<TBasic>() {
            @Override
            public void OnMap(Map<String, String> map) {
                map.putAll(mapBuilding);
            }

            @Override
            public void onSuccess(TBasic response) {
                Log.e("success", "response---->" + response);
                XToast.normal(context, "保存成功").show();
                int id = response.getBasicId();

                FileInputStream fis = null;
                String picture = "";

                if (drawMediaList.size() > 0) {
                    for (LocalMedia drawMedia : drawMediaList
                    ) {
                        try {
                            fis = new FileInputStream(drawMedia.getRealPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        picture = VolleyUtils.create(getContext()).bitmapToBase64(bitmap);
                        String finalPicture = picture;
                        VolleyUtils.create(context).post(Constant.TD_ADD, TDraw.class, new VolleyUtils.OnResponse<TDraw>() {

                            @Override
                            public void OnMap(Map<String, String> map) {
                                map.put("basicId", String.valueOf(id));
                                map.put("fileName", "");
                                map.put("drawProportion", "");
                                map.put("drawName", "");
                                map.put("drawPath", finalPicture);
                                map.put("drawDate", "");
                            }

                            @Override
                            public void onSuccess(TDraw response) {
                                Log.e("draw success", "TDraw response---->" + response);
                                XToast.normal(context, "测绘图纸保存成功").show();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("draw error", "TDraw error---->" + error);
                                XToast.normal(context, "测绘图纸保存失败" + error).show();
                            }
                        });
                    }
                }

                if (imageMediaList.size() > 0) {
                    for (LocalMedia imageMedia : imageMediaList
                    ) {
                        try {
                            fis = new FileInputStream(imageMedia.getRealPath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        picture = VolleyUtils.create(getContext()).bitmapToBase64(bitmap);
                        String finalPicture = picture;
                        VolleyUtils.create(context).post(Constant.TI_ADD, TDraw.class, new VolleyUtils.OnResponse<TDraw>() {

                            @Override
                            public void OnMap(Map<String, String> map) {
                                map.put("basicId", String.valueOf(id));
                                map.put("imageName", "");
                                map.put("photoName", "");
                                map.put("imagePath", finalPicture);
                                map.put("imageDate", "");
                            }

                            @Override
                            public void onSuccess(TDraw response) {
                                Log.e("draw success", "TDraw response---->" + response);
                                XToast.normal(context, "影像保存成功").show();
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("draw error", "TDraw error---->" + error);
                                XToast.normal(context, "影像保存失败" + error).show();
                            }
                        });
                    }
                }

                if(marker != null){
                    TBasic tBasic;
                    if (marker.getObject() instanceof TBasic) {
                        marker.setObject(response);
                    }else {
                        PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
                        polygonBasic.setTBasic(response);
                    }
                }

            }

            @Override
            public void onError(String error) {
                Log.e("error", "error---->" + error);
                XToast.normal(context, "保存失败").show();
            }
        });

    }
}
