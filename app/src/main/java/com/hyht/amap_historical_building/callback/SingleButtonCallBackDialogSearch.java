package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.EntService;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogSelectAllOverlays;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.listener.OnColumnItemClickListener;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputCallback;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.toast.XToast;

import java.lang.reflect.Type;
import java.util.List;

import static com.xuexiang.xui.XUI.getContext;

public class SingleButtonCallBackDialogSearch implements MaterialDialog.SingleButtonCallback {
    Context context;
    AMap aMap;

    public SingleButtonCallBackDialogSearch(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        DialogLoader.getInstance().showInputDialog(context,
                R.drawable.layout,
                "搜索",
                "根据输入的值进行模糊查询",
                new InputInfo(InputType.TYPE_CLASS_TEXT, "请输入"),
                new InputCallback() {
                    @Override
                    public void onInput(@NonNull DialogInterface dialog, CharSequence input) {
                    }
                },
                "确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (dialog instanceof MaterialDialog) {
                            final RequestQueue requestQueue = Volley.newRequestQueue(context);
                            //创建一个请求
                            StringRequest stringRequest = new StringRequest(Constant.TB_GETAllLike+ "?cityType=" + ((MaterialDialog) dialog).getInputEditText().getText().toString(), new Response.Listener<String>() {
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
                                            .neutralText("搜索")
                                            .onNeutral(new SingleButtonCallBackDialogSearch(context, aMap))
                                            .cancelable(false)
                                            .show();

                                    //普通列
                                    Column<String> column1 = new Column<>("建筑名称", "buildingName");
                                    column1.setOnColumnItemClickListener(new OnColumnItemClickListener(context, basicList, materialDialog, aMap));
                                    Column<String> column2 = new Column<>("编号", "buildingNumber");
                                    column2.setOnColumnItemClickListener(new OnColumnItemClickListener(context, basicList, materialDialog, aMap));
                                    final TableData<TBasic> tableData = new TableData<TBasic>("建筑基本档案", basicList, column1, column2);
                                    //设置数据
                                    SmartTable table = materialDialog.getCustomView().findViewById(R.id.table);
                                    table.getConfig().setMinTableWidth(materialDialog.getWindow().getAttributes().width);
                                    table.getConfig().setShowXSequence(false).setShowYSequence(false);
                                    table.setTableData(tableData);

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
                },
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DialogSelectAllOverlays dialogSelectAllOverlays = new DialogSelectAllOverlays(context, aMap);
                    }
                }
        );
    }
}
