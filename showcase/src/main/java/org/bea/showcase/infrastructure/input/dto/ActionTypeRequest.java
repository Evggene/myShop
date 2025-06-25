package org.bea.showcase.infrastructure.input.dto;

import lombok.Data;
import org.bea.showcase.application.type.ActionType;

@Data
public class ActionTypeRequest {
    private ActionType action;
}
