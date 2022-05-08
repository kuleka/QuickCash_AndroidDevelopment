package ca.dal.cs.csci3130.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AppliedActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseDatabase database;
    private DatabaseReference jobs;

    private RecyclerView recyclerView;
    private AppliedJobsAdapter appliedJobsAdapter;

    String employee;

    Query searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied);
        init();

        SharedPreferences user = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        employee = user.getString("firstName", "") + " " + user.getString("lastName", "");

        initializeDatabase();
    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        jobs = database.getReference("jobs");

        searchQuery = FirebaseDatabase
                .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                .getReference().child("jobs").orderByChild("employee").equalTo(employee);

        dataQuery(searchQuery);
    }

    private void init() {
        recyclerView = findViewById(R.id.AppliedPage_Content);
        recyclerView.setLayoutManager(new WrapLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void dataQuery(Query searchQuery) {

        FirebaseRecyclerOptions<Job> options = new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(searchQuery, new SnapshotParser<Job>() {
                    @NonNull
                    @Override
                    public Job parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getValue(Job.class);
                    }
                })
                .build();
        appliedJobsAdapter = new AppliedJobsAdapter(options, AppliedActivity.this);
        recyclerView.setAdapter(appliedJobsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        appliedJobsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appliedJobsAdapter.stopListening();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AppliedActivity.this, EmployeeHomepageActivity.class);
        startActivity(intent);
        AppliedActivity.this.finish();
    }


}