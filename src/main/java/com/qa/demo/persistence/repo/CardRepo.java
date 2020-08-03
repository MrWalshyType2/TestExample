package com.qa.demo.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.demo.persistence.domain.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

	List<Card> findAllByName(String name);

}
