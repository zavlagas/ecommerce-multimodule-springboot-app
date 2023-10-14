package com.agileactors.product.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus httpStatus, String message) {
}
