package com.microsoft.hackathon.copilotdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc 
class CopilotDemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
	void hello() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/hello?key=world"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("hello world"));
	}

    @Test
    void helloWithoutKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("key not passed"));
    }

	// create unit test to /diffdates that calculates the difference between two dates. The operation should receive two dates as parameter in format dd-MM-yyyy and return the difference in days.
	@Test
	void diffDates() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date1=01-01-2020&date2=05-01-2020"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Difference between 01-01-2020 and 05-01-2020 is 4 days"));
	}

	@Test
	void diffDatesReverse() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date1=05-01-2020&date2=01-01-2020"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Difference between 05-01-2020 and 01-01-2020 is -4 days"));
	}

	@Test
	void diffDatesSameDate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date1=01-01-2020&date2=01-01-2020"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Difference between 01-01-2020 and 01-01-2020 is 0 days"));
	}

	@Test
	void diffDatesMissingDate1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date2=01-01-2020"))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void diffDatesMissingDate2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date1=01-01-2020"))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}



	//Write unit test to validate the format of a spanish phone number (+34 prefix, then 9 digits, starting with 6, 7 or 9). The operation should receive a phone number as parameter and return true if the format is correct, false otherwise
	@Test
	void validatePhone() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/validatephone?phone=+34612345678"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));
	}

	@Test
	void validatePhoneInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/validatephone?phone=+3461234567"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("false"));
	}

	// Write unit test to validate the format of a spanish DNI (8 digits and 1 letter). The operation should receive a DNI as parameter and return true if the format is correct, false otherwise
	@Test
	void validateDNI() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/validatedni?dni=12345678A"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));
	}

	@Test
	void validateDNIInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/validatedni?dni=12345678"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("false"));
	}


	//test for /color/{color} endpoint
	@Test
	void color() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/color/red"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("#FF0000"));
	}


	// Create a unit test for new operation that call the API https://api.chucknorris.io/jokes/random and return the joke
	@Test
	void joke() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/joke"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}


	// create unit test for the a given url as query parameter, and parse it, return the protocol, host, port, path and query parameters. The response should be in Json format
	@Test
	void parseUrl() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/parseurl").param("url", "https://www.example.com:8080/path/to/resource?param1=value1&param2=value2"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.protocol").value("https"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.host").value("www.example.com"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.port").value(8080))
			.andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/path/to/resource"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.query.param1").value("value1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.query.param2").value("value2"));
	}


	// Given the path of a file and count the number of occurrences of a provided word. The path and the word should be query parameters. The response should be in Json format.
	@Test
	void countWord() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/countword").param("path", "test.txt").param("word", "example"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2));
	}

}