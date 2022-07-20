package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class BuyerPostProject extends AppCompatActivity {

    private ImageButton backToBuyerAcc;
    private RadioGroup categoryGroup, statusGroup;
    private RadioButton post_radioGraphic, post_radioWriting, post_radioVideo, post_radioMarketing, post_radioData, post_radioTech, post_radioOpen, post_radioClose, post_radioCompleted;
    private Button btn_projectPublish;
    private EditText post_title, post_desc, post_budget, post_days ;
    private String PTitle, PDescription, PBudget, PDeliveryDays, PCategory, PStatus, UName, UContact, UEmail, ULocation, UImage;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_post_project);

        categoryGroup = findViewById(R.id.categoryGroup);
        statusGroup = findViewById(R.id.statusGroup);
        backToBuyerAcc = findViewById(R.id.backToBuyerAcc);
        btn_projectPublish = findViewById(R.id.btn_projectPublish);
        post_radioGraphic = findViewById(R.id.post_radioGraphic);
        post_radioWriting = findViewById(R.id.post_radioWriting);
        post_radioVideo = findViewById(R.id.post_radioVideo);
        post_radioMarketing = findViewById(R.id.post_radioMarketing);
        post_radioData = findViewById(R.id.post_radioData);
        post_radioTech = findViewById(R.id.post_radioTech);
        post_radioOpen = findViewById(R.id.post_radioOpen);
        post_radioClose = findViewById(R.id.post_radioClose);
        post_radioCompleted = findViewById(R.id.post_radioCompleted);

        post_title = findViewById(R.id.post_title);
        post_desc = findViewById(R.id.post_desc);
        post_budget = findViewById(R.id.post_budget);
        post_days = findViewById(R.id.post_days);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        btn_projectPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputProject();
            }
        });

        backToBuyerAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent backToBuyerAcc = new Intent (BuyerPostProject.this , BuyerAccount.class);
                //startActivity(backToBuyerAcc);
            }
        });
    }

    //insert data
    private void inputProject() {
        PTitle = post_title.getText().toString().trim();
        PDescription = post_desc.getText().toString().trim();
        PBudget = post_budget.getText().toString().trim();
        PDeliveryDays = post_days.getText().toString().trim();

        if (post_radioGraphic.isChecked()) {
            PCategory = "Graphic & Design";
        } else if (post_radioWriting.isChecked()) {
            PCategory = "Writing & Translation";
        } else if (post_radioVideo.isChecked()) {
            PCategory = "Video & Animation";
        } else if (post_radioMarketing.isChecked()) {
            PCategory = "Digital Marketing";
        } else if (post_radioData.isChecked()) {
            PCategory = "Data";
        } else if (post_radioTech.isChecked()) {
            PCategory = "Programming & Tech";
        }

        if (post_radioOpen.isChecked()) {
            PStatus = "Open";
        } else if (post_radioClose.isChecked()) {
            PStatus = "Closed";
        } else if (post_radioCompleted.isChecked()) {
            PStatus = "Completed";
        }

        if(PTitle.isEmpty()){
            post_title.setError("Title is required.");
            return;
        }
        if(PDescription.isEmpty()){
            post_desc.setError("Description is required.");
            return;
        }
        if(PBudget.isEmpty()){
            post_budget.setError("Budget is required.");
            return;
        }
        if(PDeliveryDays.isEmpty()){
            post_days.setError("Delivery Days is required.");
            return;
        }

        if(categoryGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(statusGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Status is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        createProject();
    }

    //create project
    private void createProject() {

        progressDialog.setMessage("Posting Project...");
        progressDialog.show();

        final String timestamp = "" + System.currentTimeMillis();

        //add project data to database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ProjectID", "" + timestamp);
        hashMap.put("PTitle", "" + PTitle);
        hashMap.put("PDescription", "" + PDescription);
        hashMap.put("PBudget", "" + PBudget);
        hashMap.put("PDeliveryDays", "" + PDeliveryDays);
        hashMap.put("PCategory", "" + PCategory);
        hashMap.put("PStatus", "" + PStatus);
        hashMap.put("UID", "" + auth.getUid());

        //save to database

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Project");
        dr.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(BuyerPostProject.this,"Project posted.",Toast.LENGTH_SHORT).show();
                clearData();

                Intent intentToBuyerAcc = new Intent (BuyerPostProject.this , BuyerAccount.class);
                startActivity(intentToBuyerAcc);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearData() {
        PTitle = post_title.getText().toString().trim();
        PDescription = post_desc.getText().toString().trim();
        PBudget = post_budget.getText().toString().trim();
        PDeliveryDays = post_days.getText().toString().trim();

        post_title.setText("");
        post_desc.setText("");
        post_budget.setText("");
        post_days.setText("");
    }

}