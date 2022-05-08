package ca.dal.cs.csci3130.groupproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // initialize strings
    private EditText emailAddressField;
    private EditText passwordField;
    FirebaseDatabase database;
    private DatabaseReference users;

    String emailAddress = null;
    String password = null;
    String[] tempEmail;
    String encrypted;

    String extractedFirstName = new String();
    String extractedLastName = new String();
    String extractedEmail = new String();
    Boolean extractedUserType = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // jump to sign up page
        TextView signUpLink = findViewById(R.id.signUpLink);
        signUpLink.setTextColor(Color.BLUE);
        signUpLink.setOnClickListener(this);

        // find email and password from the view
        emailAddressField = findViewById(R.id.emailAddressField);
        passwordField = findViewById(R.id.passwordField);

        initializeDatabase();

        Button login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    // database initialization
    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        users = database.getReference("users");
    }

    @Override
    public void onClick(View v) {
        // if it is to sign up, so jump to the sign up activity page
        if (v.getId() == R.id.signUpLink){
            signUp();
        }
        // if it is to login, call loginUser() function
        else if (v.getId() == R.id.login){
            loginUser();
        }
    }

    protected boolean isValidEmailAddress(String emailAddress) {
        // If it is a valid email address
        if(emailAddress.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            return true;
        }
        return false;
    }

    protected void loginCheck(String emailAddress, String encrypted) { // compair the input info with the info in database

        users.addValueEventListener(new ValueEventListener() { // get the data in database
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(tempEmail[0]).exists()){ // check if the inputted info is exist in database

                    User extractedUser = dataSnapshot.child(tempEmail[0]).getValue(User.class); // get the user object from the database

                    if(extractedUser.getEmailAddress().equals(emailAddress) && extractedUser.getPassword().equals(encrypted)){ // if inputted info is correct
                        // save user info from the database to local
                        extractedEmail = extractedUser.getEmailAddress();
                        extractedFirstName = extractedUser.getFirstName();
                        extractedLastName = extractedUser.getLastName();
                        extractedUserType = extractedUser.getType();
                        if(extractedUserType) { // if user type is true, direct to employee home page

                            storeInfo("employee", extractedFirstName, extractedLastName);
                            jumpToHomepage("employee");
                        }
                        else{ // the user is employer

                            storeInfo("employer", extractedFirstName, extractedLastName);
                            jumpToHomepage("employer");
                        }
                    }
                    else{ // inputted info can't match the info in database
                        emailAddressField.setError(getResources().getString(R.string.INVALID_EMAIL_OR_PASSWORD).trim());
                        emailAddressField.requestFocus();
                    }
                }
                else { //inputted info is not exist in database
                    emailAddressField.setError(getResources().getString(R.string.INVALID_EMAIL_OR_PASSWORD).trim());
                    emailAddressField.requestFocus();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(LoginActivity.this, "Cannot retrieve user data", Toast.LENGTH_LONG).show();
            }
        });
    }


    /* https://stackoverflow.com/questions/3934331/how-to-hash-a-string-in-android */
    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //log in function
    private void loginUser(){
        // get the eamil address and password from user input
        emailAddress = emailAddressField.getText().toString().trim();
        password = passwordField.getText().toString().trim();
        tempEmail = emailAddress.split("@");

        // set the error message
        String errorMessage = new String("ERROR MESSAGE");

        // check if the email address is empty
        if (emailAddress.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_ADDRESS).trim();
            emailAddressField.setError(errorMessage);
            emailAddressField.requestFocus();
        }
        // check if the email address is valid
        else if (!isValidEmailAddress(emailAddress)) {
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_ADDRESS).trim();
            emailAddressField.setError(errorMessage);
            emailAddressField.requestFocus();
        }
        // check if the password is empty
        else if (password.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD).trim();
            passwordField.setError(errorMessage);
            passwordField.requestFocus();
        }
        else { // input is valid

            encrypted = md5(password);
            loginCheck(emailAddress, encrypted);
        }
    }

    //Sign up function, jump to the sign up activity
    protected void signUp(){
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    //jump to homepage function
    protected void jumpToHomepage(String type){

        Intent intent;

        //According to the user type, create corresponding intent
        if (type.equals("employee")){
            intent=new Intent(LoginActivity.this, EmployeeHomepageActivity.class);
        }else{
            intent=new Intent(LoginActivity.this, EmployerHomepageActivity.class);
        }

        //start the intent
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // clear current activity, block back button
        startActivity(intent);
        LoginActivity.this.finish();

    }

    //Store the user information into the shared preference
    protected void storeInfo(String type, String firstName, String lastName){

        SharedPreferences.Editor editor;
        editor = getSharedPreferences("login_info",Context.MODE_PRIVATE).edit();
        editor.putString("type",type);
        editor.putString("firstName",firstName);
        editor.putString("lastName",lastName);
        editor.commit();

    }
}