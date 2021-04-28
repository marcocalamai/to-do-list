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

class ToDoListE2E {
	
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	
	private static String baseUrl = "http://localhost:" + port;
	
	private WebDriver webDriver;
			
	@BeforeAll
	static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}
	
	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port;
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
		
		webDriver.findElement(By.linkText("Register")).click();;
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("passwordConfirmation")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("Login")).click();;
		webDriver.findElement(By.name("username")).sendKeys("username1");
		webDriver.findElement(By.name("password")).sendKeys("password1");
		webDriver.findElement(By.name("btn_submit")).click();
		
		webDriver.findElement(By.linkText("New ToDo")).click();
		webDriver.findElement(By.name("title")).sendKeys("newTitle");
		webDriver.findElement(By.name("description")).sendKeys("newDescription");
		webDriver.findElement(By.name("deadline")).sendKeys(LocalDate.now().toString());
		webDriver.findElement(By.name("btn_createToDo")).click();
		
		assertThat(webDriver.findElement(By.id("toDo_table")).getText())
		.contains("username1", "newTitle",  "newDescription", LocalDate.now().toString());
	}
	

}
