package com.example.project_rajut;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MyAdapters extends RecyclerView.Adapter<MyAdapters.MyViewHolder>{

    Context context;
    ArrayList<Userselesaimembayar> list;

    public MyAdapters(Context context, ArrayList<Userselesaimembayar> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item ,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Userselesaimembayar userselesaimembayar = list.get(position);

        holder.Name.setText(userselesaimembayar.getNamapemesan());
        holder.Contact.setText(userselesaimembayar.getKontak());
        holder.Order.setText(userselesaimembayar.getKategori());
        holder.Buy.setText(userselesaimembayar.getJumlahbayar());
        holder.Statuspesanan.setText(userselesaimembayar.getSelesai());
        holder.Totalbarang.setText(userselesaimembayar.getTotalbarang());
        holder.Hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("apakah kamu ingin menghapus data pembayaran ini?");
                builder.setMessage("Jika anda ingin menghapus data ini, data terhapus secara permanen");
                builder.setCancelable(false);
                builder.setNegativeButton("cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                builder.setPositiveButton("Hapus", (dialogInterface, i) -> {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("pembayaranselesai").child(Objects.requireNonNull(userselesaimembayar.getId()));

                    System.out.println(userselesaimembayar.getId());
                    database.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Sudah terhapus", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();

                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            view.getContext().startActivity(intent);
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

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, Contact, Order, Buy, Statuspesanan, Totalbarang;
        ImageView Hapus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.teksnama);
            Contact = itemView.findViewById(R.id.kontak);
            Order = itemView.findViewById(R.id.pesanan);
            Buy = itemView.findViewById(R.id.harga);
            Statuspesanan = itemView.findViewById(R.id.statuspesanan);
            Totalbarang = itemView.findViewById(R.id.jumlahproduks);
            Hapus = itemView.findViewById(R.id.hapus);
        }
    }
}
