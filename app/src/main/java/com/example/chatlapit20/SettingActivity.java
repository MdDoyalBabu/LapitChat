package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView displayName,description;
    private Button changeNamebutton,changeStatusButton;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentuser;

    private StorageReference imageStorage;

    private static  final int GALLERY_PICK=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        imageStorage= FirebaseStorage.getInstance().getReference();
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

changeNamebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        /*
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(SettingActivity.this);

         */


        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALLERY_PICK);

    }
});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK && resultCode==RESULT_OK){

            Uri imageUri=data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                StorageReference filepath=imageStorage.child("profile_images").child("profile_image.jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SettingActivity.this, "Working", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SettingActivity.this, "Some Error Uploading file", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }
}
