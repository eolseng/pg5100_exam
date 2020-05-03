// Based on: https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/test/java/org/tsdes/intro/exercises/quizgame/backend/service/ResetService.java

package no.kristiania.pg5100_exam.backend.service;

import no.kristiania.pg5100_exam.backend.entity.PlaceholderItem;
import no.kristiania.pg5100_exam.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
@Transactional
public class ResetService {

    @Autowired
    private EntityManager em;

    private Class<?>[] entityList = new Class[]{
            User.class,
            PlaceholderItem.class
    };

    public void resetDatabase() {

        Query query = em.createNativeQuery("" +
                "DELETE FROM user_roles;" +
                "DELETE FROM transactions");
        query.executeUpdate();

        for (Class<?> entity : entityList) {
            deleteEntities(entity);
        }
    }

    private void deleteEntities(Class<?> entity) {

        if (entity == null || entity.getAnnotation(Entity.class) == null) {
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        /*
            Class<?> is passed as input to avoid SQL Injection.
            JPA does not allow parameterized DELETE FROM queries like "DELETE FROM :className",
            and this was used in the course.
            This is not a perfect way, but the code is only used in tests so it should be OK.
         */
        String className = entity.getSimpleName();
        Query query = em.createQuery("DELETE FROM " + className);
        query.executeUpdate();

    }

}
