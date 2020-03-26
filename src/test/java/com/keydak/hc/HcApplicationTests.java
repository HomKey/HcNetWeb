package com.keydak.hc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HcApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(HcApplicationTests.class.getResource(""));
		System.out.println(HcApplicationTests.class.getResource("/"));
	}

}
