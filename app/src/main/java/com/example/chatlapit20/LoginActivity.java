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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Toolbar loginToolbar;
    private EditText nameEdittext,emailEdittext;
    private Button loginInButtond;
    private FirebaseAuth mAuth;
    private ProgressDialog loginprogressDialog;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginToolbar=findViewById(R.id.login_page_toolBar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        emailEdittext=findViewById(R.id.login_passwordl_Edittext_Id);
        nameEdittext=findViewById(R.id.login_email_Id);

        loginprogressDialog=new ProgressDialog(this);

        loginInButtond=findViewById(R.id.login_button_Id);
        loginInButtond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=nameEdittext.getText().toString();
                String email=emailEdittext.getText().toString();

                if (!TextUtils.isEmpty(name)||!TextUtils.isEmpty(email)){

                    loginprogressDialog.setTitle("Logging In");
                    loginprogressDialog.setMessage("Please wait for few time ");
                    loginprogressDialog.setCanceledOnTouchOutside(false);
                    loginprogressDialog.show();

                    loginUser(name,email);

                }

            }
        });


    }

    private void loginUser(String name, String email) {

        mAuth.signInWithEmailAndPassword(name,email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    loginprogressDialog.dismiss();

                    String current_Id=mAuth.getCurrentUser().getUid();
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_Id).child("deviceToken").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });


                }
                else {
                    loginprogressDialog.hide();
                    Toast.makeText(LoginActivity.this, "some error", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
