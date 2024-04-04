package com.quynhlm.dev.lab5;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.quynhlm.dev.lab5.Api.ApiServer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActiviry extends AppCompatActivity {

    EditText ed_name,ed_email,ed_pass;
    private String url = "http://10.0.2.2:3000/";
    ApiServer apiServer;
    ImageView img_upload;
    File avatarFile = null;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activiry);
        ed_name = findViewById(R.id.ed_username_register);
        ed_email = findViewById(R.id.ed_email_register);
        ed_pass = findViewById(R.id.ed_password_register);
        img_upload = findViewById(R.id.img_Upload);
        img_upload.setOnClickListener(v -> {
            chooseImage();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);

        findViewById(R.id.btn_register).setOnClickListener(v -> {
            Map<String, RequestBody> mapRequestBody = new HashMap<>();
            String username = ed_name.getText().toString().trim();
            String password = ed_pass.getText().toString().trim();
            String email = ed_email.getText().toString().trim();

            mapRequestBody.put("username", getRequestBody(username));
            mapRequestBody.put("password", getRequestBody(password));
            mapRequestBody.put("email", getRequestBody(email));

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), avatarFile);
            MultipartBody.Part avatar = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), requestFile);

            Call<Void> call = apiServer.createUser(mapRequestBody,avatar);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(RegisterActiviry.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActiviry.this, "Dang ky that bai", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Log.e("TAG", "onFailure: "+throwable.getMessage());
                }
            });
        });
    }
    private RequestBody getRequestBody(String value) {
        if(value != null) {
            return RequestBody.create(MediaType.parse("multipart/form-data"),value);
        }else{
            Log.e("TAG", "getRequestBody: value null");
            return  null;
        }
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        img_upload.setImageURI(imageUri);
                        String realPath = RealPathUtil.getRealPath(this, imageUri);
                        if (realPath != null) {
                            avatarFile = new File(realPath);
                        }
                    }
                }
            }
    );
}