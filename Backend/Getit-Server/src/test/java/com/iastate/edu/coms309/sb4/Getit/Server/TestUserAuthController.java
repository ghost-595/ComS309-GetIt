//package com.iastate.edu.coms309.sb4.Getit.Server;
//
//import static org.mockito.Mockito.mock;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
//
//@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
//public class TestUserAuthController {
//
//	@InjectMocks
//	com.iastate.edu.coms309.sb4.Getit.Server.controller.userAuthController userAuthController;
//
//	@Mock
//	User user;
//
//	@Test
//	public void sample() {
//
//		User user = mock(User.class);
//
//		Mockito.when(user.getName()).thenReturn("Max");
//		String testName = user.getName();
//		Assert.assertEquals("Max", testName);
//	}
//
//}
