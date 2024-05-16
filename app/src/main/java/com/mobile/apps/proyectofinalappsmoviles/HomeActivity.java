package com.mobile.apps.proyectofinalappsmoviles;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.ListFragment;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.NotesFragment;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frmHome;
    BottomNavigationView btmNav;
    NotesFragment notesFragment = new NotesFragment();
    ListFragment listFragment = new ListFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        loadFragment(notesFragment);

        frmHome = findViewById(R.id.frmHome);
        btmNav = findViewById(R.id.btmNav);

        btmNav.setOnItemSelectedListener(menuItem -> {
           if (menuItem.getItemId() == R.id.itm_notes){
                loadFragment(notesFragment);
                return true;
            }
            if (menuItem.getItemId() == R.id.itm_list){
                loadFragment(listFragment);
                return true;
            }
            /*final int noteId = R.id.itm_notes;
            switch (menuItem.getItemId()){
                case noteId:

            }*/
            return false;
        });


    }
    public void loadFragment(Fragment fr){
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmHome, fr);
        transaction.commit();
    }
}
