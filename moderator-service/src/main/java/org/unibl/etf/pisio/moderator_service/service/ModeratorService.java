package org.unibl.etf.pisio.moderator_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.unibl.etf.pisio.moderator_service.model.ModerationAction;
import org.unibl.etf.pisio.moderator_service.model.ModerationActionRequest;
import org.unibl.etf.pisio.moderator_service.model.ModerationDecision;
import org.unibl.etf.pisio.moderator_service.repository.ModeratorRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository, WebClient.Builder webClientBuilder) {
        this.moderatorRepository = moderatorRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Boolean> updateIncident(Long incidentId, ModerationActionRequest moderationActionRequest) {
        System.out.println("MODERATOR SERVICE");
        ModerationAction moderationAction = new ModerationAction(incidentId, moderationActionRequest.getModeratorUsername(), ModerationDecision.valueOf(moderationActionRequest.getIncident().getStatus()), LocalDateTime.now());
        moderatorRepository.save(moderationAction);

        System.out.println("MODERATOR SERVICE - ModerationAction saved");

        return webClientBuilder.build().put()
                .uri("http://incident-service/api/incidents/update")
                .bodyValue(moderationActionRequest.getIncident())
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
