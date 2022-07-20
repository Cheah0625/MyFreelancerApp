package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProject extends AppCompatActivity {

    private ImageButton backToProjectDetails;
    private RadioButton edit_radioGraphic,edit_radioWriting,edit_radioTech,edit_radioData,edit_radioVideo,edit_radioMarketing,edit_radioOpen,edit_radioClose,edit_radioCompleted;
    private EditText edit_postTitle,edit_postDesc,edit_postBudget,edit_postDays;
    private Button btn_projectSave;
    private RadioGroup edit_rgCategory, edit_rgStatus;

    private String projectID;
    private String graphic,writing,tech,data,video,other,open,close,complete;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String PTitle, PDescription, PDeliveryDays, PStatus, PCategory, PBudget;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        backToProjectDetails = findViewById(R.id.backToProjectDetails);
        btn_projectSave = findViewById(R.id.btn_projectSave);

        edit_radioGraphic = findViewById(R.id.edit_radioGraphic);
        edit_radioWriting = findViewById(R.id.edit_radioWriting);
        edit_radioTech = findViewById(R.id.edit_radioTech);
        edit_radioData = findViewById(R.id.edit_radioData);
        edit_radioVideo = findViewById(R.id.edit_radioVideo);
        edit_radioMarketing = findViewById(R.id.edit_radioMarketing);
        edit_radioOpen = findViewById(R.id.edit_radioOpen);
        edit_radioClose = findViewById(R.id.edit_radioClose);
        edit_radioCompleted = findViewById(R.id.edit_radioCompleted);

        edit_rgCategory = findViewById(R.id.edit_rgCategory);
        edit_rgStatus = findViewById(R.id.edit_rgStatus);

        edit_postTitle = findViewById(R.id.edit_postTitle);
        edit_postDesc = findViewById(R.id.edit_postDesc);
        edit_postBudget = findViewById(R.id.edit_postBudget);
        edit_postDays = findViewById(R.id.edit_postDays);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        projectID = getIntent().getStringExtra("ProjectID");

        backToProjectDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent back2proDetail = new Intent(EditProject.this, ManagePostingList.class);
                //startActivity(back2proDetail);
            }
        });

        btn_projectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProject();

            }
        });

        loadProjectDetails();

    }

    private void loadProjectDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Project");
        databaseReference.child(projectID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String ProjectID = ""+snapshot.child("ProjectID").getValue();
                String PTitle = ""+snapshot.child("PTitle").getValue();
                String PDescription = ""+snapshot.child("PDescription").getValue();
                String PCategory = ""+snapshot.child("PCategory").getValue();
                String PBudget = ""+snapshot.child("PBudget").getValue();
                String PDeliveryDays = ""+snapshot.child("PDeliveryDays").getValue();
                String PStatus = ""+snapshot.child("PStatus").getValue();

                //set data to text view
                edit_postTitle.setText(PTitle);
                edit_postDesc.setText(PDescription);
                edit_rgCategory.getCheckedRadioButtonId();
                edit_postBudget.setText(PBudget);
                edit_postDays.setText(PDeliveryDays);
                edit_rgStatus.getCheckedRadioButtonId();

                if (PCategory.equals("Graphic & Design")) {
                    edit_radioGraphic.setChecked(true);
                } else {
                    edit_radioGraphic.setChecked(false);
                }
                if (PCategory.equals("Writing & Translation")) {
                    edit_radioWriting.setChecked(true);
                } else {
                    edit_radioWriting.setChecked(false);
                }
                if (PCategory.equals("Video & Animation")) {
                    edit_radioVideo.setChecked(true);
                } else {
                    edit_radioVideo.setChecked(false);
                }
                if (PCategory.equals("Digital Marketing")) {
                    edit_radioMarketing.setChecked(true);
                } else {
                    edit_radioMarketing.setChecked(false);
                }
                if (PCategory.equals("Data")) {
                    edit_radioData.setChecked(true);
                } else {
                    edit_radioData.setChecked(false);
                }
                if (PCategory.equals("Programming & Tech")) {
                    edit_radioTech.setChecked(true);
                } else {
                    edit_radioTech.setChecked(false);
                }


                if (PStatus.equals("Open")) {
                    edit_radioOpen.setChecked(true);
                } else {
                    edit_radioOpen.setChecked(false);
                }
                if (PStatus.equals("Closed")) {
                    edit_radioClose.setChecked(true);
                } else {
                    edit_radioClose.setChecked(false);
                }
                if (PStatus.equals("Completed")) {
                    edit_radioCompleted.setChecked(true);
                } else {
                    edit_radioCompleted.setChecked(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void insertProject() {
        PTitle = edit_postTitle.getText().toString().trim();
        PDescription = edit_postDesc.getText().toString().trim();
        PBudget = edit_postBudget.getText().toString().trim();
        PDeliveryDays = edit_postDays.getText().toString().trim();

        if (edit_radioGraphic.isChecked()) {
            PCategory = "Graphic & Design";
        } else if (edit_radioWriting.isChecked()) {
            PCategory = "Writing & Translation";
        } else if (edit_radioVideo.isChecked()) {
            PCategory = "Video & Animation";
        } else if (edit_radioMarketing.isChecked()) {
            PCategory = "Digital Marketing";
        } else if (edit_radioData.isChecked()) {
            PCategory = "Data";
        } else if (edit_radioTech.isChecked()) {
            PCategory = "Programming & Tech";
        }

        if (edit_radioOpen.isChecked()) {
            PStatus = "Open";
        } else if (edit_radioClose.isChecked()) {
            PStatus = "Closed";
        } else if (edit_radioCompleted.isChecked()) {
            PStatus = "Completed";
        }


        if(PTitle.isEmpty()){
            edit_postTitle.setError("Title is required.");
            return;
        }
        if(PDescription.isEmpty()){
            edit_postDesc.setError("Description is required.");
            return;
        }
        if(PBudget.isEmpty()){
            edit_postBudget.setError("Budget is required.");
            return;
        }
        if(PDeliveryDays.isEmpty()){
            edit_postDays.setError("Delivery Days is required.");
            return;
        }

        if(edit_rgCategory.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(edit_rgStatus.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Status is required.",Toast.LENGTH_SHORT).show();
            return;
        }
        //add data into database
        updateProject();
    }

    private void updateProject() {

        progressDialog.setMessage("Saving...");
        progressDialog.show();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("PTitle", "" + PTitle);
            hashMap.put("PDescription", "" + PDescription);
            hashMap.put("PBudget", "" + PBudget);
            hashMap.put("PDeliveryDays", "" + PDeliveryDays);
            hashMap.put("PCategory", "" + PCategory);
            hashMap.put("PStatus", "" + PStatus);

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Project");
            dr.child(projectID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Intent intent2ordPendingLs = new Intent(EditProject.this, ManagePostingList.class);
                    startActivity(intent2ordPendingLs);
                    Toast.makeText(getApplicationContext(),"Updated.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
    }
}
