package org.wflis.javaproj2.dao;

import org.wflis.javaproj2.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface userDao extends CrudRepository<User, Integer> {
    public User findByLogin(String login);
}
