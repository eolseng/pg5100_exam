package no.kristiania.pg5100_exam.backend.repository;

import no.kristiania.pg5100_exam.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Boolean existsByName(String name);

}
