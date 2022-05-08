package ca.dal.cs.csci3130.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Search extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);

        Button toSearchButton = findViewById(R.id.jobsSearchButton);
        toSearchButton.setOnClickListener(this);

    }

    protected void toSearchPage(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        toSearchPage();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(Search.this, EmployeeHomepageActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}

