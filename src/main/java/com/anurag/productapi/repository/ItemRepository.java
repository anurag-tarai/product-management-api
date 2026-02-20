package com.anurag.productapi.repository;

import com.anurag.productapi.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {}
