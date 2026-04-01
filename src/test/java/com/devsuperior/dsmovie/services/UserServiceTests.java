package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.TokenUtil;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	//@Autowired
	//private TokenUtil tokenUtil;

	//@Autowired
	//private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private CustomUserUtil userUtil;

	@Mock
	private UserRepository repository;

	private String existingUsername, nonExistingUsername;
	//private String adminToken;
	private UserEntity admin, selfClient, otherClient;

	private UserEntity user;

	@BeforeEach
	void setUp() throws Exception {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "user@gmail.com";

		user = UserFactory.createCustomClientUser(1L, existingUsername);

		Mockito.when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		UserService spyUserService = Mockito.spy(service);
		Mockito.doReturn(user).when(spyUserService).authenticated();

		UserEntity result = spyUserService.getMe();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getName(), existingUsername);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserService spyUserService = Mockito.spy(service);
		Mockito.doReturn(admin).when(spyUserService).authenticated();
		UserDetails result = spyUserService.loadUserByUsername(existingUsername);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUsername);

	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		UserService spyUserService = Mockito.spy(service);
		Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			UserDetails result = spyUserService.loadUserByUsername(nonExistingUsername);
		});
	}
}
