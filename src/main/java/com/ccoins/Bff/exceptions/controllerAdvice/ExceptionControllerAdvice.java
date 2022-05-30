package com.ccoins.Bff.exceptions.controllerAdvice;

import com.ccoins.Bff.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.ccoins.Bff.exceptions.utils.ErrorUtils.buildMessage;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice { // se encarga de enviar una respuesta cuando se lanza una excepcion explicita

    @ExceptionHandler({ObjectNoContentException.class})
    @ResponseStatus(NO_CONTENT)
    public ResponseEntity<?> handleNoContent(CustomException e){
        this.exceptionLog(e);
        return this.buildResponse(NO_CONTENT, e);
    }

    @ExceptionHandler({BadRequestException.class, ObjectNotAllowedException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> handleBadRequest(CustomException e){
        this.exceptionLog(e);
        return this.buildResponse(BAD_REQUEST, e);
    }

    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<?> handleUnauthorized(CustomException e) {
        this.exceptionLog(e);
        return this.buildResponse(UNAUTHORIZED,e);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<?> handleForbidden(CustomException e) {
        this.exceptionLog(e);
        return this.buildResponse(FORBIDDEN,e);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<?> handleNotFound(CustomException e){
        this.exceptionLog(e);
        return this.buildResponse(NOT_FOUND,e);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<?> handleConflict(CustomException e){
        this.exceptionLog(e);
        return this.buildResponse(CONFLICT, e);
    }

    private ResponseEntity<?> buildResponse(HttpStatus status, CustomException e) {

        String code = e.getCode();
        String message = e.getMessage();
        return status(status).body(buildMessage(code, message));
    }

    private void exceptionLog(CustomException e){
        log.error("[ERROR]: [{}] - {}", e.getCode(), e.getMessage());
    }
}
