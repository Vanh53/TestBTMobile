package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication3.model.Room;

public class AddEditRoomActivity extends AppCompatActivity {

    private EditText etId, etName, etPrice, etTenantName, etTenantPhone;
    private CheckBox cbIsOccupied;
    private Button btnSave, btnEdit;
    private Room roomToEdit;
    private boolean isEditMode = false;

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
        btnEdit = findViewById(R.id.btnEdit);

        roomToEdit = (Room) getIntent().getSerializableExtra("ROOM");
        
        if (roomToEdit != null) {
            // Chế độ Xem/Sửa phòng đã có
            isEditMode = true;
            etId.setText(roomToEdit.getId());
            etName.setText(roomToEdit.getName());
            etPrice.setText(String.valueOf((long)roomToEdit.getPrice()));
            cbIsOccupied.setChecked(roomToEdit.isOccupied());
            etTenantName.setText(roomToEdit.getTenantName());
            etTenantPhone.setText(roomToEdit.getTenantPhone());
            
            setFieldsEnabled(false);
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        } else {
            // Chế độ Thêm mới
            isEditMode = false;
            etId.setText("Tự động tạo");
            etId.setEnabled(false);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            setFieldsEnabled(true);
        }

        btnEdit.setOnClickListener(v -> {
            setFieldsEnabled(true);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void setFieldsEnabled(boolean enabled) {
        etName.setEnabled(enabled);
        etPrice.setEnabled(enabled);
        cbIsOccupied.setEnabled(enabled);
        etTenantName.setEnabled(enabled);
        etTenantPhone.setEnabled(enabled);
    }

    private void saveRoom() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isOccupied = cbIsOccupied.isChecked();
        String tenantName = etTenantName.getText().toString().trim();
        String tenantPhone = etTenantPhone.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String id = isEditMode ? roomToEdit.getId() : ""; // ID sẽ được gán ở MainActivity nếu là thêm mới

        Room room = new Room(id, name, price, isOccupied, tenantName, tenantPhone);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("ROOM", room);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
