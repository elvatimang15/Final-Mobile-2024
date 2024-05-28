package com.example.pizzaanddesserts.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pizzaanddesserts.EditProfileActivity;
import com.example.pizzaanddesserts.LoginActivity;
import com.example.pizzaanddesserts.R;
import com.example.pizzaanddesserts.sqlite.DbConfig;

public class ProfileFragment extends Fragment {
    private TextView tv_welcome, tv_name, tv_number;
    private Button btn_logout, btn_change;
    private ImageView iv_delete;
    private DbConfig dbConfig;
    private int recordId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbConfig = new DbConfig(getActivity());

        tv_welcome = view.findViewById(R.id.tv_welcome);
        tv_name = view.findViewById(R.id.tv_name);
        tv_number = view.findViewById(R.id.tv_number);
        btn_change = view.findViewById(R.id.btn_change);
        btn_logout = view.findViewById(R.id.btn_logout);
        iv_delete = view.findViewById(R.id.btn_delete);

        loadUserData();

        btn_change.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), EditProfileActivity.class), 1);
        });

        btn_logout.setOnClickListener(v -> logoutUser());

        iv_delete.setOnClickListener(v -> {
            dbConfig.deleteRecords(recordId);
            showDeleteConfirmationDialog();
        });
    }

    private void loadUserData() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_USERNAME, DbConfig.COLUMN_PHONE},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                recordId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_USERNAME));
                int phone = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));

                tv_welcome.setText("Hai, " + username + "!");
                tv_name.setText(username);
                tv_number.setText(String.valueOf(phone));
            }
        }
    }

    private void logoutUser() {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        }

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            loadUserData();
            btn_change.setText("Update");
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Hapus Akun");
        builder.setMessage("Apakah anda yakin ingin menghapus akun ini?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            dbConfig.deleteRecords(recordId);
            logoutUser();
        });
        builder.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}