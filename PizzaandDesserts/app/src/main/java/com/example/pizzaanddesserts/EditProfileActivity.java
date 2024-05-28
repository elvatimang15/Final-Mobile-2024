package com.example.pizzaanddesserts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pizzaanddesserts.sqlite.DbConfig;

public class EditProfileActivity extends AppCompatActivity {
    EditText et_name, et_number;
    Button btn_simpan;
    private DbConfig dbConfig;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        // Inisialisasi objek database
        dbConfig = new DbConfig(this);

        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        btn_simpan = findViewById(R.id.btn_simpan);

        // Load user data from SQLite
        loadUserData();

        btn_simpan.setOnClickListener(view -> {
            String name = et_name.getText().toString();
            String number = et_number.getText().toString();

            if (!name.isEmpty() && !number.isEmpty()) {
                saveUserData(name, number);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(EditProfileActivity.this, "Data successfully saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditProfileActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_USERNAME, DbConfig.COLUMN_PHONE},
                DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_USERNAME));
            int phone = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));

            et_name.setText(name);
            et_number.setText(String.valueOf(phone));
        }

        cursor.close();
        db.close();
    }

    private void saveUserData(String name, String number) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_USERNAME, name);
        values.put(DbConfig.COLUMN_PHONE, Integer.parseInt(number));

        db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        db.close();
    }
}
