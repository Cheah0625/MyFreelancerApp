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

public class WritingItem extends AppCompatActivity {

    private TextView tv_NoWService, tv_NoWService2;
    private ImageView iv_NoWService;
    private ImageButton WritingBackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewWritingItemList;
    private AdapterServiceItem adapterServiceItem;
    private FirebaseAuth auth;
    private EditText WritingSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_item);

        tv_NoWService = findViewById(R.id.tv_NoWService);
        tv_NoWService2 = findViewById(R.id.tv_NoWService2);
        iv_NoWService = findViewById(R.id.iv_NoWService);

        WritingSearch = findViewById(R.id.WritingSearch);
        WritingBackToMainServiceList = findViewById(R.id.WritingBackToMainServiceList);
        recyclerViewWritingItemList = findViewById(R.id.recyclerViewWritingItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        WritingSearch.addTextChangedListener(new TextWatcher() {
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

        WritingBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent WritingBack2serList = new Intent(WritingItem.this, BuyerHome.class);
                //startActivity(WritingBack2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Writing & Translation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all writing Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }

                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(WritingItem.this, ServiceItemList);
                //set adapter
                recyclerViewWritingItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoWService.setVisibility(View.VISIBLE);
                    tv_NoWService2.setVisibility(View.VISIBLE);
                    iv_NoWService.setVisibility(View.VISIBLE);
                    WritingSearch.setVisibility(View.GONE);
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