package com.gromart.kunal.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductInfo extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference userCartRef;

    StorageReference storageReference;

    LinearLayout add_cart;

    private ImageView product_display_image;
    private TextView product_display_name;
    private TextView product_display_weight;
    private TextView product_display_features;
    private TextView product_display_price;
    private TextView product_display_disclaimer;
    private TextView product_display_ingreients;
    private TextView product_display_packaging;
    private String productURL;

    private FirebaseAuth firebaseAuth;

    private FloatingActionButton addtocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);

        String product_key = getIntent().getStringExtra("productId");
        final String product_category = getIntent().getStringExtra("category");

        add_cart =findViewById(R.id.newaddcart);

        firebaseAuth = FirebaseAuth.getInstance();

        product_display_name = (TextView)findViewById(R.id.product_info_name);
        product_display_weight = (TextView)findViewById(R.id.product_info_weight);
        product_display_features = (TextView)findViewById(R.id.product_info_features);
        product_display_price = (TextView)findViewById(R.id.product_info_price);
        product_display_disclaimer = (TextView)findViewById(R.id.product_info_disclaimer);
        product_display_ingreients = (TextView)findViewById(R.id.product_info_ingredients);
        product_display_packaging = (TextView)findViewById(R.id.product_info_packaging);
        product_display_image = (ImageView)findViewById(R.id.product_info_image);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(product_category);
        storageReference = FirebaseStorage.getInstance().getReference("User_Cart").child(product_category);


        mDatabase.child(product_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String product_name = (String) dataSnapshot.child("imageName").getValue();
                String product_weight = (String) dataSnapshot.child("imageWeight").getValue();
                String product_ingredients = (String) dataSnapshot.child("imageIngredients").getValue();
                String product_features = (String) dataSnapshot.child("imageFeatures").getValue();
                String product_price = (String) dataSnapshot.child("imagePrice").getValue();
                String product_packaging = (String) dataSnapshot.child("imagePacking").getValue();
                String product_disclaimer = (String) dataSnapshot.child("imageDisclaimer").getValue();
                String product_image = (String) dataSnapshot.child("imageURL").getValue();

                product_display_name.setText(product_name);
                product_display_weight.setText(product_weight);
                product_display_ingreients.setText(product_ingredients);
                product_display_features.setText(product_features);
                product_display_price.setText(product_price);
                product_display_packaging.setText(product_packaging);
                product_display_disclaimer.setText(product_disclaimer);

                Glide.with(getApplicationContext()).load(product_image).into(product_display_image);
                setTitle(product_display_name.getText().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(firebaseAuth.getCurrentUser() == null){
            add_cart.setVisibility(View.INVISIBLE);
        }

        if (add_cart.getVisibility() == View.VISIBLE) {

            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (firebaseAuth.getCurrentUser() != null) {

                        try{
                        userCartRef = FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("ItemsInCart");
                            addItemToCart();
                        } catch (Exception e) {
                            Toast.makeText(ProductInfo.this, "Can't add item to cart ? Go back and try again", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(ProductInfo.this, "Item Added To Your Cart", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }

        final ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        imageView.setRotation(imageView.getRotation() + 90);

        final ImageView imageView2 = (ImageView) findViewById(R.id.imageView4);
        imageView2.setRotation(imageView2.getRotation() + 90);

        final ImageView imageView3 = (ImageView) findViewById(R.id.imageView5);
        imageView3.setRotation(imageView3.getRotation() + 90);

        final ImageView imageView4 = (ImageView) findViewById(R.id.imageView6);
        imageView4.setRotation(imageView4.getRotation() + 90);

        final ImageView imageView5 = (ImageView) findViewById(R.id.imageView7);
        imageView5.setRotation(imageView5.getRotation() + 90);


        final ExpandableLinearLayout content1=(ExpandableLinearLayout) findViewById(R.id.content1);
        final ExpandableLinearLayout content2=(ExpandableLinearLayout) findViewById(R.id.content2);
        final ExpandableLinearLayout content3=(ExpandableLinearLayout) findViewById(R.id.content3);
        final ExpandableLinearLayout content4=(ExpandableLinearLayout) findViewById(R.id.content4);
        final ExpandableLinearLayout content5=(ExpandableLinearLayout) findViewById(R.id.content5);
        RelativeLayout click_layout1=(RelativeLayout) findViewById(R.id.ingredientlayout);
        RelativeLayout click_layout2=(RelativeLayout) findViewById(R.id.weightlayout);
        RelativeLayout click_layout3=(RelativeLayout) findViewById(R.id.featureslayout);
        RelativeLayout click_layout4=(RelativeLayout) findViewById(R.id.packaginglayout);
        RelativeLayout click_layout5=(RelativeLayout) findViewById(R.id.disclaimerlayout);

//to toggle content
        click_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content1.toggle();
                imageView.setRotation(imageView.getRotation() + 180);
            }
        });
        click_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content2.toggle();
                imageView2.setRotation(imageView2.getRotation() + 180);
            }
        });
        click_layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content3.toggle();
                imageView3.setRotation(imageView3.getRotation() + 180);
            }
        });
        click_layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content4.toggle();
                imageView4.setRotation(imageView4.getRotation() + 180);
            }
        });
        click_layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content5.toggle();
                imageView5.setRotation(imageView5.getRotation() + 180);
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    public void addItemToCart(){
        final String product_key = getIntent().getStringExtra("productId");

        mDatabase.child(product_key).child("imageURL").addValueEventListener(new ValueEventListener() {

            final String productName = product_display_name.getText().toString();
            final String productWeight = product_display_weight.getText().toString();
            final String productPrice = product_display_price.getText().toString();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String productURL = (String) dataSnapshot.getValue(String.class);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                CartUploadInfo imageUploadInfo = new CartUploadInfo(productName,productURL,productWeight,productPrice,"1",formattedDate);

                // Adding image upload id s child element into databaseReference.
                userCartRef.child(product_key).setValue(imageUploadInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
