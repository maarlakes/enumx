package cn.maarlakes.enumx;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;

import java.io.Serializable;

/**
 * @author linjpxc
 */
public interface Valuable<T> extends Serializable {

    @Nonnull
    @JsonValue
    @JSONField
    T value();

    @Nonnull
    @SuppressWarnings("unchecked")
    default Class<T> valueType() {
        return (Class<T>) this.value().getClass();
    }

    @Nonnull
    static <T extends Valuable<V>, V> Class<V> getValueType(@Nonnull Class<T> type) {
        return ValueUtils.getValueType(type);
    }
}
