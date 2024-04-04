package com.quynhlm.dev.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quynhlm.dev.lab5.Api.ApiServer;
import com.quynhlm.dev.lab5.Model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private String url = "http://10.0.2.2:3000/";
    private ApiServer apiServer;
    private Button btn_login;
    private EditText ed_username,ed_password;
    private TextView tv_send_register;
    List<User> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_username = findViewById(R.id.ed_username_l);
        ed_password = findViewById(R.id.ed_password_l);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);
        tv_send_register = findViewById(R.id.tv_send_register);

        btn_login = findViewById(R.id.btn_login);
        Call<List<User>> getAll = apiServer.getAllUsers();
        getAll.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Call thanhf cong", Toast.LENGTH_SHORT).show();
                    list = response.body();
                }else{
                    Toast.makeText(LoginActivity.this, "Call that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {

            }
        });
        btn_login.setOnClickListener(v -> {
            String username = ed_username.getText().toString().trim();
            String password = ed_password.getText().toString().trim();
            boolean check = false;
            for (int i = 0; i < list.size() ; i++) {
                if(list.get(i).getUsername().equalsIgnoreCase(username) && list.get(i).getPassword().equalsIgnoreCase(password)){
                    check = true;
                    break;
                }
            }
            if(check == false) {
                Toast.makeText(this, "Dang nhap that bai", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
        });
        tv_send_register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActiviry.class));
        });
    }
}
