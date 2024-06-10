package com.example.project_rajut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Userbelummembayar> list;

    public MyAdapter(Context context, ArrayList<Userbelummembayar> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_belummembayar ,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Userbelummembayar userbelummembayar = list.get(position);

        holder.Name.setText(userbelummembayar.getNamapemesan());
        holder.Tanggal.setText(userbelummembayar.getTanggal());
        holder.Contact.setText(userbelummembayar.getKontak());
        holder.Order.setText(userbelummembayar.getKategori());
        holder.Buy.setText(userbelummembayar.getJumlahbayar());
        holder.Totalbarang.setText(userbelummembayar.getTotalbarang());

        holder.selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Apakah pesanan ini sudah selesai?");
                builder.setMessage("pesanan akan berpindah ke halaman membayar");
                builder.setCancelable(false);
                builder.setNegativeButton("cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.setPositiveButton("Iya", (dialogInterface, i) -> {
                    // Buat intent untuk memulai activity lain
                    Intent intent = new Intent(view.getContext(), Catatanpemasukan.class);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);

                    // Simpan data ke SharedPreferences
                    sharedPreferences.edit().putString("id", userbelummembayar.getId()).apply();
                    sharedPreferences.edit().putString("tanggal", userbelummembayar.getTanggal()).apply();
                    sharedPreferences.edit().putString("kategori", userbelummembayar.getKategori()).apply();
                    sharedPreferences.edit().putString("nama", userbelummembayar.getNamapemesan()).apply();
                    sharedPreferences.edit().putString("kontak", userbelummembayar.getKontak()).apply();
                    sharedPreferences.edit().putString("jumlahbayar", userbelummembayar.getJumlahbayar()).apply();
                    sharedPreferences.edit().putString("totalbarang", userbelummembayar.getTotalbarang()).apply();

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("pemesanan").child(Objects.requireNonNull(userbelummembayar.getId()));

                    database.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                    // Mulai activity lain
                    view.getContext().startActivity(intent);

                });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(dialogInterface -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(view.getContext().getResources().getColor(android.R.color.holo_red_dark));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(view.getContext().getResources().getColor(android.R.color.holo_blue_dark));
                });

                dialog.show();
            }
        });

        holder.batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("apakah kamu ingin menghapus data ini?");
                builder.setMessage("Jika anda ingin menghapus data ini, data terhapus secara permanen");
                builder.setCancelable(false);
                builder.setNegativeButton("cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.setPositiveButton("Hapus", (dialogInterface, i) -> {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("pemesanan").child(Objects.requireNonNull(userbelummembayar.getId()));

                    database.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Sudah terhapus", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Gagal terhapus", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    });

                });

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(dialogInterface -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(view.getContext().getResources().getColor(android.R.color.holo_red_dark));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(view.getContext().getResources().getColor(android.R.color.holo_blue_dark));
                });

                dialog.show();

            }
        });
    }

    private void getSupportFragmentManager() {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, Tanggal, Contact, Order, Buy, Totalbarang;
        Button batal, selesai;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.teksnama);
            Tanggal = itemView.findViewById(R.id.tanggalmemesan);
            Contact = itemView.findViewById(R.id.kontak);
            Order = itemView.findViewById(R.id.pesanan);
            Buy = itemView.findViewById(R.id.harga);
            Totalbarang = itemView.findViewById(R.id.totalproduks);
            batal = itemView.findViewById(R.id.btnbatal);
            selesai = itemView.findViewById(R.id.btnselesai);
        }
    }
}
