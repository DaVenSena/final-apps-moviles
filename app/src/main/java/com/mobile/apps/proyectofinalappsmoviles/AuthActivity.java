package com.mobile.apps.proyectofinalappsmoviles;

import android.os.Bundle;
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

public class AuthActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPass;
    TextView txtSignUp, txtAuthTitle;
    Button btnLogin;

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
        }
    }
}