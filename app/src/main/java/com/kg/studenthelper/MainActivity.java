/**
 * By Gaurav Kumar
 * Application provide online complaint system for student with functionality to uplaod complain with text and image and live tracking of status of complaints
 * aid hostel incharge to look into complaints and update the status.
 * also provide online notice board  to hostel incharge to upload notices and to student to keep updated with notices
 * provide notification functionality using firebase and retrofit
 *
 */


package com.kg.studenthelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kg.studenthelper.Activity.HomeActivity;
import com.kg.studenthelper.Activity.LoginActivity;
import com.kg.studenthelper.Activity.SignupActivity;
import com.kg.studenthelper.Activity.supervisorActivity;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {


        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress);
        Dialog dialog=builder.create();
        dialog.show();
        if (currentUser != null) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            FirebaseDatabase user_database = FirebaseDatabase.getInstance();
            DatabaseReference ref = user_database.getReference().child("users").child(user.getUid());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Identity po = dataSnapshot.getValue(Identity.class);
                    String type = po.getType().toString().trim();
                    String student="student";
                    String admin="Admin";
                    String supervisor="supervisor";
                    String building=po.getBuilding();

                    if (type.equals(admin)){
                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);

                        startActivity(intent);
                    }
                    else if(type.equals(student))
                    {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("building",building);
                        startActivity(intent);

                    }
                    else if(type.equals(supervisor))
                    {
                        Intent intent=new Intent(MainActivity.this, supervisorActivity.class);
                        intent.putExtra("building",building);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

                }
            });


        }
        else
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        dialog.dismiss();
    }


}