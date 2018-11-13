package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Role;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void saveUser(User user) throws Exception {

		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			throw new Exception("User must have at least a role set!");
		}

		for (Role role : user.getRoles()) {
			if (!role.getName().startsWith("ROLE_"))
				role.setName("ROLE_" + role.getName());
			
			System.out.println(role);

			if (role.getUser() == null)
				role.setUser(user);
		}

		userRepository.save(user);
	}

	@Override
	public User findUser(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}
}
