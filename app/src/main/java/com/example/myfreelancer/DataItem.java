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

public class DataItem extends AppCompatActivity {

    private TextView tv_NoDService, tv_NoDService2;
    private ImageView iv_NoDService;

    private ImageButton DataBackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewDataItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText DataSearch;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_item);

        tv_NoDService = findViewById(R.id.tv_NoDService);
        tv_NoDService2 = findViewById(R.id.tv_NoDService2);
        iv_NoDService = findViewById(R.id.iv_NoDService);

        DataSearch = findViewById(R.id.DataSearch);
        DataBackToMainServiceList = findViewById(R.id.DataBackToMainServiceList);
        recyclerViewDataItemList = findViewById(R.id.recyclerViewDataItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        //search services
        DataSearch.addTextChangedListener(new TextWatcher() {
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

        DataBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent DataBack2serList = new Intent(DataItem.this, BuyerHome.class);
                //startActivity(DataBack2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all data Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }

                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(DataItem.this, ServiceItemList);
                //set adapter
                recyclerViewDataItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoDService.setVisibility(View.VISIBLE);
                    tv_NoDService2.setVisibility(View.VISIBLE);
                    iv_NoDService.setVisibility(View.VISIBLE);
                    DataSearch.setVisibility(View.GONE);
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