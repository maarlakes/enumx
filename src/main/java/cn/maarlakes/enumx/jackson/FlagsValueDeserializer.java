package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.EnumFlags;
import cn.maarlakes.enumx.EnumValue;
import cn.maarlakes.enumx.Flags;
import jakarta.annotation.Nonnull;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public class FlagsValueDeserializer<F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> extends BaseValueDeserializer<F, V> {

    private final Class<F> flagsType;

    public FlagsValueDeserializer(Class<?> flagsType) {
        super(Flags.getValueType((Class<F>) flagsType));
        if (!EnumFlags.class.isAssignableFrom(flagsType)) {
            throw new IllegalArgumentException();
        }
        this.flagsType = (Class<F>) flagsType;
    }

    @Override
    protected F deserializer(@Nonnull Object value) {
        return Flags.valueOf(this.flagsType, this.valueType, value, true);
    }
}
