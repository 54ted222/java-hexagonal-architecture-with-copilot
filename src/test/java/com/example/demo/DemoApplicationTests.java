package com.example.demo;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		var str = Optional.ofNullable(new String[] { "Hello, World!", "abcd", "efgh" });
		var str2 = str.stream().flatMap(Stream::of)
				.filter(s -> s.length() > 5)

				.map(String::toUpperCase)
				.findFirst()
				.orElse("No match found");
		System.out.println(str2);

		var r = Stream.of("sdf").toArray();

		System.out.println("========");
		System.out.println(r);
		var date = new Date();
		date.getHours();
	}

}