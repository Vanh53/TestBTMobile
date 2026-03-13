package com.example.myapplication3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements RoomAdapter.OnItemClickListener {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private List<Room> allRooms;
    private List<Room> displayList;
    private FloatingActionButton fabAdd;
    private TextView tvCountAll, tvCountOccupied, tvCountAvailable;
    private View btnAll, btnOccupied, btnAvailable;
    private int selectedPosition = -1;
    private String currentFilter = "ALL";

    private final ActivityResultLauncher<Intent> addRoomLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Room newRoom = (Room) result.getData().getSerializableExtra("ROOM");
                    if (newRoom != null) {
                        String nextId = generateNextId();
                        newRoom.setId(nextId);
                        allRooms.add(newRoom);
                        updateUI();
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
                        Room oldRoom = displayList.get(selectedPosition);
                        int actualIndex = allRooms.indexOf(oldRoom);
                        if (actualIndex != -1) {
                            allRooms.set(actualIndex, updatedRoom);
                        }
                        updateUI();
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
        tvCountAll = findViewById(R.id.tvCountAll);
        tvCountOccupied = findViewById(R.id.tvCountOccupied);
        tvCountAvailable = findViewById(R.id.tvCountAvailable);
        btnAll = findViewById(R.id.btnAll);
        btnOccupied = findViewById(R.id.btnOccupied);
        btnAvailable = findViewById(R.id.btnAvailable);

        allRooms = new ArrayList<>();
        allRooms.add(new Room("R001", "Phòng 101", 1500000, false, "", ""));
        allRooms.add(new Room("R002", "Phòng 102", 2000000, true, "Nguyễn Văn A", "0987654321"));

        displayList = new ArrayList<>(allRooms);
        adapter = new RoomAdapter(displayList, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        updateDashboard();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditRoomActivity.class);
            addRoomLauncher.launch(intent);
        });

        btnAll.setOnClickListener(v -> {
            currentFilter = "ALL";
            updateUI();
        });

        btnOccupied.setOnClickListener(v -> {
            currentFilter = "OCCUPIED";
            updateUI();
        });

        btnAvailable.setOnClickListener(v -> {
            currentFilter = "AVAILABLE";
            updateUI();
        });
    }

    private void updateUI() {
        displayList.clear();
        if (currentFilter.equals("ALL")) {
            displayList.addAll(allRooms);
        } else if (currentFilter.equals("OCCUPIED")) {
            for (Room r : allRooms) if (r.isOccupied()) displayList.add(r);
        } else {
            for (Room r : allRooms) if (!r.isOccupied()) displayList.add(r);
        }
        adapter.notifyDataSetChanged();
        updateDashboard();
    }

    private void updateDashboard() {
        int total = allRooms.size();
        int occupied = 0;
        for (Room r : allRooms) if (r.isOccupied()) occupied++;
        int available = total - occupied;

        tvCountAll.setText(String.valueOf(total));
        tvCountOccupied.setText(String.valueOf(occupied));
        tvCountAvailable.setText(String.valueOf(available));
    }

    private String generateNextId() {
        int maxId = 0;
        for (Room room : allRooms) {
            try {
                int idNum = Integer.parseInt(room.getId().substring(1));
                if (idNum > maxId) maxId = idNum;
            } catch (Exception ignored) {}
        }
        return String.format("R%03d", maxId + 1);
    }

    @Override
    public void onItemClick(Room room) {
        selectedPosition = displayList.indexOf(room);
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
                    allRooms.remove(room);
                    updateUI();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
