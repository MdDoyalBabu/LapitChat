package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText displaynameEdittext,emailEdittext,passwordeditText;

    private Button createAccountButon;
    private ProgressDialog registesrprogressDialog;

    private Toolbar registertoolbar;
//firebase auth

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        registesrprogressDialog=new ProgressDialog(this);
        displaynameEdittext=findViewById(R.id.display_name_start_id);
        emailEdittext=findViewById(R.id.emial_address_start_id);
        passwordeditText=findViewById(R.id.password_start_id);

        registertoolbar=findViewById(R.id.register_page_toolBar);
        setSupportActionBar(registertoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createAccountButon=findViewById(R.id.create_account_start_Id);


        createAccountButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name=displaynameEdittext.getText().toString();
                String email=emailEdittext.getText().toString();
                String password=passwordeditText.getText().toString();

                if (!TextUtils.isEmpty(name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){

                    registesrprogressDialog.setTitle("Logging In");
                    registesrprogressDialog.setMessage("Please wait for few time ");
                    registesrprogressDialog.setCanceledOnTouchOutside(false);
                    registesrprogressDialog.show();
                    register_user(name,email,password);
                }

            }
        });

    }

    public  void  register_user(final String name, final String email, final String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()){
                 FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                String uid=current_user.getUid();

                databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("status","Developer Ferdous");
                    hashMap.put("image","default");
                    hashMap.put("thum_image","default");


                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            registesrprogressDialog.dismiss();
                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }
                });

                }
                else {

                    registesrprogressDialog.hide();
                    Toast.makeText(RegisterActivity.this, "You got some error", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
