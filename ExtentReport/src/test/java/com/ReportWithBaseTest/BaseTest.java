package com.ReportWithBaseTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class BaseTest {

	ExtentReports extent;
	ExtentTest logger;
	public WebDriver driver;
	ExtentHtmlReporter reporter;

	@BeforeSuite
	public void setupReport() {
		reporter = new ExtentHtmlReporter("./Reports/ReportWithBaseTest.html");
		// reporter = new ExtentHtmlReporter("./Reports/" + System.currentTimeMillis() +
		// "ReportWithBaseTest.html");
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

	// @BeforeMethod
	public void openApplication() {
		System.out.println("excuting method");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.salesforce.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
			String screenShotPath = BaseTest.captureFullPge(driver, result.getName());
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
			String screenShotPath = BaseTest.getScreenshot(driver, result.getName());
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
		// driver.close();
	}

	@AfterTest
	public void closeApp() {
		driver.quit();
		// extent.flush();
	}

	public void init(String bType) {
		if (bType.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (bType.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

		} else if (bType.equalsIgnoreCase("ie")) {
			WebDriverManager.edgedriver();
			driver = new EdgeDriver();

		} else {
			System.out.println("please enter valid driver info");
		}
	}

	@AfterSuite(alwaysRun = true)
	public void writeReport() {
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

	public static String captureFullPge(WebDriver driver, String fullPage) throws IOException {

		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		String dest = System.getProperty("user.dir") + "/Screenshot/" + fullPage + System.currentTimeMillis() + ".png";
		ImageIO.write(screenshot.getImage(), "PNG", new File(dest));

		return dest;

	}
}
