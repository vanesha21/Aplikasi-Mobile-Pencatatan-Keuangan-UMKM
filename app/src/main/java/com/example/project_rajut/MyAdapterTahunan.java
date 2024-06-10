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
import java.util.List;

public class MyAdapterTahunan extends RecyclerView.Adapter<MyAdapterTahunan.MyViewHolder> {

    Context context;
    ArrayList<UserTahunan> list;
    DatabaseReference databaseReference, Databasereference;

    Query query, querys, queries, queriest;

    public MyAdapterTahunan(Context context, ArrayList<UserTahunan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapterTahunan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_tahunan ,parent,false);
        return new MyAdapterTahunan.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterTahunan.MyViewHolder holder, int position) {

        UserTahunan userTahunan = list.get(position);

        databaseReference = FirebaseDatabase.getInstance().getReference("pemasukan");
        Databasereference = FirebaseDatabase.getInstance().getReference("pengeluaran");

        // Dapatkan tanggal awal tahun ini
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date tanggalAwal = calendar.getTime();

        // Hari terakhir tahun ini
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date tanggalAkhir = calendar.getTime();

        // Format tanggal awal dan akhir mingguan
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String tanggalAwalString = format.format(tanggalAwal);
        String tanggalAkhirString = format.format(tanggalAkhir);

        // System.out.println(tanggalAwalString);
        // System.out.println(tanggalAkhirString);

        // Dapatkan tanggal awal tahun ini
        Calendar calender = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date tanggalAwaltahun = calendar.getTime();

        // Hari terakhir tahun ini
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 1);
        Date tanggalAkhirtahun = calendar.getTime();

        String tanggalAwalStrings = format.format(tanggalAwaltahun);
        String tanggalAkhirStrings = format.format(tanggalAkhirtahun);

        System.out.println(tanggalAwalStrings);
        System.out.println(tanggalAkhirStrings);

        query = databaseReference.orderByChild("tahun").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        querys = Databasereference.orderByChild("tahun").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        queries = Databasereference.orderByChild("tahun").startAt(tanggalAwalStrings).endAt(tanggalAkhirStrings);
        queriest = databaseReference.orderByChild("tahun").startAt(tanggalAwalStrings).endAt(tanggalAkhirStrings);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        query.addValueEventListener(new ValueEventListener() {
            double sum = 0;
            String hargaRupiah, ambildata;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    // Ambil harga
                    int price = dataSnapshotChild.child("jumlahbayar").getValue(Integer.class);

                    // Tambahkan harga ke sum
                    sum += price;
                }
                hargaRupiah = decimalFormat.format(sum);
                holder.Jumlahpemasukan.setText("Rp. "+hargaRupiah);
                ambildata = hargaRupiah;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        querys.addValueEventListener(new ValueEventListener() {
            double sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                    // Ambil harga
                    int price = dataSnapshotChild.child("jumlahbayar").getValue(Integer.class);

                    // Tambahkan harga ke sum
                    sum += price;
                }
                String hargaRupiahs = decimalFormat.format(sum);
                holder.Jumlahpengeluaran.setText("Rp. "+hargaRupiahs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        holder.Tahun.setText(userTahunan.getTahun());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Tahun, Jumlahpemasukan, Jumlahpengeluaran;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Tahun = itemView.findViewById(R.id.tahun);
            Jumlahpengeluaran = itemView.findViewById(R.id.jumlahpengeluaran);
            Jumlahpemasukan = itemView.findViewById(R.id.jumlahpemasukan);
        }
    }
}
