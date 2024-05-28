package com.mobile.apps.proyectofinalappsmoviles;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.ListFragment;
import com.mobile.apps.proyectofinalappsmoviles.Fragments.NotesFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FrameLayout frmHome;
    BottomNavigationView btmNav;
    NotesFragment notesFragment = new NotesFragment();
    ListFragment listFragment = new ListFragment();
    ArrayList<ListObject> list;
    ArrayList<Note> notes;
    RecyclerView rcv_list;
    RecyclerView rcv_notes;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        uid = getIntent().getStringExtra("uid");
        Log.d("UID", "Uid Home: " + uid);
        list = getIntent().getParcelableArrayListExtra("list");
        Log.d("LIST", "List Home: " + list);
        loadFragment(notesFragment);

        notes = getIntent().getParcelableArrayListExtra("notes");
        Log.d("NOTES", "Notes Home: " + notes);

        frmHome = findViewById(R.id.frmHome);
        btmNav = findViewById(R.id.btmNav);
        rcv_list = findViewById(R.id.rcv_list);
        rcv_notes = findViewById(R.id.rcv_notes);

        btmNav.setOnItemSelectedListener(menuItem -> {
           if (menuItem.getItemId() == R.id.itm_notes){
                loadFragment(notesFragment);
                return true;
            }
            if (menuItem.getItemId() == R.id.itm_list){
                loadFragment(listFragment);
                return true;
            }
            return false;
        });


    }
    public void loadFragment(Fragment fr){
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmHome, fr);
        transaction.commit();
    }
}
