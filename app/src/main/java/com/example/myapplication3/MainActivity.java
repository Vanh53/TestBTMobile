package com.example.myapplication3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication3.adapter.RoomAdapter;
import com.example.myapplication3.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FloatingActionButton fabAdd;
    private int selectedPosition = -1;

    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room newRoom = (Room) result.getData().getSerializableExtra("ROOM");
                    if (newRoom != null) {
                        roomList.add(newRoom);
                        adapter.notifyItemInserted(roomList.size() - 1);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> editRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room updatedRoom = (Room) result.getData().getSerializableExtra("ROOM");
                    if (updatedRoom != null && selectedPosition != -1) {
                        roomList.set(selectedPosition, updatedRoom);
                        adapter.notifyItemChanged(selectedPosition);
                        selectedPosition = -1;
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRooms = findViewById(R.id.rvRooms);
        fabAdd = findViewById(R.id.fabAdd);

        roomList = new ArrayList<>();
        // Dữ liệu mẫu
        roomList.add(new Room("R001", "Phòng 101", 1500000, false, "", ""));
        roomList.add(new Room("R002", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321"));

        adapter = new RoomAdapter(roomList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditRoomActivity.class);
            addRoomLauncher.launch(intent);
        });
    }

    @Override
    public void onItemClick(Room room) {
        selectedPosition = roomList.indexOf(room);
        Intent intent = new Intent(MainActivity.this, AddEditRoomActivity.class);
        intent.putExtra("ROOM", room);
        editRoomLauncher.launch(intent);
    }

    @Override
    public void onItemLongClick(Room room) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa phòng")
                .setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int pos = roomList.indexOf(room);
                    roomList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
