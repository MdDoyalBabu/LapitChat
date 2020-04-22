package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private ImageView mImageView;
    private TextView currentUser,totalFriend,dislpalyName;
    private Button sendFriendRequest;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String user_id=getIntent().getStringExtra("user_id");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mImageView=findViewById(R.id.image_view_Id);
        currentUser=findViewById(R.id.current_UserStatus_id);
        dislpalyName=findViewById(R.id.profile_display_Id);
        totalFriend=findViewById(R.id.total_friend_Id);
        sendFriendRequest=findViewById(R.id.sendFriendRequest_Id);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();

                dislpalyName.setText(name);
                currentUser.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
