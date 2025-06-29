package org.bea.payment.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class RootController {

    @GetMapping("/")
    public Mono<Rendering> root() {
        return Mono.just(Rendering.view("hello").build());
    }

}
