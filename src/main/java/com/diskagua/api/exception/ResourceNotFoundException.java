package com.diskagua.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Uma exceção indicando que um determinado recurso não foi econtrado.
 *
 * @author Carlos
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    /**
     * Constrói uma nova exceção com uma mensagem detalhando a causa da exceção.
     *
     * @param message a mensagem que informa a causa da exceção
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
