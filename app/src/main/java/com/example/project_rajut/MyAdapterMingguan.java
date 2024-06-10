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

public class MyAdapterMingguan extends RecyclerView.Adapter<MyAdapterMingguan.MyViewHolder> {
    Context context;
    ArrayList<UserMingguan> list;

    DatabaseReference databaseReference, Databasereference;

    Query query, querys;

    SimpleDateFormat format, Format;

    public MyAdapterMingguan(Context context, ArrayList<UserMingguan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapterMingguan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_mingguan ,parent,false);
        return new MyAdapterMingguan.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterMingguan.MyViewHolder holder, int position) {

        UserMingguan userMingguan = list.get(position);

        // Dapatkan tanggal awal mingguan
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date tanggalAwal = calendar.getTime();

        // Dapatkan tanggal akhir mingguan
        // calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date tanggalAkhir = calendar.getTime();

        // Format tanggal awal dan akhir mingguan
        format = new SimpleDateFormat("dd-MMMM-yyyy");
        String tanggalAwalString = format.format(tanggalAwal);
        String tanggalAkhirString = format.format(tanggalAkhir);

        Format = new SimpleDateFormat("dd.MM");
        String AwalString = Format.format(tanggalAwal);
        String AkhirString = Format.format(tanggalAkhir);

        System.out.println(AwalString);
        System.out.println(AkhirString);



        databaseReference = FirebaseDatabase.getInstance().getReference("pemasukan");
        Databasereference = FirebaseDatabase.getInstance().getReference("pengeluaran");

        query = databaseReference.orderByChild("tanggal").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        querys = Databasereference.orderByChild("tanggal").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        // Iterasi semua data
        query.addValueEventListener(new ValueEventListener() {
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
                holder.Jumlahpemasukan.setText("Rp. "+hargaRupiah);
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
                String hargaRupiah = decimalFormat.format(sum);
                holder.Jumlahpengeluaran.setText("Rp. "+hargaRupiah);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        holder.Tanggalmingguan.setText(AwalString+" - "+AkhirString);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Tanggalmingguan, Jumlahpemasukan, Jumlahpengeluaran;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Tanggalmingguan = itemView.findViewById(R.id.tanggalmingguan);
            Jumlahpemasukan = itemView.findViewById(R.id.jumlahpemasukan);
            Jumlahpengeluaran = itemView.findViewById(R.id.jumlahpengeluaran);
        }
    }
}
