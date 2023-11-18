package cn.maarlakes.enumx;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author linjpxc
 */
public interface EnumValue<E extends Enum<E>, T> extends Valuable<T> {

    @Nonnull
    Class<E> getDeclaringClass();

    @Nonnull
    static <E extends Enum<E> & EnumValue<E, T>, T> E valueOf(@Nonnull Class<E> enumType, @Nonnull T value) {
        Objects.requireNonNull(value, "Value is null");
        final E[] values = enumType.getEnumConstants();
        for (E item : values) {
            if (Objects.equals(item.value(), value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + " value: " + value);
    }

    @Nonnull
    static <E extends Enum<E> & EnumValue<E, V>, V> String toString(@Nonnull E e) {
        final V value = e.value();
        if (value instanceof CharSequence) {
            return value.toString();
        }

        return String.format("%s: %s", e.name(), value);
    }

    @SuppressWarnings("unchecked")
    static <E extends Enum<E> & EnumValue<E, V>, V> int compare(E left, E right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }

        final Class<V> type = left.valueType();
        if (Comparable.class.isAssignableFrom(type)) {
            final Comparable<Object> comparable = (Comparable<Object>) left.value();
            return comparable.compareTo(right.value());
        }

        return left.compareTo(right);
    }
}
