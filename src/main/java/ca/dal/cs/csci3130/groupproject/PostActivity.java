package ca.dal.cs.csci3130.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner jobType, urgencyType;
    private EditText dateField, expectedDurationField, placeField, salaryField;
    private String employer, employee;
    String date, expectedDuration, place, title, salary;
    String evaluationToEmployer = "";
    String evaluationToEmployee = "";
    Boolean urgency;
    String LatLong;

    private FirebaseDatabase database;
    private DatabaseReference jobs;

    String status = "incomplete";

    Integer number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        jobType = findViewById(R.id.jobType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.jobs_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        jobType.setAdapter(adapter1);

        urgencyType = findViewById(R.id.urgencyType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.urgency_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        urgencyType.setAdapter(adapter2);

        SharedPreferences user = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        employer = user.getString("firstName", "") + " " + user.getString("lastName", "");
        employee = "";
        dateField = findViewById(R.id.dateField);
        expectedDurationField = findViewById(R.id.expectedDurationField);
        placeField = findViewById(R.id.placeField);
        salaryField = findViewById(R.id.salaryField);

        Button cancel = findViewById(R.id.cancelJobButton);
        cancel.setOnClickListener(this);

        Button postJob = findViewById(R.id.postJobButton);
        postJob.setOnClickListener(this);

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

    //initializeDatabase function
    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        jobs = database.getReference();
    }

    protected boolean isValidDate(String dateString) {
        Date currentDate = new Date();

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(dateString);
            //System.out.println(date);
            if (date.before(currentDate)){
                return false;
            }

        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelJobButton){
            Intent intent = new Intent(PostActivity.this, EmployerHomepageActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.postJobButton){
            postJob();
        }
    }

    /*https://www.tutorialspoint.com/how-to-find-the-latitude-and-longitude-from-address-in-android*/
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //locationAddress.setText(locationAddress);
            LatLong = locationAddress;
            if (!place.isEmpty() && LatLong == null){
                placeField.setError("invalid place");
                placeField.requestFocus();
                return;
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("jobs");
            System.out.println(reference);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check job whether exist or not
                    String temp;

                    number = 0;
                    for (DataSnapshot allSnapshot: snapshot.getChildren()) {

                        temp = allSnapshot.getKey();

                        if (temp.contains(employer+number)) {
                            number = number + 1;
                        }
                    }

                    if (!date.isEmpty() && !expectedDuration.isEmpty() && !place.isEmpty() && !salary.isEmpty() && isValidDate(date) && (LatLong != null)) {
                        writeJob(employer, employee, title, date, expectedDuration, place, LatLong, salary, urgency, number, status, evaluationToEmployer, evaluationToEmployee);
                    }
                    number = 0;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            reference.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void postJob(){
        date = dateField.getText().toString().trim();
        expectedDuration = expectedDurationField.getText().toString().trim();
        place = placeField.getText().toString().trim();
        salary = salaryField.getText().toString().trim();
        title = jobType.getSelectedItem().toString();

        GeoCodeLocation locationAddress = new GeoCodeLocation();
        locationAddress.getAddressFromLocation(place, getApplicationContext(), new
                GeoCoderHandler());

        if (urgencyType.getSelectedItem().toString().equals("urgent")){
            urgency = true;
        }else {
            urgency = false;
        }

        String errorMessage;

        if (date.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_DATE).trim();
            dateField.setError(errorMessage);
            dateField.requestFocus();
            return;
        }

        if (!isValidDate(date)){
            errorMessage = getResources().getString(R.string.INVALID_DATE).trim();
            dateField.setError(errorMessage);
            dateField.requestFocus();
            return;
        }

        if (expectedDuration.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_EXPECTED_DURATION).trim();
            expectedDurationField.setError(errorMessage);
            expectedDurationField.requestFocus();
            return;
        }

        if (place.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_PLACE).trim();
            placeField.setError(errorMessage);
            placeField.requestFocus();
            return;
        }

        if (salary.isEmpty()){
            errorMessage = getResources().getString(R.string.EMPTY_SALARY).trim();
            salaryField.setError(errorMessage);
            salaryField.requestFocus();
            return;
        }
    }

    private void writeJob(String employer, String employee, String title, String date, String expectedDuration, String place, String latLong, String salary, Boolean urgency, int number, String status, String evaluationToEmployer, String evaluationToEmployee){
        Job job = new Job(employer, employee, title, date, expectedDuration, place, latLong, salary, urgency, status, evaluationToEmployer, evaluationToEmployee);
        jobs.child("jobs").child(employer+number).setValue(job);
        Toast.makeText(PostActivity.this, "Post Successful!", Toast.LENGTH_LONG).show();

        notification();

        Intent intent = new Intent(PostActivity.this, PostedActivity.class);
        startActivity(intent);
        PostActivity.this.finish();
    }

    public void notification(){
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("preferJobs");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check job whether exist or not
                String temp;

                for (DataSnapshot allSnapshot: dataSnapshot.getChildren()){
                    String preferEmployee, upperSalary, lowerExpectedDuration, upperExpectedDuration, lowerSalary, preferTitle;
                    temp = allSnapshot.getKey();
                    PreferJob preferJob = dataSnapshot.child(temp).getValue(PreferJob.class);
                    lowerExpectedDuration = preferJob.getLowerExpectedDuration();
                    upperExpectedDuration = preferJob.getUpperExpectedDuration();
                    lowerSalary = preferJob.getLowerSalary();
                    upperSalary = preferJob.getUpperSalary();
                    preferTitle = preferJob.getTitle();
                    preferEmployee = preferJob.getEmployee();

                    /*System.out.println(title);
                    System.out.println(preferTitle);
                    System.out.println(lowerExpectedDuration + " " + expectedDuration + " " + upperExpectedDuration);
                    System.out.println(lowerSalary + " " + salary + " " + upperSalary);*/
                    if (employee.equals("") && title.equals(preferTitle) &&
                            ((Integer.parseInt(lowerExpectedDuration) <= Integer.parseInt(expectedDuration))&&(Integer.parseInt(expectedDuration) <= Integer.parseInt(upperExpectedDuration))) &&
                            ((Integer.parseInt(lowerSalary) <= Integer.parseInt(salary)) && (Integer.parseInt(salary) <= Integer.parseInt(upperSalary))))
                    {
                        //System.out.println("should have notification");
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(PostActivity.this, "notification")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Work: " + preferTitle)
                                .setContentText("The employee " + preferEmployee + " meet your recruitment requirements")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PostActivity.this);

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PostActivity.this, EmployerHomepageActivity.class);
        startActivity(intent);
        PostActivity.this.finish();
    }
}