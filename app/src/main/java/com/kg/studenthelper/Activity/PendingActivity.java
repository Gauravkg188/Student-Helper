package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.View;
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
import com.kg.studenthelper.MainActivity;
import com.kg.studenthelper.NewComplain;
import com.kg.studenthelper.R;

import java.util.ArrayList;
import java.util.List;


//Student Pending Complain Activity
public class PendingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    List<NewComplain> complains=new ArrayList<>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_pending);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAuth=FirebaseAuth.getInstance();
        String userID=mAuth.getUid();
        database=FirebaseDatabase.getInstance();


       //
        Query query=FirebaseDatabase.getInstance().getReference().child("complain").orderByChild("userId")
              .equalTo(mAuth.getCurrentUser().getUid());
        adapter=new Adapter(PendingActivity.this);
        recyclerView.setAdapter(adapter);


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
                                      Toast.makeText(PendingActivity.this, "Successfully reinserted", Toast.LENGTH_SHORT).show();

                                  }
                              });
                          }
                      }).show();

          }
      }).attachToRecyclerView(recyclerView);




    }
}