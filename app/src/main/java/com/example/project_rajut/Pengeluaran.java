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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Pengeluaran extends AppCompatActivity {

    String[] item = {"Benang", "Jarum jahit", "Lem", "Kancing", "Permata"};

    AutoCompleteTextView autoCompleteTextView;

    EditText Tanggal, Keterangan, Jumlahbayar, Totalbarang;
    ImageView Keluar;

    SimpleDateFormat dateFormat, format, Format;
    DatePickerDialog datePickerDialog;

    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    ArrayAdapter arrayAdapter;

    Button confirm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);

        autoCompleteTextView = findViewById(R.id.dropdown);
        arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, item);

        confirm = findViewById(R.id.confirmpengeluaran);
        dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        Tanggal = findViewById(R.id.tanggalpengeluaran);
        Keterangan = findViewById(R.id.keterangan);
        Jumlahbayar = findViewById(R.id.jumlahpengeluaran);
        Totalbarang = findViewById(R.id.totalbarang);
        Keluar = findViewById(R.id.out);

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
                reference = database.getReference("pengeluaran");

                String kategoriitem = autoCompleteTextView.getText().toString();
                String tanggal = Tanggal.getText().toString();
                String keterangan = Keterangan.getText().toString();
                int jumlahbayar = Integer.parseInt(Jumlahbayar.getText().toString());
                String totalbarang = Totalbarang.getText().toString();
                progressDialog = new ProgressDialog(Pengeluaran.this);

                // Konversi data tanggal ke objek Date
                Date date = null;
                try {
                    date = dateFormat.parse(tanggal);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                // Format tanggal awal dan akhir mingguan
                format = new SimpleDateFormat("yyyy");
                Format = new SimpleDateFormat("MM");
                String tahun = format.format(date);
                String bulan = Format.format(date);

                String id = getRandomId(10);
                String jenistransaksi = "pengeluaran";

                Helperclasses helperclasses = new Helperclasses(id, kategoriitem, totalbarang, keterangan, jenistransaksi, tanggal, bulan, tahun, jumlahbayar);
                reference.child(id).setValue(helperclasses);

                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Dimohon untuk tunggu");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Inputan Pengeluaran telah berhasil", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }, 2000);

                Intent konfirmasi;
                konfirmasi = new Intent(Pengeluaran.this, Catatankeuangan.class);
                startActivity(konfirmasi);
            }
        });

        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        Tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatedialog();
            }
        });

        Tanggal.setOnClickListener(new View.OnClickListener() {
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
                Tanggal.setText(dateFormat.format(newDate.getTime()));
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