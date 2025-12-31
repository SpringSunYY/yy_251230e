package com.lz.manage.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 预约状态 枚举
 * 对应字典类型：appointment_status
 */
@Getter
public enum AppointmentStatusEnum {

    /** 已预约 */
    APPOINTMENT_STATUS_0("0", "已预约"),

    /** 已过期 */
    APPOINTMENT_STATUS_1("1", "已过期");

    private static final Map<String, AppointmentStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (AppointmentStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    AppointmentStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取对应的枚举
     */
    public static Optional<AppointmentStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
