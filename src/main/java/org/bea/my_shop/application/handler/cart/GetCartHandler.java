//package org.bea.my_shop.application.handler.cart;
//
//import lombok.RequiredArgsConstructor;
//import org.bea.my_shop.application.dto.ItemAndPriceInfo;
//import org.bea.my_shop.application.handler.ItemsPriceInCartCalculation;
//import org.bea.my_shop.application.mapper.ItemMapper;
//import org.bea.my_shop.domain.CartStateType;
//import org.bea.my_shop.domain.Item;
//import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.Comparator;
//
//@Service
//@RequiredArgsConstructor
//public class GetCartHandler {
//
//    private final CartRepository cartRepository;
//
//    public ItemAndPriceInfo getCartStatePrepare() {
//        var prepareCartOpt = cartRepository.findFirstByCartState(CartStateType.PREPARE);
//        if (prepareCartOpt.isEmpty()) {
//            return new ItemAndPriceInfo(null, null, BigDecimal.ZERO);
//        }
//        var prepareCart = prepareCartOpt.get();
//        var total = ItemsPriceInCartCalculation.calculate(prepareCart);
//        var items = prepareCart
//                .getPositions()
//                .entrySet()
//                .stream()
////                .peek(it -> {
////                    // надо конвертировать в ItemInCartResponse
////                    it.getKey().getItemCountEntity().setCount(it.getValue());
////                })
//                .map(it -> ItemMapper.toModel(it.getKey()))
//                .sorted(Comparator.comparing(Item::getTitle))
//                .toList();
//        return new ItemAndPriceInfo(prepareCart.getId(), items, total);
//    }
//}
