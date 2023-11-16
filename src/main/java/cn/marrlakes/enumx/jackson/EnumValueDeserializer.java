package cn.marrlakes.enumx.jackson;

import cn.marrlakes.enumx.EnumValue;
import cn.marrlakes.enumx.Enums;
import jakarta.annotation.Nonnull;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public class EnumValueDeserializer<E extends Enum<E> & EnumValue<E, V>, V> extends BaseValueDeserializer<E, V> {

    private final Class<E> enumType;

    public EnumValueDeserializer(Class<?> enumType) {
        super(Enums.getValueType((Class<E>) enumType));
        if (!(enumType.isEnum() && EnumValue.class.isAssignableFrom(enumType))) {
            throw new IllegalArgumentException();
        }
        this.enumType = (Class<E>) enumType;
    }

    @Override
    protected E deserializer(@Nonnull Object value) {
        return Enums.valueOf(this.enumType, this.valueType, value, true);
    }
}
