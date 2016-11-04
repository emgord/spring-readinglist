package com.readinglist;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes=ReadingListApplication.class,
        webEnvironment= RANDOM_PORT)

public class ServerWebTests {

    private static FirefoxDriver browser;

    @Value("${local.server.port}")
    private int port;
    @BeforeClass
    public static void openBrowser() {
        browser = new FirefoxDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void addBookToEmptyList() {
        String baseUrl = "http://localhost:" + port;

        browser.get(baseUrl);

        assertEquals("You have no books in your book list", browser.findElementByTagName("div").getText());
        browser.findElementByName("title").sendKeys("BOOK TITLE");
        browser.findElementByName("author").sendKeys("BOOK AUTHOR");
        browser.findElementByName("isbn").sendKeys("1234");
        browser.findElementByName("description").sendKeys("DESCRIPTION");
        browser.findElementByName("form").submit();

        WebElement dl = browser.findElementByCssSelector("dt.bookHeadline");
        assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234)", dl.getText());

        WebElement dt = browser.findElementByCssSelector("dd.bookDescription");
        assertEquals("DESCRIPTION", dt.getText());

    }
}
