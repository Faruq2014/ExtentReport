package com.ReportBasics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReportInSingleTest {

	public WebDriver driver;

	@Test
	public void LoginTest() throws IOException {
		ExtentHtmlReporter reporter = new ExtentHtmlReporter("./Reports/ReportInSingleTest.html");
		reporter.config().setDocumentTitle("Automation Report"); // Tile of report
		reporter.config().setReportName("Functional Testing"); // Name of the report
		reporter.config().setTheme(Theme.STANDARD);

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);

		extent.setSystemInfo("User name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("User Location", System.getProperty("user.country"));
		extent.setSystemInfo("OS name", System.getProperty("os.name"));
		extent.setSystemInfo("OS version", System.getProperty("os.version"));
		extent.setSystemInfo("JDK version", System.getProperty("java.version"));

		// this info bellow are coming from properties reader file.
		// extent.setSystemInfo("Maven
		// version",Properties_reader.fetchPropertyValue("Maven"));
		// extent.setSystemInfo("Selenium
		// version",Properties_reader.fetchPropertyValue("SeleniumVersion"));

		ExtentTest logger = extent.createTest("LoginTest");

		// System.setProperty("webdriver.chrome.driver",
		// "C:\\SeleniumDriver\\chromedriver_win32\\chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.salesforce.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println(driver.getTitle().contains("salesforce"));
		logger.log(Status.INFO, "open SalesForce");
		logger.log(Status.PASS, "Title is true");
		extent.flush();

	}
}
