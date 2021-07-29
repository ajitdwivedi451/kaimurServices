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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Sign_inFragment extends Fragment {



    public Sign_inFragment() {
        // Required empty public constructor
    }

    private TextView dontHaveAccount;
    private FrameLayout parentFragmentLayout;
    private EditText email,password;
    private Button signInButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_sign_in, container, false);
       dontHaveAccount=view.findViewById(R.id.donotHaveAccount);
       parentFragmentLayout=getActivity().findViewById(R.id.register_frameLayout);
       email=view.findViewById(R.id.signIn_email);
       password=view.findViewById(R.id.signIn_password);
       signInButton=view.findViewById(R.id.signInBtn);
       firebaseAuth=FirebaseAuth.getInstance();
       loadingBar=new ProgressDialog(getActivity());
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new Sign_upFragment());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputChecks();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputChecks();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait your account is logging..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validationEmailAndPassword();
            }
        });

    }
    private void validationEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if(password.length() >= 6){
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    loadingBar.dismiss();
                                    Toast.makeText(getActivity(), "Task is successful", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getActivity(),HomeActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                }else{
                                    loadingBar.dismiss();
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    signInButton.setEnabled(true);
                                    signInButton.setTextColor(Color.parseColor("#ffffff")); }
                            }
                        });

            }else{
                Toast.makeText(getActivity(), "Email or Passwod are incorrect...", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                signInButton.setEnabled(true);
                signInButton.setTextColor(Color.parseColor("#ffffff"));}
        }else{
            Toast.makeText(getActivity(), "Email or Passwod are incorrect...", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            signInButton.setEnabled(true);
            signInButton.setTextColor(Color.parseColor("#ffffff"));
        }

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFragmentLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void inputChecks() {
        if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(password.getText())){
                        signInButton.setEnabled(true);
                        signInButton.setTextColor(Color.WHITE);
                }else{
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(), "Enter Passowrd ...", Toast.LENGTH_SHORT).show();
                    signInButton.setEnabled(false);
                    signInButton.setTextColor(Color.parseColor("#50ffffff")); }
        }else{
            loadingBar.dismiss();
            Toast.makeText(getActivity(), "Enter Email ....", Toast.LENGTH_SHORT).show();
            signInButton.setEnabled(false);
            signInButton.setTextColor(Color.parseColor("#50ffffff")); }
    }
}