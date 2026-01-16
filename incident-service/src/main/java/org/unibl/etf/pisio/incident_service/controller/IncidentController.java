package org.unibl.etf.pisio.incident_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.pisio.incident_service.model.*;
import org.unibl.etf.pisio.incident_service.service.IncidentService;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        System.out.println("INCIDENT CONTROLLER - getAllIncidents()");
        List<Incident> incidents = incidentService.getAllIncidents();
        System.out.println("IncidentController: " + incidents);
        return ResponseEntity.ok(incidents);
    }

    @PostMapping("/report")
    public ResponseEntity<Boolean> reportNewIncident(@RequestBody IncidentDto incidentDto) throws MalformedURLException {
        Incident incidentReported = incidentService.reportNewIncident(incidentDto);
        if (incidentReported != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateIncident(@RequestBody UpdateIncidentRequest incidentToUpdate) {

        System.out.println("INCIDENT CONTROLLER");
        System.out.println("incidentToUpdate: " + incidentToUpdate);

        Incident updatedIncident = incidentService.updateIncident(incidentToUpdate);
        if (updatedIncident != null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
