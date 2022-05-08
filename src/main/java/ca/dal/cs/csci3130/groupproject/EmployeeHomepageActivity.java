package ca.dal.cs.csci3130.groupproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class EmployeeHomepageActivity extends AppCompatActivity implements View.OnClickListener {

    static String Employee_Name = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_homepage);

        Button preferenceButton = findViewById(R.id.EmployeeHomepage_Preference);
        preferenceButton.setOnClickListener(this);

        //attach an event handler to the logout button
        Button logOutButton = findViewById(R.id.EmployeeHomepage_LogOut);
        logOutButton.setOnClickListener(this);

        Button appliedButton = findViewById(R.id.EmployeeHomepage_Applied_Job);
        appliedButton.setOnClickListener(this);

        Button locationButton = findViewById(R.id.EmployeeHomepage_CURRENT_LOCATION);
        appliedButton.setOnClickListener(this);

        //get data from sharedPreferences, store in the static variables.
        this.getValues();

        //set user information shown on the page.
        this.setText();

    }

    //get user name from shared preferences
    protected void getValues(){
        SharedPreferences user = getSharedPreferences("login_info",Context.MODE_PRIVATE);
        Employee_Name = user.getString("firstName", "") + " " + user.getString("lastName", "");

    }

    //Change the content of the text field on the page with personal information
    protected void setText(){
        TextView welcome = findViewById(R.id.EmployeeHomepageWelcome);
        welcome.setText("Welcome Employee " + Employee_Name + "!");
    }

    //log out function
    protected void logOut(){
        // set the data in shared preference to null
        SharedPreferences.Editor editor = getSharedPreferences("login_info",Context.MODE_PRIVATE).edit();
        editor.putString("type",null);
        editor.putString("firstName",null);
        editor.putString("lastName",null);
        editor.commit();
        // jump to main activity
        Intent intent = new Intent(EmployeeHomepageActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		//Set the flag at top
        startActivity(intent);
        DestroyActivityUtil destroyActivityUtil = new DestroyActivityUtil();
        destroyActivityUtil.exit();
        EmployeeHomepageActivity.this.finish();
    }

    protected void toPreference(){
        Intent intent = new Intent(EmployeeHomepageActivity.this, PreferenceActivity.class);
        startActivity(intent);
    }

    protected void toJobList(){
        Intent intent = new Intent(EmployeeHomepageActivity.this, JobsListActivity.class);
        startActivity(intent);
    }

    protected void toAppliedJob(){
        Intent intent = new Intent(EmployeeHomepageActivity.this, AppliedActivity.class);
        startActivity(intent);
    }

    protected void toCurrentLocation(){
        Intent intent = new Intent(EmployeeHomepageActivity.this, CurrentLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        //Log out
        if (view.getId() == R.id.EmployeeHomepage_LogOut){
            logOut();
        }else if (view.getId() == R.id.EmployeeHomepage_Preference){
            toPreference();
        }else if(view.getId() == R.id.EmployeeHomepage_FIND_A_JOB){
            toJobList();
        }else if (view.getId() == R.id.EmployeeHomepage_Applied_Job){
            toAppliedJob();
        }else if (view.getId() == R.id.EmployeeHomepage_CURRENT_LOCATION) {
            toCurrentLocation();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public static class DestroyActivityUtil {
        public static List<Activity> activityList = new LinkedList();       // Set all activities into the list
        public void exit()              // Remove all list
        {
            for(Activity act:activityList)
            {
                Log.d("TAGS",act.toString());
                act.finish();
            }
            System.exit(0);
        }
    }
}