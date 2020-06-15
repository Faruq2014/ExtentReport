package com.ReportListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class TestListener2 implements ITestListener {
	private static ExtentReports extent = ExtentManager.createInstance();
	public static ThreadLocal<ExtentTest> extenttest = new ThreadLocal<ExtentTest>();

	public void onTestFinish(ITestResult result) {
		if (extent != null) {
			extent.flush();
		}
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " test just finish");
	}

	public void onStart(ITestResult result) {

	}

	public void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Test just start " + methodName);
		ExtentTest test = extent
				.createTest(result.getTestClass().getName() + "::" + result.getMethod().getMissingGroup());
		extenttest.set(test);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult Result) {

	}

	public void onTestFailure(ITestResult result, WebDriver driver) {
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " test just fail");

		// First arugement
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		extenttest.get()
				.fail("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
						+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>"
						+ " \n");

		String failureLogg = "TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		extenttest.get().log(Status.FAIL, m);
		/*
		 * // second argument String methodName = result.getMethod().getMethodName();
		 * 
		 * String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() +
		 * " FAILED" + "</b>";
		 * 
		 * Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.RED);
		 * extenttest.get().fail(m1); // Third argument individual
		 * extenttest.get().fail(result.getThrowable().getMessage());
		 * extenttest.get().log(Status.FAIL, result.getName() + " TEST CASE ERROR IS " +
		 * result.getThrowable().getMessage());
		 * 
		 * // screen shot String path = takeScreenshot(driver,
		 * result.getMethod().getMethodName()); try {
		 * extenttest.get().fail("<b><font color=red>" + "screenshot of failure" +
		 * "</font><b>", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
		 * } catch (IOException e) {
		 * extenttest.get().fail("test failed, can not catch screen shot"); }
		 * 
		 * String logtest = "<b> Test Method " + methodName + "Failed </b>";
		 * MarkupHelper.createLabel(logtest, ExtentColor.RED);
		 * extenttest.get().log(Status.FAIL, m); }
		 * 
		 * public void onTestSkipped(ITestResult result) { String methodName =
		 * result.getMethod().getMethodName();
		 * 
		 * String logText = "<b>" + "TEST METHOD: - " + methodName.toUpperCase() +
		 * "  SKIPPED" + "</b>";
		 * 
		 * Markup m = MarkupHelper.createLabel(logText, ExtentColor.INDIGO);
		 * extenttest.get().log(Status.SKIP, m);
		 * 
		 * }
		 * 
		 * public void onTestStart(ITestResult result) { ExtentTest test =
		 * extent.createTest(result.getClass().getName() + "::" +
		 * result.getMethod().getMethodName()); extenttest.set(test);
		 * System.out.println(test + "test is starting");
		 */}

	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " test just pass");
		System.out.println(methodName + "this test pass");
		String logText = "<b>" + "TEST METHOD: - " + methodName.toUpperCase() + "  PASSED" + "</b>";

		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		extenttest.get().log(Status.PASS, m);

		// logger.getStatus();
		// logger.log(Status.PASS, " TEST CASE PASS IS " + result.getName());
	}

	public static String takeScreenshot(WebDriver driver, String methodeName) {
		String fileName = getScreenshotName(methodeName);
		String directory = System.getProperty("user.dir") + "/myscreenshot/";
		new File(directory).mkdirs();
		String path = directory + fileName;
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(path));
			System.out.println("screen" + path);
		} catch (IOException e) {
			System.out.println("Capture Failed " + e.getMessage());
			e.printStackTrace();
		}
		return path;

	}

	public static String getScreenshotName(String methodName) {
		Date d = new Date();
		String fileName = methodName + d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// String fileName ="automationReport" +System.currentTimeMillis();
		return fileName;
	}
}
