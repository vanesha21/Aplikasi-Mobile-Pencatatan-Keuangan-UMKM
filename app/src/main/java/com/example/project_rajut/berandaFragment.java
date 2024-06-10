package com.example.project_rajut;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class berandaFragment extends Fragment {
    
    private CardView masukan, pengeluaran, catatanKeuangan, Riwayatpemesanan;
    TextView titleTeks;

    SharedPreferences sharedPreferences, sharedpreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String SHARED_PREF_NAMES = "my_pref";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME_PEMASUKAN = "usernames";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beranda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String Username = sharedPreferences.getString(KEY_NAME, "");

        titleTeks = (TextView)view.findViewById(R.id.titleteks);
        masukan = (CardView)view.findViewById(R.id.pemasukan);
        pengeluaran = (CardView)view.findViewById(R.id.Pengeluaran);
        catatanKeuangan = (CardView)view.findViewById(R.id.catatankeuangan);
        Riwayatpemesanan = (CardView)view.findViewById(R.id.riwayatpemesanan);

        titleTeks.setText(Username.toUpperCase());

        masukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Pemasukan.class);
                getActivity().startActivity(intent);
            }
        });

        pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Pengeluaran.class);
                getActivity().startActivity(intent);
            }
        });

        catatanKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Catatankeuangan.class);
                getActivity().startActivity(intent);
            }
        });

        Riwayatpemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Catatanpemasukan.class);
                getActivity().startActivity(intent);
            }
        });



    }
}