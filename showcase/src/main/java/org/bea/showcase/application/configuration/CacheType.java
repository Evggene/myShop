package org.bea.showcase.application.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {
    ORDERS("orders");

    public final String value;

}
