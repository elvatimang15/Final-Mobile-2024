package com.example.pizzaanddesserts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaanddesserts.sqlite.DbConfig;

public class LoginActivity extends AppCompatActivity {
    private EditText namaLogin;
    private EditText passLogin;
    private Button btnLogin;
    private Button btnRegistLogin;
    private DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbConfig = new DbConfig(this);

        // Initialize the views
        namaLogin = findViewById(R.id.nama_login);
        passLogin = findViewById(R.id.pass_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegistLogin = findViewById(R.id.btn_registlogin);

        btnRegistLogin.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(view -> {
            String username = namaLogin.getText().toString().trim();
            String password = passLogin.getText().toString().trim();

            if (username.isEmpty()) {
                namaLogin.setError("Please enter your name");
            } else if (password.isEmpty()) {
                passLogin.setError("Please enter your password");
            } else {
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_USERNAME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=?",
                     new String[]{username, password},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                updateLoginStatus(username, true);

                int newIncome = 20000; // Income baru yang ingin diupdate
                dbConfig.updateIncome(username, newIncome);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect Name or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLoginStatus(String username, boolean isLoggedIn) {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_USERNAME + " = ?", new String[]{username});
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.getCount() > 0) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}
