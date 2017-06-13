package com.hui.devframe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hui.devframe.R;
import com.hui.devframe.common.base.BaseActivity;

/**
 * Created by wanghui on 16/9/29.
 */
public class ItemAdapter extends BaseAdapter {

    private BaseActivity initActivity;
    private ArrayList<String> mList = new ArrayList<>();

    public ItemAdapter(BaseActivity initActivity, ArrayList<String> mList) {
        this.initActivity = initActivity;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(initActivity).inflate(R.layout.ui_grid_item, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.item_tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivity.toast("test");
            }
        });
        return convertView;
    }
}
