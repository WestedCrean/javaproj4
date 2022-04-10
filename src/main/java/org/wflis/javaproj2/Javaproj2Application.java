package org.wflis.javaproj2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wflis.javaproj2.dao.userDao;
import org.wflis.javaproj2.entity.User;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Javaproj2Application {
	@Autowired
	private userDao dao;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Javaproj2Application.class, args);
	}

	@PostConstruct
	public void init(){
		dao.save(new User("Wiktor", "Flis",
			"root",passwordEncoder.encode("password")));
	}

}
