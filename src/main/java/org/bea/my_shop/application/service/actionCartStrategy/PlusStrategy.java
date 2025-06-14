//package org.bea.my_shop.application.service.actionCartStrategy;
//
//import org.bea.my_shop.application.type.ActionType;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PlusStrategy implements ActionStrategy {
//    @Override
//    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
//        var itemEntity = itemAndCartToEditInfo.itemEntity();
//        var cartEntity = itemAndCartToEditInfo.cartEntity();
//        var itemCount = itemEntity.getItemCountEntity();
//
//        if (itemCount.getCount() == 0) {
//            return new ItemAndCartToEditInfo(null, null);
//        }
//        itemCount.setCount(itemCount.getCount() - 1);
//        cartEntity.getPositions().computeIfPresent(itemEntity, (k, v) -> v + 1);
//        cartEntity.getPositions().putIfAbsent(itemEntity, 1);
//        return itemAndCartToEditInfo;
//    }
//
//    @Override
//    public ActionType getType() {
//        return ActionType.PLUS;
//    }
//}
