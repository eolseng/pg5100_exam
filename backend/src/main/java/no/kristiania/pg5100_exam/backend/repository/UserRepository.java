package no.kristiania.pg5100_exam.backend.repository;

import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByUsername(String name);

}
