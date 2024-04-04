package com.quynhlm.dev.lab5;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quynhlm.dev.lab5.Adapter.DistributeAdapter;
import com.quynhlm.dev.lab5.Adapter.DistributorAdapter;
import com.quynhlm.dev.lab5.Adapter.FruitsAdapter;
import com.quynhlm.dev.lab5.Api.ApiServer;
import com.quynhlm.dev.lab5.Model.Distribute;
import com.quynhlm.dev.lab5.Model.Fruits;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    private String url = "http://10.0.2.2:3000/";
    FruitsAdapter fruitsAdapter;
    Uri imageUri;
    RecyclerView recyclerView;
    private ApiServer apiServer;
    private ArrayList<File> images = new ArrayList<>();
    Spinner spinner_distributor;
    DistributorAdapter distributorAdapter;
    List<Fruits> list = new ArrayList<>();
    List<Distribute> list_distributes = new ArrayList<>();
    private EditText ed_name,ed_price,ed_quantity,ed_describe;
    private RadioButton rd1,rd2;
    String distributor_ID;
    private ImageView img_upload;
    private Button btn_add;
    FloatingActionButton fab_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activiry);
        recyclerView = findViewById(R.id.recyclerView_Fruits);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);

        Call<List<Fruits>> call = apiServer.getAllData();
        call.enqueue(new Callback<List<Fruits>>() {
            @Override
            public void onResponse(Call<List<Fruits>> call, Response<List<Fruits>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    fruitsAdapter = new FruitsAdapter(HomeActivity.this,list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    recyclerView.setAdapter(fruitsAdapter);
                    Toast.makeText(HomeActivity.this, "Call thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Call thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Fruits>> call, Throwable throwable) {
                Log.e("TAG", "onFailure: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                View view = LayoutInflater.from(getApplication()).inflate(R.layout.diglog_fruits,null);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();

                spinner_distributor = view.findViewById(R.id.sp_distributor);

                Call<List<Distribute>> spinnerDistributor = apiServer.getAllDistributor();
                spinnerDistributor.enqueue(new Callback<List<Distribute>>() {
                    @Override
                    public void onResponse(Call<List<Distribute>> call, Response<List<Distribute>> response) {
                        if(response.isSuccessful()){
                            list_distributes = response.body();
                            distributorAdapter = new DistributorAdapter(HomeActivity.this,list_distributes);
                            spinner_distributor.setAdapter(distributorAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Distribute>> call, Throwable throwable) {

                    }
                });

                ed_name = view.findViewById(R.id.ed_name_fruits);
                ed_price = view.findViewById(R.id.ed_price_fruits);
                ed_quantity = view.findViewById(R.id.ed_quantity_fruits);
                ed_describe = view.findViewById(R.id.ed_describe_fruits);

                rd1 = view.findViewById(R.id.rd_1);
                rd2 = view.findViewById(R.id.rd_2);
                img_upload = view.findViewById(R.id.img_add_fruits);
                btn_add = view.findViewById(R.id.btn_add);

                img_upload.setOnClickListener(v1 -> {
                    chooseImage();
                });

                btn_add.setOnClickListener(v1 -> {
                    Map<String,RequestBody> mapRequestBody = new HashMap<>();
                    String name = ed_name.getText().toString().trim();
                    String quantity = ed_quantity.getText().toString().trim();
                    String price = ed_price.getText().toString().trim();
                    String status = "";
                    if(rd1.isChecked()){
                        status = "1";
                    }else{
                        status = "0";
                    }
                    spinner_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Distribute distribute = list_distributes.get(position);
                            Toast.makeText(HomeActivity.this, "distributor" + distribute.get_id(), Toast.LENGTH_SHORT).show();
                            distributor_ID = distribute.get_id();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Do nothing
                        }
                    });
                    String description = ed_describe.getText().toString().trim();
                    mapRequestBody.put("name", getRequestBody(name));
                    mapRequestBody.put("quantity", getRequestBody(quantity));
                    mapRequestBody.put("price", getRequestBody(price));
                    mapRequestBody.put("status", getRequestBody(status));
                    mapRequestBody.put("description", getRequestBody(description));
                    mapRequestBody.put("distributor", getRequestBody("65ffea52d102d8ee75cad3dd"));
                    ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
                    Log.e("TAG", "onClick: "+mapRequestBody);

                    images.forEach(file1 -> {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
                        MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("images", file1.getName(),requestFile);
                        _ds_image.add(multipartBodyPart);
                    });
                    Call<Void> createFruit = apiServer.addFruitWithFileImage(mapRequestBody,_ds_image);
                    createFruit.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(HomeActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(HomeActivity.this, "Add not success", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "onResponse: "+response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Log.e("TAG", "onResponse: "+throwable);
                        }
                    });
                });
                alertDialog.show();
            }
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
                        File avatarFile = null;
                        if (realPath != null) {
                            avatarFile = new File(realPath);
                        }
                        images.add(avatarFile);
                    }
                }
            }
    );
//    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        images.clear();
//                        Intent data = result.getData();
//                        if (data.getClipData() != null) {
//                            int count = data.getClipData().getItemCount();
//                            for (int i = 0; i < count; i++) {
//                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                                File file = createFileFromUri(imageUri, "image" + i);
//                                images.add(file);
//                            }
//                        } else if (data.getData() != null) {
//                            Uri imageUri = data.getData();
//                            File file = createFileFromUri(imageUri, "image");
//                            images.add(file);
//                        }
//                        if (!images.isEmpty()) {
//                            Glide.with(HomeActivity.this)
//                                    .load(images.get(0))
//                                    .thumbnail(Glide.with(HomeActivity.this).load(R.drawable.ic_launcher_foreground))
//                                    .centerCrop()
//                                    .circleCrop()
//                                    .into(img_upload);
//                        }
//                    }
//                }
//            }
//    );
    private File createFileFromUri(Uri uri, String name) {
        File file = new File(HomeActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = HomeActivity.this.getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}