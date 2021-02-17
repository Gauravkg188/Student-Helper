package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kg.studenthelper.Identity;
import com.kg.studenthelper.R;

public class SignupActivity extends AppCompatActivity {
    EditText input_email, input_name, input_password;

    Button button;
    String email, name, password, type, building;
    FirebaseAuth mAuth;
    RadioGroup radioGroup_role, radioGroup_building;
    RadioButton radioButton_role,radioButton_building;

    FirebaseDatabase user_Database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_name = findViewById(R.id.input_name);

        radioGroup_role=findViewById(R.id.radio_role);
        radioGroup_building=findViewById(R.id.radio_building);
        button = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Admin");



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = input_email.getText().toString().trim();
                password = input_password.getText().toString().trim();
                name = input_name.getText().toString().trim();
                int roleId=radioGroup_role.getCheckedRadioButtonId();
                int buildingId=radioGroup_building.getCheckedRadioButtonId();

                if(buildingId==-1 || roleId==-1)
                {
                    Toast.makeText(SignupActivity.this,"select options",Toast.LENGTH_LONG).show();
                }
                else
                    {


                radioButton_role=findViewById(roleId);
                radioButton_building=findViewById(buildingId);
                type=radioButton_role.getText().toString();
                building=radioButton_building.getText().toString();


                if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty()) {



                 mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        updateUserInfo(name,type,building, mAuth.getCurrentUser());


                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();

                                    }

                                }

                            })
                            .addOnFailureListener(SignupActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {

                    Toast.makeText(SignupActivity.this, "Enter data properly", Toast.LENGTH_SHORT).show();

                }

            }

        }

    });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logOut:
                mAuth.signOut();
                Toast.makeText(this, "Log out successfully", Toast.LENGTH_SHORT).show();
                finish();

            default:return super.onOptionsItemSelected(item);
        }
    }

    private void updateUserInfo(final String name, final String type, final String building, final FirebaseUser currentUser) {


        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();


        currentUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // user info updated successfully
                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                           String user_id = current_user.getUid();

                           String typo=type+building;
                            user_Database = FirebaseDatabase.getInstance();
                            DatabaseReference user_Reference = user_Database.getReference().child("users").child(user_id);
                            Identity post = new Identity(name,type,building,user_id,typo);


                            user_Reference.setValue(post).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Sign up successfully", Toast.LENGTH_LONG).show();
                                        input_email.setText(" ");
                                        input_name.setText(" ");
                                        input_password.setText(" ");
                                        radioGroup_building.clearCheck();
                                        radioGroup_role.clearCheck();

                                    }
                                }
                            })
                                    .addOnFailureListener(SignupActivity.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });


                        }


                    }


                });


    }

}