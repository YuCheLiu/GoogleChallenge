package com.example.googlechallenge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class TestAdapter extends BaseAdapter {
    private final Context myContext;
    private final LinkedHashMap<Item, Integer> items;
    final Item[] keys;

    public TestAdapter(Context context, LinkedHashMap<Item, Integer> items){
        this.myContext = context;
        this.items = items;
        this.keys = items.keySet().toArray(new Item[items.size()]);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(keys[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final Item item = keys[position];
        final int amount = items.get(keys[position]);
        Log.i("get View", (String) item.getName());
        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(myContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_item, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView itemNameTextView = (TextView)convertView.findViewById(R.id.itemNameTextView);
        final TextView itemValueTextView = (TextView)convertView.findViewById(R.id.itemValueTextView);
        final TextView amountTextView = (TextView) convertView.findViewById(R.id.amountTextView);

        // 4
        imageView.setImageResource(item.getIcon());
        itemValueTextView.setText(String.valueOf(item.getValue()) + " G");
        itemNameTextView.setText(item.getName());
        amountTextView.setText('x'+String.valueOf(items.get(keys[position])));


        return convertView;
    }

}
