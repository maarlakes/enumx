package cn.maarlakes.enumx;

import jakarta.annotation.Nonnull;

import java.lang.reflect.Array;
import java.util.*;

public interface EnumFlags<F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> extends Iterable<E>, Valuable<V> {

    default boolean contains(F flag) {
        if (flag == null) {
            return false;
        }
        return this.contains(flag.value());
    }

    default boolean contains(E enumValue) {
        if (enumValue == null) {
            return false;
        }
        return this.contains(enumValue.value());
    }

    boolean contains(V flagValue);

    @Nonnull
    @SuppressWarnings("unchecked")
    default F add(@Nonnull F flag) {
        if (this.value().equals(flag.value())) {
            return (F) this;
        }
        return this.add(flag.value());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    default F add(@Nonnull E flag) {
        if (this.value().equals(flag.value())) {
            return (F) this;
        }
        return this.add(flag.value());
    }

    @Nonnull
    F add(@Nonnull V flagValue);

    @Nonnull
    default F remove(@Nonnull F flag) {
        return this.remove(flag.value());
    }

    @Nonnull
    default F remove(@Nonnull E flag) {
        return this.remove(flag.value());
    }

    @Nonnull
    F remove(@Nonnull V flag);

    default boolean isDefined() {
        return Enums.exists(this.enumType(), this.value());
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    default E[] toEnumArray() {
        final List<E> list = new ArrayList<>();
        for (E item : this.enumType().getEnumConstants()) {
            if (this.contains(item)) {
                list.add(item);
            }
        }
        return list.toArray((E[]) Array.newInstance(this.enumType(), list.size()));
    }

    @Nonnull
    Class<E> enumType();

    @Nonnull
    @Override
    default Iterator<E> iterator() {
        final E[] array = this.enumType().getEnumConstants();
        return new Iterator<E>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                while (this.index < array.length) {
                    if (contains(array[this.index])) {
                        return true;
                    }
                    this.index++;
                }
                return false;
            }

            @Override
            public E next() {
                if (this.hasNext()) {
                    return array[this.index++];
                }
                throw new NoSuchElementException();
            }
        };
    }
}
