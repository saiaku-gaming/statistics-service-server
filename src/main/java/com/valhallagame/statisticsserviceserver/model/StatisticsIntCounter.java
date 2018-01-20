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
@Table(name = "statistics_int_counter")
public class StatisticsIntCounter {

	@Id
	@SequenceGenerator(name = "statistics_counter_statistics_counter_id_seq", sequenceName = "statistics_counter_statistics_counter_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statistics_counter_statistics_counter_id_seq")
	@Column(name = "statistics_int_counter_id", updatable = false)
	private Integer id;

	@Column(name = "count")
	private int count;

	@Column(name = "key")
	private String key;

	@Column(name = "character_name")
	private String characterName;
}
