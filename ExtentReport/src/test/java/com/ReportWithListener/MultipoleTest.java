package com.ReportWithListener;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MultipoleTest {

	WebDriver driver;

	@BeforeMethod
	public void openApplication() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.salesforce.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void urlTest() {

		System.out.println(driver.getCurrentUrl().contains("salesforce"));

		// creating sub test

		Assert.assertNotEquals(driver.getCurrentUrl(), "faruq");
	}

	@Test(priority = 2)
	public void titleTest() {

		System.out.println(driver.getTitle().contains("salesforce"));
	}

	@Test(priority = 3)
	public void linkTest() {

		Assert.assertTrue(driver.getTitle().contains("fabiha"));
	}

	@Test(priority = 4)
	public void skipTest() {

		throw new SkipException(" We will build it latter. ");
	}

	@AfterMethod
	public void closeApplication() {

		driver.quit();
	}

}
