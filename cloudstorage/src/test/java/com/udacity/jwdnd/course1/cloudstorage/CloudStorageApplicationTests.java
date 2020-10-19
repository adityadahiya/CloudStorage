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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CloudStorageApplicationTests {

    private static String userName = "UserName";
    private static String password = "Password";

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
    public void getHomePageForUnauthorizedUser() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Test
    public void signupAndLoginTest() {
        String firstName = "firstName";
        String lastName = "lastName";

        driver.get("http://localhost:" + this.port + "/signup");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputFirstName"))).sendKeys(firstName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputLastName"))).sendKeys(lastName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("signup"))).click();

        driver.get("http://localhost:" + this.port + "/login");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());
    }

    @Test
    @Order(4)
    public void addNoteTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        String noteTitle = "Title for Note";
        String noteDescription = "Description for Note";
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("newNote"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description"))).sendKeys(noteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("saveNote"))).click();
        Assertions.assertEquals("Home", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        List<WebElement> notesList = wait.until(driver -> driver.findElement(By.id("notesTable"))).findElements(By.tagName("th"));
        Boolean noteAdded = false;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(noteTitle)) {
                noteAdded = true;
                break;
            }
        }
        Assertions.assertTrue(noteAdded);
    }

    @Test
    @Order(5)
    public void updateNoteTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        List<WebElement> notesList = wait.until(driver -> driver.findElement(By.id("notesTable"))).findElements(By.tagName("td"));
        WebElement editButton = null;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            editButton = element.findElement(By.id("editNote"));
            if (editButton != null) {
                break;
            }
        }
        String newNoteTitle = "New title for Note";
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
        WebElement inputNoteTitle = wait.until(driver -> driver.findElement(By.id("note-title")));
        wait.until(ExpectedConditions.elementToBeClickable(inputNoteTitle));
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(newNoteTitle);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("saveNote"))).click();
        Assertions.assertEquals("Home", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        notesList = wait.until(driver -> driver.findElement(By.id("notesTable"))).findElements(By.tagName("th"));
        Boolean noteEdited = false;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
                noteEdited = true;
                break;
            }
        }
        Assertions.assertTrue(noteEdited);
    }

    @Test
    @Order(6)
    public void deleteNoteTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        List<WebElement> notesList = wait.until(driver -> driver.findElement(By.id("notesTable"))).findElements(By.tagName("td"));
        WebElement deleteNote = null;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            deleteNote = element.findElement(By.id("deleteNote"));
            if (deleteNote != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteNote)).click();
        Assertions.assertEquals("Home", driver.getTitle());
        String newNoteTitle = "New title for Note";
        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-notes-tab")));
        notesList = wait.until(driver -> driver.findElement(By.id("notesTable"))).findElements(By.tagName("th"));
        Boolean noteDeleted = false;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
                noteDeleted = true;
                break;
            }
        }
        Assertions.assertFalse(noteDeleted);
    }

    @Test
    @Order(7)
    public void addAndEncryptCredentialTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        String credURL = "https://www.google.co.in/";
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("newCredential"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("saveCredential"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        List<WebElement> credList = wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("td"));
        Boolean credAdded = false;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            if (element.getAttribute("innerHTML").equals(userName)) {
                credAdded = true;
                break;
            }
        }
        Assertions.assertTrue(credAdded);
        //check for encrypted password
        credList = wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("td"));
        Boolean isEncryptedPass = true;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            if (element.getAttribute("innerHTML").equals(password)) {
                isEncryptedPass = false;
                break;
            }
        }
        Assertions.assertTrue(isEncryptedPass);
    }

    @Test
    @Order(8)
    public void updateAndDecryptCredentialTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        List<WebElement> credList = wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("td"));
        WebElement editCredential = null;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            editCredential = element.findElement(By.id("editCredential"));
            if (editCredential != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editCredential)).click();
        WebElement credUserName = driver.findElement(By.id("credential-username"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
        Assertions.assertEquals(password, wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).getAttribute("value")); //check for decrypted password
        String newUserName = "new user name";
        credUserName.clear();
        credUserName.sendKeys(newUserName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("saveCredential"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        credList = wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("td"));
        Boolean updatedCred = false;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            if (element.getAttribute("innerHTML").equals(newUserName)) {
                updatedCred = true;
                break;
            }
        }
        Assertions.assertTrue(updatedCred);
    }

    @Test
    @Order(9)
    public void deleteCredentialTest() {
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputUsername"))).sendKeys(userName);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inputPassword"))).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login"))).click();
        Assertions.assertEquals("Home", driver.getTitle());

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        List<WebElement> credList =  wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("td"));
        WebElement deleteCredential = null;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            deleteCredential = element.findElement(By.id("deleteCredential"));
            if (deleteCredential != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteCredential)).click();
        Assertions.assertEquals("Home", driver.getTitle());

        String newUserName = "new user name";
        driver.get("http://localhost:" + this.port + "/home");
        jse.executeScript("arguments[0].click()", driver.findElement(By.id("nav-credentials-tab")));
        credList =  wait.until(driver -> driver.findElement(By.id("credentialTable"))).findElements(By.tagName("th"));
        Boolean deletedCred = false;
        for (int i = 0; i < credList.size(); i++) {
            WebElement element = credList.get(i);
            if (element.getAttribute("innerHTML").equals(newUserName)) {
                deletedCred = true;
                break;
            }
        }
        Assertions.assertFalse(deletedCred);
    }
}
