package pl.demo.mapper;

import java.util.List;

import pl.demo.entity.User;
import pl.demo.to.UserTo;

public interface UserMapper {

    UserTo map2To(User user);

    User map(UserTo userTo);

    List<UserTo> map2To(List<User> userEntities);

    List<User> map2Entity(List<UserTo> userEntities);
}
