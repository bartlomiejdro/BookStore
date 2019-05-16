package pl.demo.service;

import pl.demo.to.UserTo;

public interface UserService {

	UserTo findUserByName(String name);
}
