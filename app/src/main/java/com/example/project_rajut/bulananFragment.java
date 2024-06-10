package com.example.project_rajut;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class bulananFragment extends Fragment {

    TextView Jumlahpemasukan, Jumlahpengeluaran;

    DatabaseReference databaseReference, Databasereference;

    Query query, querys;

    RecyclerView recyclerView;

    MyAdapterBulanan myAdapterBulanan;

    ArrayList<UserBulanan> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("pemasukan");
        Databasereference = FirebaseDatabase.getInstance().getReference("pengeluaran");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bulanan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Jumlahpemasukan = view.findViewById(R.id.jumlahpemasukan);
        Jumlahpengeluaran = view.findViewById(R.id.jumlahpengeluaran);
        recyclerView = (RecyclerView) view.findViewById(R.id.listviewbulanan);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapterBulanan = new MyAdapterBulanan(getActivity(), list);
        recyclerView.setAdapter(myAdapterBulanan);

        // Dapatkan tanggal awal bulan ini
        // Calendar calendar = Calendar.getInstance();
        // calendar.set(Calendar.DAY_OF_MONTH, 1);
        // Date tanggalAwal = calendar.getTime();

        // Dapatkan tanggal akhir bulan selanjutnya
        // calendar.add(Calendar.MONTH, 1);
        // Date tanggalAkhir = calendar.getTime();

        // Dapatkan tanggal awal bulan ini
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date tanggalAwal = calendar.getTime();

        // Dapatkan tanggal akhir bulan ini
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date tanggalAkhir = calendar.getTime();

        // Format tanggal awal dan akhir mingguan
        SimpleDateFormat format = new SimpleDateFormat("MM");
        String tanggalAwalString = format.format(tanggalAwal);
        String tanggalAkhirString = format.format(tanggalAkhir);

        System.out.println(tanggalAwalString);
        System.out.println(tanggalAkhirString);

        query = databaseReference.orderByChild("bulan").equalTo(tanggalAwalString);
        querys = Databasereference.orderByChild("bulan").startAt(tanggalAwalString).endAt(tanggalAkhirString);
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
                Jumlahpemasukan.setText("Rp. " + hargaRupiah);
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
                Jumlahpengeluaran.setText("Rp. " + hargaRupiah);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            UserBulanan userBulanan;
            boolean tahunPertamaTerambil = false;

            int bulan = Integer.parseInt(tanggalAwalString);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (tahunPertamaTerambil) {
                        break; // Keluar dari loop setelah mengambil tahun pertama
                    }
                    if (bulan > 12) {
                        list = new ArrayList<>();
                    }
                    userBulanan = dataSnapshot.getValue(UserBulanan.class);
                    if (userBulanan != null) {
                        list.add(userBulanan);
                        tahunPertamaTerambil = true;
                    }
                }
                myAdapterBulanan.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}