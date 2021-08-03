package fr.pierre.apirest.services;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.pierre.apirest.entities.User;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Test
	@Transactional
	@Rollback
	public void saveUpdateDeleteProcessUser() {
		User user = new User();
		
		user.setEmail("test@user.com");
		user.setUsername("usernametest");
		user.setPassword("passwordtest");
		
		User userReturn = userService.save(user);

		Assert.assertEquals("test@user.com", userReturn.getEmail());
		Assert.assertEquals("usernametest", userReturn.getUsername());

		userReturn.setEmail("test2@user.com");
		userReturn.setUsername("usernametest2");
		
		User userUpdate = userService.updateUser(userReturn);
		
		Assert.assertEquals("test2@user.com", userUpdate.getEmail());
		Assert.assertEquals("usernametest2", userUpdate.getUsername());
		
		userService.deleteById(userUpdate.getId());
	}
}
