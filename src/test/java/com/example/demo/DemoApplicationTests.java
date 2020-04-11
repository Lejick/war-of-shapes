package com.example.demo;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DemoApplicationTests {

	@Test
	void contextLoads() {
		int a=-1;
		assertEquals(Math.abs(a), abs(a));
	}
	public static final float abs(final float x) {
			return StrictMath.abs(x);
	}

}
