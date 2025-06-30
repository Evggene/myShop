package org.bea.showcase.application.service;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.configuration.TechnicalUserProperty;
import org.bea.showcase.application.exception.MyShopException;
import org.bea.showcase.application.mapper.OrderMapper;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.application.service.cart.OrderCartService;
import org.bea.showcase.infrastructure.input.dto.OrderResponse;
import org.bea.showcase.application.port.output.OrderRepository;
import org.bea.showcase.infrastructure.output.client.OrderWebClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderWebClient orderWebClient;
    private final OrderCartService orderCartService;
    private final GetCartService getCartService;

    @Cacheable(value = "orders", key = "'all'")
    public Mono<List<OrderResponse>> getAll() {
        return orderRepository.getAll()
                .map(OrderMapper::fromEntityToRequest)
                .collectList();
    }

    public Mono<OrderResponse> getById(UUID id) {
        return orderRepository.getById(id)
                .map(OrderMapper::fromEntityToRequest);
    }


    public Mono<Boolean> tryPay(UUID orderId) {
        return orderCartService.orderCart(orderId)
                .thenReturn(true);
//        return getCartService.getCartStatePrepare()
//                .switchIfEmpty(Mono.error(new MyShopException(orderId.toString())))
//                .flatMap(order ->
//                        orderWebClient.tryPay(TechnicalUserProperty.technicalUserId, order.totalPrice())
//                        .flatMap(newBalance -> {
//                            System.out.println(newBalance);
//                            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
//                                return Mono.just(false);
//                            }
//                            return orderCartService.orderCart(orderId)
//                                    .thenReturn(true);
//                        }))
//                .onErrorResume(e -> Mono.just(false));
    }
}
