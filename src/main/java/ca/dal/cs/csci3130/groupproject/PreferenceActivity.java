package ca.dal.cs.csci3130.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreferenceActivity extends AppCompatActivity implements View.OnClickListener{
    private RadioGroup jobChoice;
    private RadioButton computer, lawn, dog, babysitting, grocery;
    String selectedJob;

    private String employee, lowerExpectedDuration, upperExpectedDuration, lowerSalary, upperSalary;

    private EditText SalaryRangeField1, SalaryRangeField2, ExpectedDurationField1, ExpectedDurationField2;

    private FirebaseDatabase database;
    private DatabaseReference preferJobs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        jobChoice = (RadioGroup) findViewById(R.id.preferenceJobTitleChoice);

        computer = (RadioButton) findViewById(R.id.preferenceJobTitleOption1);
        lawn = (RadioButton) findViewById(R.id.preferenceJobTitleOption2);
        dog = (RadioButton) findViewById(R.id.preferenceJobTitleOption3);
        babysitting = (RadioButton) findViewById(R.id.preferenceJobTitleOption4);
        grocery = (RadioButton) findViewById(R.id.preferenceJobTitleOption5);

        SalaryRangeField1 = findViewById(R.id.SalaryRangeField1);
        SalaryRangeField2 = findViewById(R.id.SalaryRangeField2);
        ExpectedDurationField1 = findViewById(R.id.ExpectedDurationField1);
        ExpectedDurationField2 = findViewById(R.id.ExpectedDurationField2);

        SharedPreferences user = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        employee = user.getString("firstName", "") + " " + user.getString("lastName", "");

        Button cancel = findViewById(R.id.preferencePage_Back);
        cancel.setOnClickListener(this);

        Button confirm = findViewById(R.id.preferencePage_Confirm);
        confirm.setOnClickListener(this);

        initializeDatabase();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification", "notification", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        preferJobs = database.getReference();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.preferencePage_Back){
            Intent intent = new Intent(PreferenceActivity.this, EmployeeHomepageActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.preferencePage_Confirm){
            //Toast.makeText(getApplicationContext(), selectedJob, Toast.LENGTH_LONG).show();
            postPreferJob();
        }
    }

    /* https://developer.android.com/guide/topics/ui/controls/radiobutton */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.preferenceJobTitleOption1:
                if (checked)
                    selectedJob = computer.getText().toString();
                break;
            case R.id.preferenceJobTitleOption2:
                if (checked)
                    selectedJob = lawn.getText().toString();
                break;
            case R.id.preferenceJobTitleOption3:
                if (checked)
                    selectedJob = dog.getText().toString();
                break;
            case R.id.preferenceJobTitleOption4:
                if (checked)
                    selectedJob = babysitting.getText().toString();
                break;
            case R.id.preferenceJobTitleOption5:
                if (checked)
                    selectedJob = grocery.getText().toString();
                break;
        }
    }

    /*post prefer job method*/
    private void postPreferJob(){
        lowerSalary = SalaryRangeField1.getText().toString().trim();
        upperSalary = SalaryRangeField2.getText().toString().trim();
        lowerExpectedDuration = ExpectedDurationField1.getText().toString().trim();
        upperExpectedDuration = ExpectedDurationField2.getText().toString().trim();

        String errorMessage;

        if (lowerSalary.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_LOWER_BOUND_SALARY).trim();
            SalaryRangeField1.setError(errorMessage);
            SalaryRangeField1.requestFocus();
            return;
        }

        if (upperSalary.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_UPPER_BOUND_SALARY).trim();
            SalaryRangeField2.setError(errorMessage);
            SalaryRangeField2.requestFocus();
            return;
        }

        if (lowerExpectedDuration.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_LOWER_BOUND_DURATION).trim();
            ExpectedDurationField1.setError(errorMessage);
            ExpectedDurationField1.requestFocus();
            return;
        }

        if (upperExpectedDuration.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_UPPER_BOUND_DURATION).trim();
            ExpectedDurationField2.setError(errorMessage);
            ExpectedDurationField2.requestFocus();
            return;
        }

        if (Integer.parseInt(lowerSalary) > Integer.parseInt(upperSalary)){
            Toast.makeText(PreferenceActivity.this, "Lower salary need to smaller than upper salary", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(lowerExpectedDuration) > Integer.parseInt(upperExpectedDuration)){
            Toast.makeText(PreferenceActivity.this, "Lower expected duration need to smaller than upper expected duration", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedJob == null){
            Toast.makeText(PreferenceActivity.this, "You need to select a prefer job title", Toast.LENGTH_LONG).show();
            return;
        }

        if (!lowerSalary.isEmpty() && !upperSalary.isEmpty() && !lowerExpectedDuration.isEmpty() && !upperExpectedDuration.isEmpty() && selectedJob != null && (Integer.parseInt(lowerSalary) < Integer.parseInt(upperSalary)) && (Integer.parseInt(lowerExpectedDuration) < Integer.parseInt(upperExpectedDuration))){
            System.out.println(selectedJob);
            writeJob(employee, selectedJob, lowerExpectedDuration, upperExpectedDuration, lowerSalary, upperSalary);
        }
    }

    /*write job method*/
    private void writeJob(String employee, String title, String lowerExpectedDuration, String upperExpectedDuration, String lowerSalary, String upperSalary){
        PreferJob preferJob = new PreferJob(employee, title, lowerExpectedDuration, upperExpectedDuration, lowerSalary, upperSalary);
        preferJobs.child("preferJobs").child(employee).setValue(preferJob);
        Toast.makeText(PreferenceActivity.this, "Post Prefer Job Successful!", Toast.LENGTH_LONG).show();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("jobs");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check job whether exist or not
                String temp;

                for (DataSnapshot allSnapshot: dataSnapshot.getChildren()){
                    String realEmployer, realSalary, realExpectedDuration, realTitle, realEmployee;
                    temp = allSnapshot.getKey();
                    Job job = dataSnapshot.child(temp).getValue(Job.class);
                    realEmployer = job.getEmployer();
                    realSalary = job.getSalary();
                    realExpectedDuration = job.getExpectedDuration();
                    realTitle = job.getTitle();
                    realEmployee = job.getEmployee();

                    System.out.println(realEmployer);
                    System.out.println(realEmployee);
                    System.out.println(realSalary);
                    System.out.println(lowerExpectedDuration + " " + realExpectedDuration + " " + upperExpectedDuration);
                    System.out.println(lowerSalary + " " + realSalary + " " + upperSalary);
                    if (realEmployee.equals("") && title.equals(realTitle) &&
                            ((Integer.parseInt(lowerExpectedDuration) <= Integer.parseInt(realExpectedDuration))&&(Integer.parseInt(realExpectedDuration) <= Integer.parseInt(upperExpectedDuration))) &&
                            ((Integer.parseInt(lowerSalary) <= Integer.parseInt(realSalary)) && (Integer.parseInt(realSalary) <= Integer.parseInt(upperSalary))))
                    {
                        System.out.println("should have notification");
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(PreferenceActivity.this, "notification")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Work: " + realTitle)
                                .setContentText("The employer " + realEmployer + " posted job fit your find job requirements.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PreferenceActivity.this);

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(1, builder.build());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reference2.addListenerForSingleValueEvent(valueEventListener);
        Intent intent = new Intent(PreferenceActivity.this, EmployeeHomepageActivity.class);
        startActivity(intent);
        PreferenceActivity.this.finish();
    }


}
