package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kg.studenthelper.Identity;
import com.kg.studenthelper.MainActivity;
import com.kg.studenthelper.R;

import java.util.Objects;

//Login Activity
public class LoginActivity extends AppCompatActivity {


    private EditText input_email,input_password;
    private Button login_button;

    private FirebaseAuth mAuth;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        login_button = findViewById(R.id.button_login);

        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = input_email.getText().toString().trim();
                password = input_password.getText().toString().trim();


                if(!email.isEmpty() && !password.isEmpty())
                {
                   mAuth.signInWithEmailAndPassword(email,password)
                           .addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {

                                   if (task.isSuccessful()) {

                                       Toast.makeText(LoginActivity.this, "Authentication successful.",
                                               Toast.LENGTH_SHORT).show();

                                       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


                                       FirebaseDatabase user_database=FirebaseDatabase.getInstance();
                                       DatabaseReference ref=user_database.getReference().child("users").child(user.getUid());

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
                                                   Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                                   startActivity(intent);
                                               }
                                               else if(type.equals(student))
                                               {
                                                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                   intent.putExtra("building",building);
                                                   startActivity(intent);

                                               }
                                               else if(type.equals(supervisor))
                                               {
                                                   Intent intent=new Intent(LoginActivity.this, supervisorActivity.class);
                                                   intent.putExtra("building",building);
                                                   startActivity(intent);
                                               }

                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {
                                               Toast.makeText(LoginActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

                                           }
                                       });



                                   } else {
                                       // If sign in fails, display a message to the user.

                                       Toast.makeText(LoginActivity.this, "Authentication failed.",
                                               Toast.LENGTH_SHORT).show();
                                       input_email.setText(" ");
                                       input_password.setText(" ");

                                   }
                               }
                           });

                }

            }


        });





    }



    }