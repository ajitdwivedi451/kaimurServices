package com.ajit.kaimurservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Sign_upFragment extends Fragment {



    public Sign_upFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAccount;
    private FrameLayout parentFrameLayout;
    private EditText name,email,password,conformPassword;
    private Button signUpButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog loadingBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);
        parentFrameLayout=getActivity().findViewById(R.id.register_frameLayout);
        alreadyHaveAccount=view.findViewById(R.id.alreadyHaveAnAccount);
        name=view.findViewById(R.id.signUp_name);
        email=view.findViewById(R.id.signUp_email);
        password=view.findViewById(R.id.signUp_password);
        conformPassword=view.findViewById(R.id.signUp_conformPassword);
        signUpButton=view.findViewById(R.id.signUpBtn);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        loadingBar=new ProgressDialog(getActivity());
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Sign_inFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                inputChecks();

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                inputChecks();

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                inputChecks();
            }
        });
        conformPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                inputChecks();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait your Account is creating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                emailPasswordValidation();


            }
        });

    }

    private void emailPasswordValidation() {
        if(email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(conformPassword.getText().toString())){
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(Color.parseColor("#50ffffff"));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Map<Object,String> userData=new HashMap<>();
                                    userData.put("name",name.getText().toString());
                                    userData.put("Email",email.getText().toString());
                                    userData.put("password",password.getText().toString());
                                    firebaseFirestore.collection("Users").add(userData)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if(task.isSuccessful()){
                                                        loadingBar.dismiss();
                                                        Toast.makeText(getActivity(), "Task is successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(getActivity(),HomeActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();

                                                    }else{
                                                        String error=task.getException().getMessage();
                                                        loadingBar.dismiss();
                                                        Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                                        signUpButton.setEnabled(true);
                                                        signUpButton.setTextColor(Color.parseColor("#ffffff"));
                                                    }

                                                }
                                            });
                                }else{
                                    String error=task.getException().getMessage();
                                    loadingBar.dismiss();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                    signUpButton.setEnabled(true);
                                    signUpButton.setTextColor(Color.parseColor("#ffffff"));
                                }
                            }
                        });

            }else{
                loadingBar.dismiss();
                conformPassword.setError("password not match");
                Toast.makeText(getActivity(), "Confirm password is not match...", Toast.LENGTH_SHORT).show();

            }
        }else{
            loadingBar.dismiss();
            Toast.makeText(getActivity(), "Invalid email...", Toast.LENGTH_SHORT).show();
        }
    }

    private void inputChecks() {
        if(!TextUtils.isEmpty(email.getText())){
           if(!TextUtils.isEmpty(name.getText())){
               if(!TextUtils.isEmpty(password.getText())){
                   if(!TextUtils.isEmpty(conformPassword.getText())){

                       signUpButton.setEnabled(true);
                       signUpButton.setTextColor(Color.WHITE);
                   }
                   else {
                       Toast.makeText(getActivity(), " Enter Confirm Password... ", Toast.LENGTH_SHORT).show();
                       signUpButton.setEnabled(false);
                       signUpButton.setTextColor(Color.parseColor("#50ffffff")); }

               }else{
                   Toast.makeText(getActivity(), "Enter Passowrd ...", Toast.LENGTH_SHORT).show();
                   signUpButton.setEnabled(false);
                   signUpButton.setTextColor(Color.parseColor("#50ffffff")); }

           }else{
               Toast.makeText(getActivity(), "Enter Name...", Toast.LENGTH_SHORT).show();
               signUpButton.setEnabled(false);
               signUpButton.setTextColor(Color.parseColor("#50ffffff")); }

        }else{
            Toast.makeText(getActivity(), "Enter Email ....", Toast.LENGTH_SHORT).show();
            signUpButton.setEnabled(false);
            signUpButton.setTextColor(Color.parseColor("#50ffffff")); }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slideout_from_left,R.anim.slide_from_right);
        fragmentTransaction.commit();
    }
}