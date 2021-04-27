package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@DisplayName("IT ToDoManager web controller")
class ToDoManagerWebControllerIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private ToDoMongoRepository toDoMongoRepository;
	
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
	toDoMongoRepository.deleteAll();
	}
	
	@AfterEach
	void tearDown() {
		userMongoRepository.deleteAll();
		toDoMongoRepository.deleteAll();
		webDriver.quit();
	}
	
	@Test @DisplayName("Test ToDoManager show all todo")
	void testToDoManagerViewShowAllToDo() throws Exception {
		toDoMongoRepository.save(new ToDo("username_2", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username_2", "title_1",  "description_1", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test ToDoManager show all my todo")
	void testToDoManagerViewShowAllMyToDo() throws Exception {
		ToDo toDoSaved = toDoMongoRepository.save(new ToDo("username_1", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		webDriver.get(baseUrl + "/toDoManager/AllMyToDo");
		
		assertThat(webDriver.findElement(By.id("myToDo_table")).getText())
		.contains("username_1", "title_1",  "description_1", LocalDate.now().toString());
		
		webDriver.findElement(By.cssSelector("a[href='/toDoManager/editToDo/" + toDoSaved.getId() + "']"));
		
		webDriver.findElement(By.name("btn_deleteToDo"));
	}
	
	@Test @DisplayName("Test show toDo by title")
	void testToDoManagerViewShowToDoByTitle() throws Exception {
		toDoMongoRepository.save(new ToDo("username_1", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.name("title")).sendKeys("title_1");
		webDriver.findElement(By.name("btn_searchByTitle")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username_1", "title_1",  "description_1", LocalDate.now().toString());
	}
}
