package fr._42.cinema.dto;

public class UploadedFileInfo {

    private final String originalName;
    private final String storedName;
    private final String readableSize;
    private final String mimeType;

    public UploadedFileInfo(String originalName, String storedName, String readableSize, String mimeType) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.readableSize = readableSize;
        this.mimeType = mimeType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public String getReadableSize() {
        return readableSize;
    }

    public String getMimeType() {
        return mimeType;
    }
}