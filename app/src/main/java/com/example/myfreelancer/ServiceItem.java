package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class ServiceItem extends AppCompatActivity {

    private TextView tv_NoGService, tv_NoGService2;
    private ImageView iv_NoGService;

    private ImageButton BackToMainServiceList;
    private ArrayList<Model_Service> ServiceItemList;
    private RecyclerView recyclerViewServiceItemList;
    private AdapterServiceItem adapterServiceItem;
    private EditText GraphicSearch;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item);

        tv_NoGService = findViewById(R.id.tv_NoGService);
        tv_NoGService2 = findViewById(R.id.tv_NoGService2);
        iv_NoGService = findViewById(R.id.iv_NoGService);

        GraphicSearch = findViewById(R.id.GraphicSearch);
        BackToMainServiceList = findViewById(R.id.BackToMainServiceList);
        recyclerViewServiceItemList = findViewById(R.id.recyclerViewServiceItemList);

        auth = FirebaseAuth.getInstance();

        loadServices();

        GraphicSearch.addTextChangedListener(new TextWatcher() {
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


        //ArrayList<Model_Service> ServiceItemList = new ArrayList<>();

        //ServiceItemList.add(new Model_Service(R.drawable.service_image, "Design minimalist and creative logo", "3","20.50" ));
        //ServiceItemList.add(new Model_Service(R.drawable.service_images, "Design logo","13","210" ));
        //ServiceItemList.add(new Model_Service(R.drawable.service_image, "Design minimalist and creative logo Design minimalist and creative logoDesign minimalist and creative logo", "23","110.00" ));
        //ServiceItemList.add(new Model_Service(R.drawable.service_image, "Design minimalist and creative logo", "50","210.50" ));

        //recyclerViewServiceItemList.setHasFixedSize(true);

       // adapterServiceItem = new AdapterServiceItem(this,ServiceItemList);
       // recyclerViewServiceItemList.setLayoutManager(new LinearLayoutManager(this));

        //recyclerViewServiceItemList.setAdapter(adapterServiceItem);

        BackToMainServiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent back2serList = new Intent(ServiceItem.this, BuyerHome.class);
                //startActivity(back2serList);
            }
        });
    }

    private void loadServices() {

        ServiceItemList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.orderByChild("SCategory").equalTo("Graphic & Design").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServiceItemList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Model_Service model_service = ds.getValue(Model_Service.class);

                    //show all graphic Services except from current login user
                    String UID = ""+ds.child("UID").getValue();
                    if(!UID.equals(auth.getUid())) {
                        ServiceItemList.add(model_service);
                    }
                }
                //setup adapter
                adapterServiceItem = new AdapterServiceItem(ServiceItem.this, ServiceItemList);
                //set adapter
                recyclerViewServiceItemList.setAdapter(adapterServiceItem);

                if(ServiceItemList.isEmpty()){
                    tv_NoGService.setVisibility(View.VISIBLE);
                    tv_NoGService2.setVisibility(View.VISIBLE);
                    iv_NoGService.setVisibility(View.VISIBLE);
                    GraphicSearch.setVisibility(View.GONE);
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
