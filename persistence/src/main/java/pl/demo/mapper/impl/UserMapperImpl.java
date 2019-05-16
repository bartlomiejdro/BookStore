package pl.demo.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import pl.demo.entity.User;
import pl.demo.mapper.UserMapper;
import pl.demo.to.UserTo;

import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserTo map2To(User user) {
        if (user != null) {
            return new UserTo(user.getId(), user.getUserName(), user.getPassword());
        }
        return null;
    }

    @Override
    public User map(UserTo userTo) {
        if (userTo != null) {
            return new User(userTo.getId(), userTo.getUserName(), userTo.getPassword());
        }
        return null;
    }

    @Override
    public List<UserTo> map2To(List<User> userEntities) {
        return userEntities.stream().map(this::map2To).collect(Collectors.toList());
    }

    @Override
    public List<User> map2Entity(List<UserTo> userEntities) {
        return userEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
