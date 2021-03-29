package com.example.googlechallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.example.googlechallenge.R.drawable.bronzethread;

public class ItemInventory extends AppCompatActivity {

    // for now, just use the test data
    Item bronzeThread = new Item("Bronze Thread", bronzethread, 1, 5, 3);
    Item silverThread = new Item("Silver Thread", R.drawable.silverthread, 2, 10, 2);
    Item goldThread = new Item("Gold Thread", R.drawable.goldthread, 3, 15, 1);

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    ArrayList<Item> storeItems = new ArrayList<Item>();
    UserItemAdapter userItemAdapter;
    ItemAdapter storeAdapter;
    GridView gridView;
    GridView storeGridView;
    TextView goldText;
    Button sellBtn;
    Button buyBtn;

    Toast text;
    LinkedHashMap<Item, Integer> userItems = new LinkedHashMap<>();
    Item[] keys;

    // User Profile
    int userGold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_inventory);

        sharedPref = this.getSharedPreferences("com.example.googlechallenge", MODE_PRIVATE);
        editor = sharedPref.edit();
        //TODO: Change the default value back to 0
        userGold = sharedPref.getInt("userGold", 100);





        getItems();
        saveObject(getApplicationContext(), "userItems", userItems);
        sellBtn = findViewById(R.id.sellBtn);
        buyBtn = findViewById(R.id.buyBtn);
        gridView = findViewById(R.id.gridView);
        goldText = findViewById(R.id.userGold);
        storeGridView = findViewById(R.id.storeGridView);

        storeAdapter = new ItemAdapter(this, storeItems);
        userItemAdapter = new UserItemAdapter(this, userItems);
        keys = userItems.keySet().toArray(new Item[userItems.size()]);

        goldText.setText(String.valueOf(userGold));
        gridView.setAdapter(userItemAdapter);
        storeGridView.setAdapter(storeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean[] hasSold = {false};
                sellBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("update", "1");
                        if (hasSold[0]){
                            return;
                        }

                        hasSold[0] = sellItem(view, position);

                    }
                });
            }
        });

        storeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        buyItem(view, position);
                    }
                });
            }
        });


    }

    public boolean sellItem(View view, int position){
        controlText();
        if (userItems.size() == 0){
            return false;
        }
        Item item = keys[position];
        int amount = userItems.get(item);
        userGold += item.getValue();
        updateItem(item, amount - 1);
        updateGold();
        text.setText("You have sold " + item.getName() + " !");
        text.show();
        return true;
    }

    public void updateItem(Item item, int amount){
        LinkedHashMap<Item, Integer> newUserItems = new LinkedHashMap<>(userItems);

        if (amount == 0) {

            newUserItems.remove(item);

        } else {

            newUserItems.put(item, amount);

        }

        userItemAdapter = new UserItemAdapter(getApplicationContext(), newUserItems);
        userItems = newUserItems;
        keys = userItems.keySet().toArray(new Item[userItems.size()]);
        gridView.setAdapter(userItemAdapter);
        saveObject(getApplicationContext(), "userItems", userItems);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buyItem(View view, int position){

        controlText();
        final Item item = storeItems.get(position);
        final int price = item.getValue();
        int amount = userItems.getOrDefault(item, 0);
        if (userGold >= price){
            userGold -= price;
            updateItem(item, amount + 1);
            text.setText("Success!");

        }else{

            text.setText("You don't have enough golds!");

        }

        updateGold();
        text.show();
    }

    public void controlText() {
        if (text == null){

            text = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        } else {

            text.cancel();

        }
    }
    public void updateGold(){

        goldText.setText(String.valueOf(userGold));
        editor.putInt("userGold", userGold).apply();

    }


    public void getItems(){
        // get items from user data

        userItems = (LinkedHashMap<Item, Integer>) readObj(getApplicationContext(), "userItems");
        storeItems = new ArrayList<Item>(Arrays.asList(new Item[] {bronzeThread, silverThread, silverThread, goldThread}));



    }

   public void saveObject(Context mContext, String filename, Object obj){
        try {
            FileOutputStream f = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(obj);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    public Object readObj(Context mContext, String filename) {
        try {
            FileInputStream fis = mContext.openFileInput(filename + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedHashMap<Item, Integer>();
        }
    }

}