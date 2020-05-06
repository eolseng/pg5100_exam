package no.kristiania.pg5100_exam.backend.repository;

import no.kristiania.pg5100_exam.backend.entity.Copy;
import no.kristiania.pg5100_exam.backend.entity.Item;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    @Query("SELECT t FROM Copy t WHERE t.user.username = ?1")
    List<Copy> findAllByUsername(String username);

    @Query("SELECT t FROM Copy t WHERE t.item.id = ?1")
    List<Copy> findAllByItemId(Long itemId);

    Boolean existsByUserAndItem(User user, Item item);

    Copy findFirstByUserAndItem(User user, Item item);

}
