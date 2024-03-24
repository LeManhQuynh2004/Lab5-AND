package com.quynhlm.dev.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quynhlm.dev.lab5.Adapter.DistributeAdapter;
import com.quynhlm.dev.lab5.Api.ApiServer;
import com.quynhlm.dev.lab5.Model.Distribute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String url = "http://10.0.2.2:3000/";
    private List<Distribute> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private FloatingActionButton fab;
    private EditText ed_name;
    private DistributeAdapter distributeAdapter;
    ApiServer apiServer;
        private void handleCallData () {
            Call<List<Distribute>> call = apiServer.getAllData();
            call.enqueue(new Callback<List<Distribute>>() {
                @Override
                public void onResponse(Call<List<Distribute>> call, Response<List<Distribute>> response) {
                    if(response.isSuccessful()){
                        list = response.body();
                        distributeAdapter = new DistributeAdapter(MainActivity.this,list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(distributeAdapter);
                    }else{
                        Toast.makeText(MainActivity.this, "Call thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onResponse: "+response.code()+"");
                    }
                }

                @Override
                public void onFailure(Call<List<Distribute>> call, Throwable t) {
                    Log.e("TAG", "onResponse: "+t.getMessage());
                }
            });
        }
    private void initView(){
        recyclerView = findViewById(R.id.RecyclerView_Distribute);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer =  retrofit.create(ApiServer.class);
        editTextSearch = findViewById(R.id.editTextSearch);
        fab = findViewById(R.id.fab_add);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        handleCallData();

        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add,null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();

            ed_name = view.findViewById(R.id.ed_name);
            view.findViewById(R.id.btn_add).setOnClickListener(v1 -> {
                String name = ed_name.getText().toString().trim();
                if(name.isEmpty()){
                    Toast.makeText(this, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
                }else{
                    Distribute distribute = new Distribute();
                    distribute.setName(name);
                    Call<Void> createDistribute = apiServer.createUser(distribute);
                    createDistribute.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                list.add(distribute);
                                distributeAdapter.notifyDataSetChanged();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Log.e("TAG", "onFailure: "+throwable.getMessage());
                        }
                    });
                }
            });
            view.findViewById(R.id.btn_cancle).setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });
            alertDialog.show();
        });

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("TAG", "onEditorAction: "+editTextSearch.getText().toString());
                    String keyword = editTextSearch.getText().toString(); // Lấy từ khóa tìm kiếm từ EditText

                    // Gửi yêu cầu tìm kiếm đến API với từ khóa và xử lý kết quả
                    Call<List<Distribute>> search = apiServer.search(keyword);
                    search.enqueue(new Callback<List<Distribute>>() {
                        @Override
                        public void onResponse(Call<List<Distribute>> call, Response<List<Distribute>> response) {
                            if (response.isSuccessful()) {
                                // Nếu yêu cầu thành công, xử lý danh sách kết quả trả về
                                list = response.body();
                                distributeAdapter = new DistributeAdapter(MainActivity.this, list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                recyclerView.setAdapter(distributeAdapter);
                            } else {
                                Toast.makeText(MainActivity.this, "Search thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Distribute>> call, Throwable throwable) {
                            // Xử lý lỗi khi gửi yêu cầu tìm kiếm
                            Log.e("TAG", "onFailure: " + throwable.getMessage());
                        }
                    });
                    return true;
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = editTextSearch.getText().toString().trim();
                if(search.length() == 0){
                    handleCallData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}