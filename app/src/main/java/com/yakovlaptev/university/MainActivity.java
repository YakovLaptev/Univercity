package com.yakovlaptev.university;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yakovlaptev.university.entity.Survey;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        button1 = (Button)findViewById(R.id.button2);
        button2 = (Button)findViewById(R.id.button3);
        button3 = (Button)findViewById(R.id.button10);
        button4 = (Button)findViewById(R.id.button11);
        button5 = (Button)findViewById(R.id.button12);
        button6 = (Button)findViewById(R.id.button13);
        button7 = (Button)findViewById(R.id.button14);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        if(v.getId() == R.id.button14) {
            intent = new Intent(MainActivity.this, RequestActivity.class);
        } else {
            intent = new Intent(MainActivity.this, RequestActivity.class);
        }
        switch (v.getId()) {
            case R.id.button2: intent.putExtra("request", "specialtyCode"); break;
            case R.id.button3: intent.putExtra("request", "curse"); break;
            case R.id.button10: intent.putExtra("request", "department"); break;
            case R.id.button11: intent.putExtra("request", "institute"); break;
            case R.id.button12: intent.putExtra("request", "specialty"); break;
            case R.id.button13: intent.putExtra("request", "studyForm"); break;
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_export || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, RequestActivity.class);
        if (id == R.id.request1) {
            intent.putExtra("request", "specialtyCode");
            startActivity(intent);
        } else if (id == R.id.request2) {
            intent.putExtra("request", "curse");
            startActivity(intent);
        } else if (id == R.id.request3) {
            intent.putExtra("request", "department");
            startActivity(intent);
        } else if (id == R.id.request4) {
            intent.putExtra("request", "institute");
            startActivity(intent);
        } else if (id == R.id.request5) {
            intent.putExtra("request", "specialty");
            startActivity(intent);
        } else if (id == R.id.request6) {
            intent.putExtra("request", "studyForm");
            startActivity(intent);
        } else if (id == R.id.settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
