package ca.dal.cs.csci3130.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    //initialize editText, spinner, textview, firebaseDatabase, databaseReference
    private EditText firstNameField, lastNameField, emailAddressField, passwordField, confirmPasswordField, phoneNumberField;
    private Spinner userType;
    private TextView loginLink;

    private FirebaseDatabase database;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userType = findViewById(R.id.userType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.users_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userType.setAdapter(adapter);

        //find the every field by find the id in layout
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        emailAddressField = findViewById(R.id.emailAddressField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        phoneNumberField = findViewById(R.id.phoneNumberField);

        //set the clickListener to signup button
        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        //set the clickListener to loginLink
        loginLink = findViewById(R.id.loginLink);
        loginLink.setTextColor(Color.BLUE);
        loginLink.setOnClickListener(this);

        initializeDatabase();
    }

    //initializeDatabase function
    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        users = database.getReference();
    }

    //boolean method to check validation of email address
    protected boolean isValidEmailAddress(String emailAddress) {
        if (emailAddress == null){
            return false;
        }
        String emailRegex = "^[.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    //boolean method to check validation of password
    protected boolean isValidPassword(String password) {
        if (password == null){
            return false;
        }
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*_+-]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //boolean method to check validation of confirm password
    protected boolean isValidConfirmPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null){
            return false;
        }else if (password.equals(confirmPassword)){
            return true;
        }
        return false;
    }

    //boolean method to check validation of phone number
    protected boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null){
            return false;
        }else if (phoneNumber.length() == 10){
            return true;
        }
        return false;
    }

    protected boolean isValidName(String name){
        if (name == null){
            return false;
        }
        String allowAlphabetsRegex = "^[A-Za-z]+$";
        Pattern pattern = Pattern.compile(allowAlphabetsRegex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    //Log in function, jump to Log in page
    protected void logIn(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginLink){
            logIn();
        }else if (v.getId() == R.id.signUp){
            registerUser();
        }
    }

    //registerUser method
    private void registerUser(){
        //get text from each field, and initialize the string
        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String emailAddress = emailAddressField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        String phoneNumber = phoneNumberField.getText().toString().trim();
        Boolean type;
        //if user is employee, then boolean type is true
        if (userType.getSelectedItem().toString().equals("Employee")){
            type = true;
        }else {
            type = false;
        }

        String errorMessage;

        //give the different errorMessage to different error situation
        if (firstName.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_FIRST_NAME).trim();
            firstNameField.setError(errorMessage);
            firstNameField.requestFocus();
            return;
        }

        if (lastName.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_LAST_NAME).trim();
            lastNameField.setError(errorMessage);
            lastNameField.requestFocus();
            return;
        }

        if (emailAddress.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_ADDRESS).trim();
            emailAddressField.setError(errorMessage);
            emailAddressField.requestFocus();
            return;
        }

        if (password.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_PASSWORD).trim();
            passwordField.setError(errorMessage);
            passwordField.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_CONFIRM_PASSWORD).trim();
            confirmPasswordField.setError(errorMessage);
            confirmPasswordField.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_PHONE_NUMBER).trim();
            phoneNumberField.setError(errorMessage);
            phoneNumberField.requestFocus();
            return;
        }

        if (!isValidName(firstName)){
            errorMessage = "First name is invalid".trim();
            firstNameField.setError(errorMessage);
            firstNameField.requestFocus();
            return;
        }

        if (!isValidName(lastName)){
            errorMessage = "Last name is invalid".trim();
            lastNameField.setError(errorMessage);
            lastNameField.requestFocus();
            return;
        }

        if (!isValidEmailAddress(emailAddress)){
            errorMessage = getResources().getString(R.string.INVALID_EMAIL_ADDRESS).trim();
            emailAddressField.setError(errorMessage);
            emailAddressField.requestFocus();
            return;
        }

        if (!isValidPassword(password)){
            errorMessage = getResources().getString(R.string.INVALID_PASSWORD).trim();
            passwordField.setError(errorMessage);
            passwordField.requestFocus();
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)){
            errorMessage = getResources().getString(R.string.INVALID_PHONE_NUMBER).trim();
            phoneNumberField.setError(errorMessage);
            phoneNumberField.requestFocus();
            return;
        }

        if (!confirmPassword.isEmpty()){
            if (!isValidConfirmPassword(password, confirmPassword)){
                errorMessage = getResources().getString(R.string.INVALID_CONFIRM_PASSWORD).trim();
                confirmPasswordField.setError(errorMessage);
                confirmPasswordField.requestFocus();
                return;
            }
        }

        //split emailAddress by @
        String[] tempEmail = emailAddress.split("@");
        checkRegister(firstName, lastName, emailAddress, password, phoneNumber, type, tempEmail);

    }

    //checkRegister method
    public void checkRegister(String firstName, String lastName, String emailAddress, String password, String phoneNumber, Boolean type, String[] tempEmail){
        //set the databaseReference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(tempEmail[0]);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check email whether exist or not
                if (snapshot.exists()){
                    //if exist, set error
                    emailAddressField.setError(getResources().getString(R.string.ALREADY_REGISTERED).trim());
                    emailAddressField.requestFocus();
                    return;
                }else {
                    //else add it to database
                    if (!firstName.isEmpty() && !lastName.isEmpty() && !emailAddress.isEmpty() && !password.isEmpty() && !phoneNumber.isEmpty() && isValidPassword(password) && isValidPhoneNumber(phoneNumber) && isValidEmailAddress(emailAddress)) {
                        writeUser(firstName, lastName, emailAddress, password, phoneNumber, type);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);
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

    //write user method
    public void writeUser(String firstName, String lastName, String emailAddress, String password, String phoneNumber, Boolean type){
        //encrypt password
        String encrypted = md5(password);

        //initialize user
        User user = new User(firstName, lastName, emailAddress, encrypted, phoneNumber, type);
        String[] temp = emailAddress.split("@");
        users.child("users").child(temp[0]).setValue(user);
        Toast.makeText(SignUpActivity.this, "Register Successful!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        SignUpActivity.this.finish();
    }

}
