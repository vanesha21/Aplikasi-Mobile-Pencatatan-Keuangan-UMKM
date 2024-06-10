package com.example.project_rajut;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapterHarian extends RecyclerView.Adapter<MyAdapterHarian.MyViewHolder> {
    Context context;
    ArrayList<UserHarian> list;

    public MyAdapterHarian(Context context, ArrayList<UserHarian> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapterHarian.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_harian ,parent,false);
        return new MyAdapterHarian.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterHarian.MyViewHolder holder, int position) {
        UserHarian userHarian = list.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        holder.Kategori.setText(userHarian.getKategori());
        holder.Totalbarang.setText(userHarian.getTotalbarang()+" Pcs");
        holder.Totalharga.setText("Rp. "+decimalFormat.format(userHarian.getJumlahbayar()));
        holder.Totalhargabarang.setText("Rp. "+decimalFormat.format(userHarian.getJumlahbayar()));

        if (userHarian.getJenistransaksi().equals("pemasukan")) {
            holder.Totalharga.setTextColor(Color.GREEN);
            holder.Totalhargabarang.setTextColor(Color.GREEN);
        } else {
            holder.Totalharga.setTextColor(Color.RED);
            holder.Totalhargabarang.setTextColor(Color.RED);
        }

        // Dapatkan tanggal hari ini
        Date tanggalHariIni = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        String tanggalhariini = format.format(tanggalHariIni);

        holder.Tanggal.setText(tanggalhariini);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Tanggal, Totalharga, Kategori, Totalbarang, Totalhargabarang;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tanggal = itemView.findViewById(R.id.tanggal);
            Totalharga = itemView.findViewById(R.id.totalharga);
            Kategori = itemView.findViewById(R.id.kategori);
            Totalbarang = itemView.findViewById(R.id.totalbarang);
            Totalhargabarang = itemView.findViewById(R.id.totalhargabarang);

        }
    }
}
