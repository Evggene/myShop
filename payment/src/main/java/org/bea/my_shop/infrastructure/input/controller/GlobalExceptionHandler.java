package org.bea.my_shop.infrastructure.input.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Mono<Rendering> handle(RuntimeException ex) {
        var res = Rendering.view("error-page")
                .modelAttribute("error", ex.getMessage())
                .build();
        return Mono.just(res);
    }
}
