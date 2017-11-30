package com.valhallagame.wardrobeserviceserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valhallagame.wardrobeserviceserver.model.WardrobeItem;

public interface WardrobeItemRepository extends JpaRepository<WardrobeItem, Integer> {
	public List<WardrobeItem> findByCharacterOwner(String characterOwner);
}
