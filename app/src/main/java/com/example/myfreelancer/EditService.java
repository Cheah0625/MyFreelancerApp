package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditService extends AppCompatActivity {

    private ImageButton backToServiceDetails;
    private ImageView edit_servicePhoto;
    private RadioButton edit_serRadioGraphic,edit_serRadioTech,edit_serRadioWriting,edit_serRadioData,edit_serRadioVideo,edit_serRadioMarketing;
    private EditText edit_serviceTitle,edit_serviceDesc,edit_servicePrice,edit_serviceDays;
    private Button btn_editServiceSave;
    private RadioGroup edit_categoryGrp;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private String serviceID;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;
    //image picked uri
    private Uri image_uri;

    private String SCategory, SDeliveryDays, SDescription, SPrice, STitle, SImage;

    //progress progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);

        edit_servicePhoto = findViewById(R.id.edit_servicePhoto);
        edit_categoryGrp = findViewById(R.id.edit_categoryGrp);
        edit_serRadioGraphic = findViewById(R.id.edit_serRadioGraphic);
        edit_serRadioTech = findViewById(R.id.edit_serRadioTech);
        edit_serRadioWriting = findViewById(R.id.edit_serRadioWriting);
        edit_serRadioData = findViewById(R.id.edit_serRadioData);
        edit_serRadioVideo = findViewById(R.id.edit_serRadioVideo);
        edit_serRadioMarketing = findViewById(R.id.edit_serRadioMarketing);

        edit_serviceTitle = findViewById(R.id.edit_serviceTitle);
        edit_serviceDesc = findViewById(R.id.edit_serviceDesc);
        edit_servicePrice = findViewById(R.id.edit_servicePrice);
        edit_serviceDays = findViewById(R.id.edit_serviceDays);

        backToServiceDetails = findViewById(R.id.backToServiceDetails);
        btn_editServiceSave = findViewById(R.id.btn_editServiceSave);

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();
        serviceID = getIntent().getStringExtra("SID");

        edit_servicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(); }
        });

        backToServiceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent EDSerBack2Det= new Intent(EditService.this, ManageServiceList.class);
                //startActivity(EDSerBack2Det);
            }
        });

        btn_editServiceSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertService();
            }
        });


        loadServicesDetail();

    }

    private void loadServicesDetail() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");
        databaseReference.child(serviceID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String SID = ""+snapshot.child("SID").getValue();
                String STitle = ""+snapshot.child("STitle").getValue();
                String SDescription = ""+snapshot.child("SDescription").getValue();
                String SCategory = ""+snapshot.child("SCategory").getValue();
                String SPrice = ""+snapshot.child("SPrice").getValue();
                String SDeliveryDays = ""+snapshot.child("SDeliveryDays").getValue();
                String SImage = ""+snapshot.child("SImage").getValue();

                //set data to text view
                edit_serviceTitle.setText(STitle);
                edit_serviceDesc.setText(SDescription);
                edit_categoryGrp.getCheckedRadioButtonId();
                edit_servicePrice.setText(SPrice);
                edit_serviceDays.setText(SDeliveryDays);

                if (SCategory.equals("Graphic & Design")) {
                    edit_serRadioGraphic.setChecked(true);
                } else {
                    edit_serRadioGraphic.setChecked(false);
                }
                if (SCategory.equals("Writing & Translation")) {
                    edit_serRadioWriting.setChecked(true);
                } else {
                    edit_serRadioWriting.setChecked(false);
                }
                if (SCategory.equals("Video & Animation")) {
                    edit_serRadioVideo.setChecked(true);
                } else {
                    edit_serRadioVideo.setChecked(false);
                }
                if (SCategory.equals("Digital Marketing")) {
                    edit_serRadioMarketing.setChecked(true);
                } else {
                    edit_serRadioMarketing.setChecked(false);
                }
                if (SCategory.equals("Data")) {
                    edit_serRadioData.setChecked(true);
                } else {
                    edit_serRadioData.setChecked(false);
                }
                if (SCategory.equals("Programming & Tech")) {
                    edit_serRadioTech.setChecked(true);
                } else {
                    edit_serRadioTech.setChecked(false);
                }

                try {
                    Picasso.get().load(SImage).placeholder(R.drawable.ic_no_image).into(edit_servicePhoto);
                }
                catch (Exception e) {
                    edit_servicePhoto.setImageResource(R.drawable.ic_no_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void insertService() {

        STitle = edit_serviceTitle.getText().toString().trim();
        SDescription = edit_serviceDesc.getText().toString().trim();
        SPrice = edit_servicePrice.getText().toString().trim();
        SDeliveryDays = edit_serviceDays.getText().toString().trim();


        if (edit_serRadioGraphic.isChecked()) {
            SCategory = "Graphic & Design";
        } else if (edit_serRadioWriting.isChecked()) {
            SCategory = "Writing & Translation";
        } else if (edit_serRadioVideo.isChecked()) {
            SCategory = "Video & Animation";
        } else if (edit_serRadioMarketing.isChecked()) {
            SCategory = "Digital Marketing";
        } else if (edit_serRadioData.isChecked()) {
            SCategory = "Data";
        } else if (edit_serRadioTech.isChecked()) {
            SCategory = "Programming & Tech";
        }

        if(STitle.isEmpty()){
            edit_serviceTitle.setError("Title is required.");
            return;
        }
        if(SDescription.isEmpty()){
            edit_serviceDesc.setError("Description is required.");
            return;
        }
        if(SPrice.isEmpty()){
            edit_servicePrice.setError("Price is required.");
            return;
        }
        if(SDeliveryDays.isEmpty()){
            edit_serviceDays.setError("Delivery Days is required.");
            return;
        }

        if(edit_categoryGrp.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Category is required.",Toast.LENGTH_SHORT).show();
            return;
        }
        updateService();

    }

    private void updateService() {

        progressDialog.setMessage("Saving...");
        progressDialog.show();

        if(image_uri==null){
            //update product without image
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("STitle", ""+ STitle);
            hashMap.put("SDescription", ""+SDescription);
            hashMap.put("SCategory", ""+SCategory);
            hashMap.put("SDeliveryDays", ""+SDeliveryDays);
            hashMap.put("SPrice", ""+SPrice);

            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
            dr.child(serviceID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Intent intentSellerAcc = new Intent(EditService.this, ManageServiceList.class);
                    startActivity(intentSellerAcc);
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

        else {
            String filePathAndName = "service_images/" + "" + serviceID;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //image upload, get the url of uploaded image
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadImageUri = uriTask.getResult();

                    if (uriTask.isSuccessful()){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("SImage", ""+downloadImageUri);
                        hashMap.put("STitle", ""+ STitle);
                        hashMap.put("SDescription", ""+SDescription);
                        hashMap.put("SCategory", ""+SCategory);
                        hashMap.put("SDeliveryDays", ""+SDeliveryDays);
                        hashMap.put("SPrice", ""+SPrice);

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Services");
                        dr.child(serviceID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Intent intentSellerAcc = new Intent(EditService.this, ManageServiceList.class);
                                startActivity(intentSellerAcc);
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //upload failed
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void showImageDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image:").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which==0){
                    //camera clicked
                    if (checkCameraPermission()){
                        //allowed, open camera
                        pickFromCamera();
                    }
                    else {
                        requestCameraPermission();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermission()){
                        //allowed, open gallery
                        pickFromGallery();
                    }
                    else
                        requestStoragePermissions();
                }
            }
        }).show();
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Camera Permissions are necessary.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Storage Permissions are necessary.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //get chosen image
                image_uri = data.getData();
                //set to image view
                edit_servicePhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                edit_servicePhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}