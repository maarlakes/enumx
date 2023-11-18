package cn.maarlakes.enumx;

import jakarta.annotation.Nonnull;

/**
 * @author linjpxc
 */
public interface FlagEnum<F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> extends EnumValue<E, V> {

    @Nonnull
    F flags();
}
