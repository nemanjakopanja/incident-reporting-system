package org.unibl.etf.pisio.moderator_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.pisio.moderator_service.model.ModerationActionRequest;
import org.unibl.etf.pisio.moderator_service.service.ModeratorService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PutMapping("/update/{id}")
    public Mono<Boolean> updateIncident(@PathVariable(name = "id") Long incidentId, @RequestBody ModerationActionRequest moderationActionRequest) {
        System.out.println("MODERATOR CONTROLLER");
        return this.moderatorService.updateIncident(incidentId, moderationActionRequest);
    }
}
