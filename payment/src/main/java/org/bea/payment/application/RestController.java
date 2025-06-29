package org.bea.payment.application;

import lombok.RequiredArgsConstructor;
import org.bea.payment.persistence.Users;
import org.bea.payment.persistence.UsersRepository;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class RestController {
    private final UsersRepository usersRepository;

    @GetMapping("/all-users")
    public Flux<Users> root() {
        return usersRepository.findAll();
    }
}
