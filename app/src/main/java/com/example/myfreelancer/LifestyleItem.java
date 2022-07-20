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

public class LifestyleItem extends AppCompatActivity {

    private TextView tv_NoMService, tv_NoMService2;
    private ImageView iv_NoMService;

    private ImageButton LifestyleBackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewLifestyleItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText LifestyleSearch;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_item);

        tv_NoMService = findViewById(R.id.tv_NoMService);
        tv_NoMService2 = findViewById(R.id.tv_NoMService2);
        iv_NoMService = findViewById(R.id.iv_NoMService);

        LifestyleSearch = findViewById(R.id.LifestyleSearch);
        LifestyleBackToMainServiceList = findViewById(R.id.LifestyleBackToMainServiceList);
        recyclerViewLifestyleItemList = findViewById(R.id.recyclerViewLifestyleItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        LifestyleSearch.addTextChangedListener(new TextWatcher() {
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

        LifestyleBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent LifestyleBack2serList = new Intent(LifestyleItem.this, BuyerHome.class);
                //startActivity(LifestyleBack2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Digital Marketing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all lifestyle Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }
                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(LifestyleItem.this, ServiceItemList);
                //set adapter
                recyclerViewLifestyleItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoMService.setVisibility(View.VISIBLE);
                    tv_NoMService2.setVisibility(View.VISIBLE);
                    iv_NoMService.setVisibility(View.VISIBLE);
                    LifestyleSearch.setVisibility(View.GONE);
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