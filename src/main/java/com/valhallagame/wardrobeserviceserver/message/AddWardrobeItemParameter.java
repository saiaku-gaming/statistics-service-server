package com.valhallagame.wardrobeserviceserver.message;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddWardrobeItemParameter {
	@NotNull
	private String characterName;

	@NotNull
	private String itemName;
}
