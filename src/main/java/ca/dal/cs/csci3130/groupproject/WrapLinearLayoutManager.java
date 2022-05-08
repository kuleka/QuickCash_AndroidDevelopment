package ca.dal.cs.csci3130.groupproject;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapLinearLayoutManager extends LinearLayoutManager {
// Code from Lab contents : firebaseCRUD

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("WrapLinearLayoutManager", "Index Out Of Bound Exception in RecyclerView");
        }
    }
}
