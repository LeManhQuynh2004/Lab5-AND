package com.quynhlm.dev.lab5.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.quynhlm.dev.lab5.Api.ApiServer;
import com.quynhlm.dev.lab5.MainActivity;
import com.quynhlm.dev.lab5.Model.Distribute;
import com.quynhlm.dev.lab5.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DistributeAdapter extends RecyclerView.Adapter<DistributeAdapter.DistributeViewHolder> {
    Context context;
    private String url = "http://10.0.2.2:3000/";
    TextView title;
    EditText ed_name;
    List<Distribute> list;
    ApiServer apiServer;


    public DistributeAdapter(Context context, List<Distribute> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DistributeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_distribute, parent, false);
        return new DistributeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributeViewHolder holder, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_index.setText(String.valueOf(position + 1));
        holder.img_delete.setOnClickListener(v -> {
            handleDeleteData(position);
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdateData(position);
                Log.e("TAG", "onClick: "+position);
            }
        });
    }

    private void handleUpdateData(int position) {
        Distribute distributeUpdate = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        title = view.findViewById(R.id.title);
        ed_name = view.findViewById(R.id.ed_name);
        title.setText("Update Distribute");
        ed_name.setText(distributeUpdate.getName());
        view.findViewById(R.id.btn_add).setOnClickListener(v1 -> {
            String name = ed_name.getText().toString().trim();
            distributeUpdate.setName(name);
            if (name.isEmpty()) {
                Toast.makeText(context, "Vui lòng không bỏ trống", Toast.LENGTH_SHORT).show();
            } else {
                Call<Distribute> update = apiServer.updateData(list.get(position).get_id(), distributeUpdate);
                update.enqueue(new Callback<Distribute>() {
                    @Override
                    public void onResponse(Call<Distribute> call, Response<Distribute> response) {
                        if (response.isSuccessful()) {
                            Distribute distribute = response.body();
                            Log.e("TAG", "onResponse: "+distribute.getName());
                            Log.e("TAG", "onResponse 2: "+distributeUpdate.getName());
                            if(distribute != null){
                                Toast.makeText(context, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                list.set(position,distribute);
                                alertDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(context, "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onResponse: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Distribute> call, Throwable throwable) {
                        Log.e("TAG", "onFailure: " + throwable.getMessage());
                    }
                });
            }
        });
        view.findViewById(R.id.btn_cancle).setOnClickListener(v1 -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void handleDeleteData(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa nhà cung cấp");
        builder.setMessage("Bạn có chắc chắn muốn xóa nhà cung cấp này không ?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void> call = apiServer.deleteDistribute(list.get(position).get_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Log.e("TAG", "onFailure: " + throwable.getMessage());
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DistributeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_index, tv_name;
        ImageView img_delete;

        public DistributeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_index = itemView.findViewById(R.id.tv_index);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
