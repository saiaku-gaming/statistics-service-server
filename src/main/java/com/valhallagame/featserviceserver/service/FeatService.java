package com.valhallagame.featserviceserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.featserviceserver.model.Feat;
import com.valhallagame.featserviceserver.repository.FeatRepository;

@Service
public class FeatService {

	@Autowired
	private FeatRepository featItemRepository;

	public Feat saveFeat(Feat feat) {
		return featItemRepository.save(feat);
	}

	public void deleteFeat(Feat feat) {
		featItemRepository.delete(feat);
	}

	public List<Feat> getFeats(String characterName) {
		return featItemRepository.findByCharacterOwner(characterName);
	}
}
