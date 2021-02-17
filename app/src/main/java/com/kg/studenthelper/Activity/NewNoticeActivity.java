package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kg.studenthelper.Identity;
import com.kg.studenthelper.Notice;
import com.kg.studenthelper.Notification.APIServices;
import com.kg.studenthelper.Notification.Client;
import com.kg.studenthelper.Notification.Data;
import com.kg.studenthelper.Notification.MyResponse;
import com.kg.studenthelper.Notification.NotificationSender;
import com.kg.studenthelper.Notification.Token;
import com.kg.studenthelper.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Supervisor New Notice addition Activity
public class NewNoticeActivity extends AppCompatActivity {
    EditText text_notice;
    ImageView img_notice;
    Button button_noticeUpload;
    Button imgClose;
    Uri imageUri;
    String type="student";
    String building;
    private static int Code = 1 ;
    private static int REQUESTCODE = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notice);

        Bundle bundle=getIntent().getExtras();
        building=bundle.getString("building");


        text_notice=findViewById(R.id.text_notice);
        img_notice=findViewById(R.id.img_notice);
        button_noticeUpload=findViewById(R.id.button_noticeUpload);
        imgClose=findViewById(R.id.imgClose);

        imageUri=null;

        img_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Build.VERSION.SDK_INT>=23)
                {
                    checkPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri=null;
                Picasso.get().load(imageUri).into(img_notice);
            }
        });

        button_noticeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(text_notice.getText().toString().trim().isEmpty() && imageUri==null)
                {
                    Toast.makeText(NewNoticeActivity.this,"Both can't be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    AddonFirebase(text_notice.getText().toString().trim(),imageUri);



                }



            }
        });




    }

    private void AddonFirebase(final String noticeText, Uri imageUri) {

        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(building).push();
        final String key=reference.getKey();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String date = df.format(c);

        if(imageUri!=null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("notice-image");

            final StorageReference mStorage = storageReference.child(imageUri.getLastPathSegment());

            mStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Notice notice=new Notice(noticeText,uri.toString(),date,key);




                            reference.setValue(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    notificationPreparation();
                                    Toast.makeText(NewNoticeActivity.this, "successfully uploaded notice", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }
                    });
                }
            });



        }
        else
        {
            Notice notice=new Notice(noticeText,"no",date,key);


             notificationPreparation();
            reference.setValue(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    Toast.makeText(NewNoticeActivity.this, "successfully uploaded notice", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });
        }



    }


    private void checkPermission() {


        if(ContextCompat.checkSelfPermission(NewNoticeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(NewNoticeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(NewNoticeActivity.this, "Please Accept the required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(NewNoticeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Code);
            }



        }
        else
        {
            openGallery();
        }
    }


    private void openGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQUESTCODE && data!=null)
        {
            imageUri=data.getData();

            img_notice.setImageURI(null);
            Picasso.get().load(imageUri).into(img_notice);

        }

    }

    public void notificationPreparation()
    {


        Query query=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("building").equalTo(building);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Identity identity = dataSnapshot.getValue(Identity.class);

                        if(identity.getType().equals(type)) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens").child(identity.getUserId());

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Token token = dataSnapshot.getValue(Token.class);

                                    String userToken = token.getToken();

                                    sendNotifications(userToken, "Update", "New Notice");


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void sendNotifications(String userToken, String title, String message) {

        String type="notice";
        APIServices apiService = Client.getClient().create(APIServices.class);
        Data data = new Data(title, message,type,building);
        NotificationSender sender = new NotificationSender(data, userToken);
        Log.d("from","sender");
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success == 1) {
                        Toast.makeText(NewNoticeActivity.this, "success ", Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Toast.makeText(NewNoticeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}