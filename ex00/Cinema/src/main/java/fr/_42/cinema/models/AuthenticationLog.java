package fr._42.cinema.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "authentication_log")
public class AuthenticationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "authenticated_at", nullable = false)
    private LocalDateTime authenticatedAt;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    public AuthenticationLog() {
    }

    public AuthenticationLog(User user, LocalDateTime authenticatedAt, String ipAddress) {
        this.user = user;
        this.authenticatedAt = authenticatedAt;
        this.ipAddress = ipAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getAuthenticatedAt() {
        return authenticatedAt;
    }

    public void setAuthenticatedAt(LocalDateTime authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}