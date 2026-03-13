package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication3.model.Room;

public class AddEditRoomActivity extends AppCompatActivity {

    private EditText etId, etName, etPrice, etTenantName, etTenantPhone;
    private CheckBox cbIsOccupied;
    private Button btnSave;
    private Room roomToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_room);

        etId = findViewById(R.id.etRoomId);
        etName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etRoomPrice);
        cbIsOccupied = findViewById(R.id.cbIsOccupied);
        etTenantName = findViewById(R.id.etTenantName);
        etTenantPhone = findViewById(R.id.etTenantPhone);
        btnSave = findViewById(R.id.btnSave);

        roomToEdit = (Room) getIntent().getSerializableExtra("ROOM");
        if (roomToEdit != null) {
            etId.setText(roomToEdit.getId());
            etId.setEnabled(false);
            etName.setText(roomToEdit.getName());
            etPrice.setText(String.valueOf(roomToEdit.getPrice()));
            cbIsOccupied.setChecked(roomToEdit.isOccupied());
            etTenantName.setText(roomToEdit.getTenantName());
            etTenantPhone.setText(roomToEdit.getTenantPhone());
        }

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void saveRoom() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isOccupied = cbIsOccupied.isChecked();
        String tenantName = etTenantName.getText().toString().trim();
        String tenantPhone = etTenantPhone.getText().toString().trim();

        if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        Room room = new Room(id, name, price, isOccupied, tenantName, tenantPhone);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ROOM", room);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
