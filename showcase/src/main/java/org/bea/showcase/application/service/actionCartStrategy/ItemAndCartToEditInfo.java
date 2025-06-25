package org.bea.showcase.application.service.actionCartStrategy;

import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.Item;

public record ItemAndCartToEditInfo(Item item, Cart cart) { }
