package ca.dal.cs.csci3130.groupproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class JobsListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private JobsAdapter jobsAdapter;
    String jobTitle = null;
    String salary = null;
    String expectedDuration = null;
    private FusedLocationProviderClient client;
    private static final Integer REQUEST_CODE = 111;
    double la1;
    double lon1;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    // Part of Code from Lab contents : firebaseCRUD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        Button toSearchButton = findViewById(R.id.jobsSearchButton);
        toSearchButton.setOnClickListener(this);
        init();

        // Get filter option from shared preferences
        SharedPreferences filterOption = getSharedPreferences("search_info", Context.MODE_PRIVATE);
        jobTitle = filterOption.getString("jobTitle", "");
        salary = filterOption.getString("salary", "");
        expectedDuration = filterOption.getString("expectedDuration", "");
        System.out.println(jobTitle);
        System.out.println(salary);
        System.out.println(expectedDuration);

        //initialize the variables relate with FusedLocationProviderClient
        client = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        la1 = location.getLatitude();
                        lon1 = location.getLongitude();
                        calculateDistance();
                    }
                }
            }
        };

        //If have permission, set the requestLocationUpdate with looper & callback function
        if (ActivityCompat.checkSelfPermission(JobsListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            setThingsUp();
        } else {
            ActivityCompat.requestPermissions(JobsListActivity.this, new
                    String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        connectToFirebaseRTDB(jobTitle, salary, expectedDuration);
    }


    private void init() {
        recyclerView = findViewById(R.id.jobsRecyclerView);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void connectToFirebaseRTDB(String jobTitle, String salary, String expectedDuration) {
        // If the filter option is null
        if ((jobTitle.isEmpty()) && (salary.isEmpty()) && (expectedDuration.isEmpty())) {
            Query searchQuery = FirebaseDatabase
                    .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                    .getReference().child("jobs");

            dataQuery(searchQuery);
        }
        // If the job title filter option exists, order the job by title and select jobs equals to jobTitle
        else if (!(jobTitle.isEmpty()) && (salary.isEmpty()) && (expectedDuration.isEmpty())) {
            Query searchQuery = FirebaseDatabase
                    .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                    .getReference().child("jobs").orderByChild("title").equalTo(jobTitle);

            dataQuery(searchQuery);
        }
        // If the salary filter option exists, order the job by salary and select salary equals to salary
        else if ((jobTitle.isEmpty()) && (!(salary.isEmpty())) && (expectedDuration.isEmpty())) {
            Query searchQuery = FirebaseDatabase
                    .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                    .getReference().child("jobs").orderByChild("salary").equalTo(salary);

            dataQuery(searchQuery);
        }
        // If the expected duration filter option exists, order the job by expected duration and select jobs equals to expected duration
        else if ((jobTitle.isEmpty()) && (salary.isEmpty()) && (!(expectedDuration.isEmpty()))) {
            Query searchQuery = FirebaseDatabase
                    .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                    .getReference().child("jobs").orderByChild("expectedDuration").equalTo(expectedDuration);

            dataQuery(searchQuery);
        }
    }

    private void dataQuery(Query searchQuery) {
        FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(searchQuery, new SnapshotParser<Job>() {
                    @NonNull
                    @Override
                    public Job parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Job job = snapshot.getValue(Job.class);
                        return job;
                    }
                })
                .build();
        jobsAdapter = new JobsAdapter(options, JobsListActivity.this);

        recyclerView.setAdapter(jobsAdapter);
        removeInfo();
    }

    // Remove filter options
    protected void removeInfo() {
        SharedPreferences.Editor editor;
        editor = getSharedPreferences("search_info", Context.MODE_PRIVATE).edit();
        editor.putString("jobTitle", "");
        editor.putString("salary", "");
        editor.putString("expectedDuration", "");
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        jobsAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //when leave the activity, stop update distance
        client.removeLocationUpdates(mLocationCallback);

    }

    @Override
    protected void onStop() {
        super.onStop();
        jobsAdapter.stopListening();
    }

    @Override
    public void onClick(View view) {
        toSearchPage();
    }

    protected void toSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent();
            myIntent = new Intent(JobsListActivity.this, EmployeeHomepageActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //Function that calculate the distance between the current location & work, update the database
    public void calculateDistance() {

        FirebaseDatabase job_database = FirebaseDatabase.getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/");
        DatabaseReference jobs = job_database.getReference("jobs");

        if (la1 != 0.0 && lon1 != 0.0) {

            System.out.println(la1);
            System.out.println(lon1);
            jobs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                        Job job = adSnapshot.getValue(Job.class);
                        if (job.getLatLong() != null) {

                            double la2 = Double.parseDouble(job.getLatLong().split(" ")[0]);
                            double lon2 = Double.parseDouble(job.getLatLong().split(" ")[1]);

                            // Haversine formula
                            double dlon = (Math.PI / 180) * lon2 - (Math.PI / 180) * lon1;
                            double dlat = (Math.PI / 180) * la2 - la1 * (Math.PI / 180);
                            double a = Math.pow(Math.sin(dlat / 2), 2)
                                    + Math.cos(la1) * Math.cos(dlat)
                                    * Math.pow(Math.sin(dlon / 2), 2);


                            double c = 2 * Math.asin(Math.sqrt(a));

                            // Radius of earth
                            double r = 6371 * 1000;

                            // calculate the result
                            double d = c * r;

                            Map<String, Object> jobUpdates = new HashMap<>();
                            jobUpdates.put("distance", d);
                            adSnapshot.getRef().updateChildren(jobUpdates);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(JobsListActivity.this, "Cannot read database!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    //setting LocationRequest and the initial distance when user enter the activity
    private void setThingsUp() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(6000).setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        client.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            la1 = location.getLatitude();
                            lon1 = location.getLongitude();
                            calculateDistance();
                        }
                    }
                });

        client.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setThingsUp();
            } else {
                Toast.makeText(this, "Permission Denied by user !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}