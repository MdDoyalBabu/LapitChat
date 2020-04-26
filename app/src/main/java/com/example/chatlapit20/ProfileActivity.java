package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private ImageView mImageView;
    private TextView currentUser,totalFriend,dislpalyName;
    private Button sendFriendRequest,declineFriendRequest;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendRequestDatabase;
    private FirebaseUser mCurrent_user;
    private DatabaseReference mFriendDatabase;

    private  String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        final String user_id=getIntent().getStringExtra("user_id");

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase=FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase=FirebaseDatabase.getInstance().getReference().child("Friends");

        mCurrent_user= FirebaseAuth.getInstance().getCurrentUser();


        mImageView=findViewById(R.id.image_view_Id);
        currentUser=findViewById(R.id.current_UserStatus_id);
        dislpalyName=findViewById(R.id.profile_display_Id);
        totalFriend=findViewById(R.id.total_friend_Id);
        sendFriendRequest=findViewById(R.id.sendFriendRequest_Id);
        declineFriendRequest=findViewById(R.id.declineFriendRequest_Id);

        mCurrent_state="not_friend";


        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();

                dislpalyName.setText(name);
                currentUser.setText(status);

                //----------Friend List/Request Feature------------//
                mFriendRequestDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)){

                            String req_type=dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("recived")){
                                mCurrent_state="req_recived";
                                sendFriendRequest.setText("Accept Friend Request");

                            }else if (req_type.equals("sent")) {
                                mCurrent_state="req_sent";
                                sendFriendRequest.setText("Cancel Friend Request");
                            }


                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //......-----Not Friend Request--------
                sendFriendRequest.setEnabled(false);

                if (mCurrent_state.equals("not_friend")){

                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").
                            setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").
                                        setValue("recived").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        mCurrent_state="req_sent";
                                        sendFriendRequest.setText("Cancel Friend Request ");

                                        Toast.makeText(ProfileActivity.this, "Request sent successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {

                                Toast.makeText(ProfileActivity.this, "Failed sending Request", Toast.LENGTH_SHORT).show();

                            }
                            sendFriendRequest.setEnabled(true);
                        }
                    });

                }
                //......-----Cancel Friend Request--------//

                if (mCurrent_state.equals("req_sent")){

                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    sendFriendRequest.setEnabled(true);
                                    mCurrent_state="not_friend";
                                    sendFriendRequest.setText(" sent Friend Request ");

                                }
                            });
                        }

                    });
                }

                ///////////-------------------////////////
                if (mCurrent_state.equals("req_recived")){

                    final String currentDate= DateFormat.getDateTimeInstance().format(new Date());

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).setValue(currentDate)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mFriendRequestDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            mFriendRequestDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    sendFriendRequest.setEnabled(true);
                                                                    mCurrent_state="friends";
                                                                    sendFriendRequest.setText(" UnFriend this person ");

                                                                }
                                                            });
                                                        }

                                                    });
                                                }
                                            });


                                }
                            });

                }


            }
        });


    }
}
