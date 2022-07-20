package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tasks extends AppCompatActivity {

    private Button btn_taskPending, btn_taskProgress, btn_taskCompleted, btn_taskCancel;
    private ImageButton btn_tSeller_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        btn_taskCancel = findViewById(R.id.btn_taskCancel);
        btn_tSeller_message = findViewById(R.id.btn_tSeller_message);
        btn_taskPending = findViewById(R.id.btn_taskPending);
        btn_taskProgress = findViewById(R.id.btn_taskProgress);
        btn_taskCompleted = findViewById(R.id.btn_taskCompleted);

        BottomNavigationView sellerNV = findViewById(R.id.sellerNavigation);

        btn_tSeller_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent STaskIntentToChat = new Intent(Tasks.this, ChatMessage.class);
                startActivity(STaskIntentToChat);
            }
        });

        //set Tasks Selected
        sellerNV.setSelectedItemId(R.id.sellerTask);
        sellerNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerHome:
                        startActivity(new Intent(getApplicationContext(), BrowseJobList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.sellerTask:
                        return true;

                    case R.id.sellerAccount:
                        startActivity(new Intent(getApplicationContext(), SellerAccount.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        btn_taskPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskPendingLs = new Intent(Tasks.this, SellerTaskPendingList.class);
                startActivity(intent2taskPendingLs);
            }
        });

        btn_taskProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskProgressLs = new Intent(Tasks.this, SellerTaskAcceptedList.class);
                startActivity(intent2taskProgressLs);
            }
        });

        btn_taskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskCompleteLs = new Intent(Tasks.this, SellerTaskCompletedList.class);
                startActivity(intent2taskCompleteLs);
            }
        });

        btn_taskCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2taskCancelLs = new Intent(Tasks.this, SellerTaskCancelList.class);
                startActivity(intent2taskCancelLs);
            }
        });
    }
}