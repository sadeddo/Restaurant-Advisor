package com.restaurantadvisor.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewMenuAdapter extends BaseAdapter {
    private Context context;
    private List<Menus> menusList;

    public ListViewMenuAdapter (Context context, List menusList) {
        this.context = context;
        this.menusList = menusList;
    }

    @Override
    public int getCount() {
        return menusList.size();
    }

    @Override
    public Object getItem(int position) {
        return menusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R. layout.menu_square, null);
        }
        Menus menu = menusList.get(position);

        TextView textViewMenuName = (TextView) convertView.findViewById(R.id.menu_name);
        TextView textViewMenuDescription = (TextView) convertView.findViewById(R.id.menu_description);
        TextView textViewMenuPrice = (TextView) convertView.findViewById(R.id.menu_price);

        textViewMenuName.setText(menu.getNameMenu());
        textViewMenuDescription.setText(menu.getDescriptionMenu());
        textViewMenuPrice.setText(String.valueOf(menu.getPriceMenu()));
        return convertView;
    }
}
