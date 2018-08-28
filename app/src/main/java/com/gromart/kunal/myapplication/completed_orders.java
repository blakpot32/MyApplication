package com.gromart.kunal.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class completed_orders extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView mOrderslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Completed_orders");

        mOrderslist = (RecyclerView)findViewById(R.id.complete_order_list);
        mOrderslist.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<allorders> options = new FirebaseRecyclerOptions.Builder<allorders>().setQuery(mDatabase, allorders.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<allorders, all_orders.BlogViewHolder>(options) {
            @Override
            public all_orders.BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_list, parent, false);

                return new all_orders.BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final all_orders.BlogViewHolder viewHolder, final int position, @NonNull final allorders model) {

                viewHolder.setOrder(model.getorder());

                viewHolder.mView.findViewById(R.id.imageButton).setVisibility(View.INVISIBLE);

            }
        };
        firebaseRecyclerAdapter.startListening();
        mOrderslist.setAdapter(firebaseRecyclerAdapter);


    }
}
