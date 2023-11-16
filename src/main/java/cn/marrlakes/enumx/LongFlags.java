package cn.marrlakes.enumx;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public abstract class LongFlags<F extends LongFlags<F, E>, E extends Enum<E> & EnumValue<E, Long>> implements EnumFlags<F, E, Long> {

    @Override
    public boolean contains(Long flagValue) {
        if (flagValue == null) {
            return false;
        }
        return Objects.equals(this.value() & flagValue, flagValue);
    }

    @Nonnull
    @Override
    public F add(@Nonnull Long flagValue) {
        final long value = this.value();
        final long newFlagValue = value | flagValue;
        if (newFlagValue == value) {
            return (F) this;
        }
        return createFlag(newFlagValue);
    }

    @Nonnull
    @Override
    public F remove(@Nonnull Long flag) {
        final long value = this.value();
        final long newFlagValue = value & (~flag);
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
        long value = this.value();
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
            appendFlag(builder, Long.toUnsignedString(value));
        }
        if (builder.length() < 1) {
            return this.value().toString();
        }
        return builder.toString();
    }

    @Nonnull
    protected abstract F createFlag(long value);

    private static void appendFlag(@Nonnull StringBuilder builder, @Nonnull String name) {
        if (builder.length() > 0) {
            builder.append("|");
        }
        builder.append(name);
    }
}
