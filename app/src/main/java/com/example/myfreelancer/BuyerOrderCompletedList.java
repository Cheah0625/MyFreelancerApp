package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyerOrderCompletedList extends AppCompatActivity {

    private TextView tv_NoCOrder;
    private ImageView iv_NoCOrder;
    private ImageButton CompletedBackToOrderList;
    private Button btn_rated;

    private ArrayList<Model_Order> BuyerOrderCompletedList;
    private RecyclerView recyclerViewBuyerCompletedList;
    private AdapterOrderCompleted adapterOrderCompleted;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_completed_list);

        tv_NoCOrder = findViewById(R.id.tv_NoCOrder);
        iv_NoCOrder = findViewById(R.id.iv_NoCOrder);
        CompletedBackToOrderList = findViewById(R.id.CompletedBackToOrderList);
        btn_rated = findViewById(R.id.btn_rated);
        recyclerViewBuyerCompletedList = findViewById(R.id.recyclerViewBuyerCompletedList);

        loadCompletedOrders();
        loadAdapter();

        //ArrayList<Model_Order> BuyerOrderCompletedList = new ArrayList<>();

        ///BuyerOrderCompletedList.add(new Model_Order(R.drawable.service_image, "Design minimalist and creative logo", "Graphics & Design ", "Yan Yan ","3","Completed","20.50" ));
        //BuyerOrderCompletedList.add(new Model_Order(R.drawable.service_image, "Design minimalist and creative logo", "Graphics & Design ", "goh Lim ","13","Completed","210" ));
        //BuyerOrderCompletedList.add(new Model_Order(R.drawable.service_image, "Design minimalist and creative logo", "Writing & Translation", "jane Lim ","23","Completed","110.00" ));
        //BuyerOrderCompletedList.add(new Model_Order(R.drawable.service_image, "Design minimalist and creative logo", "Graphics & Design ", "Kenny Lim ","3","Completed","210.50" ));

        //recyclerViewBuyerCompletedList.setHasFixedSize(true);

        //adapterOrderCompleted = new AdapterOrderCompleted(this,BuyerOrderCompletedList);
        //recyclerViewBuyerCompletedList.setLayoutManager(new LinearLayoutManager(this));

        //recyclerViewBuyerCompletedList.setAdapter(adapterOrderCompleted);


        CompletedBackToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent compBack2ordList = new Intent(BuyerOrderCompletedList.this, Orders.class);
                //startActivity(compBack2ordList);
            }
        });


    }

    private void loadAdapter() {
        recyclerViewBuyerCompletedList.setHasFixedSize(true);
        recyclerViewBuyerCompletedList.setLayoutManager(new LinearLayoutManager(this));
        //setup adapter
        adapterOrderCompleted = new AdapterOrderCompleted(BuyerOrderCompletedList.this, BuyerOrderCompletedList);
        //set adapter
        recyclerViewBuyerCompletedList.setAdapter(adapterOrderCompleted);
    }

    private void loadCompletedOrders() {
        BuyerOrderCompletedList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        databaseReference.orderByChild("BuyerID").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BuyerOrderCompletedList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Order model_order = ds.getValue(Model_Order.class);
                    //OrderPendingList.add(model_order);

                    //show all completed orders
                    String oStatus = ""+ds.child("OStatus").getValue();
                    if(oStatus.equals("Completed")) {
                        BuyerOrderCompletedList.add(0, model_order);
                    }
                }
                adapterOrderCompleted.notifyDataSetChanged();

                if(BuyerOrderCompletedList.isEmpty()){
                    tv_NoCOrder.setVisibility(View.VISIBLE);
                    iv_NoCOrder.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}