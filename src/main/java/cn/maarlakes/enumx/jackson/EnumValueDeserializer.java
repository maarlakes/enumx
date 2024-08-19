package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.DynamicEnum;
import cn.maarlakes.enumx.EnumValue;
import cn.maarlakes.enumx.Enums;
import jakarta.annotation.Nonnull;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public class EnumValueDeserializer<E extends Enum<E> & EnumValue<E, V>, V> extends BaseValueDeserializer<E, V> {

    private final Class<E> enumType;
    private final boolean isDynamicEnum;

    public EnumValueDeserializer(Class<?> enumType) {
        super(Enums.getValueType((Class<E>) enumType));
        if (!(enumType.isEnum() && EnumValue.class.isAssignableFrom(enumType))) {
            throw new IllegalArgumentException();
        }
        this.enumType = (Class<E>) enumType;
        this.isDynamicEnum = enumType.getAnnotation(DynamicEnum.class) != null;
    }

    @Override
    protected E deserializer(@Nonnull Object value) {
        if (this.isDynamicEnum) {
            return Enums.getOrCreate(this.enumType, this.valueType, value, true);
        }
        return Enums.valueOf(this.enumType, this.valueType, value, true);
    }
}
