package pl.demo.service.impl;

import pl.demo.entity.User;
import pl.demo.mapper.UserMapper;
import pl.demo.repository.UserRepository;
import pl.demo.service.UserService;
import pl.demo.to.UserTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserTo findUserByName(String name) {
        User entity = userRepository.findUserByName(name);
        return userMapper.map2To(entity);
    }

}
