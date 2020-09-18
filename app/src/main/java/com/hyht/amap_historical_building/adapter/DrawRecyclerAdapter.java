package com.hyht.amap_historical_building.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hyht.amap_historical_building.R;

import java.util.List;

public class DrawRecyclerAdapter extends RecyclerView.Adapter<DrawRecyclerAdapter.ViewHolder> {
    private List<String> drawList;

    public DrawRecyclerAdapter(List<String> drawList) {
        this.drawList = drawList;
        System.out.println("???");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.draw_item, parent, false);
        System.out.println("onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("onbind");
        holder.textView.setText(drawList.get(position));
        holder.itemView.findViewById(R.id.iv_select_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("ViewHoldera");
        }
    }

}
