package no.kristiania.pg5100_exam.backend.repository;

import no.kristiania.pg5100_exam.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Boolean existsByName(String name);

}
