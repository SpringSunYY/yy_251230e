package com.lz.manage.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自习室状态 枚举
 */
@Getter
public enum StudyRoomStatusEnum {

    /**
     * 开启
     */
    STUDY_ROOM_STATUS_0("0", "开启"),

    /**
     * 关闭
     */
    STUDY_ROOM_STATUS_1("1", "关闭");

    private static final Map<String, StudyRoomStatusEnum> VALUE_TO_ENUM = new HashMap<>();

    static {
        for (StudyRoomStatusEnum item : values()) {
            VALUE_TO_ENUM.put(item.value, item);
        }
    }

    private final String value;
    private final String label;

    StudyRoomStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 根据 value 获取枚举
     */
    public static Optional<StudyRoomStatusEnum> getEnumByValue(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(VALUE_TO_ENUM.get(value));
    }
}
