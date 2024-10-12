package com.example.hostal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public ItemAdapter(List<Item> itemList, OnItemLongClickListener onItemLongClickListener) {
        this.itemList = itemList;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textView.setText(item.getText());
        Picasso.get().load(item.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem);
            textView = itemView.findViewById(R.id.textViewItem);

            itemView.setOnLongClickListener(v -> {
                if (onItemLongClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.onItemLongClick(position);
                        return true;
                    }
                }
                return false;
            });
        }
    }
}
