package com.Report;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FirstTest {

	@BeforeClass
	public void openApp() {
		System.out.println("Bismillah");
	}

	@Test
	public void loginTest() {
		System.out.println("Assalamualaikum");
	}

}
