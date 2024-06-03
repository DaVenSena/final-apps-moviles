package com.mobile.apps.proyectofinalappsmoviles;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.ListFragment;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.NotesFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frmHome;
    BottomNavigationView btmNav;
    NotesFragment notesFragment;
    ListFragment listFragment;
    Button btnAddList, btnAddNote;
    ArrayList<ListObject> list;
    ArrayList<Note> notes;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        frmHome = findViewById(R.id.frmHome);
        btmNav = findViewById(R.id.btmNav);

        try {
            uid = getIntent().getStringExtra("uid");
            Log.d("UID", "Uid Home: " + uid);
            notes = getIntent().getParcelableArrayListExtra("notes");
            Log.d("NOTES", "Notes Home: " + notes);
            list = getIntent().getParcelableArrayListExtra("list");
            Log.d("LIST", "List Home: " + list);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final @Nullable ListObject lo = getIntent().getParcelableExtra("listObject");
        if (lo != null) list.add(lo);

        listFragment = ListFragment.newInstance(list, uid);
        getSupportFragmentManager().beginTransaction().replace(R.id.frmHome, listFragment).commit();

        notesFragment = NotesFragment.newInstance(notes);
        getSupportFragmentManager().beginTransaction().replace(R.id.frmHome, notesFragment).commit();

        loadFragment(notesFragment);

        btmNav.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.itm_notes) {
                loadFragment(notesFragment);
                return true;
            }
            if (menuItem.getItemId() == R.id.itm_list) {
                loadFragment(listFragment);
                return true;
            }
            return false;
        });


    }

    public void loadFragment(Fragment fr) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmHome, fr);
        transaction.commit();
    }
}
