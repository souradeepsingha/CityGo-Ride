package com.citygo.user.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.citygo.user.R;
import com.citygo.user.databinding.FragmentUserRegisterBinding;


public class UserRegisterFragment extends Fragment {
    FragmentUserRegisterBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentUserRegisterBinding.inflate(inflater, container, false);

        SharedPreferences pref = requireContext().getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        binding.getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameEt.getText().toString().trim();
                String email = binding.RegisterEmailEt.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Enter your name...", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), "Invalid Email...", Toast.LENGTH_SHORT).show();
                } else if (binding.loginNoEt.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please fill-up the number...",Toast.LENGTH_LONG).show();
                } else if (binding.loginNoEt.getText().toString().length()<10||binding.loginNoEt.getText().toString().length()>10) {
                    Toast.makeText(getContext(),"Please enter a 10 digit valid number...",Toast.LENGTH_LONG).show();
                }else {
                    editor.putString("userNumber",binding.loginNoEt.getText().toString());
                    editor.putString("userEmail",binding.RegisterEmailEt.getText().toString().trim());
                    editor.putString("userName",binding.nameEt.getText().toString().trim());
                    editor.commit();
                    sendDtata(name , email);
                }

            }
        });
        return binding.getRoot();
    }
    private void sendDtata(String name, String email) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,new OtpVerifyFragment());
        Bundle result = new Bundle();
        result.putString("number",binding.loginNoEt.getText().toString());
        result.putString("name",name);
        result.putString("email",email);
        getParentFragmentManager().setFragmentResult("dataFromNumFillup",result);
        binding.loginNoEt.setText("");
        ft.commit();
    }
}