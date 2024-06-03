package com.mobile.apps.proyectofinalappsmoviles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mobile.apps.proyectofinalappsmoviles.Classes.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddNoteActivity extends AppCompatActivity {

    EditText edtNoteTitle, edtNoteDescription;
    Button btnAddNote;
    RequestQueue requestQueue;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);

        requestQueue = Volley.newRequestQueue(this);

        uid = getIntent().getStringExtra("uid");

        edtNoteTitle = findViewById(R.id.edt_noteTitle);
        edtNoteDescription = findViewById(R.id.edt_noteDescription);
        btnAddNote = findViewById(R.id.btn_addNote);

        btnAddNote.setOnClickListener(this::onAddNoteClick);
    }

    public void onAddNoteClick(View view) {
        String title = edtNoteTitle.getText().toString();
        String description = edtNoteDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty()) {
            return;
        }

        try {
            Note note = new Note(null, title, description, new Date());
            HashMap<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("desc", description);
            map.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note.getCreatedAt()));
            map.put("uid", uid);
            JsonObjectRequest noteRequest = getNoteRequest(map, note);
            requestQueue.add(noteRequest);
        } catch (ParseException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private JsonObjectRequest getNoteRequest(HashMap<String, Object> map, Note note) {
        String url = Constants.BASE_URL + "notes";
        return new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), (response) -> {
            try {
                JSONObject noteJson = response.getJSONObject("note");
                if (!noteJson.isNull("id")) {
                    String uid = noteJson.getString("id");
                    note.setId(uid);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", note);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(this, "Error al crear la nota: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, (error) -> {
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
