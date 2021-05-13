package org.marco.calamai.todolist;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.github.bonigarcia.wdm.WebDriverManager;

@DisplayName("E2E tests")
class ToDoListE2ETest {
	
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	
	private static String baseUrl = "http://localhost:" + port;
	
	private WebDriver webDriver;
			
	@BeforeAll
	static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}
	
	@BeforeEach
	void setup() {
		webDriver = new FirefoxDriver();
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
		mongoClient.getDatabase("ToDoList-MongoDb").drop();
	}
	
	@AfterEach
	void teardown() {
		webDriver.quit();
	}
	
	@Test @DisplayName("Test insert new ToDo")
	void testInsertNewToDo() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("newTitle");
		webDriver.findElement(By.name("description")).sendKeys("newDescription");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "newTitle",  "newDescription", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test insert then edit ToDo")
	void testInsertThenEditToDo() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("newTitle");
		webDriver.findElement(By.name("description")).sendKeys("newDescription");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All my ToDo")).click();
		webDriver.findElement(By.linkText("Edit")).click();
		
		webDriver.findElement(By.name("title")).clear();
		webDriver.findElement(By.name("title")).sendKeys("editTitle");
		webDriver.findElement(By.name("description")).clear();
		webDriver.findElement(By.name("description")).sendKeys("editDescription");
		webDriver.findElement(By.name("done")).click();
		webDriver.findElement(By.name("deadline")).clear();  
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().plusDays(1).toString());
		webDriver.findElement(By.name("btn_updateToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "editTitle",  "editDescription", "true", LocalDate.now().plusDays(1).toString());
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.doesNotContain("newTitle",  "newDescription", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test insert then delete ToDo")
	void testInsertThenDeleteToDo() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("newTitle");
		webDriver.findElement(By.name("description")).sendKeys("newDescription");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All my ToDo")).click();
		webDriver.findElement(By.name("btn_deleteToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		assertThat(webDriver.findElement(By.id("message")).getText()).contains("There are no to do");
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.doesNotContain("username1", "newTitle",  "newDescription", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test insert two ToDo then search ToDo by title")
	void testInsertThenSearchToDoByTitle() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("Title1");
		webDriver.findElement(By.name("description")).sendKeys("Description1");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("Title2");
		webDriver.findElement(By.name("description")).sendKeys("Description2");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().plusDays(1).toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		//verify that there are two ToDo inserted
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "Title1", "Description1", "false", LocalDate.now().toString(),
				 "Title2", "Description2", LocalDate.now().plusDays(1).toString());
		
		webDriver.findElement(By.name("title")).sendKeys("Title2");
		webDriver.findElement(By.name("btn_searchByTitle")).click();
		
		//verify that table contains ToDo with Title2 but does not contains ToDo with Title1
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "Title2", "Description2", "false", LocalDate.now().plusDays(1).toString());
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.doesNotContain("Title1", "Description1", LocalDate.now().toString());
	}
	
	@Test @DisplayName("Test insert two ToDo then search ToDo by deadline")
	void testInsertThenSearchToDoByDeadline() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("Title1");
		webDriver.findElement(By.name("description")).sendKeys("Description1");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("Title2");
		webDriver.findElement(By.name("description")).sendKeys("Description2");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().plusDays(1).toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		//verify that there are two ToDo inserted
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "Title1", "Description1", "false", LocalDate.now().toString(),
				 "Title2", "Description2", LocalDate.now().plusDays(1).toString());
		
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_searchByDeadline")).click();
		
		//verify that table contains ToDo with date of today as deadline but does not contains 
		//ToDo with date of tomorrow as deadline
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "Title1", "Description1", "false", LocalDate.now().toString());
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.doesNotContain("Title2", "Description2", LocalDate.now().plusDays(1).toString());
	}
	
	
	
	@Test @DisplayName("Test user insert ToDo then another user insert another ToDo then show All My ToDo")
	void testUserInsertToDoThenAnotherUserInsertAnotherToDoThenShowAllMyToDo() throws InterruptedException {
		//User1 register, login, insert new ToDo and logout
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("User1Title");
		webDriver.findElement(By.name("description")).sendKeys("User1Description");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.name("btn_logout")).click();

		//User2 register, login and insert new ToDo
		webDriver.findElement(By.linkText("Register")).click();
		webDriver.findElement(By.name("username")).sendKeys("username2");
		webDriver.findElement(By.name("password")).sendKeys("password2");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password2");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();
		webDriver.findElement(By.name("username")).sendKeys("username2");
		webDriver.findElement(By.name("password")).sendKeys("password2");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("User2Title");
		webDriver.findElement(By.name("description")).sendKeys("User2Description");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().plusDays(1).toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		webDriver.findElement(By.linkText("All ToDo")).click();
		
		//verify that there are two ToDo inserted
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "User1Title", "User1Description", "false", LocalDate.now().toString(),
				"username2", "User2Title", "false", "User2Description", LocalDate.now().plusDays(1).toString());
		
		webDriver.findElement(By.linkText("All my ToDo")).click();
		
		//verify that All my ToDo show only one ToDo associated with User2
		assertThat(webDriver.findElement(By.id("myToDo_table")).getText())
		.contains("username2", "User2Title",  "User2Description", "false", LocalDate.now().plusDays(1).toString());
		
		assertThat(webDriver.findElement(By.id("myToDo_table")).getText())
		.doesNotContain("username1", "User1Title", "User1Description", LocalDate.now().toString());
	}
}
