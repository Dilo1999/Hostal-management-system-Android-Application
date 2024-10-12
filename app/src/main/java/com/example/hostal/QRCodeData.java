package com.example.hostal;

public class QRCodeData {
    private String content;
    private String timestamp;

    public QRCodeData() {
        // Default constructor required for calls to DataSnapshot.getValue(QRCodeData.class)
    }

    public QRCodeData(String content, String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
