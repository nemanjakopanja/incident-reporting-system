package org.unibl.etf.pisio.incident_service.model;

public enum IncidentType {
    SAOBRACAJNA_NESRECA("Saobraćajna nesreća"),
    RADOVI_NA_PUTU("Radovi na putu"),
    ODRON_NA_PUTU("Odron na putu"),
    POZAR("Požar"),
    POPLAVA("Poplava");

    private final String displayName;

    IncidentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
