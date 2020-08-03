package com.qa.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test; // for junit 4
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.demo.persistence.domain.Card;
import com.qa.demo.persistence.repo.CardRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private CardRepo cardRepo;
	
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private List<Card> testCards = new ArrayList<>();
	
	private Card emptyCard;
	
	private Card testCard;
	
	private Card testCardWithId;
	
	private Long id;
	
	@Before
	public void init() {
		this.cardRepo.deleteAll();
		this.testCard = new Card("Dark Magician", "Brown", "5");
		this.testCardWithId = this.cardRepo.save(this.testCard);
		this.testCards.add(testCardWithId);
		this.id = testCardWithId.getId();
		this.emptyCard = new Card();
	}
	
	@Test
	public void createTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
				HttpMethod.POST, "/card/create");
		
		mockRequest.contentType(MediaType.APPLICATION_JSON)
			.content(this.objectMapper.writeValueAsString(testCard)) // Writes as a JSON string
			// Acceptable to receive back
			.accept(MediaType.APPLICATION_JSON);
		
		// Header
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isCreated();
		// Body
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.objectMapper.writeValueAsString(testCardWithId));
		
		this.mvc.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void readTest() throws Exception {
		Long id = testCardWithId.getId();
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
				HttpMethod.GET, "/card/get/" + id);
		
		mockRequest.contentType(MediaType.APPLICATION_JSON)
			.content(this.objectMapper.writeValueAsString(testCardWithId))
			.accept(MediaType.APPLICATION_JSON);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.objectMapper.writeValueAsString(testCardWithId));
		
		this.mvc.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void getAllTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
				HttpMethod.GET, "/card/getAll");
		
		mockRequest.contentType(MediaType.APPLICATION_JSON)
			.content(this.objectMapper.writeValueAsString(testCards))
			.accept(MediaType.APPLICATION_JSON);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isOk();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.objectMapper.writeValueAsString(testCards));
		
		this.mvc.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void updateTest() throws Exception {
		testCard.setColour("Orange");
		testCard.setId(this.id);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
				HttpMethod.GET, "/card/update/" + this.id);
		
		mockRequest.contentType(MediaType.APPLICATION_JSON)
			.content(this.objectMapper.writeValueAsString(testCard))
			.accept(MediaType.APPLICATION_JSON);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher matchContent = MockMvcResultMatchers.content()
				.json(this.objectMapper.writeValueAsString(testCard));
		
		this.mvc.perform(mockRequest).andExpect(matchStatus).andExpect(matchContent);
	}
	
	@Test
	public void deletePassTest() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
				HttpMethod.DELETE, "/card/remove/" + this.id);
		
		ResultMatcher matchStatus = MockMvcResultMatchers.status().isNoContent();

		this.mvc.perform(mockRequest).andExpect(matchStatus);
	}
	
//	@Test
//	public void deleteFailTest() throws Exception {
//		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(
//				HttpMethod.DELETE, "/card/remove/null");
//		
//		ResultMatcher matchStatus = MockMvcResultMatchers.status().isInternalServerError();
//		
//		this.mvc.perform(mockRequest).andExpect(matchStatus);
//	}
	
}
