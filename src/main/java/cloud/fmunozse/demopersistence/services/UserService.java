package cloud.fmunozse.demopersistence.services;

import cloud.fmunozse.demopersistence.model.User;
import cloud.fmunozse.demopersistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class UserService {

    UserRepository userRepository;

    EntityManagerFactory entityManagerFactory;

    public UserService(UserRepository userRepository, EntityManagerFactory entityManagerFactory) {
        this.userRepository = userRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void insertMassive () {

        for(int i =0; i < 100 ; i ++) {
            User u = new User();
            u.setName("name-test " + i);
            u.setEmail("email-test " + i);
            userRepository.save(u);
            log.info("insert {}", u );
        }
    }


    public void insertMassiveEntityManagerWithPartialCommits() {

        int entityCount = 100000;
        int batchSize = 100;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            for (int i = 0; i < entityCount; i++) {
                if (i > 0 && i % batchSize == 0) {
                    entityTransaction.commit();
                    entityTransaction.begin();
                    entityManager.clear();
                    log.info("reset the context at {}",i);
                }

                User u = new User();
                u.setName(String.format("User %d", i + 1));
                entityManager.persist(u);
                //log.info("insert {}", u );

            }

            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

    }



    public void insertMassiveEntityManagerWithoutPartialCommits() {

        int entityCount = 50000;
        int batchSize = 25;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            for (int i = 0; i < entityCount; i++) {
                if (i > 0 && i % batchSize == 0) {
                    //entityTransaction.commit();
                    //entityTransaction.begin();
                    entityManager.flush();
                    entityManager.clear();
                    log.info("reset the context at {}", i);
                }

                User u = new User();
                u.setName(String.format("User %d", i + 1));
                entityManager.persist(u);
                //log.info("insert {}", u );

            }

            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

    }

}
