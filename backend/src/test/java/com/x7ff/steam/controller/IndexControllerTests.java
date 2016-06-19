package com.x7ff.steam.controller;

import javax.inject.Inject;

import com.x7ff.steam.SteamTracker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SteamTracker.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class IndexControllerTests {

	@Inject
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testIndexTemplateServedOk() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());

	}

}