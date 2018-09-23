package cloud.fmunozse.demopersistence.repositories;

import cloud.fmunozse.demopersistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testStoreUser() {
        User u = new User();
        u.setName("paco");

        User u2 = userRepository.save(u);

        assertThat(u2).isNotNull();
        assertThat(userRepository.count()).isEqualTo(1);

    }

    @Test
    public void test_iteration_User() {

        Iterable<User> iter = userRepository.findAll();

        iter.forEach(user -> {
                    log.info("user {}", user);
                }
        );
    }

}
