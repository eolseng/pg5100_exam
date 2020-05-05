package no.kristiania.pg5100_exam.backend.repository;

import no.kristiania.pg5100_exam.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT t FROM Booking t WHERE t.user.username = ?1")
    List<Booking> findAllByUsername(String username);

    @Query("SELECT t FROM Booking t WHERE t.trip.id = ?1")
    List<Booking> findAllByItemId(Long itemId);

}
