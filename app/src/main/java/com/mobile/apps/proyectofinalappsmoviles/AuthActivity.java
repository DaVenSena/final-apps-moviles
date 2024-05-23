package com.mobile.apps.proyectofinalappsmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPass;
    TextView txtSignUp, txtAuthTitle;
    Button btnLogin;
    ArrayList<ListObject> list = new ArrayList<>();
    ArrayList<Note> notes = new ArrayList<>();
    RequestQueue requestQueue;

    private FirebaseAuth mAuth;
    boolean signUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestQueue = Volley.newRequestQueue(this);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        txtSignUp = findViewById(R.id.txt_signup);
        txtAuthTitle = findViewById(R.id.txtAuthTitle);
        btnLogin = findViewById(R.id.btn_login);
        txtSignUp.setOnClickListener(view -> changeAuth());
        btnLogin.setOnClickListener(view -> login());
    }

    public void changeAuth() {
        signUp = !signUp;
        String title = "Log In";
        String account = "Don't have an account?";
        if (signUp) {
            edtName.setVisibility(View.VISIBLE);
            title = "Sign Up";
            account = "You already have an account? Sign In";
        } else {
            edtName.setVisibility(View.GONE);
        }
        txtAuthTitle.setText(title);
        btnLogin.setText(title);
        txtSignUp.setText(account);
    }

    public Request getList(String uid) {
        String url = Constants.BASE_URL + "list/" + uid;
        StringRequest listRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("GET", "List response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject listObjectJson = jsonArray.getJSONObject(i);
                    int quantity = listObjectJson.getInt("quantity");
                    String imageURL = listObjectJson.getString("imageUrl");
                    String name = listObjectJson.getString("name");
                    ListObject listObject = new ListObject(quantity, imageURL, name);
                    list.add(listObject);
                }
                Log.d("GET", "List: " + list);
            } catch (JSONException e) {
                Toast.makeText(this, "ERROR List: " + e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
        });
        return listRequest;
    }

    public void login() {
        if (signUp) {
            Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
        } else {
            try {
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(this, HomeActivity.class);
                            String uid = task.getResult().getUser().getUid();
                            Log.d("LOGIN", "Uid: " + uid);
                            Request listRequest = getList(uid);
                            Request notesRequest = getNotes(uid);
                            requestQueue.add(listRequest);
                            requestQueue.add(notesRequest);
                            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
                            i.putParcelableArrayListExtra("list", list);
                            i.putParcelableArrayListExtra("notes", notes);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
            }catch (Exception e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Request getNotes(String uid) {
        String url = Constants.BASE_URL + "notes/" + uid;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("GET", "List response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("notes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject noteJson = jsonArray.getJSONObject(i);
                    Note note = new Note(noteJson.getString("id"), noteJson.getString("title"), noteJson.getString("desc"), noteJson.getString("createdAt"));
                    notes.add(note);
                }
                Log.d("GET", "Notes: " + notes);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "ERROR Notes: " + e.toString(), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
        });
        return request;
    }
}