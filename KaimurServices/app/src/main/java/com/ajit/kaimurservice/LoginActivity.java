package com.ajit.kaimurservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajit.kaimurservice.Modal.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText Inputphone,Inputpassword;
    private Button loginbtn;
    private String parentDbName="Users";
    private ProgressDialog loadingBar;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Inputphone=(EditText)findViewById(R.id.loginEnterPhone);
        Inputpassword=(EditText)findViewById(R.id.loginEnterPassword);
        loginbtn=(Button)findViewById(R.id.login_btn);
        loadingBar=new ProgressDialog(this);
        
        
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }

            private void loginAccount() {
                String phone=Inputphone.getText().toString();
                String password=Inputpassword.getText().toString();
                 if(TextUtils.isEmpty(phone)){
                     Toast.makeText(LoginActivity.this, "Enter Phone Number...", Toast.LENGTH_SHORT).show();
                 }
                 else if(TextUtils.isEmpty(password)){
                     Toast.makeText(LoginActivity.this, "Enter password...", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     loadingBar.setTitle("LoginAccount");
                     loadingBar.setMessage("Account Login Successfully...");
                     loadingBar.setCanceledOnTouchOutside(false);
                     loadingBar.show();
                     AllowAccessToAccount(phone,password);
                 }
            }

            private void AllowAccessToAccount(final String phone, String password) {
                final DatabaseReference Rootref;
                Rootref= FirebaseDatabase.getInstance().getReference();

                Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(parentDbName).child(phone).exists()){
                            Users userData=snapshot.child(parentDbName).child(phone).getValue(Users.class);
                               if(userData.getPhone().equals(phone)){
                                   if(userData.getPassword().equals(password)){
                                       Toast.makeText(LoginActivity.this, "Account Login sucessfully...", Toast.LENGTH_SHORT).show();
                                       loadingBar.dismiss();
                                       Intent intent =new Intent(LoginActivity.this,HomeActivity.class);
                                   }
                               }

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "User with this"+phone+"Number Not exist", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}