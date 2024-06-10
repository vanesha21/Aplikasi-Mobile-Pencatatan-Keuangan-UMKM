package com.example.project_rajut;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class pesanan extends AppCompatActivity {

    EditText Tanggalpesanan, Namapemesan, Kontak, Jumlahbayarpesanan, Totalbarang ;

    SimpleDateFormat dateFormat;
    DatePickerDialog datePickerDialog;

    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    Button confirm;

    ImageView Keluar;

    String[] item = {"Tas", "Gelang", "Kalung", "Sandal"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter arrayAdapters;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        Tanggalpesanan = findViewById(R.id.tanggalpesan);
        confirm = findViewById(R.id.confirmpesanan);
        Namapemesan = findViewById(R.id.namapemesan);
        Kontak = findViewById(R.id.kontakpemesan);
        Jumlahbayarpesanan = findViewById(R.id.jumlahbayarpesanan);
        Totalbarang = findViewById(R.id.totalbarang);
        Keluar = findViewById(R.id.out);
        dateFormat = new SimpleDateFormat("dd-MM-yy");

        arrayAdapters = new ArrayAdapter(this, R.layout.dropdown_item, item);

        autoCompleteTextView = findViewById(R.id.dropdown);

        autoCompleteTextView.setDropDownHeight(600);

        Keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("pemesanan");

                String kategoriitem = autoCompleteTextView.getText().toString();
                String tanggal = Tanggalpesanan.getText().toString();
                String kontakpemesan = Kontak.getText().toString();
                String nama = Namapemesan.getText().toString();
                String jumlahbayar = Jumlahbayarpesanan.getText().toString();
                String totalbarang = Totalbarang.getText().toString();
                progressDialog = new ProgressDialog(pesanan.this);

                String id = getRandomId(10);

                HelperPesanan helperPesanan = new HelperPesanan(id, kategoriitem, nama, tanggal, jumlahbayar, kontakpemesan, totalbarang);
                reference.child(id).setValue(helperPesanan);

                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Dimohon untuk tunggu");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Inputan Pemesanan telah berhasil", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }, 2000);

                Intent konfirmasi;
                konfirmasi = new Intent(pesanan.this, Catatanpemasukan.class );
                startActivity(konfirmasi);
            }
        });

        autoCompleteTextView.setAdapter(arrayAdapters);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        Tanggalpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatedialog();
            }
        });

        Tanggalpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatedialog();
            }
        });
    }

    private void showdatedialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayofmonth);
                Tanggalpesanan.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static String getRandomId(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] random = new char[length];

        for (int i = 0; i < length; i++) {
            random[i] = characters.charAt((int) Math.floor(Math.random() * characters.length()));
        }

        return new String(random);
    }
}