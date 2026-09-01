package com.thz.house_paint.api.management.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

	private boolean isSuccess;
	private String message;
	private T data;
	
}
