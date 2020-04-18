package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView displayName,description;
    private Button changeNamebutton,changeStatusButton;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        String currert_user=mCurrentuser.getUid();


        circleImageView=findViewById(R.id.circleIamge_status_Id);
        displayName=findViewById(R.id.display_name_Id);
        description=findViewById(R.id.discription_id);

        changeNamebutton=findViewById(R.id.changeName_Id);
        changeStatusButton=findViewById(R.id.changeStatus_Id);



        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(currert_user);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String thum_image=dataSnapshot.child("thum_image").getValue().toString();

                displayName.setText(name);
                description.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

changeStatusButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String status_value=description.getText().toString();

        Intent intent=new Intent(SettingActivity.this,StatusActivity.class);
        intent.putExtra("status_value",status_value);
        startActivity(intent);
    }
});
    }
}
