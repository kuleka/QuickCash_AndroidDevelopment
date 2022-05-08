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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

public class PostedJobsAdapter extends FirebaseRecyclerAdapter<Job, PostedJobsAdapter.JobsViewHolder> {

    // Part of Code from Lab contents : firebaseCRUD

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public PostedJobsAdapter(@NonNull FirebaseRecyclerOptions<Job> options, Context context) {
        super(options);
        this.context = context;
        SharedPreferences user = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted_job, parent, false);
        return new JobsViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobsViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Job job) {
        holder.jobTitle.setText(job.getTitle());
        holder.jobDate.setText("Date: " + job.getDate());
        holder.jobDuration.setText("Duration: " + job.getExpectedDuration() + " Days");
        holder.jobSalary.setText("Salary: " + job.getSalary() + " CAD");
        holder.jobPlace.setText("Place: " + job.getPlace());
        holder.jobUrgency.setText("Urgency: " + job.getUrgency());
        holder.jobStatus.setText("Status: " + job.getStatus());
        holder.jobEmployee.setText("Employee: " + job.getEmployee());
        holder.jobEvaluationToEmployer.setText("Evaluation to Employer: " + job.getEvaluationToEmployer());
        holder.jobEvaluationToEmployee.setText("Evaluation to Employee: " + job.getEvaluationToEmployee());

        if(!employeeIsEvaluated(job)){
            holder.jobEvaluationToEmployee.setVisibility(View.GONE);
        }

        if(!employerIsEvaluated(job)){
            holder.jobEvaluationToEmployer.setVisibility(View.GONE);
        }

        if(job.getStatus().equals("incomplete") || employeeIsEvaluated(job)){
            holder.evaluateButton2.setVisibility(View.GONE);
        }

        if(job.getStatus().equals("incomplete")){
            holder.payASalary.setVisibility(View.GONE);
        }


        holder.payASalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("salary", Context.MODE_PRIVATE).edit();
                editor.putString("Salary", job.getSalary());
                editor.commit();
                Intent intent = new Intent(holder.context, PaySalaryActivity.class);
                context.startActivity(intent);
            }
        });

        holder.evaluateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, EvaluationToEmployeeActivity.class);
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
        private final TextView jobEmployee;
        private final TextView jobEvaluationToEmployer;
        private final TextView jobEvaluationToEmployee;
        private final Context context;
        private final Button payASalary;
        private final Button evaluateButton2;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobDuration = itemView.findViewById(R.id.jobDuration);
            jobPlace = itemView.findViewById(R.id.jobPlace);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobUrgency = itemView.findViewById(R.id.jobUrgency);
            jobStatus = itemView.findViewById(R.id.jobStatus);
            jobEmployee = itemView.findViewById(R.id.jobEmployee);
            jobEvaluationToEmployer = itemView.findViewById(R.id.jobEvaluationToEmployer);
            jobEvaluationToEmployee = itemView.findViewById(R.id.jobEvaluationToEmployee);
            payASalary = itemView.findViewById(R.id.payASalaryButton);
            evaluateButton2 = itemView.findViewById(R.id.EvaluateJobButton2);
            context = itemView.getContext();

        }
    }

    protected boolean employeeIsEvaluated(Job job) {
        return !job.getEvaluationToEmployee().equals("");
    }

    protected boolean employerIsEvaluated(Job job) {
        return !job.getEvaluationToEmployer().equals("");
    }
}

