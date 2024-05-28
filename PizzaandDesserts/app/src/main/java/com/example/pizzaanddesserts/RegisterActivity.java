package com.example.pizzaanddesserts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaanddesserts.sqlite.DbConfig;

public class RegisterActivity extends AppCompatActivity {
    private EditText namaRegist;
    private EditText passRegist;
    private Button btnRegist;
    private Button btnLoginRegist;
    private DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbConfig = new DbConfig(this);

        // Initialize the views
        namaRegist = findViewById(R.id.nama_regist);
        passRegist = findViewById(R.id.pass_regist);
        btnRegist = findViewById(R.id.btn_regist);
        btnLoginRegist = findViewById(R.id.btn_loginregist);

        btnRegist.setOnClickListener(view -> {
            String username = namaRegist.getText().toString().trim();
            String password = passRegist.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                dbConfig.insertData(username, password, 0, 0); // Initial phone and income values set to 0

                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoginRegist.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
}
