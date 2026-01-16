package org.unibl.etf.pisio.incident_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.pisio.incident_service.model.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
}
