package org.unibl.etf.pisio.moderator_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.pisio.moderator_service.model.ModerationAction;

@Repository
public interface ModeratorRepository extends JpaRepository<ModerationAction, Long> {
}
