package com.hyht.amap_historical_building.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.draw.MultiLineDrawFormat;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.callback.SingleButtonCallBackDialogSearch;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnColumnItemClickListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * 选择显示建筑物类型弹窗
 */
public class DialogSelectAllOverlays {
    private Context context;
    private AMap aMap;

    public DialogSelectAllOverlays(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
        getDialog();
    }

    private void getDialog(){
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        //创建一个请求
        StringRequest stringRequest = new StringRequest(Constant.TB_GETAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Type type = new TypeToken<List<TBasic>>() {
                }.getType();
                final Gson gson = new Gson();
                final List<TBasic> basicList = gson.fromJson(response, type);


                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.dialog_select_overlay, false)
                        .iconRes(R.drawable.ic_build)
                        .title("历史建筑列表")
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .neutralText("搜索")
                        .onNeutral(new SingleButtonCallBackDialogSearch(context, aMap))
                        .cancelable(false)
                        .build();


                Column<String> column1 = new Column<>("建筑名称", "buildingName", new MultiLineDrawFormat<String>(materialDialog.getWindow().getAttributes().width/5*3));
                Column<String> column2 = new Column<>("编号", "buildingNumber", new MultiLineDrawFormat<String>(materialDialog.getWindow().getAttributes().width/5*2));
                final TableData<TBasic> tableData = new TableData<TBasic>("建筑基本档案", basicList, column1, column2);
                //设置数据
                SmartTable table = materialDialog.getCustomView().findViewById(R.id.table);
                table.getConfig().setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
                table.getConfig().setHorizontalPadding(0);table.getConfig().setColumnTitleHorizontalPadding(0);
                //table.setZoom(true,3);是否缩放
                table.setTableData(tableData);
                /*table.setCanVerticalScroll(false);*/
                column1.setOnColumnItemClickListener(new OnColumnItemClickListener(context, basicList, materialDialog, aMap));
                column2.setOnColumnItemClickListener(new OnColumnItemClickListener(context, basicList, materialDialog, aMap));
                materialDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                XToast.normal(context, "错误，无法获取信息" + error.getMessage()).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
