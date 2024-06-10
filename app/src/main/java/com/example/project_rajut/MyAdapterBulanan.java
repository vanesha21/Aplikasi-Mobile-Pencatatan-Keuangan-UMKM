package com.example.project_rajut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyAdapterBulanan extends RecyclerView.Adapter<MyAdapterBulanan.MyViewHolder> {

    Context context;
    ArrayList<UserBulanan> list;

    SimpleDateFormat format, Format, formats;

    Query querybulan, querysbulan, querytahun, querynamabulan;

    DatabaseReference databaseReference, Databasereference;

    public MyAdapterBulanan(Context context, ArrayList<UserBulanan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapterBulanan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_bulanan ,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterBulanan.MyViewHolder holder, int position) {

        UserBulanan userBulanan = list.get(position);

        databaseReference = FirebaseDatabase.getInstance().getReference("pemasukan");
        Databasereference = FirebaseDatabase.getInstance().getReference("pengeluaran");

        // Dapatkan tanggal awal bulan ini
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date bulanAwal = calendar.getTime();

        // Dapatkan tanggal akhir bulan ini
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date bulanAkhir = calendar.getTime();

        // Format tanggal awal dan akhir mingguan
        Format = new SimpleDateFormat("MM");
        String tanggalAwalString = Format.format(bulanAwal);
        String tanggalAkhirString = Format.format(bulanAkhir);

        // formats = new SimpleDateFormat("MMMM");
        // String namabulan = formats.format(userBulanan.getBulan());

        /* format = new SimpleDateFormat("YYYY");
        String tahunawalString = format.format(bulanAwal);
        String tahunakhirString = format.format(bulanAkhir); */

        querybulan = databaseReference.orderByChild("bulan").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        querysbulan = Databasereference.orderByChild("bulan").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        // querytahun = databaseReference.orderByChild("tahun").startAt(tahunawalString).endAt(tahunakhirString);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        // Iterasi semua data
         querybulan.addValueEventListener(new ValueEventListener() {
            double sum = 0;
            String month = null;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    // Ambil harga
                    int price = dataSnapshotChild.child("jumlahbayar").getValue(Integer.class);
                    String bulan = dataSnapshotChild.child("bulan").getValue(String.class);

                    // Tambahkan harga ke sum
                    sum += price;
                }
                String hargaRupiah = decimalFormat.format(sum);
                holder.Jumlahpemasukan.setText("Rp. " + hargaRupiah);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

         querysbulan.addValueEventListener(new ValueEventListener() {
            double sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    // Ambil harga
                    int price = dataSnapshotChild.child("jumlahbayar").getValue(Integer.class);

                    // Tambahkan harga ke sum
                    sum += price;
                }
                String hargaRupiah = decimalFormat.format(sum);
                holder.Jumlahpengeluaran.setText("Rp. " + hargaRupiah);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Iterasi semua data
        /* querytahun.addValueEventListener(new ValueEventListener() {
            String Tahun = null;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    // Ambil harga
                    String Year = dataSnapshotChild.child("tahun").getValue(String.class);

                    // Tambahkan tahun
                    Tahun = Year;
                }
                // Jika masih ada data
                // Set data tahun ke holder
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        }); */

        holder.Tahun.setText(userBulanan.getTahun());
        holder.Bulan.setText(userBulanan.getBulan());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Bulan, Tahun, Jumlahpemasukan, Jumlahpengeluaran;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Bulan = itemView.findViewById(R.id.bulan);
            Tahun = itemView.findViewById(R.id.tahun);
            Jumlahpemasukan = itemView.findViewById(R.id.jumlahpemasukan);
            Jumlahpengeluaran = itemView.findViewById(R.id.jumlahpengeluaran);
        }
    }
}
