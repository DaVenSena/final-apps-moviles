package com.mobile.apps.proyectofinalappsmoviles.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.apps.proyectofinalappsmoviles.Adapters.ListAdapter;
import com.mobile.apps.proyectofinalappsmoviles.AddListActivity;
import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.R;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String ARG_MY_OBJECT = "list";
    RecyclerView rcv_list;
    ImageView img_addItem;
    ArrayList<ListObject> list;
    String uid;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public static ListFragment newInstance(ArrayList<ListObject> list, String uid) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MY_OBJECT, list);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rcv_list = view.findViewById(R.id.rcv_list);
        img_addItem = view.findViewById(R.id.btn_addItemList);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                ListObject item = (ListObject) data.getSerializableExtra("result");
                                ListAdapter adapter = (ListAdapter) rcv_list.getAdapter();
                                adapter.addList(item);
                            }
                        }
                    }
                }
        );
        img_addItem.setOnClickListener(this::goToAddListActivity);
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
            list = getArguments().getParcelableArrayList(ARG_MY_OBJECT);
//            Log.d("List", list + "");
            rcv_list.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv_list.setAdapter(new ListAdapter(list));
        }
        return view;
    }

    public void goToAddListActivity(View view) {
        Intent intent = new Intent(view.getContext(), AddListActivity.class);
        intent.putExtra("uid", uid);
        activityResultLauncher.launch(intent);
    }
}