package com.quynhlm.dev.lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quynhlm.dev.lab5.Model.Distribute;
import com.quynhlm.dev.lab5.R;

import java.util.List;

public class DistributorAdapter extends BaseAdapter {
    Context context;
    List<Distribute> list;

    public DistributorAdapter(Context context, List<Distribute> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_distributor_spinner,parent,false);
        TextView tv_name = convertView.findViewById(R.id.tv_distrbute_spinner);
        tv_name.setText(list.get(position).getName());
        return convertView;
    }
}
