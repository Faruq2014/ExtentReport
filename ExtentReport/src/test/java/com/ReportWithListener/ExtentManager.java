package com.ReportWithListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.ReportBasics.CustomizedReport;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;
	public static WebDriver driver;
	public static ExtentTest logger;

	public static ExtentReports createInstance(String filename) {

		// ExtentHtmlReporter reporter = new
		// ExtentHtmlReporter("./Reports/MultipoleTestReport.html");
		ExtentHtmlReporter reporter = new ExtentHtmlReporter(filename);
		extent = new ExtentReports();
		extent.attachReporter(reporter);

		// ExtentHtmlReporter reporter is for setting up look and feel environment.
		reporter.config().setDocumentTitle("Automation Report"); // Tile of report
		reporter.config().setReportName("Functional Testing"); // Name of the report
		reporter.config().setDocumentTitle("utf-8");
		// reporter.config().setTheme(Theme.STANDARD);
		reporter.config().setTheme(Theme.DARK);

//ExtentReports extent; is setting environment with the help of java system class. 
		extent.setSystemInfo("User name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("User Location", System.getProperty("user.country"));
		extent.setSystemInfo("OS name", System.getProperty("os.name"));
		extent.setSystemInfo("OS version", System.getProperty("os.version"));
		extent.setSystemInfo("JDK version", System.getProperty("java.version"));
		extent.setSystemInfo("Selenium version", "3.141.59");
		extent.setSystemInfo("Maven version", "3.15"); // you can write any thing.

		return extent;

	}

	public static ExtentManager failMethod(ITestResult result) throws IOException {

//First arugement
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		logger.fail("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
				+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>" + " \n");

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
		return failMethod(result);
	}

	public static ExtentManager skippMethod(ITestResult result) throws IOException {

		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		logger.skip("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
				+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>" + " \n");

		String skippLogg = "TEST CASE SKIPPED  " + result.getMethod().getMethodName();
		Markup m = MarkupHelper.createLabel(skippLogg, ExtentColor.ORANGE);
		logger.log(Status.SKIP, m);
//taking screen shot
		String screenShotPath = CustomizedReport.getScreenshot(driver, result.getName());
		logger.addScreenCaptureFromPath(screenShotPath);
//second argument
		String methodName = result.getMethod().getMethodName();

		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " SKIPPED" + "</b>";

		Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
		logger.pass(m1);
//third argument
		logger.skip(result.getThrowable().getMessage());
		logger.log(Status.SKIP, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());

		return skippMethod(result);

	}

	public static ExtentManager passMethod(ITestResult result) throws IOException {

		String methodName = result.getMethod().getMethodName();

		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + "  PASSED" + "</b>";

		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		logger.pass(m);

		// logger.getStatus();
		// logger.log(Status.PASS, " TEST CASE PASS IS " + result.getName());

		return passMethod(result);

	}

	// two different take screen shot method with different time input and from
	// different pckage.

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
