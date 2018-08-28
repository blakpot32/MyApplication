package com.gromart.kunal.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class all_orders extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private RecyclerView mOrderslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Pending_orders");

        mOrderslist = (RecyclerView)findViewById(R.id.order_list);
        mOrderslist.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<allorders> options = new FirebaseRecyclerOptions.Builder<allorders>().setQuery(mDatabase, allorders.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<allorders, BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_list, parent, false);

                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder viewHolder, final int position, @NonNull final allorders model) {

                viewHolder.setOrder(model.getorder());

                viewHolder.mView.findViewById(R.id.imageButton).setVisibility(View.INVISIBLE);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            viewHolder.mView.findViewById(R.id.imageButton).setVisibility(View.VISIBLE);

                        }
                        catch (Exception e){

                            Toast.makeText(getApplicationContext(), "Can't Delete Item ? Go back and try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                viewHolder.mView.findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(all_orders.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        } else {
                            builder = new AlertDialog.Builder(all_orders.this);
                        }
                        builder.setTitle("Order Completed ?")
                                .setMessage("Are you sure you want to mark this order as complete?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference databaseReference;

                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Completed_orders");

                                        allorders allorders = new allorders(model.getorder());

                                        // Getting image upload ID.
                                        String ImageUploadId = databaseReference.push().getKey();

                                        // Adding image upload id s child element into databaseReference.
                                        databaseReference.child(ImageUploadId).setValue(allorders);

                                        getRef(position).removeValue();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

            }
        };
        firebaseRecyclerAdapter.startListening();
        mOrderslist.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView product_title;

        public BlogViewHolder(final View itemView) {
            super(itemView);

            product_title = itemView.findViewById(R.id.order_display);
            mView = itemView;
        }

        public void setOrder(String title){
            product_title.setText(title);
        }
    }
}
