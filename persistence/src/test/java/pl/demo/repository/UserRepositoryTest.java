package pl.demo.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import pl.demo.PersistenceModuleConfig;
import pl.demo.entity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceModuleConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testShouldFindUsersByName() {
        // given
        final String userName = "First User";
        // when
        User userEntity = userRepository.findUserByName(userName);
        // then
        assertNotNull(userEntity);
        assertEquals(userName, userEntity.getUserName());
    }
}
