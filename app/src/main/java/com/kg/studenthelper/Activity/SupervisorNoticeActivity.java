package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kg.studenthelper.NewComplain;
import com.kg.studenthelper.Notice;
import com.kg.studenthelper.NoticeAdapter;
import com.kg.studenthelper.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupervisorNoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    NoticeAdapter adapter;
    List<Notice> noticeList=new ArrayList<>();
    String building;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_notice);



        recyclerView=findViewById(R.id.recyclerView_notice);
        add_button=findViewById(R.id.floating_addButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new NoticeAdapter(SupervisorNoticeActivity.this);
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        building = bundle.getString("building");

        getAll();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupervisorNoticeActivity.this,NewNoticeActivity.class);
                intent.putExtra("building",building);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final Notice notice = adapter.getNotice(viewHolder.getAdapterPosition());
                final DatabaseReference reference = database.getReference().child(building).child(notice.getKey());
                reference.removeValue();


                Snackbar.make(recyclerView,null,Snackbar.LENGTH_LONG).
                        setAction("undo delete", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                reference.setValue(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SupervisorNoticeActivity.this,"successfully readded",Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();

                                    }
                                });

                            }
                        }).show();


            }

        }).attachToRecyclerView(recyclerView);

    }

    private void getAll() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(building);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();

                if(snapshot.exists())
                {



                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        Notice notice=dataSnapshot.getValue(Notice.class);
                        noticeList.add(notice);
                    }

                }


                adapter.setNoticeList(noticeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


}