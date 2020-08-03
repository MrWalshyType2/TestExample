package com.qa.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test; // for junit 5
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.demo.persistence.domain.Card;
import com.qa.demo.persistence.repo.CardRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceUnitTest {

	@MockBean
	CardRepo cardRepo;
	
	@Autowired
	CardService cardService;
	
	@Test
	public void createTest() {
		Card card = new Card("Birthday", "Red", "1.99");
		when(this.cardRepo.save(card)).thenReturn(card);
		
		assertEquals(card, cardService.create(card));
		
		verify(cardRepo, times(1)).save(card);
	}
	
	@Test
	public void readTest() {
		Optional<Card> card = Optional.ofNullable(new Card("Birthday", "Red", "1.99"));
		when(this.cardRepo.findById(card.get().getId())).thenReturn(card);
		
		assertEquals(card.get(), cardService.read(card.get().getId()));
		
		verify(cardRepo, times(1)).findById(card.get().getId());
	}
	
	@Test
	public void readAllTest() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card("Birthday", "Red", "1.99"));
		cards.add(new Card("Birthday", "Orange", "1.99"));

		when(this.cardRepo.findAll()).thenReturn((List<Card>)cards);
		
		assertEquals(cards, cardService.readAll());
		
		verify(cardRepo, times(1)).findAll();
	}
	
	@Test
	public void updateTest() {
		Long id = 1l;
		Card newCard = new Card("Birthday", "Yellow", "1.99");
		Optional<Card> saved = Optional.ofNullable(new Card("Birthday", "Blue", "2.99"));
		
		// For found = read(id)
		when(this.cardRepo.findById(id)).thenReturn(saved);
		when(this.cardRepo.save(saved.get())).thenReturn(newCard);
		
		assertEquals(newCard, cardService.update(newCard, 1l));
		
		verify(this.cardRepo, times(1)).findById(id);
		verify(this.cardRepo, times(1)).save(saved.get());
	}
	
	@Test
	public void deletePassTest() {
		Long id = 1l;
		when(this.cardRepo.existsById(id)).thenReturn(false);
		
		assertEquals(true, cardService.delete(id));
		
		verify(this.cardRepo, times(1)).deleteById(id);
		verify(this.cardRepo, times(1)).existsById(id);
	}
	
	@Test
	public void deleteFailTest() {
		Long id = 1l;
		when(this.cardRepo.existsById(id)).thenReturn(true);
		
		assertEquals(false, cardService.delete(id));
		
		verify(this.cardRepo, times(1)).deleteById(id);
		verify(this.cardRepo, times(1)).existsById(id);
	}
	
}
