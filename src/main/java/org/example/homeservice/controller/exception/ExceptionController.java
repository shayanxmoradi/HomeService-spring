package org.example.homeservice.controller.exception;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {GeneralRuntimeException.class})
    public ResponseEntity<ErrorDTO> handleGeneralRuntimeException(GeneralRuntimeException e) {
        return new ResponseEntity<>(
                new ErrorDTO(
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        e.getMessage(),
                        e.getHttpStatus().name()
                ), e.getHttpStatus()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST);

        //Get all errors
        List<FieldErrorDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(
                body, headers, status
        );
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<String> handleMissingRequestBody(HttpMessageNotReadableException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("Invalid or missing request body");
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorDTO implements Serializable {
        private String field;
        private String message;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDTO implements Serializable {
        private String date;
        private String message;
        private String status;
    }
}
