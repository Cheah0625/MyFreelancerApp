package com.example.myfreelancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout layout_email, layout_password;
    private TextInputEditText ed_email, ed_password;
    private Button btn_login;
    private TextView SignUpNow,ForgotPW;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_email = findViewById(R.id.layout_email);
        layout_password = findViewById(R.id.layout_password);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        SignUpNow = findViewById(R.id.SignUpNow);
        ForgotPW = findViewById(R.id.ForgotPW);

        firebaseAuth = FirebaseAuth.getInstance();

        SignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent2signUp);
            }
        });

        ForgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2resetPW = new Intent(MainActivity.this, ResetPassword.class);
                startActivity(intent2resetPW);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_email.getText().toString().isEmpty()){
                    ed_email.setError("Email is required.");
                    return;
                }
                if(ed_password.getText().toString().isEmpty()){
                    ed_password.setError("Password is required.");
                    return;
                }
                //login user
                firebaseAuth.signInWithEmailAndPassword(ed_email.getText().toString(), ed_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //read data from database
                        //read from Users table
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //String userType = snapshot.child("UID").getValue().toString();

                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){

                                        //set status online
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("OnlineStatus", "Online");
                                        databaseReference.updateChildren(hashMap);

                                        startActivity(new Intent(MainActivity.this, BuyerHome.class));
                                        finish();
                                        //Toast.makeText(MainActivity.this, "Welcome To Freelancer", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Please check your email verification.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), EmailVerification.class));
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}