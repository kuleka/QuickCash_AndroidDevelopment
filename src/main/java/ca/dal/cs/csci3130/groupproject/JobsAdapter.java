package ca.dal.cs.csci3130.groupproject;

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

import java.util.HashMap;
import java.util.Map;

public class JobsAdapter extends FirebaseRecyclerAdapter<Job, JobsAdapter.JobsViewHolder> {

    // Part of Code from Lab contents : firebaseCRUD

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    String employee;
    String employeeInDatabase;
    public JobsAdapter(@NonNull FirebaseRecyclerOptions<Job> options, Context context) {
        super(options);
        this.context = context;
        SharedPreferences user = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        employee = user.getString("firstName", "") + " " + user.getString("lastName", "");
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobsViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobsViewHolder holder, int position, @NonNull Job job) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        holder.jobTitle.setText(job.getTitle());
        holder.jobDate.setText("Date: " + job.getDate());
        holder.jobDuration.setText("Duration: " + job.getExpectedDuration() + " Days");
        holder.jobSalary.setText("Salary: " + job.getSalary() + " CAD");
        holder.jobPlace.setText("Place: " + job.getPlace());
        if (job.getDistance() >= 0){
            holder.jobDistance.setText("Distance: " + job.getDistance() + " m");
        }else{
            holder.jobDistance.setVisibility(View.GONE);
        }

        if (job.getEmployee().equals("")){
            holder.apply.setOnClickListener(
                    view -> FirebaseDatabase.getInstance("https://groupproject-f4fa0-default-rtdb.firebaseio.com/")
                            .getReference()
                            .child("jobs").child(getRef(position).getKey())
                            .updateChildren(map).addOnSuccessListener(aVoid -> {
                                Toast.makeText(holder.context, "Job applied successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(holder.context, "Job apply failed", Toast.LENGTH_SHORT).show())
            );
        }else{
            holder.apply.setOnClickListener(
                    aVoid -> {
                        Toast.makeText(holder.context, "This job has already been applied for, choose another.", Toast.LENGTH_SHORT).show();
                    }
            );
        }
    }

    public class JobsViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitle;
        private final TextView jobDate;
        private final TextView jobDuration;
        private final TextView jobSalary;
        private final TextView jobPlace;
        private final TextView jobDistance;
        private final Button apply;
        private final Context context;

        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobDuration = itemView.findViewById(R.id.jobDuration);
            jobPlace = itemView.findViewById(R.id.jobPlace);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobDistance = itemView.findViewById(R.id.jobDistance);
            apply = itemView.findViewById(R.id.button);
            context = itemView.getContext();
        }
    }
}

