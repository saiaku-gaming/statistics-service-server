package com.valhallagame.wardrobeserviceserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wardrobe_item")
public class WardrobeItem {
	@Id
	@SequenceGenerator(name = "wardrobe_item_wardrobe_item_id_seq", sequenceName = "wardrobe_item_wardrobe_item_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wardrobe_item_wardrobe_item_id_seq")
	@Column(name = "wardrobe_item_id", updatable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "character_owner")
	private String characterOwner;

	public WardrobeItem(String name, String characterOwner) {
		this.name = name;
		this.characterOwner = characterOwner;
	}
}
