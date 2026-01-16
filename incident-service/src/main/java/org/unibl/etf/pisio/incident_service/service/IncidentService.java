package org.unibl.etf.pisio.incident_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.pisio.incident_service.model.*;
import org.unibl.etf.pisio.incident_service.repository.IncidentRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident reportNewIncident(IncidentDto incidentDto) throws MalformedURLException {
        IncidentType incidentType = IncidentType.valueOf(incidentDto.getType());
        String fileName;
        if (!"N/A".equals(incidentDto.getImageUrl())) {
            URL url = new URL(incidentDto.getImageUrl());
            String path = url.getPath();
            fileName = path.substring(path.lastIndexOf("/") + 1);
        } else {
            fileName = "N/A";
        }
        IncidentStatus incidentStatus = IncidentStatus.WAITING;
        LocalDateTime createrdAt = LocalDateTime.now();

        Incident incident = new Incident(incidentDto.getLatitude(), incidentDto.getLongitude(), incidentType,
                                        incidentDto.getDescription(), fileName, incidentStatus, createrdAt);

        return incidentRepository.save(incident);
    }

    public Incident updateIncident(UpdateIncidentRequest incidentToUpdate) {

        System.out.println("INCIDENT SERVICE");
        System.out.println("incidentToUpdate: " + incidentToUpdate);

        //IncidentType incidentType = IncidentType.valueOf(incidentToUpdate.getType());
        IncidentStatus incidentStatus = IncidentStatus.valueOf(incidentToUpdate.getStatus());

        Incident incident = incidentRepository.findById(incidentToUpdate.getId()).orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(incidentStatus);

        System.out.println("INCIDENT SERVICE");
        System.out.println("newIncident: " + incident);

        return incidentRepository.save(incident);
    }
}
