package com.qa.demo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.demo.persistence.domain.Card;
import com.qa.demo.service.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

	private CardService service;

	public CardController(CardService service) {
		super();
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<Card> create(@RequestBody Card card) {
		Card created = this.service.create(card);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Card> read(@PathVariable Long id) {
		Card found = this.service.read(id);
		return ResponseEntity.ok(found);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Card>> readAll() {
		List<Card> found = this.service.readAll();
		return ResponseEntity.ok(found);
	}

	@GetMapping("/update/{id}")
	public ResponseEntity<Card> update(@RequestBody Card newCard, @PathVariable Long id) {
		Card updated = this.service.update(newCard, id);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		boolean deleted = this.service.delete(id);
		return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
