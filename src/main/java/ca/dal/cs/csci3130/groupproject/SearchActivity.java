package ca.dal.cs.csci3130.groupproject;

import androidx.appcompat.app.AppCompatActivity;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup jobTitleField;
    private EditText salaryField, expectedDurationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        jobTitleField = findViewById(R.id.Employee_SearchJobTitleResult);
        salaryField = findViewById(R.id.Employee_SalaryRange);
        expectedDurationField = findViewById(R.id.Employee_ExpectedDurationRange);

        Button cancelButton = findViewById(R.id.Employee_Search_Cancel);
        cancelButton.setOnClickListener(this);

        Button searchButton = findViewById(R.id.Employee_Search_Search);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Employee_Search_Cancel) {
            searchCancel();
        } else if (view.getId() == R.id.Employee_Search_Search) {
            toSearch();
        }
    }

    public void searchCancel() {
        emptySearchFilter();
    }

    public void toSearch() {
        getSearchFilter();
    }

    // Set all search filter to null
    public void emptySearchFilter() {
        int len = jobTitleField.getChildCount();
        for (int i = 0; i < len; i++) {
            RadioButton radioField = (RadioButton) jobTitleField.getChildAt(i);
            if (i == 0) {
                radioField.setChecked(true);
            } else {
                radioField.setChecked(false);
            }
        }
        salaryField.setText("");
        expectedDurationField.setText("");
    }

    public void getSearchFilter() {
        String jobTitle;
        RadioButton radioButton = (RadioButton) findViewById(jobTitleField.getCheckedRadioButtonId());
        jobTitle = radioButton.getText().toString().trim();
        String salary = salaryField.getText().toString().trim();
        String expectedDuration = expectedDurationField.getText().toString().trim();

        // No parameter is selected
        if ((jobTitle.equals("All jobs")) && (salary.isEmpty()) && (expectedDuration.isEmpty())) {
            // Toast.makeText(SearchActivity.this, "no filter", Toast.LENGTH_LONG).show();
            storeInfo(null, null, null);
            Intent intent = new Intent(SearchActivity.this, JobsListActivity.class);
            startActivity(intent);
        }
        // Job title is selected
        else if (!(jobTitle.equals("All jobs")) && (salary.isEmpty()) && (expectedDuration.isEmpty())) {
            // Toast.makeText(SearchActivity.this, jobTitle, Toast.LENGTH_LONG).show();
            storeInfo(jobTitle, null, null);
            Intent intent = new Intent(SearchActivity.this, JobsListActivity.class);
            startActivity(intent);
        }
        // Salary is selected
        else if ((jobTitle.equals("All jobs")) && (!salary.isEmpty()) && (expectedDuration.isEmpty())) {
            // Toast.makeText(SearchActivity.this, salary, Toast.LENGTH_LONG).show();
            storeInfo(null, salary, null);
            Intent intent = new Intent(SearchActivity.this, JobsListActivity.class);
            startActivity(intent);
        }
        // Expected duration is selected
        else if ((jobTitle.equals("All jobs")) && (salary.isEmpty()) && (!expectedDuration.isEmpty())) {
            // Toast.makeText(SearchActivity.this, expectedDuration, Toast.LENGTH_LONG).show();
            storeInfo(null, null, expectedDuration);
            Intent intent = new Intent(SearchActivity.this, JobsListActivity.class);
            startActivity(intent);
        }
        // Select multiple parameters
        else {
            Toast.makeText(SearchActivity.this, "Please input only one filter", Toast.LENGTH_LONG).show();
            emptySearchFilter();
        }

    }

    protected void storeInfo(String jobTitle, String salary, String expectedDuration) {
        // Set all information to null first
        SharedPreferences.Editor editor;
        editor = getSharedPreferences("search_info", Context.MODE_PRIVATE).edit();
        editor.putString("jobTitle", "");
        editor.putString("salary", "");
        editor.putString("expectedDuration", "");
        editor.commit();

        // Set the parameters to shared preferences
        SharedPreferences.Editor newEditor;
        newEditor = getSharedPreferences("search_info", Context.MODE_PRIVATE).edit();
        newEditor.putString("jobTitle", jobTitle);
        newEditor.putString("salary", salary);
        newEditor.putString("expectedDuration", expectedDuration);
        newEditor.commit();
    }
}