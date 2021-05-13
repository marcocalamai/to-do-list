package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

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
		webDriver.findElement(By.xpath("//form[@action='/toDoManager/deleteToDo/" + toDoSaved.getId() + "']"));
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
	
	@Test @DisplayName("Test show toDo by deadline")
	void testToDoManagerViewShowToDoByDeadline() throws Exception {
		toDoMongoRepository.save(new ToDo("username_1", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_searchByDeadline")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username_1", "title_1",  "description_1", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test newToDoPage insert new toDo")
	void testNewToDoPageInsertNewToDo() throws Exception {
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.get(baseUrl + "/toDoManager/newToDo");
		webDriver.findElement(By.name("title")).sendKeys("newTitle");
		webDriver.findElement(By.name("description")).sendKeys("newDescription");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username_1", "newTitle",  "newDescription", LocalDate.now().toString());
		
		ToDo result = toDoMongoRepository.findAll().get(0);
		assertEquals("username_1", result.getUser());
		assertEquals("newTitle", result.getTitle());
		assertEquals("newDescription", result.getDescription());
		assertFalse(result.isDone());
		assertEquals(LocalDate.now(), result.getDeadline());
	}
	
	@Test @DisplayName("Test editToDoPage edit toDo")
	void testEditToDoPageEditToDo() throws Exception {
		ToDo toDoSaved = toDoMongoRepository.save(new ToDo("username_1", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.get(baseUrl + "/toDoManager/AllMyToDo");
		webDriver.get(baseUrl + "/toDoManager/editToDo/" + toDoSaved.getId());
		
		webDriver.findElement(By.name("title")).clear();
		webDriver.findElement(By.name("title")).sendKeys("editTitle");
		webDriver.findElement(By.name("description")).clear();
		webDriver.findElement(By.name("description")).sendKeys("editDescription");
		webDriver.findElement(By.name("done")).click();
		webDriver.findElement(By.name("deadline")).clear();  
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().plusDays(1).toString());
		webDriver.findElement(By.name("btn_updateToDo")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username_1", "editTitle",  "editDescription", "true", LocalDate.now().plusDays(1).toString());
		
		ToDo result = toDoMongoRepository.findAll().get(0);
		assertEquals("username_1", result.getUser());
		assertEquals("editTitle", result.getTitle());
		assertEquals("editDescription", result.getDescription());
		assertTrue(result.isDone());
		assertEquals(LocalDate.now().plusDays(1), result.getDeadline());
	}
	
	@Test @DisplayName("Test delete toDo")
	void testDeleteToDo() throws Exception {
		ToDo toDoSaved = toDoMongoRepository.save(new ToDo("username_1", "title_1", "description_1", LocalDate.now()));
		userMongoRepository.save(new User("username_1", passwordEncoder.encode("password1"))) ;
		webDriver.get(baseUrl + "/login");
		webDriver.findElement(By.name("username")).sendKeys("username_1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.get(baseUrl + "/toDoManager/AllMyToDo");
		assertEquals(1, toDoMongoRepository.count()); 
		WebElement form = webDriver.findElement(By.xpath("//form[@action='/toDoManager/deleteToDo/" + toDoSaved.getId() + "']"));
		form.findElement(By.name("btn_deleteToDo")).click();
		assertEquals(0, toDoMongoRepository.count()); 
	}
}
