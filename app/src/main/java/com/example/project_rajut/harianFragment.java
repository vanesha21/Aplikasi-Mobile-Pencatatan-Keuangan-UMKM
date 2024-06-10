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
import android.view.ViewManager;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class harianFragment extends Fragment {

    TextView Jumlahpemasukan, Jumlahpengeluaran;
    DatabaseReference databaseReference, Databasereference;

    Query query, querys;

    RecyclerView recyclerView;

    MyAdapterHarian myAdapterHarian;

    ArrayList<UserHarian> list;


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
        return inflater.inflate(R.layout.fragment_harian, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Jumlahpemasukan = view.findViewById(R.id.jumlahpemasukan);
        Jumlahpengeluaran = view.findViewById(R.id.jumlahpengeluaran);
        recyclerView = (RecyclerView) view.findViewById(R.id.listviewharian);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapterHarian = new MyAdapterHarian(getActivity(), list);
        recyclerView.setAdapter(myAdapterHarian);

        // Dapatkan tanggal hari ini
        Date tanggalHariIni = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy");
        String tanggalhariini = format.format(tanggalHariIni);

        // Filter data berdasarkan tanggal
        query = databaseReference.orderByChild("tanggal").equalTo(tanggalhariini);
        querys = Databasereference.orderByChild("tanggal").equalTo(tanggalhariini);
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserHarian userHarian = dataSnapshot.getValue(UserHarian.class);
                    list.add(userHarian);
                }
                myAdapterHarian.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        querys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserHarian userHarian = dataSnapshot.getValue(UserHarian.class);
                    list.add(userHarian);
                }
                myAdapterHarian.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    
}