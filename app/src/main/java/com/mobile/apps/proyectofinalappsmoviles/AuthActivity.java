package com.mobile.apps.proyectofinalappsmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;

public class AuthActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPass;
    TextView txtSignUp, txtAuthTitle;
    Button btnLogin;
    ArrayList<ListObject> list = new ArrayList<>();
    ArrayList<Note> notes = new ArrayList<>();
    RequestQueue requestQueue;
    boolean signUp = false;
    Intent intent;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);
        if (uid != null)
            processRequestsSequentially(uid);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        intent = new Intent(this, HomeActivity.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


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

    public void processRequestsSequentially(String uid) {
        // Obtener la lista primero
        Request listRequest = getList(uid, () -> {
            // Después de obtener la lista, obtener las notas
            Request notesRequest = getNotes(uid, () -> {
                // Después de obtener las notas, iniciar la actividad
                intent.putParcelableArrayListExtra("list", list);
                intent.putParcelableArrayListExtra("notes", notes);
                intent.putExtra("uid", uid);
                startActivity(intent);
                finish();
            });
            requestQueue.add(notesRequest);
        });
        requestQueue.add(listRequest);
    }

    public Request getList(String uid, Runnable onComplete) {
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
                    String id = listObjectJson.getString("id");
                    String name = listObjectJson.getString("name");
                    ListObject listObject = new ListObject(id, quantity, imageURL, name);
                    list.add(listObject);
                }
                Log.d("GET", "List: " + list);
                onComplete.run(); // Llamar al siguiente request
            } catch (JSONException e) {
                Toast.makeText(this, "ERROR List: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                onComplete.run(); // Aún así llamar al siguiente request
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
            onComplete.run(); // Aún así llamar al siguiente request
        });
        return listRequest;
    }

    public Request getNotes(String uid, Runnable onComplete) {
        String url = Constants.BASE_URL + "notes/" + uid;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("GET", "Notes response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("notes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject noteJson = jsonArray.getJSONObject(i);
                    Note note = new Note(noteJson.getString("id"), noteJson.getString("title"), noteJson.getString("desc"), noteJson.getString("createdAt"));
                    notes.add(note);
                }
                Log.d("GET", "Notes: " + notes);
                onComplete.run(); // Llamar al siguiente request
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "ERROR Notes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                onComplete.run(); // Aún así llamar al siguiente request
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
            onComplete.run(); // Aún así llamar al siguiente request
        });
        return request;
    }

    public void singUp() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", edtName.getText().toString());
        params.put("email", edtEmail.getText().toString());
        params.put("pass", edtPass.getText().toString());
        String url = Constants.BASE_URL + "users/signUp";
        JsonObjectRequest signUpRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), response -> {
            Log.d("POST", "Sign Up response: " + response);
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONObject userJson = jsonObject.getJSONObject("user");
                String uid = userJson.getString("uid");
                Log.d("POST", "Uid: " + uid);
                processRequestsSequentially(uid);
                Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Toast.makeText(this, "ERROR Sign Up: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(signUpRequest);
    }

    public void login() {
        if (signUp) {
            Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
            singUp();
        } else {
            try {
                mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPass.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                Log.d("LOGIN", "Uid: " + uid);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uid", uid);
                                editor.apply();
                                processRequestsSequentially(uid);
                            } else {
                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
