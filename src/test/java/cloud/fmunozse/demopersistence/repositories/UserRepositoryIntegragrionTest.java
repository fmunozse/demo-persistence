package cloud.fmunozse.demopersistence.repositories;


import cloud.fmunozse.demopersistence.model.User;
import cloud.fmunozse.demopersistence.repositories.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public class UserRepositoryIntegragrionTest {
    @Autowired
    private UserRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSaveUser() throws SQLException {
        User u = new User();
        u.setName("TEST");

        repository.save(u);

        entityManager.flush();
        assertThat(jdbcTemplate.queryForObject("SELECT count(*) FROM t_user", Long.class)).isEqualTo(1L);
    }


    @Test
    public void test_iteration_User_jpa() {
        Instant start = Instant.now();
        log.info("Init {}", dumpMemory());
        Iterable<User> iter = repository.findAll();
        /*
        AtomicInteger count = new AtomicInteger(0);
        iter.forEach(user -> {
                    if ( count.incrementAndGet() % 1000 == 0){
                        log.info("{} - user {} ", dumpMemory(), user);
                    }
                }
        );
        */
        Duration timeElapsed = Duration.between(start, Instant.now());
        log.info("END time:{} - {}", prettyDuration(timeElapsed),  dumpMemory());
    }

    @Test
    public void test_iteration_User_em () {

        log.info("Init {}", dumpMemory());
        Instant start = Instant.now();
        List<User> list = entityManager.createQuery("SELECT user FROM User user").getResultList();
        Duration timeElapsed = Duration.between(start, Instant.now());
        log.info("END time:{} - {}",  prettyDuration(timeElapsed),  dumpMemory());
    }


    @Test
    public void test_iteration_UserDTO_em () {
        log.info("Init {}", dumpMemory());
        Instant start = Instant.now();
        List<UserDTO> list = entityManager.createQuery("SELECT " +
                "new cloud.fmunozse.demopersistence.repositories.dto.UserDTO(u.id,u.name) " +
                " FROM User u")
                .getResultList();
        Duration timeElapsed = Duration.between(start, Instant.now());
        log.info("END time:{} - {}",  prettyDuration(timeElapsed),  dumpMemory());
    }



    private String dumpMemory() {
        return String.format("Memory %s /%s /%s (free/used/total)",
                humanReadableByteCount(Runtime.getRuntime().freeMemory(), false),
                humanReadableByteCount(Runtime.getRuntime().totalMemory(), false),
                humanReadableByteCount(Runtime.getRuntime().maxMemory(), false)
        );
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private String prettyDuration (Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }


}
