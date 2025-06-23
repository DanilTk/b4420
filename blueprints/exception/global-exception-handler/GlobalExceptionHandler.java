package com.example.shared.api.error;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;
import static com.example.shared.api.error.BaseLoggerFactory.getBaseLogger;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_PREFIX = "Caught exception:";

    private final BaseErrorResponseDtoFactory baseErrorResponseDtoFactory;
    private final ValidationErrorResponseDtoFactory validationErrorResponseDtoFactory;

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResponseDto handleAnyException(Exception exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createUnknown();
    }

    @ResponseStatus(TOO_MANY_REQUESTS)
    @ExceptionHandler(RequestNotPermitted.class)
    public BaseErrorResponseDto handleRequestNotPermitted(RequestNotPermitted exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createRateLimitError();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseErrorResponseDto handleException(MaxUploadSizeExceededException exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createFileSizeError(exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CustomValidationException.class)
    public ValidationErrorResponseDto handleException(CustomValidationException exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return validationErrorResponseDtoFactory.create(exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CustomBusinessException.class)
    public BaseErrorResponseDto handleException(CustomBusinessException exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createBusinessError(exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseErrorResponseDto handleException(IllegalArgumentException exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createUnknown();
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class, CustomAccessDeniedException.class})
    public BaseErrorResponseDto handleException(Exception exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createAccessDenied();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public BaseErrorResponseDto handleException(CustomNotFoundException exception) {
        getBaseLogger().error(LOG_PREFIX, exception);
        return baseErrorResponseDtoFactory.createObjectNotFound(exception);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        getBaseLogger().error("Caught Spring exception:", exception);
        ResponseEntity<Object> defaultResponse = super.handleExceptionInternal(
            exception, body, headers, status, request);
        Object responseBody = createBodyForSpringException(exception);
        return new ResponseEntity<>(responseBody, defaultResponse.getHeaders(), defaultResponse.getStatusCode());
    }

    private Object createBodyForSpringException(Exception exception) {
        if (exception instanceof BindException ex) {
            return validationErrorResponseDtoFactory.create(ex);
        }
        if (exception instanceof MissingRequestValueException ex) {
            return baseErrorResponseDtoFactory.createSpringError(ex);
        }
        if (exception instanceof HttpMessageNotReadableException) {
            return baseErrorResponseDtoFactory.createInvalidInputError();
        }
        return baseErrorResponseDtoFactory.createUnknown();
    }
}
