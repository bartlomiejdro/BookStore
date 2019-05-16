package pl.demo.repository;


import pl.demo.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select user from User user where upper(user.userName) like concat(upper(:name), '%')")
	User findUserByName(@Param("name") String name);

}
