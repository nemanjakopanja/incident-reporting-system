package org.unibl.etf.pisio.moderator_service.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ModerationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long incidentId;
    private String moderatorUsername;
    @Enumerated(EnumType.STRING)
    private ModerationDecision decision;
    private LocalDateTime createdAt;

    public ModerationAction() {
    }

    public ModerationAction(Long incidentId, String moderatorUsername, ModerationDecision decision, LocalDateTime createdAt) {
        this.incidentId = incidentId;
        this.moderatorUsername = moderatorUsername;
        this.decision = decision;
        this.createdAt = createdAt;
    }

    public ModerationAction(Long id, Long incidentId, String moderatorUsername, ModerationDecision decision, LocalDateTime createdAt) {
        this.id = id;
        this.incidentId = incidentId;
        this.moderatorUsername = moderatorUsername;
        this.decision = decision;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Long incidentId) {
        this.incidentId = incidentId;
    }

    public String getModeratorUsername() {
        return moderatorUsername;
    }

    public void setModeratorUsername(String moderatorUsername) {
        this.moderatorUsername = moderatorUsername;
    }

    public ModerationDecision getDecision() {
        return decision;
    }

    public void setDecision(ModerationDecision decision) {
        this.decision = decision;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
