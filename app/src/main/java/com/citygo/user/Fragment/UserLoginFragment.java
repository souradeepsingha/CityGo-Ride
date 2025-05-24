package com.citygo.user.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citygo.user.databinding.FragmentUserLoginBinding;

public class UserLoginFragment extends Fragment {

  FragmentUserLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentUserLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}