package com.eazot.e_note.ui;

import android.os.Bundle;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.eazot.e_note.R;
import com.eazot.e_note.RouterHolder;

public class MainActivity extends AppCompatActivity implements RouterHolder {

    private MainRouter router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        router = new MainRouter(getSupportFragmentManager());

        if (savedInstanceState == null) {
            router.showNotes();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_notes) {
                    router.showNotes();
                }

                if (item.getItemId() == R.id.action_info) {
                    router.showInfo();
                }
                return true;
            }
        });


//
//        Context applicationContext = getApplicationContext();
//
//        Context application = getApplication();
//
//        Context thisContext = this;
//
//        new SomeStrangeClass(applicationContext);
//
//        LayoutInflater.from(thisContext);
    }

    @Override
    public MainRouter getMainRouter() {
        return router;
    }
}