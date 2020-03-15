package com.example.online_quize_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.example.online_quize_app.Model.User;

import Common.Common;

public class MainActivity extends AppCompatActivity {
    MaterialEditText editNewUser, editNewPassword, editNewEmail;
    MaterialEditText editUser, editPassword;
    Button btnSignUp, btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        editUser =findViewById(R.id.edit_userName);
        editPassword = findViewById(R.id.edit_password);

        btnSignIn=findViewById(R.id.btn_sign_in);
        btnSignUp=findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn(editUser.getText().toString(),editPassword.getText().toString());


            }
        });

    }

    private void SignIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()){
                    if(!user.isEmpty()){
                        User login= dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd)) {
                            Intent homeActivity = new Intent(MainActivity.this, Home.class);
                            Common.currentUser=login;
                            startActivity(homeActivity);
                            finish();

                        }
                        else {
                            Toast.makeText(MainActivity.this,"Wrong Password", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {
                        Toast.makeText(MainActivity.this,"Please enter your user name", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(MainActivity.this,"Users is not exists!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=this.getLayoutInflater();
        View sign_up_layout=inflater.inflate(R.layout.sign_up_layout,null);

        editNewUser=sign_up_layout.findViewById(R.id.edit_new_userName);
        editNewPassword=sign_up_layout.findViewById(R.id.edit_new_password);
        editNewEmail=sign_up_layout.findViewById(R.id.edit_newEmail);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.account);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user=new User(editNewUser.getText().toString(),editNewPassword.getText().toString(),editNewEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUserName()).exists())
                            Toast.makeText(MainActivity.this,"User already exists !", Toast.LENGTH_SHORT).show();
                        else
                        {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this,"User registration success!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
}
