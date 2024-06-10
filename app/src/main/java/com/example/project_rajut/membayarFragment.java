package com.example.project_rajut;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class membayarFragment extends Fragment {

    ListView listView;


    FirebaseDatabase database;
    DatabaseReference reference, Reference;

    MyAdapters myAdapters;
    ArrayList<Userselesaimembayar> list;
    RecyclerView recyclerView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_membayar, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();

        if (sharedPreferences.contains("id") || sharedPreferences.contains("nama") || sharedPreferences.contains("kategori") || sharedPreferences.contains("tanggal")) {
            String Id = sharedPreferences.getString("id", "");
            String Nama = sharedPreferences.getString("nama", "");
            String Kategori = sharedPreferences.getString("kategori", "");
            String Tanggal = sharedPreferences.getString("tanggal", "");
            String Jumlahbayar = sharedPreferences.getString("jumlahbayar", "");
            String Kontak = sharedPreferences.getString("kontak", "");
            String Totalbarang = sharedPreferences.getString("totalbarang", "");
            String Selesaimembayar = "Selesai";

            reference = database.getReference("pembayaranselesai");

            Helperpesananselesai helperPesananselesai = new Helperpesananselesai(Id, Kategori, Tanggal, Nama, Jumlahbayar, Kontak, Selesaimembayar, Totalbarang);
            reference.child(Id).setValue(helperPesananselesai);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("id");
            editor.remove("nama");
            editor.remove("kategori");
            editor.remove("tanggal");
            editor.remove("jumlahbayar");
            editor.remove("kontak");
            editor.remove("totalbayar");
            editor.commit();
       }

        Reference = database.getReference("pembayaranselesai");

        recyclerView = (RecyclerView) view.findViewById(R.id.listviewmembayar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        myAdapters = new MyAdapters(getActivity(), list);
        recyclerView.setAdapter(myAdapters);

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Userselesaimembayar userselesaimembayar = dataSnapshot.getValue(Userselesaimembayar.class);
                    list.add(userselesaimembayar);
                }
                myAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //recyclerView = (RecyclerView) view.findViewById(R.id.listviewmembayar);

    }
}