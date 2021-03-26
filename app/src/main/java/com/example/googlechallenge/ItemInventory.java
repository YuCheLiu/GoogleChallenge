package com.example.googlechallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.googlechallenge.R.drawable.bronzethread;

public class ItemInventory extends AppCompatActivity {
    // Use getGold() method to get user's gold amount;
    int userGold = 0;
    ArrayList<Item> testItems = new ArrayList<Item>();
    ArrayList<Item> storeItems = new ArrayList<Item>();
    GridView gridView;
    GridView storeGridView;
    Button sellBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_inventory);

        getItems();
        sellBtn = findViewById(R.id.sellBtn);
        gridView = findViewById(R.id.gridView);
        storeGridView = findViewById(R.id.storeGridView);
        ItemAdapter itemAdapter = new ItemAdapter(this, testItems);
        ItemAdapter storeAdapter = new ItemAdapter(this, storeItems);
        gridView.setAdapter(itemAdapter);
        storeGridView.setAdapter(storeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("selected item", String.valueOf(position));
                sellBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sellItem(view, position);
                    }
                });
            }
        });


    }

    public void sellItem(View view, int position){
        Log.i("I want to sell", String.valueOf(position));
        userGold += testItems.get(position).getValue();
        Log.i("Now I have", String.valueOf(userGold));
        testItems.remove(position);
        view.setVisibility(View.GONE);
    }

    public void getItems(){
        // get items from user data
        // for now, just use the test data
        Item bronzeThread = new Item("Bronze Thread", bronzethread, 1, 5, 3);
        Item silverThread = new Item("Silver Thread", R.drawable.silverthread, 2, 10, 2);
        Item goldThread = new Item("Gold Thread", R.drawable.goldthread, 3, 15, 1);
        ArrayList<Item> items = new ArrayList<Item>(Arrays.asList(new Item[] {bronzeThread, silverThread, goldThread}));

        testItems = items;
        storeItems = new ArrayList<Item>(Arrays.asList(new Item[] {bronzeThread, silverThread, silverThread, goldThread}));



    }
}