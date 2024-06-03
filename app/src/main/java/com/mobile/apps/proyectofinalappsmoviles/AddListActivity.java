package com.mobile.apps.proyectofinalappsmoviles;

import static com.mobile.apps.proyectofinalappsmoviles.Constants.BASE_URL;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mobile.apps.proyectofinalappsmoviles.Classes.ListObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddListActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    final String tag = "AddListActivity";
    EditText edtListTitle, edtListQuantity;
    ImageView imgAddItem;
    Button btnAddItem;
    String imagePath;
    String uid;
    OkHttpClient client = new OkHttpClient();
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_list);

        edtListTitle = findViewById(R.id.edt_listName);
        edtListQuantity = findViewById(R.id.edt_listQuantity);

        btnAddItem = findViewById(R.id.btn_addItem);
        btnAddItem.setOnClickListener(v -> addList());

        imgAddItem = findViewById(R.id.img_addItem);

        uid = getIntent().getStringExtra("uid");

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        Log.d("IMG", "Se ha seleccionado la imagen: " + imageUri);
                        imagePath = getRealPathFromURI(imageUri);
                        imgAddItem.setImageURI(imageUri);
                    }
                }
        );

        imgAddItem.setOnClickListener(v -> {
            if (checkPermissions()) {
                openImageChooser();
            } else {
                requestPermissions();
            }
        });
    }

    public void addList() {
        if (imagePath == null || imagePath.isEmpty() || edtListTitle.getText().toString().isEmpty() || edtListQuantity.getText().toString().isEmpty()) {
            Toast.makeText(AddListActivity.this, "Debes seleccionar una imagen, un nombre y una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ListObject object = new ListObject(Long.parseLong(edtListQuantity.getText().toString()), imagePath, edtListTitle.getText().toString());
            File file = new File(object.getImageURL());

            RequestBody fileBody = RequestBody.create(file, MediaType.parse("image/*"));
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("name", object.getName())
                    .addFormDataPart("quantity", object.getQuantity() + "")
                    .addFormDataPart("image", file.getName(), fileBody)
                    .addFormDataPart("uid", uid)
                    .build();

            Request request = new Request.Builder().url(BASE_URL + "list").post(requestBody).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(tag, "Upload failed: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(AddListActivity.this, "Error al cargar la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    AddListActivity.this.onResponse(call, response, object);
                }
            });
        } catch (Exception e) {
            Toast.makeText(AddListActivity.this, "Error al cargar la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onResponse(Call call, Response response, ListObject object) throws IOException {
        if (response.isSuccessful()) {
            Log.d(tag, "File uploaded successfully");
            try {
                assert response.body() != null;
                JSONObject jsonObject = new JSONObject(response.body().string());
                final String id = jsonObject.getString("listId");
                final String publicURL = jsonObject.getString("imageURL");
                Log.d(tag, "List id: " + id);
                Log.d(tag, "Image URL: " + publicURL);
                object.setId(id);
                object.setImageURL(publicURL);
                Intent intent = new Intent(AddListActivity.this, HomeActivity.class);
                intent.putExtra("listObject", object);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AddListActivity.this, "Error al cargar la lista: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                throw new RuntimeException(e);
            }
        } else {
            runOnUiThread(() -> Toast.makeText(AddListActivity.this, "Hubo un error al cargar la lista: " + response.message(), Toast.LENGTH_SHORT).show());
            Log.e(tag, "Upload failed: " + response.message());
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Seleccione una imagen"));
    }

    private boolean checkPermissions() {
        int permissionStateRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionStateWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("PERMISSIONS", "checkPermissions: permissionStateRead: " + permissionStateRead + ", permissionStateWrite: " + permissionStateWrite);
        return permissionStateRead == PackageManager.PERMISSION_GRANTED && permissionStateWrite == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("PERMISSIONS", "onRequestPermissionsResult: requestCode: " + requestCode + ", permissions: " + Arrays.toString(permissions) + ", grantResults: " + Arrays.toString(grantResults));
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Permisos necesarios denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String filePath = "";
        if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx != -1) {
                    filePath = cursor.getString(idx);
                } else {
                    idx = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                    filePath = cursor.getString(idx);
                    try (InputStream input = contentResolver.openInputStream(uri)) {
                        File file = new File(getCacheDir(), filePath);
                        try (OutputStream output = new FileOutputStream(file)) {
                            byte[] buffer = new byte[4 * 1024]; // Adjust if you want
                            int read;
                            while ((read = input.read(buffer)) != -1) {
                                output.write(buffer, 0, read);
                            }
                            output.flush();
                            filePath = file.getAbsolutePath();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
        }
        return filePath;
    }

}
