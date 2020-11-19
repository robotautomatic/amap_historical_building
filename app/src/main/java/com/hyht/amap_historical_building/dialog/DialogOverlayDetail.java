package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.WebActivity;
import com.hyht.amap_historical_building.adapter.PreviewRecycleAdapter;
import com.hyht.amap_historical_building.adapter.SelectDrawRecyclerAdapter;
import com.hyht.amap_historical_building.callback.DialogSingleButtonCallBackEditor;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackHideOverlayOnMap;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackShowOverlayOnMap;
import com.hyht.amap_historical_building.entity.*;
import com.hyht.amap_historical_building.utils.DefaultButton;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.just.agentweb.AgentWeb;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.app.ActivityUtils;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (marker.getObject() instanceof TBasic) {
            tBasic = (TBasic) marker.getObject();
        }else {
            PolygonBasic polygonBasic = (PolygonBasic) marker.getObject();
            tBasic = polygonBasic.getTBasic();
        }
        getDialog();
    }

    private void getDialog() {
        List<LocalMedia> drawMediaList = new ArrayList<>();
        List<LocalMedia> imageMediaList = new ArrayList<>();
        MaterialDialog.SingleButtonCallback singleButtonCallback = null;
        MaterialDialog materialDialog;
        if (marker == null){
            negativeText = "显示在地图上";
            singleButtonCallback = new SingleButtonCallBackShowOverlayOnMap(aMap, tBasic, context);
            materialDialog = new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_overlay_detail, true)
                    .positiveText("确认")
                    .negativeText(negativeText)
                    .onNegative(singleButtonCallback)
                    .neutralText("编辑")
                    .onNeutral(new DialogSingleButtonCallBackEditor(tBasic, context, aMap, drawMediaList, imageMediaList))
                    .cancelable(false)
                    .show();
        }else {
            negativeText = "在地图上隐藏";
            singleButtonCallback = new SingleButtonCallBackHideOverlayOnMap(marker, aMap);
            materialDialog = new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_overlay_detail, true)
                    .positiveText("确认")
                    .negativeText(negativeText)
                    .onNegative(singleButtonCallback)
                    .neutralText("编辑")
                    .onNeutral(new DialogSingleButtonCallBackEditor(context, aMap, marker, drawMediaList, imageMediaList))
                    .cancelable(false)
                    .show();
        }

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

        ProgressManager.getInstance().with(new OkHttpClient.Builder())
                .build();
        List<Uri> drawUriList =  new ArrayList<>();
        VolleyUtils.create(context).get(Constant.TD_GET, TDraw.class, new VolleyUtils.OnResponses<TDraw>() {
            @Override
            public void OnMap(Map<String, String> map) {
                map.put("basicId", String.valueOf(tBasic.getBasicId()));
            }

            @Override
            public void onSuccess(List<TDraw> response) {
                System.out.println("td rrrr = "+ response);
                for (TDraw tDraw : response
                     ) {
                   drawUriList.add(Uri.parse(Constant.BASE_URL + tDraw.getDrawPath()));
                }

                RecyclerView drawRecyclerView = materialDialog.getCustomView().findViewById(R.id.rv_draw);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                drawRecyclerView.setLayoutManager(linearLayoutManager);
                PreviewRecycleAdapter drawAdapter = new PreviewRecycleAdapter(context, drawUriList);
                drawRecyclerView.setAdapter(drawAdapter);
            }

            @Override
            public void onError(String error) {

            }
        });

        List<Uri> imageUriList =  new ArrayList<>();
        VolleyUtils.create(context).get(Constant.TI_GET, TImage.class, new VolleyUtils.OnResponses<TImage>() {
            @Override
            public void OnMap(Map<String, String> map) {
                map.put("basicId", String.valueOf(tBasic.getBasicId()));
            }

            @Override
            public void onSuccess(List<TImage> response) {
                for (TImage tImage : response
                ) {
                    imageUriList.add(Uri.parse(Constant.BASE_URL + tImage.getImagePath()));
                }
                System.out.println("iiiiii"+imageUriList);
                RecyclerView imageRecyclerView = materialDialog.getCustomView().findViewById(R.id.rv_image);
                LinearLayoutManager imageLinearLayoutManager = new LinearLayoutManager(context);
                imageLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                imageRecyclerView.setLayoutManager(imageLinearLayoutManager);
                PreviewRecycleAdapter imageAdapter = new PreviewRecycleAdapter(context, imageUriList);
                imageRecyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onError(String error) {

            }
        });


        List<Uri> panoramaSketchUriList =  new ArrayList<>();
        List<Integer> panoramaSketchID = new ArrayList<>();
        VolleyUtils.create(context).get(Constant.TTD_GET+"/"+tBasic.getBasicId(), TTD.class, new VolleyUtils.OnResponses<TTD>() {
            @Override
            public void OnMap(Map<String, String> map) {
            }

            @Override
            public void onSuccess(List<TTD> response) {
                for (TTD ttd : response
                ) {
                    panoramaSketchUriList.add(Uri.parse(Constant.TTD_PREVIEW+ttd.getTdPath()));
                    panoramaSketchID.add(ttd.getTdId());

                    RecyclerView panoramaSketch = materialDialog.getCustomView().findViewById(R.id.rv_panorama_sketch);
                    LinearLayoutManager panoramaSketchManager = new LinearLayoutManager(context);
                    panoramaSketchManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    panoramaSketch.setLayoutManager(panoramaSketchManager);
                    PreviewRecycleAdapter panoramaSketchAdapter = new PreviewRecycleAdapter(context, panoramaSketchUriList);
                    panoramaSketchAdapter.setViewClickListener(new PreviewRecycleAdapter.OnAdapterItemClickListener() {
                        @Override
                        public View.OnClickListener onItemClick(int i) {
                            return new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = Constant.TTD_GET_Panorama_Sketch + panoramaSketchID.get(i);
                                    Intent intent = new Intent(context,WebActivity.class);
                                    intent.putExtra("url", url);
                                    context.startActivity(intent);
                                }
                            };
                        }
                    });
                    panoramaSketch.setAdapter(panoramaSketchAdapter);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
