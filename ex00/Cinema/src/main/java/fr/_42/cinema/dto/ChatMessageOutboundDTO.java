package fr._42.cinema.dto;

public class ChatMessageOutboundDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String content;
    private String sentAt;

    public ChatMessageOutboundDTO() {
    }

    public ChatMessageOutboundDTO(Long userId, String firstName, String lastName, String content, String sentAt) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}