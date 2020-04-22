package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsers extends AppCompatActivity {


    private Toolbar mtoolbar;
    private RecyclerView mUserList;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mtoolbar=findViewById(R.id.users_page_toolBar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mUserList=findViewById(R.id.users_recyclerView_Id);

        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerAdapter<Users,UserViewHoder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UserViewHoder>(


                Users.class,
                R.layout.users_single_layout,
                UserViewHoder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(UserViewHoder userViewHoder, Users users, int position) {


                userViewHoder.setName(users.getName());
                userViewHoder.setStatus(users.getStatus());

                final String user_id=getRef(position).getKey();

                userViewHoder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(AllUsers.this,ProfileActivity.class);
                       intent.putExtra("user_id",user_id);
                        startActivity(intent);

                    }
                });


            }
        };

        mUserList.setAdapter(firebaseRecyclerAdapter);

    }


     public  static class  UserViewHoder extends RecyclerView.ViewHolder{


         View mView;
         public UserViewHoder(@NonNull View itemView) {
             super(itemView);

             mView=itemView;
         }
         public void setName(String name){
             TextView userTextName=mView.findViewById(R.id.users_display_name_Id);
             userTextName.setText(name);
         }
         public  void setStatus(String status){
             TextView userTextName=mView.findViewById(R.id.default_users_status_Id);
             userTextName.setText(status);
         }
     }
}
