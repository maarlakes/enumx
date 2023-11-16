package cn.marrlakes.enumx;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public abstract class IntFlags<F extends IntFlags<F, E>, E extends Enum<E> & EnumValue<E, Integer>> implements EnumFlags<F, E, Integer> {
    @Override
    public boolean contains(Integer flagValue) {
        if (flagValue == null) {
            return false;
        }
        return Objects.equals(this.value() & flagValue, flagValue);
    }

    @Nonnull
    @Override
    public F add(@Nonnull Integer flagValue) {
        final int value = this.value();
        final int newFlagValue = value | flagValue;
        if (newFlagValue == value) {
            return (F) this;
        }
        return createFlag(newFlagValue);
    }

    @Nonnull
    @Override
    public F remove(@Nonnull Integer flag) {
        final int value = this.value();
        final int newFlagValue = value & (~flag);
        if (newFlagValue == value) {
            return (F) this;
        }
        return createFlag(newFlagValue);
    }

    @Override
    public boolean equals(Object obj) {
        return Flags.equals((F) this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value());
    }

    @Override
    public String toString() {
        int value = this.value();
        final E[] enums = this.enumType().getEnumConstants();
        final StringBuilder builder = new StringBuilder();
        for (int i = enums.length - 1; i >= 0; i--) {
            final E item = enums[i];
            if ((value & item.value()) == item.value()) {
                if (builder.length() > 0) {
                    builder.insert(0, item.name() + "|");
                } else {
                    builder.append(item.name());
                }
                value &= (~item.value());
                if (value == 0) {
                    break;
                }
            }
        }
        if (value != 0) {
            appendFlag(builder, Integer.toUnsignedString(value));
        }
        if (builder.length() < 1) {
            return this.value().toString();
        }
        return builder.toString();
    }

    @Nonnull
    protected abstract F createFlag(int value);

    protected static void appendFlag(@Nonnull StringBuilder builder, @Nonnull String name) {
        if (builder.length() > 0) {
            builder.append("|");
        }
        builder.append(name);
    }
}
