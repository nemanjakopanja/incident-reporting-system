package org.unibl.etf.pisio.moderator_service.model;

public class ModerationActionRequest {

    private Incident incident;
    private String moderatorUsername;

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public String getModeratorUsername() {
        return moderatorUsername;
    }

    public void setModeratorUsername(String moderatorUsername) {
        this.moderatorUsername = moderatorUsername;
    }
}
