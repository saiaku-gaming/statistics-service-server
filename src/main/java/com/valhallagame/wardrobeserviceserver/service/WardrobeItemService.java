package com.valhallagame.wardrobeserviceserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.wardrobeserviceserver.model.WardrobeItem;
import com.valhallagame.wardrobeserviceserver.repository.WardrobeItemRepository;

@Service
public class WardrobeItemService {

	@Autowired
	private WardrobeItemRepository wardrobeItemRepository;

	public WardrobeItem saveWardrobeItem(WardrobeItem wardrobeItem) {
		return wardrobeItemRepository.save(wardrobeItem);
	}

	public void deleteWardrobeItem(WardrobeItem wardrobeItem) {
		wardrobeItemRepository.delete(wardrobeItem);
	}

	public List<WardrobeItem> getWardrobeItems(String characterName) {
		return wardrobeItemRepository.findByCharacterOwner(characterName);
	}
}
