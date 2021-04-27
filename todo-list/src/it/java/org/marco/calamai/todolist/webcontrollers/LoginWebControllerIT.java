package org.marco.calamai.todolist.webcontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@DisplayName("IT login web controller")
class LoginWebControllerIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private UserMongoRepository userMongoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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

	
	@Test @DisplayName("Test successful login")
	void testLogin() throws Exception {
		userMongoRepository.save(new User("username1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertEquals("ToDo Manager", webDriver.getTitle());
	}

}
