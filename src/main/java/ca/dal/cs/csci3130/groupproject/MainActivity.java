package ca.dal.cs.csci3130.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLoginStatus();

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

    }

    //Log in function, jump to login
    protected void logIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //signUp function, jump tp signUp
    protected void signUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        //Log in
        if (v.getId() == R.id.loginButton) {
            logIn();
        }else if (v.getId() == R.id.signUpButton){ //Sign up
            signUp();
        }
    }


    protected void checkLoginStatus(){
        SharedPreferences loginStatus = getSharedPreferences("login_info",Context.MODE_PRIVATE);
        if(loginStatus.getString("type",null) != null) {
            if (loginStatus.getString("type", null).equals("employee")) {
                Intent intent = new Intent(this, EmployeeHomepageActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            } else {
                Intent intent = new Intent(this, EmployerHomepageActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
        return;
    }

}

