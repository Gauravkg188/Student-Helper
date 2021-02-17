package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kg.studenthelper.Identity;
import com.kg.studenthelper.NewComplain;
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

//Student New Complain Registration Activity
public class NewComplainActivity extends AppCompatActivity {

    EditText text_room,text_problem;

    Uri imageUri;
    ImageView temp_image;
    AlertDialog.Builder builder;
    String userId;
    ImageView problemImage;
    private static int Code = 1 ;
    private static int REQUESTCODE = 1 ;
    Button button_submit;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    String building,category;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complain);


        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        text_room=findViewById(R.id.text_room);
        text_problem=findViewById(R.id.text_problem);
        problemImage=findViewById(R.id.img_problem);
        button_submit=findViewById(R.id.button_submit);
        spinner=findViewById(R.id.spinner);
        temp_image=findViewById(R.id.temp_image);
        problemImage.setEnabled(false);
      //  Intent intent=getIntent();
       // building=intent.getStringExtra("building");

        final ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        Bundle bundle = getIntent().getExtras();
        building = bundle.getString("building");

         builder = new AlertDialog.Builder(NewComplainActivity.this);
        builder.setView(R.layout.progress);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        temp_image.setOnClickListener(new View.OnClickListener() {
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

        problemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new
                        AlertDialog.Builder(NewComplainActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(imageUri).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(text_room.getText().toString().isEmpty() || text_problem.getText().toString().isEmpty()
                           || problemImage.getDrawable()==null || category.isEmpty())
                {
                    Toast.makeText(NewComplainActivity.this, "Please Fill all information", Toast.LENGTH_SHORT).show();
                }
                else
                {





                    AddOnFirebase(text_room.getText().toString(),text_problem.getText().toString(),imageUri,building,category);


                }



            }
        });





    }




    private void checkPermission() {


        if(ContextCompat.checkSelfPermission(NewComplainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(NewComplainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(NewComplainActivity.this, "Please Accept the required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(NewComplainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Code);
            }

            if(ContextCompat.checkSelfPermission(NewComplainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
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
           // problemImage.setImage(ImageSource.uri(imageUri));

            Picasso.get().load(imageUri).into(problemImage);
            problemImage.setEnabled(true);
        }

    }


    private void AddOnFirebase(final String text_room, final String text_problem, final Uri imageUri,final String build,final String category) {

        final Dialog dialog = builder.create();

        dialog.show();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String date = df.format(c);

        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("problem-image");
        final StorageReference mStorage=storageReference.child(imageUri.getLastPathSegment());

        mStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                        final String user_id=current_user.getUid();




                        String user_name=current_user.getDisplayName();
                        String status="pending";
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("complain").push();
                        String key=databaseReference.getKey();


                        NewComplain complain=new NewComplain(build,text_problem,text_room,uri.toString(),key,date,user_id,status,user_name,category);


                        databaseReference.setValue(complain).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {



                               getToken();
                                //sendNotifications("ffQqMAz0Q-GyeA420denB1:APA91bGrxC5W9SgoT8IAUmdXx8krVtIb2o5P9f3CqCTxBsEdSBxPPiKv4I-ygp8J9W5CSjY9nP108GPLeh1DUzfSq7_gUKR1sj_6pyvPNzfIMji2fFFwsOEywYeECO5k2iiZdlav5irO"
                                //,"update","New complain");
                                Toast.makeText(NewComplainActivity.this, "successfully registered your complain", Toast.LENGTH_SHORT).show();


                                dialog.dismiss();

                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewComplainActivity.this,"Failed to register,Try again",Toast.LENGTH_LONG).show();
                            }
                        });








                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewComplainActivity.this, "Failed to upload in storage", Toast.LENGTH_SHORT).show();
            }
        });








    }


    private void getToken()
    {


        Query query=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("type").equalTo("supervisor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Identity identity = dataSnapshot.getValue(Identity.class);

                        if (identity.getBuilding().equals(building)) {

                            userId = identity.getUserId();
                        }
                    }
                }


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens").child(userId);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Token token = dataSnapshot.getValue(Token.class);

                        String userToken = token.getToken();
                        sendNotifications(userToken, "Update", "New Complain From Room."+text_room.getText().toString());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error){


    }
        });


    }







    public void sendNotifications(String userToken, String title, String message) {

        String type="complain";
        APIServices apiService = Client.getClient().create(APIServices.class);
        Data data = new Data(title, message,type,building);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success == 1) {
                        Toast.makeText(NewComplainActivity.this, "success ", Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Toast.makeText(NewComplainActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}