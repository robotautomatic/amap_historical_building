package com.hyht.amap_historical_building.callback;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.amap.api.maps.AMap;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyht.amap_historical_building.Constant;
import com.hyht.amap_historical_building.MainActivity;
import com.hyht.amap_historical_building.R;
import com.hyht.amap_historical_building.dialog.DialogSelectAllOverlays;
import com.hyht.amap_historical_building.dialog.SimplePopupShowOverlaysImp;
import com.hyht.amap_historical_building.entity.TBasic;
import com.hyht.amap_historical_building.utils.VolleyUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import static com.xuexiang.xui.utils.ResUtils.getColor;

/**
 * 删除建筑物
 */
public class SingleButtonCallbackDelete implements MaterialDialog.SingleButtonCallback {
    private TBasic tBasic;
    private Context context;
    private AMap aMap;

    public SingleButtonCallbackDelete(TBasic tBasic, Context context, AMap aMap) {
        this.tBasic = tBasic;
        this.context = context;
        this.aMap = aMap;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        MaterialDialog fatherDialog = dialog;
        MaterialDialog dia_delete = new MaterialDialog.Builder(context)
                .title("警告").titleColor(getColor(R.color.colorAccent))
                .content("此操作会将选中的建筑永久删除且无法恢复")
                .positiveText("删除").positiveColor(getColor(R.color.colorAccent)).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String basicUrl = Constant.TB_DELETE + "?basicId=" + tBasic.getBasicId();
                        deleteNetwork(basicUrl);
                        String drawUrl = Constant.TD_DELETE + "?basicId=" + tBasic.getBasicId();
                        deleteNetwork(drawUrl);
                        String imageUrl = Constant.TI_DELETE + "?basicId=" + tBasic.getBasicId();
                        deleteNetwork(imageUrl);
                    }
                })
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        fatherDialog.show();
                    }
                })
                .cancelable(false)
                .show();
    }
    private void deleteNetwork(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request_Delete = new StringRequest(StringRequest.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("delete success", "删除成功---->" + response);
                aMap.clear(true);
                new SimplePopupShowOverlaysImp(context, aMap, 0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("delete success", "删除失败---->" + error);
            }
        });
        requestQueue.add(request_Delete);
    }
}
