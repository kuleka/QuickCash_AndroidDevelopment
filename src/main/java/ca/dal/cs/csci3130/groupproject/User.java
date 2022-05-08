package ca.dal.cs.csci3130.groupproject;

import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    public String firstName, lastName, emailAddress, password, phoneNumber;
    public Boolean type;

    public User(){

    }

    public User(String firstName, String lastName, String emailAddress, String password, String phoneNumber, Boolean type){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    //Getter functions
    public String getEmailAddress() {
        return emailAddress;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Boolean getType(){ return type; }

    public boolean isValidEmailAddress(String emailAddress) {
        if (emailAddress == null){
            return false;
        }
        String emailRegex = "^[.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        if (password == null){
            return false;
        }
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*-]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidConfirmPassword(String password, String confirmPassword) {
        if (password == null || confirmPassword == null){
            return false;
        }else if (password.equals(confirmPassword)){
            return true;
        }
        return false;
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null){
            return false;
        }else if (phoneNumber.length() == 10){
            return true;
        }
        return false;
    }

}
