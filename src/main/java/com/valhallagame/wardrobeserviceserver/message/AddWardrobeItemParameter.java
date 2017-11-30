package com.valhallagame.wardrobeserviceserver.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddWardrobeItemParameter {
	private String characterName;
	private String itemName;
}
