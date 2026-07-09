package com.celal.roadrunner.common.exception;

import com.celal.roadrunner.common.dto.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NotFoundException ex) {
        return localizedError(
                HttpStatus.NOT_FOUND,
                ex.getMessageKey(),
                ex.getArgs()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex) {
        return localizedError(
                HttpStatus.BAD_REQUEST,
                ex.getMessageKey(),
                ex.getArgs()
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(ConflictException ex) {
        return localizedError(
                HttpStatus.CONFLICT,
                ex.getMessageKey(),
                ex.getArgs()
        );
    }

    private ResponseEntity<ErrorResponseDTO> localizedError(
            HttpStatus status,
            String messageKey,
            Object[] args
    ) {
        String message = messageSource.getMessage(
                messageKey,
                args,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.builder()
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .timestamp(Instant.now())
                        .build());
    }
}
