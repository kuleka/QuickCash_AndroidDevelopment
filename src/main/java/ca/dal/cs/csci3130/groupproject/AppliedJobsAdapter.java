package ca.dal.cs.csci3130.groupproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AppliedJobsAdapter extends FirebaseRecyclerAdapter<Job, AppliedJobsAdapter.JobsViewHolder> {

    // Part of Code from Lab contents : firebaseCRUD

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public AppliedJobsAdapter(@NonNull FirebaseRecyclerOptions<Job> options, Context context) {
        super(options);
        this.context = context;
        SharedPreferences user = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AppliedJobsAdapter.JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applied_job, parent, false);
        return new AppliedJobsAdapter.JobsViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull AppliedJobsAdapter.JobsViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Job job) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "complete");
        holder.jobTitle.setText(job.getTitle());
        holder.jobDate.setText("Date: " + job.getDate());
        holder.jobDuration.setText("Duration: " + job.getExpectedDuration() + " Days");
        holder.jobSalary.setText("Salary: " + job.getSalary() + " CAD");
        holder.jobPlace.setText("Place: " + job.getPlace());
        holder.jobUrgency.setText("Urgency: " + job.getUrgency());
        holder.jobStatus.setText("Status: " + job.getStatus());
        holder.jobEmployer.setText("Employer: " + job.getEmployer());
        holder.jobEvaluationToEmployee.setText("Evaluation to Employee: " + job.getEvaluationToEmployee());
        holder.jobEvaluationToEmployer.setText("Evaluation to Employer: " + job.getEvaluationToEmployer());

        if(!employeeIsEvaluated(job)) {
            holder.jobEvaluationToEmployee.setVisibility(View.GONE);
        }

        if(!employerIsEvaluated(job)){
            holder.jobEvaluationToEmployer.setVisibility(View.GONE);
        }


        if( employerIsEvaluated(job) || job.getStatus().equals("incomplete")) {
            holder.evaluateButton.setVisibility(View.GONE);
        }

        if(job.getStatus().equals("complete")){
            holder.finishButton.setVisibility(View.GONE);
        }

        holder.finishButton.setOnClickListener(
                view -> FirebaseDatabase.getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                        .getReference()
                        .child("jobs").child(getRef(position).getKey())
                        .updateChildren(map).addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.context, "Job finish successful", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(holder.context, "Job finish unsuccessful", Toast.LENGTH_SHORT).show())
        );

        holder.evaluateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, EvaluationActivity.class);
                intent.putExtra("jobId", getRef(position).getKey());
                holder.context.startActivity(intent);
            }
        });
    }

    public class JobsViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitle;
        private final TextView jobDate;
        private final TextView jobDuration;
        private final TextView jobSalary;
        private final TextView jobPlace;
        private final TextView jobUrgency;
        private final TextView jobStatus;
        private final TextView jobEmployer;
        private final TextView jobEvaluationToEmployee;
        private final TextView jobEvaluationToEmployer;
        private final Context context;
        private final Button finishButton;
        private final Button evaluateButton;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle1);
            jobDate = itemView.findViewById(R.id.jobDate1);
            jobDuration = itemView.findViewById(R.id.jobDuration1);
            jobPlace = itemView.findViewById(R.id.jobPlace1);
            jobSalary = itemView.findViewById(R.id.jobSalary1);
            jobUrgency = itemView.findViewById(R.id.jobUrgency1);
            jobStatus = itemView.findViewById(R.id.jobStatus1);
            jobEmployer = itemView.findViewById(R.id.jobEmployer1);
            jobEvaluationToEmployee = itemView.findViewById(R.id.jobEvaluationToEmployee1);
            jobEvaluationToEmployer = itemView.findViewById(R.id.jobEvaluationToEmployer1);
            finishButton = itemView.findViewById(R.id.finishJobButton);
            context = itemView.getContext();
            evaluateButton = itemView.findViewById(R.id.EvaluateJobButton);
        }
    }

    protected boolean employeeIsEvaluated(Job job) {
        return !job.getEvaluationToEmployee().equals("");
    }

    protected boolean employerIsEvaluated(Job job) {
        return !job.getEvaluationToEmployer().equals("");
    }
}
