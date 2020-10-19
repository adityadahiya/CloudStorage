package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudStorageApplicationTests {

	private static String firstName = "firstName";
	private static String lastName = "lastName";
	private static String userName = "UserName";
	private static String password = "Password";
	private static String noteTitle = "Title for Note";
	private static String noteDescription = "Description for Note";
	private static String credURL = "https://www.google.co.in/";
	String newNoteTitle = "New title for Note";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	public void getUnauthorizedHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Test
	public void userSignupTest() {
		driver.get("http://localhost:" + this.port + "/signup");
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.sendKeys(firstName);
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.sendKeys(lastName);
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement signUpButton = driver.findElement(By.id("signup"));
		signUpButton.click();
	}

/*	@Test
	@Order(5)
	public void userLoginTest() {
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());
	}*/

	@Test
	@Order(4)
	public void addNoteTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebDriverWait wait = new WebDriverWait (driver, 5);
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click()", notesTab);
		wait.withTimeout(Duration.ofSeconds(30));
		WebElement newNote = driver.findElement(By.id("newnote"));
		wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
		WebElement notedescription = driver.findElement(By.id("note-description"));
		notedescription.sendKeys(noteDescription);
		WebElement savechanges = driver.findElement(By.id("saveNote"));
		savechanges.click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check for note
		driver.get("http://localhost:" + this.port + "/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		Boolean created = false;
		for (int i=0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(noteTitle)) {
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);
	}


	@Test
	@Order(5)
	public void updateNoteTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		WebDriverWait wait = new WebDriverWait (driver, 5);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement editElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			editElement = element.findElement(By.id("editNote"));
			if (editElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
		WebElement notetitle = driver.findElement(By.id("note-title"));
		wait.until(ExpectedConditions.elementToBeClickable(notetitle));
		notetitle.clear();
		notetitle.sendKeys(newNoteTitle);
		WebElement savechanges = driver.findElement(By.id("saveNote"));
		savechanges = driver.findElement(By.id("saveNote"));
		savechanges.click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check the updated note
		driver.get("http://localhost:" + this.port + "/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		notesTable = driver.findElement(By.id("userTable"));
		notesList = notesTable.findElements(By.tagName("th"));
		Boolean edited = false;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}

	@Test
	@Order(6)
	public void deleteNoteTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		WebDriverWait wait = new WebDriverWait (driver, 5);
		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			deleteElement = element.findElement(By.id("deleteNote"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check the deleted note
		driver.get("http://localhost:" + this.port + "/home");
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		jse.executeScript("arguments[0].click()", notesTab);
		notesTable = driver.findElement(By.id("userTable"));
		notesList = notesTable.findElements(By.tagName("td"));
		Boolean deleted = false;
		for (int i = 0; i < notesList.size(); i++) {
			WebElement element = notesList.get(i);
			if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
				deleted = true;
				break;
			}
		}
		Assertions.assertFalse(deleted);
	}

	@Test
	@Order(7)
	public void addCredentialTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebDriverWait wait = new WebDriverWait (driver, 5);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		wait.withTimeout(Duration.ofSeconds(30));
		WebElement newCredential = driver.findElement(By.id("newCredential"));
		wait.until(ExpectedConditions.elementToBeClickable(newCredential)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username"))).sendKeys(userName);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).sendKeys(password);

		WebElement savechanges = driver.findElement(By.id("saveCredential"));
		savechanges.click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check for credential
		driver.get("http://localhost:" + this.port + "/home");
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		WebElement credTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList = credTable.findElements(By.tagName("td"));
		Boolean created = false;
		for (int i=0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			if (element.getAttribute("innerHTML").equals(userName)) {
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);

		//check for encrypted password
		credList = credTable.findElements(By.tagName("td"));
		Boolean isEncrypted = true;
		for (int i=0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			if (element.getAttribute("innerHTML").equals(password)) {
				isEncrypted = false;
				break;
			}
		}
		Assertions.assertTrue(isEncrypted);

	}

	@Test
	@Order(8)
	public void updateCredentialTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebDriverWait wait = new WebDriverWait (driver, 5);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		WebElement credTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credList = credTable.findElements(By.tagName("td"));
		WebElement editElement = null;
		for (int i = 0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			editElement = element.findElement(By.id("editCredential"));
			if (editElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
		WebElement credUserName = driver.findElement(By.id("credential-username"));
		wait.until(ExpectedConditions.elementToBeClickable(credUserName));

		//check for decrypted password
		WebElement credUserPwd = driver.findElement(By.id("credential-password"));
		wait.until(ExpectedConditions.elementToBeClickable(credUserPwd));
		Assertions.assertEquals(password, credUserPwd.getAttribute("value"));
		String newUserName = "new user name";
		credUserName.clear();
		credUserName.sendKeys(newUserName);
		WebElement savechanges = driver.findElement(By.id("saveCredential"));
		savechanges.click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check the updated credential
		driver.get("http://localhost:" + this.port + "/home");
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		credTable = driver.findElement(By.id("credentialTable"));
		credList = credTable.findElements(By.tagName("td"));
		Boolean edited = false;
		for (int i = 0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			if (element.getAttribute("innerHTML").equals(newUserName)) {
				edited = true;
				break;
			}
		}
		Assertions.assertTrue(edited);
	}

	@Test
	@Order(9)
	public void deleteCredentialTest() {
		//login
		driver.get("http://localhost:" + this.port + "/login");
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.sendKeys(userName);
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.sendKeys(password);
		WebElement loginButton = driver.findElement(By.id("login"));
		loginButton.click();
		Assertions.assertEquals("Home", driver.getTitle());

		WebDriverWait wait = new WebDriverWait (driver, 5);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		WebElement credTable = driver.findElement(By.id("credentialTable"));
		List <WebElement>credList = credTable.findElements(By.tagName("td"));
		WebElement deleteElement = null;
		for (int i = 0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			deleteElement = element.findElement(By.id("deleteCredential"));
			if (deleteElement != null){
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
		Assertions.assertEquals("Home", driver.getTitle());

		//check the deleted credential
		String newUserName = "new user name";
		driver.get("http://localhost:" + this.port + "/home");
		credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		jse.executeScript("arguments[0].click()", credentialsTab);
		credTable = driver.findElement(By.id("credentialTable"));
		credList = credTable.findElements(By.tagName("th"));
		Boolean deleted = false;
		for (int i = 0; i < credList.size(); i++) {
			WebElement element = credList.get(i);
			if (element.getAttribute("innerHTML").equals(newUserName)) {
				deleted = true;
				break;
			}
		}
		Assertions.assertFalse(deleted);
	}
}
