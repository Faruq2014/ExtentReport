package com.ReportBasics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReportWithScreenShot {
	ExtentReports extent;
	ExtentTest logger;
	WebDriver driver;
	ExtentHtmlReporter reporter;

	@BeforeTest
	public void setUpTest() {
		reporter = new ExtentHtmlReporter("./Reports/ReportWithScreenShot.html");
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		// logger = extent.createTest("Functional Testing");
		// ExtentHtmlReporter reporter is for setting up look and feel environment.
		reporter.config().setDocumentTitle("Automation Report"); // Tile of report
		reporter.config().setReportName("Functional Testing"); // Name of the report
		reporter.config().setTheme(Theme.STANDARD);
		// reporter.config().setTheme(Theme.DARK);

		extent.setSystemInfo("User name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("User Location", System.getProperty("user.country"));
		extent.setSystemInfo("OS name", System.getProperty("os.name"));
		extent.setSystemInfo("OS version", System.getProperty("os.version"));
		extent.setSystemInfo("JDK version", System.getProperty("java.version"));
	}

	@BeforeMethod
	public void setup() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.facebook.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void loginTest() {
		logger = extent.createTest("login");
		driver.findElement(By.id("email")).sendKeys("mr@gmail.com");
		driver.findElement(By.id("pass")).sendKeys("faruq");
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.TAB).build().perform();
		actions.sendKeys(Keys.ENTER).build().perform();
		WebDriverWait wait = new WebDriverWait(driver, 8);
		wait.until(ExpectedConditions.titleContains("Log into Facebook | Facebook:"));
		Assert.assertEquals(driver.getTitle(), "faruq Molla");
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {
			String temp = ScreenShot.getScreenshot(driver, result.getName());
			logger.fail(result.getThrowable().getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(temp).build());

			// logger.fail(result.getThrowable().getMessage());
			logger.log(Status.FAIL, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());
		} else if (result.getStatus() == ITestResult.SKIP) {
			// logger.skip(result.getThrowable().getMessage());
			logger.log(Status.SKIP, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.getStatus();
			logger.log(Status.PASS, result.getName() + " TEST CASE PASS IS ");
		}
	}

	@AfterTest
	public void finishTest() {
		extent.flush();
		driver.quit();
	}
}
