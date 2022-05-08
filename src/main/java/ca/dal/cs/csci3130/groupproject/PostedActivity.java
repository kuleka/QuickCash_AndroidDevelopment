package ca.dal.cs.csci3130.groupproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostedActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    private DatabaseReference jobs;

    private RecyclerView recyclerView;
    private PostedJobsAdapter postedJobsAdapter;

    String employer;

    Query searchQuery;


    // Part of Code from Lab contents : firebaseCRUD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted);
        init();

        SharedPreferences user = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        employer = user.getString("firstName", "") + " " + user.getString("lastName", "");

        initializeDatabase();
    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        jobs = database.getReference("jobs");

        searchQuery = FirebaseDatabase
                .getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                .getReference().child("jobs").orderByChild("employer").equalTo(employer);

        dataQuery(searchQuery);
    }

    private void init() {
        recyclerView = findViewById(R.id.PostedPage_Content);
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
        postedJobsAdapter = new PostedJobsAdapter(options, PostedActivity.this);
        recyclerView.setAdapter(postedJobsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        postedJobsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        postedJobsAdapter.stopListening();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PostedActivity.this, EmployerHomepageActivity.class);
        startActivity(intent);
        PostedActivity.this.finish();
    }
}