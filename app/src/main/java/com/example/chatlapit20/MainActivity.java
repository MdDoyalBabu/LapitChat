package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private FirebaseAuth mAuth;
    private Toolbar mainToolbar;
    private ViewPager mainviewPager;
    private TabLayout mainTabLayout;

    private SectionViewPagerAdapter sectionViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar=findViewById(R.id.main_page_toolBar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("LapitChat");

        mAuth = FirebaseAuth.getInstance();


        //finding
        mainviewPager=findViewById(R.id.mainViewPager_Id);
        sectionViewPagerAdapter=new SectionViewPagerAdapter(getSupportFragmentManager());
        mainviewPager.setAdapter(sectionViewPagerAdapter);

        mainTabLayout=findViewById(R.id.mainTabLayout_Id);
        mainTabLayout.setupWithViewPager(mainviewPager);



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
            sendToStart();

        }





    }

    private void sendToStart() {

            Intent intent=new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
            finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.logout_menu_id){

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (item.getItemId()==R.id.setting_menu_id){
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);

        }
        if (item.getItemId()==R.id.users_menu_id){
            Intent intent=new Intent(MainActivity.this,AllUsers.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);



    }
}
