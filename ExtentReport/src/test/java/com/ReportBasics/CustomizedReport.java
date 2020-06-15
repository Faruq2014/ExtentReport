package com.ReportBasics;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CustomizedReport {
	ExtentReports extent;
	ExtentTest logger;
	WebDriver driver;
	ExtentHtmlReporter reporter;

	@BeforeTest
	public void SetupEnvironment() {
		reporter = new ExtentHtmlReporter("./Reports/CustomizedReport.html");
		extent = new ExtentReports();
		extent.attachReporter(reporter);

		// ExtentHtmlReporter reporter is for setting up look and feel environment.
		reporter.config().setDocumentTitle("Automation Report"); // Tile of report
		reporter.config().setReportName("Functional Testing"); // Name of the report
		// reporter.config().setTheme(Theme.STANDARD);
		reporter.config().setTheme(Theme.DARK);

// ExtentReports extent; is setting environment with the help of java system class. 
		extent.setSystemInfo("User name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("User Location", System.getProperty("user.country"));
		extent.setSystemInfo("OS name", System.getProperty("os.name"));
		extent.setSystemInfo("OS version", System.getProperty("os.version"));
		extent.setSystemInfo("JDK version", System.getProperty("java.version"));
		extent.setSystemInfo("Selenium version", "3.141.59");
		extent.setSystemInfo("Maven version", "3.15"); // you can write any thing.

	}

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
		logger = extent.createTest("urlTest");
		System.out.println(driver.getCurrentUrl().contains("salesforce"));

		// creating sub test
		logger.createNode("Testing url nagative test");
		Assert.assertNotEquals(driver.getCurrentUrl(), "faruq");
	}

	@Test(priority = 2)
	public void titleTest() {
		logger = extent.createTest("titleTest");
		System.out.println(driver.getTitle().contains("salesforce"));
	}

	@Test(priority = 3, enabled = true)
	public void linkTest() {
		logger = extent.createTest("linkTest");
		Assert.assertTrue(driver.getTitle().contains("fabiha"));
	}

	@Test(priority = 4, enabled = true)
	public void skipTest() {
		logger = extent.createTest("skipTest");
		throw new SkipException(" We will build it latter. ");
	}

	@AfterMethod
	public void closeApplication(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {
//First arugement
			String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
			logger.fail("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
					+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>"
					+ " \n");

			String failureLogg = "TEST CASE FAILED";
			Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
			logger.log(Status.FAIL, m);

// second argument
			String methodName = result.getMethod().getMethodName();

			String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " FAILED" + "</b>";

			Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.RED);
			logger.fail(m1);
//Third argument individual
			logger.fail(result.getThrowable().getMessage());
			logger.log(Status.FAIL, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());
//screen shot
			String screenShotPath = CustomizedReport.getScreenshot(driver, result.getName());
			logger.addScreenCaptureFromPath(screenShotPath);

		} else if (result.getStatus() == ITestResult.SKIP) {
// first argument
			String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
			logger.skip("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
					+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>"
					+ " \n");

			String failureLogg = "TEST CASE SKIPPED  " + result.getMethod().getMethodName();
			Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.ORANGE);
			logger.log(Status.SKIP, m);
// taking screen shot
			String screenShotPath = CustomizedReport.getScreenshot(driver, result.getName());
			logger.addScreenCaptureFromPath(screenShotPath);
//second argument
			String methodName = result.getMethod().getMethodName();

			String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " SKIPPED" + "</b>";

			Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
			logger.pass(m1);
// third argument
			logger.skip(result.getThrowable().getMessage());
			logger.log(Status.SKIP, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());

		} else if (result.getStatus() == ITestResult.SUCCESS) {

			String methodName = result.getMethod().getMethodName();

			String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + "  PASSED" + "</b>";

			Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			logger.pass(m);

			// logger.getStatus();
			// logger.log(Status.PASS, " TEST CASE PASS IS " + result.getName());
		}
		driver.quit();
	}

	@AfterTest
	public void tearDown() {
		extent.flush();

	}

	public static String getScreenshot(WebDriver driver, String screenShotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/Screenshot/" + screenShotName + System.currentTimeMillis()
				+ ".png";
		File destination = new File(path);
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			System.out.println("Capture Failed " + e.getMessage());
		}
		return path;

	}

	public static String getScreenshot2(WebDriver driver, String screenShotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/Screenshot/" + screenShotName + dateName + ".png";
		File finaldestination = new File(destination);

		FileUtils.copyFile(src, finaldestination);

		return destination;

	}

}
