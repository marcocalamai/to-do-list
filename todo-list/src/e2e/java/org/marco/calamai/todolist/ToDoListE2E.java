package org.marco.calamai.todolist;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class ToDoListE2E {
	
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	
	private static String baseUrl = "http://localhost:" + port;
	
	private WebDriver webDriver;
			
	@BeforeAll
	public static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}
	
	@BeforeEach
	public void setup() {
		baseUrl = "http://localhost:" + port;
		webDriver = new FirefoxDriver();
	}
	
	@AfterEach
	void teardown() {
		webDriver.quit();
	}
	
	@Test @DisplayName("Test insert new ToDo")
	void testInsertNewToDo() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.linkText("Register"));
	}
	

}
