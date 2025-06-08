package org.bea.my_shop.repository;

import org.bea.my_shop.configuration4.TestMyShopApplication;
import org.bea.my_shop.configuration4.TestcontainersConfiguration;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Collections;

@Transactional
@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseRepositoryTest {
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;
}
