package cn.maarlakes.enumx;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public abstract class TextFlags<F extends TextFlags<F, E>, E extends Enum<E> & EnumValue<E, String>> implements EnumFlags<F, E, String> {

    public static final String DEFAULT_DELIMITER = "|";
    protected final String delimiter;
    protected final String value;
    protected final Set<String> valueSet;

    protected TextFlags(@Nonnull String value) {
        this(value, DEFAULT_DELIMITER);
    }

    protected TextFlags(@Nonnull String value, @Nonnull String delimiter) {
        this.delimiter = delimiter.trim();
        if (this.delimiter.isEmpty()) {
            throw new IllegalArgumentException("Invalid delimiter");
        }

        this.valueSet = convert(this.splitFlags(value));
        if (this.valueSet.isEmpty()) {
            throw new IllegalArgumentException("Invalid flag value");
        }
        this.value = String.join(this.delimiter, this.valueSet);
    }

    @Override
    public boolean contains(String flagValue) {
        if (flagValue == null) {
            return false;
        }
        flagValue = flagValue.trim();
        if (flagValue.isEmpty()) {
            return false;
        }
        if (Objects.equals(this.value, flagValue)) {
            return true;
        }

        return this.valueSet.containsAll(convert(this.splitFlags(flagValue)));
    }

    @Nonnull
    @Override
    public F add(@Nonnull String flagValue) {
        if (this.contains(flagValue)) {
            return (F) this;
        }
        return this.removeFlags(this.value + this.delimiter + flagValue);
    }

    @Nonnull
    @Override
    public F remove(@Nonnull String flag) {
        return this.removeFlags(this.value, flag);
    }

    protected String[] splitFlags(@Nonnull String flagsValue) {
        return flagsValue.split("\\|");
    }

    @Nonnull
    @Override
    public final String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return Flags.equals((F) this, obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    protected F removeFlags(@Nonnull String value, String... flags) {
        final F noneFlag = this.noneFlag();
        if (noneFlag == null && flags.length == 0) {
            return this.createFlag(value);
        }
        final String noneText = noneFlag == null ? null : noneFlag.value();
        final Set<String> set = convert(this.splitFlags(value));
        set.addAll(this.valueSet);
        if (flags.length > 0) {
            set.removeIf(item -> {
                for (String flag : flags) {
                    if (item.equals(flag)) {
                        return true;
                    }
                }
                return false;
            });
        }

        if (noneText != null) {
            set.remove(noneText);
            if (set.isEmpty()) {
                return noneFlag;
            }
        }
        return this.createFlag(String.join(this.delimiter, set));
    }

    @Nonnull
    protected abstract F createFlag(@Nonnull String value);

    protected F noneFlag() {
        return null;
    }

    protected static Set<String> convert(@Nonnull String[] array) {
        return Arrays.stream(array).map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.toSet());
    }
}
