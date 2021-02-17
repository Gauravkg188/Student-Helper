package com.kg.studenthelper.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kg.studenthelper.MainActivity;
import com.kg.studenthelper.Notification.Token;
import com.kg.studenthelper.R;

public class supervisorActivity extends AppCompatActivity {

    String building;
    Button button_pendingCase,button_signOut,button_notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);

        button_pendingCase=findViewById(R.id.button_pendingCase);
        button_signOut=findViewById(R.id.button_signOut);
        button_notice=findViewById(R.id.button_notice);
        Bundle bundle = getIntent().getExtras();
        building = bundle.getString("building");

        UpdateToken();
        button_pendingCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(supervisorActivity.this, SupervisorPendingActivity.class);
                intent.putExtra("building",building);
                startActivity(intent);
            }
        });


        button_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(supervisorActivity.this, SupervisorNoticeActivity.class);
                intent.putExtra("building",building);
                startActivity(intent);
            }
        });






        button_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(supervisorActivity.this,LoginActivity.class);
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