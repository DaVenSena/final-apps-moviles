package com.mobile.apps.proyectofinalappsmoviles;

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
import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPass;
    TextView txtSignUp, txtAuthTitle;
    Button btnLogin;

    List<ListObject> list = new ArrayList<>();

    RequestQueue requestQueue;

    boolean signUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestQueue = Volley.newRequestQueue(this);

        edtName = findViewById(R.id.edtName);
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
            edtName.setVisibility(View.INVISIBLE);
        }
        txtAuthTitle.setText(title);
        btnLogin.setText(title);
        txtSignUp.setText(account);
    }

    public void login() {
        if (signUp) {
            Toast.makeText(this, "Sign Up", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
            try {
                String uid = "cGqMp3BEvMiOoJjYnQzI";
                getList(uid);
                Log.d("", "List: " + list);
            } catch (Exception e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getList(String uid) {
        String url = "https://finalappsmoviles-e4cec07b2ddb.herokuapp.com/api/list/" + uid;
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
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(this, "Error al llamar a la API " + error.toString(), Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(listRequest);
    }
}