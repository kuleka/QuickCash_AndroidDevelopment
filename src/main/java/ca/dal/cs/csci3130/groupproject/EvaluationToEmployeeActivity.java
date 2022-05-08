package ca.dal.cs.csci3130.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
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

import java.util.HashMap;
import java.util.Map;

public class EvaluationToEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText evaluationField2;
    private String jobTitle2;
    private Job job;
    private FirebaseDatabase database;
    private DatabaseReference jobs;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_employee);

        evaluationField2 = findViewById(R.id.Employer_EvaluatePoint);

        Button cancelButton2 = findViewById(R.id.Employer_Evaluation_Cancel);
        cancelButton2.setOnClickListener(this);

        Button feedbackButton2 = findViewById(R.id.Employer_Evaluation_Feedback);
        feedbackButton2.setOnClickListener(this);
        Intent intent = getIntent();
        initializeDatabase();
        this.getValues(intent);
    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance(getResources().getString(R.string.FIREBASE_LINK));
        jobs = database.getReference().child("jobs");
    }
    //get user name from shared preferences
    protected void getValues(Intent intent) {
        jobId = intent.getStringExtra("jobId");
        jobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                job = dataSnapshot.child(jobId).getValue(Job.class);
                jobTitle2 = job.getTitle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Employer_Evaluation_Cancel) {
            evaluationCancel();
        } else if (view.getId() == R.id.Employer_Evaluation_Feedback) {
            toAppliedActivity();
        }
    }

    public void evaluationCancel() {
        emptyEvaluation();
    }

    // To which page
    public void toAppliedActivity() {
        getEvaluationPoint();
        Intent intent = new Intent(EvaluationToEmployeeActivity.this, PostedActivity.class);
        startActivity(intent);
    }

    // Set all search filter to null
    public void emptyEvaluation() {
        evaluationField2.setText("");
    }

    public void getEvaluationPoint() {
        String evaluationPoint = evaluationField2.getText().toString().trim();

        if (evaluationPoint.isEmpty()) {
            Toast.makeText(EvaluationToEmployeeActivity.this, "Please input the evaluation point", Toast.LENGTH_LONG).show();
        } else {
            storeInfo(evaluationPoint);
        }
    }

    protected void storeInfo(String evaluationPoint) {
        Map<String, Object> jobEvaluation = new HashMap<>();
        jobEvaluation.put("evaluationToEmployee", evaluationPoint);
        jobs.child(jobId).updateChildren(jobEvaluation).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "evaluation successfully", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "evaluation failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EvaluationToEmployeeActivity.this, EmployerHomepageActivity.class);
        startActivity(intent);
        EvaluationToEmployeeActivity.this.finish();
    }
}