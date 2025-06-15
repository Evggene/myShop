package org.bea.my_shop.repository;

import org.bea.my_shop.configuration.TestcontainersConfiguration;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
public class BaseRepositoryTest{
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;
}
