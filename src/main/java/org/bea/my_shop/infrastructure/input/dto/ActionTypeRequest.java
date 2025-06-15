package org.bea.my_shop.infrastructure.input.dto;

import lombok.Data;
import org.bea.my_shop.application.type.ActionType;

@Data
public class ActionTypeRequest {
    private ActionType action;
}
