package cn.maarlakes.enumx.fastjson;

import cn.maarlakes.enumx.DynamicEnum;
import cn.maarlakes.enumx.EnumValue;
import cn.maarlakes.enumx.Enums;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Type;

/**
 * @author linjpxc
 */
final class EnumObjectReader<E extends Enum<E> & EnumValue<E, T>, T> implements ObjectReader<E> {

    private final Class<E> type;
    private final boolean isDynamicEnum;

    EnumObjectReader(@Nonnull Class<E> type) {
        this.type = type;
        this.isDynamicEnum = type.getAnnotation(DynamicEnum.class) != null;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public E readObject(JSONReader reader, Type fieldType, Object fieldName, long features) {
        if (reader.readIfNull()) {
            return null;
        }
        if (fieldType instanceof Class && EnumValue.class.isAssignableFrom((Class) fieldType)) {
            if (((Class<?>) fieldType).getAnnotation(DynamicEnum.class) == null) {
                return Enums.valueOf((Class<E>) fieldType, reader.readAny(), true);
            }
            return Enums.getOrCreate((Class<E>) fieldType, reader.readAny(), true);
        }
        if (this.isDynamicEnum) {
            return Enums.getOrCreate(this.type, reader.readAny(), true);
        }
        return Enums.valueOf(this.type, reader.readAny(), true);
    }
}
