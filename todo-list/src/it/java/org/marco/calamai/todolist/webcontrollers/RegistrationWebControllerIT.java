package org.marco.calamai.todolist.webcontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@DisplayName("IT registration web controller")
class RegistrationWebControllerIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private UserMongoRepository userMongoRepository;
	
	private WebDriver webDriver;
	
	private String baseUrl;
	
	
	@BeforeEach
	void setup() {
	baseUrl = "http://localhost:" + port;
	webDriver = new HtmlUnitDriver();
	userMongoRepository.deleteAll();
	}
	
	@AfterEach
	void tearDown() {
		userMongoRepository.deleteAll();
		webDriver.quit();
	}
	
	@Test @DisplayName("Test successful user registration")
	void testRegistration() throws Exception{
		webDriver.get(baseUrl + "/registration");
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertEquals("ToDo List", webDriver.getTitle());
		assertEquals(1, userMongoRepository.count());	
	}

}
