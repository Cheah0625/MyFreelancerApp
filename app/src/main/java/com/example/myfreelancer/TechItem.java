package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class TechItem extends AppCompatActivity {

    private TextView tv_NoTService, tv_NoTService2;
    private ImageView iv_NoTService;

    private ImageButton TechBackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewTechItemList;
    private AdapterServiceItem adapterServiceItem;
    private FirebaseAuth auth;
    private EditText TechSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_item);

        tv_NoTService = findViewById(R.id.tv_NoTService);
        tv_NoTService2 = findViewById(R.id.tv_NoTService2);
        iv_NoTService = findViewById(R.id.iv_NoTService);

        TechSearch = findViewById(R.id.TechSearch);
        TechBackToMainServiceList = findViewById(R.id.TechBackToMainServiceList);
        recyclerViewTechItemList = findViewById(R.id.recyclerViewTechItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        TechSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterServiceItem.getFilter().filter(s);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TechBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent TechBack2serList = new Intent(TechItem.this, BuyerHome.class);
                //startActivity(TechBack2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Programming & Tech").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all tech Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }

                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(TechItem.this, ServiceItemList);
                //set adapter
                recyclerViewTechItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoTService.setVisibility(View.VISIBLE);
                    tv_NoTService2.setVisibility(View.VISIBLE);
                    iv_NoTService.setVisibility(View.VISIBLE);
                    TechSearch.setVisibility(View.GONE);
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