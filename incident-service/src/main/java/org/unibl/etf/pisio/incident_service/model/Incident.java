package org.unibl.etf.pisio.incident_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    private IncidentType type;
    @Column(length = 2000)
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    private LocalDateTime createdAt;

    public Incident() {
    }

    public Incident(Double latitude, Double longitude, IncidentType type, String description, String imageUrl, IncidentStatus status, LocalDateTime createdAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Incident(Integer id, Double latitude, Double longitude, IncidentType type, String description, String imageUrl, IncidentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
