package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kg.studenthelper.NewComplain;
import com.kg.studenthelper.Notification.APIServices;
import com.kg.studenthelper.Notification.Client;
import com.kg.studenthelper.Notification.Data;
import com.kg.studenthelper.Notification.MyResponse;
import com.kg.studenthelper.Notification.NotificationSender;
import com.kg.studenthelper.Notification.Token;
import com.kg.studenthelper.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorDetailActivity extends AppCompatActivity {

    String name,date,content,room,status,image,category,key,userId,building;
    TextView text_room,text_name,text_content,text_date,text_status,text_category;
    ImageView imageView;
    Button button_update;
    ImageView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_detail);

        text_room = findViewById(R.id.text_room);
        text_content = findViewById(R.id.text_problem);
        text_category = findViewById(R.id.text_category);
        text_date = findViewById(R.id.text_date);
        text_name = findViewById(R.id.text_name);
        text_status = findViewById(R.id.text_status);
        view = findViewById(R.id.image_view);
        imageView = findViewById(R.id.img_problem);
        button_update = findViewById(R.id.button_update);


        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        date = bundle.getString("date");
        category = bundle.getString("category");
        content = bundle.getString("content");
        room = bundle.getString("room");
        status = bundle.getString("status");
        image = bundle.getString("image");
        key=bundle.getString("key");
        building=bundle.getString("building");
        userId=bundle.getString("userId");


        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(text_status.getText().toString().isEmpty())
                {
                    Toast.makeText(SupervisorDetailActivity.this,"status left empty",Toast.LENGTH_LONG).show();
                }
                else
                {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("complain").child(key);
                    final String current_status=text_status.getText().toString().trim();
                    NewComplain complain=new NewComplain(building,content,room,image,key,date,userId,current_status,name,category);


                    reference.setValue(complain).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens").child(userId);

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Token token = dataSnapshot.getValue(Token.class);

                                    String userToken = token.getToken();
                                    sendNotifications(userToken, "Update", "Status of your complain dated "+date+" updated.");


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            Toast.makeText(SupervisorDetailActivity.this,"successfully updated",Toast.LENGTH_LONG).show();
                            text_status.setText(current_status);
                        }
                    });

                }

            }
        });

        //   StorageReference reference= FirebaseStorage.getInstance().getReference().child("problem-image").child(image);

        // imageView.setImage(reference);

        // imageView.setImage(ImageSource.asset(image));
        // imageView.setImage(ImageSource.uri(image));
        text_name.setText(name);
        text_status.setText(status);
        text_date.setText(date);
        text_category.setText(category);
        text_content.setText(content);
        text_room.setText(room);

        Picasso.get().load(image).into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new
                        AlertDialog.Builder(SupervisorDetailActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
              Picasso.get().load(image).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


    }

    public void sendNotifications(String userToken, String title, String message) {

        String type="update";
        APIServices apiService = Client.getClient().create(APIServices.class);
        Data data = new Data(title, message,type,building);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success == 1) {
                        Toast.makeText(SupervisorDetailActivity.this, "success ", Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Toast.makeText(SupervisorDetailActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


}