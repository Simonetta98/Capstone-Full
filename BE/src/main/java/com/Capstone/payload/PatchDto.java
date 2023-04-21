package com.Capstone.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PatchDto {

	String op;
	String key;
	String value;
}
