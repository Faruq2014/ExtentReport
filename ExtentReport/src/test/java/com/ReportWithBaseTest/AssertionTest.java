package com.ReportWithBaseTest;

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AssertionTest extends BaseTest {

	@BeforeTest
	public void openbrowser() {
		init("chrome");
		driver.get("https://www.salesforce.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	// @AfterClass
	public void closeBrowser() {
		driver.quit();

	}

	@Test(priority = 1)
	public void assertEqual() {
		logger = extent.createTest("assertEqual");
		Assert.assertEquals("Salesforce: We Bring Companies and Customers Together", driver.getTitle());
		System.out.println("pass assertEqual test");
		logger.info("pass assertEqual test");
	}

	@Test(priority = 2)
	public void assertNotEqual() {
		logger = extent.createTest("assertNotEqual");
		Assert.assertNotEquals("Salesforce: We Bring Companies and Customers ", driver.getTitle());
		System.out.println("pass assertNotEqual test");
		logger.info("pass assertNotEqual test");
	}

	@Test(priority = 3)
	public void assertTrue() {
		logger = extent.createTest("assertTrue");
		System.out.println("assertTrue");
		Assert.assertTrue(true);

	}

	@Test(priority = 4)
	public void assertFalse() {
		logger = extent.createTest("assertFalse");
		System.out.println("assertFalse");
		Assert.assertFalse(true);
	}

	@Test(priority = 5, enabled = true)
	public void skipTest() {
		logger = extent.createTest("skipTest");
		throw new SkipException(" We will build it latter. ");
	}

}
