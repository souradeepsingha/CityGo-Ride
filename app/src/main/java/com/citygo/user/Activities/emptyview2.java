package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.citygo.user.R;
import com.citygo.user.adapter.PlaceAutoSuggestAdapter;

public class emptyview2 extends AppCompatActivity {
    LinearLayout copypagelinearback;

    private static final int REQUEST_CODE_AUTOCOMPLETE2 = 1001;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptyview2);
        copypagelinearback=findViewById(R.id.copypagelinearback1);

        copypagelinearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(emptyview2.this,homeDashBoardActivity.class);
                startActivity(myintent);
            }
        });


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autocomplete2);
        autoCompleteTextView.requestFocus();



        final AutoCompleteTextView autoCompleteTextView2 = findViewById(R.id.autocomplete2);

        autoCompleteTextView2.setAdapter(new PlaceAutoSuggestAdapter(emptyview2.this, android.R.layout.simple_list_item_1));


        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAddress2 = autoCompleteTextView2.getText().toString();
//                Log.d("Selected Address: ", selectedAddress);

                // Send the selected address back to homeDashBoardActivity using Intent
                Intent intent = new Intent();
                intent.putExtra("selected_address2", selectedAddress2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}