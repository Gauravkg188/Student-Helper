package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kg.studenthelper.Adapter;
import com.kg.studenthelper.NewComplain;
import com.kg.studenthelper.R;
import com.kg.studenthelper.SupervisorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SupervisorPendingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    Button button_all, button_elect, button_plumber, button_carpenter, button_other;
    FirebaseDatabase database;
    List<NewComplain> complains = new ArrayList<>();
    SupervisorAdapter adapter;
    String building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_pending);

        recyclerView=findViewById(R.id.recyclerView_pending);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
      adapter=new SupervisorAdapter(this);
       recyclerView.setAdapter(adapter);
        mAuth=FirebaseAuth.getInstance();
        button_all=findViewById(R.id.button_all);
        button_elect=findViewById(R.id.button_electrician);
        button_carpenter=findViewById(R.id.button_carpenter);
        button_plumber=findViewById(R.id.button_plumber);
        button_other=findViewById(R.id.button_other);
        database=FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        building = bundle.getString("building");

       getAll();


       button_all.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getAll();
           }
       });

       button_elect.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getCategoryWise("Electrician");
           }
       });

       button_carpenter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getCategoryWise("Carpenter");
           }
       });

       button_plumber.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getCategoryWise("Plumber");
           }
       });

       button_other.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getCategoryWise("others");
           }
       });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

               final NewComplain complain=adapter.getComplains(viewHolder.getAdapterPosition());
                final DatabaseReference reference=database.getReference().child("complain").child(complain.getKey());
                reference.removeValue();

                Snackbar.make(recyclerView,null,Snackbar.LENGTH_LONG)
                        .setAction("undo delete", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reference.setValue(complain).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SupervisorPendingActivity.this, "Successfully reinserted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).show();

            }
        }).attachToRecyclerView(recyclerView);

    }


    private void getAll()
    {
        Query query=database.getReference().child("complain").orderByChild("building").equalTo(building);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complains.clear();

                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        NewComplain complain=dataSnapshot.getValue(NewComplain.class);
                        complains.add(complain);
                    }
                    adapter.setComplains(complains);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getCategoryWise(String category)
    {
        Query query=database.getReference().child("complain").orderByChild("category").equalTo(category);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complains.clear();

                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        NewComplain complain=dataSnapshot.getValue(NewComplain.class);
                        complains.add(complain);
                    }

                }
                adapter.setComplains(complains);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}