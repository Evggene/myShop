package org.bea.payment.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UsersRepository extends R2dbcRepository<Users, Integer> {
}
