package com.valhallagame.statisticsserviceserver.model;

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
@Table(name = "statistics")
public class Statistics {
	@Id
	@SequenceGenerator(name = "statistics_statistics_id_seq", sequenceName = "statistics_statistics_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statistics_statistics_id_seq")
	@Column(name = "statistics_id", updatable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "character_owner")
	private String characterOwner;

	public Statistics(String name, String characterOwner) {
		this.name = name;
		this.characterOwner = characterOwner;
	}
}
