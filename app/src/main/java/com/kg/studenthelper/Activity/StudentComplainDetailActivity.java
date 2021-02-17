package com.kg.studenthelper.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.kg.studenthelper.R;
import com.squareup.picasso.Picasso;

public class StudentComplainDetailActivity extends AppCompatActivity {

    String name,date,content,room,status,image,category,key,userId,building;
    TextView text_room,text_name,text_content,text_date,text_status,text_category;
    ImageView imageView;

    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complain_detail);

        text_room = findViewById(R.id.text_room);
        text_content = findViewById(R.id.text_problem);
        text_category = findViewById(R.id.text_category);
        text_date = findViewById(R.id.text_date);
        text_name = findViewById(R.id.text_name);
        text_status = findViewById(R.id.text_status);
        view = findViewById(R.id.image_view);
        imageView = findViewById(R.id.img_problem);

        getSupportActionBar().setTitle("Complain Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                        AlertDialog.Builder(StudentComplainDetailActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(image).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
}