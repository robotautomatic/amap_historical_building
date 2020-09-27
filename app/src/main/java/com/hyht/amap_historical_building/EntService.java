package com.hyht.amap_historical_building;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xuexiang.xui.widget.toast.XToast;

import static com.xuexiang.xui.XUI.getContext;

public class EntService {
    private Context context;
    private String urlStr = Constant.TB_GETAll;

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public EntService(Context context) {
        this.context = context;
    }

    //查询所有的绘图
    void buildSelect(final VolleyCallback callback) {
        //创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //创建一个请求
        StringRequest stringRequest = new StringRequest(urlStr, new Response.Listener<String>() {
            //正确接受数据之后的回调
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {//发生异常之后的监听回调
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        //将创建的请求添加到请求队列当中
        requestQueue.add(stringRequest);
    }
    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(VolleyError error);
    }
}
