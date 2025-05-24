package com.citygo.user.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.citygo.user.Activities.homeDashBoardActivity;
import com.citygo.user.databinding.FragmentOtpVerifyBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class OtpVerifyFragment extends Fragment {

    String phonenumber = "+91";
    String otpid;
    String name;
    String email;
    String id;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    FragmentOtpVerifyBinding binding;
    String fcmToken;
    SharedPreferences.Editor editor;
    FirebaseMessaging firebaseMessaging;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpVerifyBinding.inflate(inflater, container, false);

        SharedPreferences pref = requireContext().getSharedPreferences("data",MODE_PRIVATE);
        editor = pref.edit();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        fcmToken = task.getResult();
                        editor.putString("userFcmToken",fcmToken);

                        Log.d("sougata", fcmToken);
                        getNumber();
                    } else {
                        Log.d("sougata", "Failed to get FCM token");
                    }
                });

        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.emailEt.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "Blank Field can not be processed", Toast.LENGTH_LONG).show();
                else if (binding.emailEt.getText().toString().length() != 6)
                    Toast.makeText(getContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                else {
                    Log.d("Sougata",binding.emailEt.getText().toString()+"          "+otpid);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, binding.emailEt.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        return binding.getRoot();
    }

    private void getNumber() {
        getFragmentManager().setFragmentResultListener("dataFromNumFillup", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                phonenumber += result.getString("number");
                name = result.getString("name");
                email = result.getString("email");
                initiateotp();
            }
        });
    }

    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpid = s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            id = mAuth.getCurrentUser().getUid();
                            editor.putString("userId",id);
                            editor.commit();
                            checkSavedtoDbOrNot(id);

                        } else {
                            Toast.makeText(getContext(), "Signin Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkSavedtoDbOrNot(String id) {
        DatabaseReference ref = db.getReference("Passengers");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(id).exists()){
                    Log.d("isSaved","yes saved");
                    saveToDb();
                }
                else {
                    Intent intent = new Intent(getContext(), homeDashBoardActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("fcmToken",fcmToken);
                    intent.putExtra("name",name);
                    intent.putExtra("number",phonenumber);
                    intent.putExtra("profileImage","");
                    intent.putExtra("driverId","notMade");
                    intent.putExtra("Uid",id);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveToDb() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Passengers");
        HashMap<String, Object> map = new HashMap<>();
        map.put("profileImage","");
        map.put("name",name);
        map.put("number",phonenumber);
        map.put("email",email);
        map.put("Uid",id);
        map.put("fcmToken",fcmToken);
        map.put("driverId","notMade");
        map.put("timeStamp",System.currentTimeMillis());
        ref.child(id).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(getContext(), homeDashBoardActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("fcmToken",fcmToken);
                intent.putExtra("name",name);
                intent.putExtra("number",phonenumber);
                intent.putExtra("profileImage","");
                intent.putExtra("driverId","notMade");
                intent.putExtra("Uid",id);
                startActivity(intent);
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(),"Data not saved to database..",Toast.LENGTH_LONG).show();
            }
        });
    }
}