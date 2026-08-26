package fr._42.cinema.dto;

public class LoginHistoryEntry {

    private final String date;
    private final String time;
    private final String ipAddress;

    public LoginHistoryEntry(String date, String time, String ipAddress) {
        this.date = date;
        this.time = time;
        this.ipAddress = ipAddress;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}