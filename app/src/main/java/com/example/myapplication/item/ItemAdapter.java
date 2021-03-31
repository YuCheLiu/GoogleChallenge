package com.example.myapplication.item;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.database.Item;

import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter {
    private final Context myContext;
    private final ArrayList<Item> items;
    private final Resources resources;

    public ItemAdapter(Context context, ArrayList<Item> items){
        this.myContext = context;
        this.items = items;
        this.resources = context.getResources();
    }

    @Override
    public int getCount() {
        return items.size();
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
        // 1
        final Item item = items.get(position);

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
        imageView.setImageResource(resources.getIdentifier(item.getIcon(), "drawable", myContext.getPackageName()));
        itemValueTextView.setText(String.valueOf(item.getValue()) + " G");
        itemNameTextView.setText(item.getName());
        amountTextView.setVisibility(View.GONE);
        convertView.setBackgroundResource(R.drawable.round_corner);
        return convertView;
    }

}
