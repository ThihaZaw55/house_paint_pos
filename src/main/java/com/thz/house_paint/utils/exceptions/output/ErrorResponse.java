package com.thz.house_paint.utils.exceptions.output;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

	private int status;
    private String message;
    private LocalDateTime timestamp;
}
