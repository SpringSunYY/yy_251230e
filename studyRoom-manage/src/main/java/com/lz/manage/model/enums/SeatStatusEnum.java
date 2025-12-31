package com.lz.manage.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 座位状态 枚举
 */
@Getter
public enum SeatStatusEnum {

    /**
     * 空闲
     */
    SEAT_STATUS_0("0", "空闲"),

    /**
     * 已预约
     */
    SEAT_STATUS_1("1", "已预约");

    private static final Map<String, SeatStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (SeatStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    SeatStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取枚举
     */
    public static Optional<SeatStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
