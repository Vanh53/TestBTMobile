package com.example.myapplication3.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication3.R;
import com.example.myapplication3.model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Room room);
        void onItemLongClick(Room room);
    }

    public RoomAdapter(List<Room> roomList, OnItemClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        holder.tvRoomPrice.setText("Giá: " + room.getPrice() + " VNĐ");
        
        if (room.isOccupied()) {
            holder.tvRoomStatus.setText("Tình trạng: Đã thuê");
            holder.tvRoomStatus.setTextColor(Color.RED);
        } else {
            holder.tvRoomStatus.setText("Tình trạng: Còn trống");
            holder.tvRoomStatus.setTextColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(room));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(room);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvRoomPrice, tvRoomStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomStatus = itemView.findViewById(R.id.tvRoomStatus);
        }
    }
}
