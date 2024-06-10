package com.example.project_rajut;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class mingguanFragment extends Fragment {

    TextView Jumlahpemasukan, Jumlahpengeluaran;

    DatabaseReference databaseReference, Databasereference;

    Query query, querys;

    SimpleDateFormat format, Format, formats;

    RecyclerView recyclerView;

    MyAdapterMingguan myAdapterMingguan;

    ArrayList<UserMingguan> list;

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
        return inflater.inflate(R.layout.fragment_mingguan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Jumlahpemasukan = view.findViewById(R.id.jumlahpemasukan);
        Jumlahpengeluaran = view.findViewById(R.id.jumlahpengeluaran);

        recyclerView = (RecyclerView) view.findViewById(R.id.listviewmingguan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapterMingguan = new MyAdapterMingguan(getActivity(), list);
        recyclerView.setAdapter(myAdapterMingguan);

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

        // formats = new SimpleDateFormat("MM");
        // String bulanAwalStrings = formats.format(tanggalAwal);
        // String bulanAkhirStrings = formats.format(tanggalAkhir);

        // Format tanggal awal dan akhir mingguan
        Format = new SimpleDateFormat("dd");
        String mingguanAwalString = Format.format(tanggalAwal);
        String mingguanAkhirString = Format.format(tanggalAkhir);

        System.out.println(tanggalAwalString);
        System.out.println(tanggalAkhirString);

        query = databaseReference.orderByChild("tanggal").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        querys = Databasereference.orderByChild("tanggal").startAt(tanggalAwalString).endAt(tanggalAkhirString);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        // Iterasi semua data
        query.addValueEventListener(new ValueEventListener() {
            double sum = 0;

            // int bulanAwal = Integer.parseInt(bulanAwalStrings);
            // int bulanAkhir = Integer.parseInt(bulanAkhirStrings);

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
            boolean tahunPertamaTerambil = false;
            UserMingguan userMingguan = null;

            int mingguanawal = Integer.parseInt(mingguanAwalString);
            int mingguanakhir = Integer.parseInt(mingguanAkhirString);

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (tahunPertamaTerambil) {
                            break; // Keluar dari loop setelah mengambil tahun pertama
                        }
                        userMingguan = dataSnapshot.getValue(UserMingguan.class);
                        if (mingguanawal > mingguanakhir) {
                            list = new ArrayList<>();
                        }
                        if (userMingguan != null) {
                            list.add(userMingguan);
                            tahunPertamaTerambil = true;
                        }

                    }
                    myAdapterMingguan.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}