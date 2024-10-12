package com.example.hostal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.QRCodeViewHolder> {

    private List<QRCodeData> qrCodeList;

    public QRCodeAdapter(List<QRCodeData> qrCodeList) {
        this.qrCodeList = qrCodeList;
    }

    @NonNull
    @Override
    public QRCodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qr_code, parent, false);
        return new QRCodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCodeViewHolder holder, int position) {
        QRCodeData qrCodeData = qrCodeList.get(position);
        holder.bind(qrCodeData);
    }

    @Override
    public int getItemCount() {
        return qrCodeList.size();
    }

    public static class QRCodeViewHolder extends RecyclerView.ViewHolder {

        private TextView contentTextView;
        private TextView timestampTextView;

        public QRCodeViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }

        public void bind(QRCodeData qrCodeData) {
            contentTextView.setText("Content: " + qrCodeData.getContent());
            timestampTextView.setText("Timestamp: " + qrCodeData.getTimestamp());
        }
    }
}
