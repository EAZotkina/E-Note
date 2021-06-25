package com.eazot.e_note.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.eazot.e_note.R;
import com.eazot.e_note.RouterHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_notes) {
                router.showNotes();
            }

            if (item.getItemId() == R.id.action_info) {
                router.showInfo();
            }
            return true;
        });
    }

    @Override
    public MainRouter getMainRouter() {
        return router;
    }
}