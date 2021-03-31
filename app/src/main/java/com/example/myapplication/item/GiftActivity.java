package com.example.myapplication.item;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.Item;
import com.example.myapplication.database.WordViewModel;
import com.example.myapplication.ui.main.PlaceholderFragment;

import java.util.Random;

public class GiftActivity extends com.example.myapplication.item.ItemInventory {

    public Item[] allGiftList = {bronzeThread, silverThread, goldThread};
    public Item[] rareGiftList = {silverThread, goldThread};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        readAllItemsFromDatabase();
    }

    // 0: Invalid sleeping; duration < 0.5 hour || duration >= 24 hours
    // 1: Valid sleeping; 0.5 hour < duration <= 6 hours
    // 2: Valid and healthy sleeping; 6 < duration < 24 hours
    public static int SleepingStatus = 1;
    private boolean hasReceivedGift = false;
    public int counter = 0;

    public void openGift(View view) throws InterruptedException {
        Item[] newGifts = getGift();
        if (hasReceivedGift || newGifts.length == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }

//        giftAnimation(newGifts);
        hasReceivedGift = true;

    }

    public void giftAnimation(Item[] newGifts) {
        ImageView itemImageView = findViewById(R.id.itemImageView);
        Animation ani = new AlphaAnimation(0.00f, 1.00f);
        Animation aniEnd = new AlphaAnimation(1.00f, 0.00f);
        ani.setDuration(1800);
        aniEnd.setDuration(800);
        ani.setAnimationListener(new Animation.AnimationListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onAnimationStart(Animation animation) {
                Item newGift = newGifts[counter];
                int amount = userItems.getOrDefault(newGift, 0);
                updateItem(newGift, amount + 1);
                String name = newGift.getName();
                String icon = newGift.getIcon();
                itemImageView.setImageResource(getResources().getIdentifier(icon, "drawable", getPackageName()));
                Toast.makeText(getApplicationContext(), "Congratulations! you get a " + name.toLowerCase() + " and some gold coins!", Toast.LENGTH_SHORT).show();


            }

            public void onAnimationRepeat(Animation animation) {


            }

            public void onAnimationEnd(Animation animation) {
                t.cancel();
                itemImageView.startAnimation(aniEnd);
                if (counter < newGifts.length - 1) {
                    counter += 1;
                    itemImageView.startAnimation(ani);
                }

            }

        });
        itemImageView.startAnimation(ani);
    }


    public Item[] getGift(){

        //
        if (SleepingStatus == 2){
            // do something
            Random generator = new Random();
            int randomIndex = generator.nextInt(rareGiftList.length);
            PlaceholderFragment.updateGold(PlaceholderFragment.getGold() + 20);
            return new Item[] {rareGiftList[randomIndex]};
        }
        //TODO: After testing reset the first condition to == 1
        if(SleepingStatus == 0){
            Random generator = new Random();
            int randomIndex = generator.nextInt(allGiftList.length);
            PlaceholderFragment.updateGold(PlaceholderFragment.getGold() + 10);
            return new Item[] {allGiftList[randomIndex]};
        }
        return new Item[] {};

    }

    public void readAllItemsFromDatabase(){

        WordViewModel mWordViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);
        mWordViewModel.getAllItems().observe(this, items -> {
            Random random = new Random();

            int index = random.nextInt(items.size());
            Item newItem = items.get(index);
            Item[] newGifts = new Item[] {newItem};


            Log.i("new gift", newItem.getName());
            giftAnimation(newGifts);

        });
    }
}