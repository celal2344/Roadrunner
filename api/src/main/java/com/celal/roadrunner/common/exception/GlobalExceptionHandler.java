package com.celal.roadrunner.common.exception;

import com.celal.roadrunner.common.dto.ErrorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        return localizedError(
                HttpStatus.UNAUTHORIZED,
                ex.getMessageKey(),
                ex.getArgs()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(AuthenticationException ex) {
        return localizedError(
                HttpStatus.UNAUTHORIZED,
                "auth.invalid_credentials",
                new Object[0]
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return localizedError(
                HttpStatus.FORBIDDEN,
                "auth.forbidden",
                new Object[0]
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> messageSource.getMessage(
                        error,
                        LocaleContextHolder.getLocale()
                ))
                .collect(Collectors.joining("; "));

        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBind(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> messageSource.getMessage(
                        error,
                        LocaleContextHolder.getLocale()
                ))
                .collect(Collectors.joining("; "));

        return error(HttpStatus.BAD_REQUEST, message);
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

        return error(status, message);
    }

    private ResponseEntity<ErrorResponseDTO> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ErrorResponseDTO.builder()
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .timestamp(Instant.now())
                        .build());
    }
}
