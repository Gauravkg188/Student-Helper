package com.kg.studenthelper.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.kg.studenthelper.R;
import com.squareup.picasso.Picasso;

public class NoticeDetailActivity extends AppCompatActivity {

    TextView text_notice,text_date;
    ImageView img_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

    text_notice=findViewById(R.id.text_notice);
    img_notice=findViewById(R.id.img_notice);
    text_date=findViewById(R.id.text_noticeDate);
    img_notice.setEnabled(false);

    Bundle bundle=getIntent().getExtras();
    text_notice.setText(bundle.getString("text"));
    text_date.setText(bundle.getString("date"));
    final String image=bundle.getString("image");
    if(!image.equals("no"))
    {Picasso.get().load(Uri.parse(image)).into(img_notice);
    img_notice.setEnabled(true );}

        img_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new
                        AlertDialog.Builder(NoticeDetailActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(Uri.parse(image)).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });




    }
}