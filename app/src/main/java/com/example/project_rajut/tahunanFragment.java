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

public class tahunanFragment extends Fragment {

    TextView Jumlahpemasukan, Jumlahpengeluaran;

    DatabaseReference databaseReference, Databasereference;

    Query query, querys, queries;

    RecyclerView recyclerView;

    MyAdapterTahunan myAdapterTahunan;

    ArrayList<UserTahunan> list;

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
        return inflater.inflate(R.layout.fragment_tahunan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Jumlahpemasukan = view.findViewById(R.id.jumlahpemasukan);
        Jumlahpengeluaran = view.findViewById(R.id.jumlahpengeluaran);
        recyclerView = (RecyclerView) view.findViewById(R.id.listviewtahunan);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapterTahunan = new MyAdapterTahunan(getActivity(), list);
        recyclerView.setAdapter(myAdapterTahunan);

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

        // Dapatkan tanggal akhir tahun selanjutnya
        // calendar.add(Calendar.YEAR, Calendar.YEAR);
        // Date tanggalAkhir = calendar.getTime();

        // Format tanggal awal dan akhir mingguan
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String tanggalAwalString = format.format(tanggalAwal);
        String tanggalAkhirString = format.format(tanggalAkhir);


        // Filter data berdasarkan tahun
        query = databaseReference.orderByChild("tahun").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        querys = Databasereference.orderByChild("tahun").startAt(tanggalAwalString).endAt(tanggalAkhirString);

        // System.out.println(tanggalAwalString);
        // System.out.println(tanggalAkhirString);

        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

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

        //System.out.println(tanggalAwalStrings);
        //System.out.println(tanggalAkhirStrings);

        // Dapatkan tanggal awal tahun ini
        Calendar calenders = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        Date tanggalAwaltahuns = calendar.getTime();

        // Hari terakhir tahun ini
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        Date tanggalAkhirtahuns = calendar.getTime();

        String tanggalAwalStringst = format.format(tanggalAwaltahuns);
        String tanggalAkhirStringst = format.format(tanggalAkhirtahuns);

        System.out.println(tanggalAwalStringst);
        System.out.println(tanggalAkhirStringst);

        queries = databaseReference.orderByChild("tahun").startAt(tanggalAwalStrings).endAt(tanggalAkhirStrings);
        query = databaseReference.orderByChild("tahun").startAt(tanggalAwalStringst).endAt(tanggalAkhirStringst);


        queries.addValueEventListener(new ValueEventListener() {
            boolean tahunPertamaTerambil = false;
            UserTahunan userTahunan = null;

            int tahunawal = Integer.parseInt(tanggalAwalStrings);
            int tahunakhir = Integer.parseInt(tanggalAkhirStrings);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (tahunPertamaTerambil) {
                        break; // Keluar dari loop setelah mengambil tahun pertama
                    }
                    if (tahunawal > tahunakhir) {
                        list = new ArrayList<>();
                    }
                    userTahunan = dataSnapshot.getValue(UserTahunan.class);
                    if (userTahunan != null) {
                        list.add(userTahunan);
                        tahunPertamaTerambil = true;
                    }

                }
                myAdapterTahunan.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}