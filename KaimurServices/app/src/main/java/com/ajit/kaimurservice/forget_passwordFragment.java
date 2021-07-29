package com.ajit.kaimurservice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class forget_passwordFragment extends Fragment {



    // TODO: Rename and change types of parameters


    public forget_passwordFragment() {
        // Required empty public constructor
    }


    public static forget_passwordFragment newInstance(String param1, String param2) {
        forget_passwordFragment fragment = new forget_passwordFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }
}