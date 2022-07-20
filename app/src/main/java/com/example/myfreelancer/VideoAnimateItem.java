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

public class VideoAnimateItem extends AppCompatActivity {

    private TextView tv_NoVService, tv_NoVService2;
    private ImageView iv_NoVService;

    private ImageButton VideoBackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewVideoItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText VideoSearch;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_animate_item);

        tv_NoVService = findViewById(R.id.tv_NoVService);
        tv_NoVService2 = findViewById(R.id.tv_NoVService2);
        iv_NoVService = findViewById(R.id.iv_NoVService);

        VideoSearch = findViewById(R.id.VideoSearch);
        VideoBackToMainServiceList = findViewById(R.id.VideoBackToMainServiceList);
        recyclerViewVideoItemList = findViewById(R.id.recyclerViewVideoItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        VideoSearch.addTextChangedListener(new TextWatcher() {
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

        VideoBackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent VideoBack2serList = new Intent(VideoAnimateItem.this, BuyerHome.class);
                //startActivity(VideoBack2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Video & Animation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all video Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }

                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(VideoAnimateItem.this, ServiceItemList);
                //set adapter
                recyclerViewVideoItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoVService.setVisibility(View.VISIBLE);
                    tv_NoVService2.setVisibility(View.VISIBLE);
                    iv_NoVService.setVisibility(View.VISIBLE);
                    VideoSearch.setVisibility(View.GONE);
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