package com.example.demo.exception;

import java.time.LocalDateTime;

public record ErrorResponse(int code,String message,LocalDateTime timestamp) {

}
