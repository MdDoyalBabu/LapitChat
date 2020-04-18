package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {


    private Toolbar statusToolbar;
    private EditText statuseditText;
    private Button saveStatusButton;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrent_User;
    private ProgressDialog status_progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        statusToolbar=findViewById(R.id.status_page_toolBar);

        setSupportActionBar(statusToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value=getIntent().getStringExtra("status_value");

        statuseditText=findViewById(R.id.status_Edittext_Id);
        saveStatusButton=findViewById(R.id.save_status_Id);



        mCurrent_User= FirebaseAuth.getInstance().getCurrentUser();
        String status_Id=mCurrent_User.getUid();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(status_Id);

        statuseditText.setText(status_value);

        saveStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status_progressDialog=new ProgressDialog(StatusActivity.this);
                status_progressDialog.setTitle("Saving Changes");
                status_progressDialog.setMessage("Please wait while we are a saving change");
                status_progressDialog.show();
                
                String status=statuseditText.getText().toString();




                databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            status_progressDialog.dismiss();
                            finish();
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "There are some error saving changes", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }
}
