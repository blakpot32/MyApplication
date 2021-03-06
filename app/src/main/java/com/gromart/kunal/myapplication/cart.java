package com.gromart.kunal.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class cart extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;

    private TextView itemCost;
    private FloatingActionButton removeItem;

    private ProgressDialog progressDialog;

    private RecyclerView cart_list;
    int i;

    String name;
    String phone;
    String address;
    String email;

    User user = new User(name,email,address,address);

    public static final String ACCOUNT_SID = "AC26f9ec29b138d3913018dd7ded1cd918";
    public static final String AUTH_TOKEN = "dcc1c6038a340bedbb3ffa27873fd2d9";

    List<String> stringArray = new ArrayList<String>();

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("ItemsInCart");

        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        cart_list = (RecyclerView)findViewById(R.id.cart_items);
        removeItem = (FloatingActionButton) findViewById(R.id.remove_item);
        itemCost = (TextView) findViewById(R.id.cart_cost);
        cart_list.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        getSupportActionBar().setTitle("Cart");


        findViewById(R.id.go_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });



        progressDialog= new ProgressDialog(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build() );
        }


//        Toast.makeText(this, name , Toast.LENGTH_SHORT).show();

//        sendEmail(
//
//                "Name : " + us + "\n"
////                        "Email : " + email + "\n" +
////                        "Phone : " + phone + "\n" +
////                        "Address : " + address
//
//
//        );

    }




    @Override
    protected void onStart() {
        super.onStart();

//        Toast.makeText(this, user.getUsername(), Toast.LENGTH_SHORT).show();

        final LinearLayout cartBottom = (LinearLayout) findViewById(R.id.cart_bottom);

        FirebaseRecyclerOptions<CartUploadInfo> options =
                new FirebaseRecyclerOptions.Builder<CartUploadInfo>()
                        .setQuery(mDatabase, CartUploadInfo.class)
                        .build();

        final FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CartUploadInfo, BlogViewHolder>(options) {
            @Override
            public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_product_row, parent, false);


                return new BlogViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder viewHolder, final int position, @NonNull final CartUploadInfo model) {

                final String product_key = getRef(position).getKey() ;

                viewHolder.setTitle(model.getImageName());
                viewHolder.setPrice("\u20B9 " + model.getImagePrice());
                viewHolder.setImage(getApplicationContext(),model.getImageURL());

//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent productInfoIntent = new Intent(getApplicationContext(),ProductInfo.class);
//                        productInfoIntent.putExtra("productId",product_key);
//                        productInfoIntent.putExtra("category",category);
//                        startActivity(productInfoIntent);
//                    }
//                });

                viewHolder.mView.findViewById(R.id.remove_item).setVisibility(View.INVISIBLE);

                viewHolder.mView.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                getRef(position).removeValue();
                            }
                            catch (Exception e){

                                Toast.makeText(cart.this, "Can't Delete Item ? Go back and try again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                final Dialog dialog=new Dialog(cart.this);
                final View customLayout3 = cart.this.getLayoutInflater().inflate(R.layout.checkout_quantity, null);

                dialog.setContentView(customLayout3);

                FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid())
                        .addValueEventListener(new ValueEventListener() {

                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (Integer.parseInt(dataSnapshot.child("Total").getValue().toString()) < 300){
                                    findViewById(R.id.checkout).setClickable(false);
                                }else if(Integer.parseInt(dataSnapshot.child("Total").getValue().toString()) >= 300){
                                    findViewById(R.id.checkout).setClickable(true);
                                }

                            }

                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                        EditText quantity = customLayout3.findViewById(R.id.checkout_item_quantity);
                        quantity.setText(model.getImageQuantity());

                        dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                EditText quantity = customLayout3.findViewById(R.id.checkout_item_quantity);

                                String quant = String.valueOf(quantity.getText());

                                getRef(position).child("imageQuantity").setValue(quant);

                                dialog.cancel();

                            }
                        });
                        dialog.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.cancel();

                            }
                        });
                        findViewById(R.id.remove_item).setVisibility(View.INVISIBLE);
                    }
                });


                TextView pname = (TextView) viewHolder.mView.findViewById(R.id.cart_display_title);
                pname.setText(model.getImageName()+" ("+model.getImageQuantity()+")");

            }
        };

        firebaseRecyclerAdapter.startListening();
        cart_list.setAdapter(firebaseRecyclerAdapter);

        findViewById(R.id.no_item).setVisibility(View.INVISIBLE);
        findViewById(R.id.cart_upper).setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("ItemsInCart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int totalPrice = 0;

                        long childrenCount = dataSnapshot.getChildrenCount();
                        int childcount = (int) childrenCount;

//                        Toast.makeText(cart.this, String.valueOf(childcount), Toast.LENGTH_SHORT).show();

                        if (childcount == 0 ){
                            cartBottom.setVisibility(View.GONE);
                            FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("Total").setValue("0");
                            findViewById(R.id.no_item).setVisibility(View.VISIBLE);
                            findViewById(R.id.cart_upper).setVisibility(View.INVISIBLE);
                            findViewById(R.id.cart_items).setVisibility(View.GONE);
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            int itemPrice = 0;

                            CartUploadInfo cartUploadInfo = snapshot.getValue(CartUploadInfo.class);

                            try {
                                itemPrice = Integer.parseInt(snapshot.getValue(CartUploadInfo.class).getImagePrice());
                                itemPrice = itemPrice*Integer.parseInt(snapshot.getValue(CartUploadInfo.class).getImageQuantity());
                            } catch (NumberFormatException nfe) {
                                System.out.println("Could not parse " + nfe);
                            }

                            totalPrice = itemPrice + totalPrice;
////
////                            try {
////                                myNum = Integer.parseInt(et.getText().toString());
////                            } catch(NumberFormatException nfe) {
////                                System.out.println("Could not parse " + nfe);
////                            }

                        }
                            FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("Total").setValue(String.valueOf(totalPrice));
//
                        try {
                            FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("Total").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    itemCost.setText("\u20B9 " + dataSnapshot.getValue(String.class));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        catch (Exception e){

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog checkOutDialog=new Dialog(cart.this);
                final View customLayout = cart.this.getLayoutInflater().inflate(R.layout.checkout_final_before, null);
                checkOutDialog.setContentView(customLayout);

                final EditText checkout_address = (EditText) customLayout.findViewById(R.id.checkout_address);
                final EditText checkout_name = (EditText) customLayout.findViewById(R.id.checkout_name);
                final EditText checkout_phone = (EditText) customLayout.findViewById(R.id.checkout_phone);



                FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("address").getValue() != null){
                            checkout_address.setText(dataSnapshot.child("address").getValue().toString());
                        }

                        if (dataSnapshot.child("username").getValue() != null){
                            checkout_name.setText(dataSnapshot.child("username").getValue().toString());
                        }

                        if (dataSnapshot.child("phone").getValue() != null){
                            checkout_phone.setText(dataSnapshot.child("phone").getValue().toString());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                final String[][][] x = {new String[][]{
                        new String[]{"foo", "bar"}
                }};

                final ArrayList<String>  limits;
                limits = new ArrayList<String>();

                final List<CartUploadInfo> list = new ArrayList<CartUploadInfo>();

                final String[] total = new String[1];

                checkOutDialog.show();

                checkOutDialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog alertDialogBuilder=new Dialog(cart.this);
                        final View customLayout3 = cart.this.getLayoutInflater().inflate(R.layout.checkout_final, null);
                        alertDialogBuilder.setContentView(customLayout3);

                        alertDialogBuilder.show();

                        alertDialogBuilder.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                TextView totalCost = (TextView) findViewById(R.id.cart_cost);
                                String cost = totalCost.toString();

                                FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("address").setValue(checkout_address.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("username").setValue(checkout_name.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("phone").setValue(checkout_phone.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("User_Carts").child(firebaseAuth.getUid()).child("ItemsInCart")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                progressDialog.setTitle("Placing Order");
                                                progressDialog.setMessage("Please Wait");

                                                // Showing progressDialog.
                                                progressDialog.show();

                                                long childrenCount = dataSnapshot.getChildrenCount();
                                                int childcount = (int) childrenCount;

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    CartUploadInfo cartUploadInfo = snapshot.getValue(CartUploadInfo.class);

                                                    Calendar c = Calendar.getInstance();

                                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                    String formattedDate = df.format(c.getTime());

                                                    CartUploadInfo cartUploadInfo1 = new CartUploadInfo(cartUploadInfo.getImageName(),cartUploadInfo.getImageURL().toString(),cartUploadInfo.getImageWeight(),cartUploadInfo.getImagePrice(),cartUploadInfo.getImageQuantity(),formattedDate) ;

                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Orders")
                                                            .child(firebaseAuth.getUid()).child("ordered_items");

                                                    String ImageUploadId = databaseReference.push().getKey();

                                                    FirebaseDatabase.getInstance().getReference().child("User_Orders").child(firebaseAuth.getCurrentUser().getUid()).child("total").setValue(((TextView) findViewById(R.id.cart_cost)).getText().toString());

                                                    databaseReference.child(ImageUploadId).setValue(cartUploadInfo1);

                                                    dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();

                                                            Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_LONG).show();

                                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                                                    .setSmallIcon(R.mipmap.ic_gromart_round)
                                                                    .setContentTitle("Gromart")
                                                                    .setContentText("Thank You for placing an order at Gromart ! You can see your order by going to My Orders")
                                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                                            .bigText("Thank You for placing an order at Gromart ! You can see your order by going to My Orders. For further queries, contact us"))
                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                                                            notificationManager.notify(1, mBuilder.build());



                                                            finish();
                                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                        }
                                                    });

                                                    list.add(snapshot.getValue(CartUploadInfo.class));

                                                    TextView totalview = (TextView) findViewById(R.id.cart_cost);
                                                    total[0] = (String) totalview.getText();


                                                }

//                                                GenericTypeIndicator<List<CartUploadInfo>> genericTypeIndicator =new GenericTypeIndicator<List<CartUploadInfo>>(){};
//
//                                                List<CartUploadInfo> taskDesList=dataSnapshot.getValue(genericTypeIndicator);

                                                 /* This method is called once with the initial value and again whenever data at this location is updated.*/

                                                //String order_data = list.toArray().toString();
                                                //Toast.makeText(cart.this, order_data, Toast.LENGTH_SHORT).show();

                                                ArrayList<String> your_order_fool = new ArrayList<>();
                                                for(int i=0;i < list.size();i++){
                                                    your_order_fool.add("Name = " + list.get(i).getImageName() + " Weight =" + list.get(i).getImageWeight());
                                                    //Toast.makeText(getApplicationContext(),"Name = "+list.get(i).getImageName() + "Weight ="+list.get(i).getImageWeight(),Toast.LENGTH_LONG).show();
                                                }


                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                mDatabase1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String ttl = total[0];

                                        saveOrder(


                                                "Name : " + dataSnapshot.child("username").getValue().toString() + "\n" +
                                                        "Phone : " + dataSnapshot.child("phone").getValue().toString() + "\n" +
                                                        "Address : " + dataSnapshot.child("address").getValue().toString() + "\n" +
                                                        "Total : " + ttl +
                                                        "\n"+"___________________________", list

                                        );
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                                alertDialogBuilder.cancel();

                            }
                        });
                        alertDialogBuilder.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                alertDialogBuilder.cancel();

                            }
                        });

                        checkOutDialog.cancel();

                    }
                });
                checkOutDialog.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkOutDialog.cancel();

                    }
                });

                checkout_address.addTextChangedListener(new TextWatcher() {
                    private void handleText() {
                        // Grab the button
                        final Button okButton = checkOutDialog.findViewById(R.id.positive);
                        if (checkout_address.getText().length() == 0) {
                            okButton.setEnabled(false);
                        } else if (checkout_address.getText().length() > 15){
                            okButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handleText();
                    }
                });




                checkout_name.addTextChangedListener(new TextWatcher() {
                    private void handleText() {
                        // Grab the button
                        final Button okButton = checkOutDialog.findViewById(R.id.positive);
                        if (checkout_name.getText().length() == 0) {
                            okButton.setEnabled(false);
                        }else if(checkout_name.getText().length() == 0){

                            okButton.setEnabled(true);

                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView product_title;
        private TextView product_price;

        public BlogViewHolder(final View itemView) {
            super(itemView);

            product_title = itemView.findViewById(R.id.cart_display_title);
            product_price = itemView.findViewById(R.id.cart_display_price);
            mView = itemView;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemView.findViewById(R.id.remove_item).setVisibility(View.VISIBLE);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.findViewById(R.id.remove_item).setVisibility(View.INVISIBLE);
                }
            });
        }

        public void setTitle(String title){
            title = title.substring(0,1).toUpperCase() + title.substring(1).toLowerCase();
            product_title.setText(title);
        }

        public void setPrice(String price){

            product_price.setText(price);

        }

        public void setImage(Context ctx, String image){

            ImageView product_image =itemView.findViewById(R.id.cart_display_image);
            Glide.with(ctx).load(image).into(product_image);

        }

    }

//    private void sendEmail(String message, List<CartUploadInfo> list) {
//        //Getting content for email
//        String email = "gromarthelp@gmail.com";
//        String subject = "New Order";
//        String final_order = "";
//        for(int i=0;i<list.size();i++){
//            final_order = final_order.concat("\n" +" Name : "+list.get(i).getImageName().toString()).concat("\n" +" Quantity : "+list.get(i).getImageQuantity().toString())
//            .concat("\n" +" Price : "+list.get(i).getImagePrice().toString()).concat("\n" +" Weight : "+list.get(i).getImageWeight().toString())
//            .concat("\n" +" Date and Time : "+list.get(i).getDateTime().toString())
//                    .concat("\n"+"___________________________");
//        }
//        Log.d("BUSTA", final_order);
//
//        //Creating SendMail object
//        SendMail sm = new SendMail(this, email, subject, message.concat(final_order));
//
//        String finallist = (String) message.concat(final_order);
//
//        DatabaseReference databaseReference;
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pending_orders");
//
//        allorders allorders = new allorders(finallist);
//
//        // Getting image upload ID.
//        String ImageUploadId = databaseReference.push().getKey();
//
//        // Adding image upload id s child element into databaseReference.
//        databaseReference.child(ImageUploadId).setValue(allorders);
//
//        //Executing sendmail to send email
//        sm.execute();
//        Log.d("BUSTA", "email sent ksss");
//    }

    private void saveOrder(String message, List<CartUploadInfo> list){

        String email = "gromarthelp@gmail.com";
        String subject = "New Order";
        String final_order = "";
        for(int i=0;i<list.size();i++){
            final_order = final_order.concat("\n" +" Name : "+list.get(i).getImageName().toString()).concat("\n" +" Quantity : "+list.get(i).getImageQuantity().toString())
                    .concat("\n" +" Price : "+list.get(i).getImagePrice().toString()).concat("\n" +" Weight : "+list.get(i).getImageWeight().toString())
                    .concat("\n" +" Date and Time : "+list.get(i).getDateTime().toString())
                    .concat("\n"+"___________________________");
        }

        String finallist = (String) message.concat(final_order);

        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pending_orders");

        allorders allorders = new allorders(finallist);

        // Getting image upload ID.
        String ImageUploadId = databaseReference.push().getKey();

        // Adding image upload id s child element into databaseReference.
        databaseReference.child(ImageUploadId).setValue(allorders);

    }

}
