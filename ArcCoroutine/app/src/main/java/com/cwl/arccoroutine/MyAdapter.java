package com.cwl.arccoroutine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public ArrayList<String> datas = null;

    private int itemLayoutId;

    public MyAdapter(ArrayList<String> datas) {
        this.datas = datas;
        itemLayoutId = android.R.layout.simple_list_item_1;
    }

    public MyAdapter(ArrayList<String> datas, int itemLayoutId) {
        this.datas = datas;
        this.itemLayoutId = itemLayoutId;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayoutId, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tv.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}