package com.hyht.amap_historical_building.listener;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.bin.david.form.data.column.Column;
import com.hyht.amap_historical_building.dialog.DialogOverlayDetail;
import com.hyht.amap_historical_building.entity.TBasic;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.List;
/**
 * 条目点击监听
 */
public class OnColumnItemClickListener implements com.bin.david.form.listener.OnColumnItemClickListener {
    private Context context;
    private List<TBasic> basicList;
    private MaterialDialog materialDialog;
    private AMap aMap;

    public OnColumnItemClickListener(Context context, List<TBasic> basicList, MaterialDialog materialDialog, AMap aMap) {
        this.context = context;
        this.basicList = basicList;
        this.materialDialog = materialDialog;
        this.aMap = aMap;
    }

    @Override
    public void onClick(Column column, String value, Object o, int position) {
        materialDialog.dismiss();
        TBasic tBasic = basicList.get(position);
        DialogOverlayDetail dialogOverlayDetail = new DialogOverlayDetail(context, tBasic, aMap);
    }
}
