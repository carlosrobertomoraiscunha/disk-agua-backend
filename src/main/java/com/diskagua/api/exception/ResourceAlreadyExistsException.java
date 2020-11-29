package com.diskagua.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Uma exceção indicando que um determinado recurso já foi encontrado em
 * determinado local.
 *
 * @author Carlos
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends Exception {

    /**
     * Constrói uma nova exceção com uma mensagem detalhando a causa da exceção.
     *
     * @param message a mensagem que informa a causa da exceção
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

}
