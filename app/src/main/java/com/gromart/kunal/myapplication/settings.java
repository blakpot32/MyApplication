package com.gromart.kunal.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Tony on 14/02/2018.
 */

public class settings extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private RecyclerView mproductlist;

    private DatabaseReference mDatabase;

    private TextView name;
    private TextView address;
    private TextView phone;
    private TextView email;

    final String[] key = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        setTitle("Settings");

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().build();

        firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

        final Dialog dialog=new Dialog(settings.this);
        final View customLayout3 = settings.this.getLayoutInflater().inflate(R.layout.settings_name1, null);
        dialog.setContentView(customLayout3);

        final Dialog dialog2=new Dialog(settings.this);
        final View customLayout4 = settings.this.getLayoutInflater().inflate(R.layout.settings_add1, null);
        dialog2.setContentView(customLayout4);

        final EditText checkout_address1 = (EditText) dialog2.findViewById(R.id.settings_add);


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                if (checkout_address1.getText().length() < 15)
                    dialog2.findViewById(R.id.positive).setEnabled(false);
                else if(checkout_address1.getText().length() >= 15)
                    dialog2.findViewById(R.id.positive).setEnabled(true);

            }
        });


        name = findViewById(R.id.settings_name);
        address = findViewById(R.id.settings_address2);

        CardView cardView1 = (CardView)findViewById(R.id.card1);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText name = customLayout3.findViewById(R.id.settings_name);

                        String sname = String.valueOf(name.getText());

                        mDatabase.child("username").setValue(sname);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(sname).build();
                        firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                        dialog.cancel();

                    }
                });
                dialog.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.cancel();

                    }
                });
            }
        });

        CardView cardView2 = (CardView)findViewById(R.id.card2);

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog2.show();

                dialog2.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText name = dialog2.findViewById(R.id.settings_add);

                        String sname = String.valueOf(name.getText());

                        mDatabase.child("address").setValue(sname);

                        dialog2.cancel();

                    }
                });
                dialog2.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog2.cancel();

                    }
                });

                checkout_address1.addTextChangedListener(new TextWatcher() {
                    private void handleText() {
                        // Grab the button
                        final Button okButton = dialog2.findViewById(R.id.positive);

                        if (checkout_address1.getText().length() > 15){
                            okButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        handleText();
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handleText();
                    }
                });
            }
        });

        TextView textView3 = (TextView) findViewById(R.id.settings_phone);
        final TextView textViewphone = (TextView) findViewById(R.id.settings_phone2);
        CardView cardView3 = (CardView)findViewById(R.id.card3);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("username").getValue().toString());
                address.setText(dataSnapshot.child("address").getValue().toString());
                textViewphone.setText(dataSnapshot.child("phone").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}