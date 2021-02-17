package com.kg.studenthelper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kg.studenthelper.Identity;
import com.kg.studenthelper.Notification.Token;
import com.kg.studenthelper.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase user_Database;
    TextView text;
    String building;
    Button signOut_button,newCase_button,pending_button,button_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        signOut_button=findViewById(R.id.button_signOut);
        newCase_button=findViewById(R.id.button_newCase);
        pending_button=findViewById(R.id.button_pendingCase);
        button_notice=findViewById(R.id.button_notice);
        Intent main_intent=getIntent();
        building=main_intent.getStringExtra("building");
        //text=findViewById(R.id.edit);

         UpdateToken();

       /* FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        FirebaseDatabase user_database=FirebaseDatabase.getInstance();
        DatabaseReference ref=user_database.getReference().child("users").child(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Identity po=dataSnapshot.getValue(Identity.class);
               // text.setText(po.getType());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });*/

       newCase_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(HomeActivity.this,NewComplainActivity.class);
               intent.putExtra("building",building);
               startActivity(intent);
           }
       });

       pending_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(HomeActivity.this,PendingActivity.class);
               startActivity(intent);
           }
       });

        signOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent =new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        button_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,StudentNoticeActivity.class);
                intent.putExtra("building",building);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
}