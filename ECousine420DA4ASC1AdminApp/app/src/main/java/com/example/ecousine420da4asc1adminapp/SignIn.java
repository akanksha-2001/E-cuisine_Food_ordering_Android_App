package com.example.ecousine420da4asc1adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import common.Common;
import model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword=findViewById(R.id.edtPassword);
        edtPhone=findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.btnSignIn);

        db=FirebaseDatabase.getInstance();
        users=db.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void signInUser(String phone, String password) {
        ProgressDialog mDialog=new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please Waiting....");
        mDialog.show();

        final String localPhone=phone;
        final String localPassword=password;
        users.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(localPhone).exists()){

                    mDialog.dismiss();
                    User user=snapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsStaff()))  {
                        if(user.getPassword().equals(localPassword)){
                            Intent login=new Intent(SignIn.this, home.class);
                            Common.currentUser=user;
                            startActivity(login);
                            finish();
                        }
                        else {
                            Toast.makeText(SignIn.this,"Wrong Password!!",Toast.LENGTH_SHORT).show();
                        }
                    }else {

                        Toast.makeText(SignIn.this,"Login with staff account",Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(SignIn.this,"User not exist in database",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}