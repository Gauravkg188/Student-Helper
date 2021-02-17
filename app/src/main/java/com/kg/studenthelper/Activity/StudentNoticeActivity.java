package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kg.studenthelper.Notice;
import com.kg.studenthelper.NoticeAdapter;
import com.kg.studenthelper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentNoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NoticeAdapter adapter;
    FirebaseDatabase database;
    String building;
    List<Notice> noticeList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notice);


        recyclerView=findViewById(R.id.recyclerView_notice);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new NoticeAdapter(StudentNoticeActivity.this);
        recyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        building = bundle.getString("building");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice Board");

        getAll();




    }

    private void getAll() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(building);

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


                Collections.reverse(noticeList);
                adapter.setNoticeList(noticeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                                        Toast.makeText(StudentNoticeActivity.this,"successfully readded",Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();

                                    }
                                });

                            }
                        }).show();


            }

        }).attachToRecyclerView(recyclerView);



    }
}