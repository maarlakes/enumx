package cn.marrlakes.enumx.jackson;

import cn.marrlakes.enumx.EnumFlags;
import cn.marrlakes.enumx.EnumValue;
import cn.marrlakes.enumx.Flags;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import jakarta.annotation.Nonnull;

import java.io.IOException;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public class FlagsValueKeyDeserializer<F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> extends KeyDeserializer {

    private final Class<F> flagsType;
    private final Class<V> valueType;

    public FlagsValueKeyDeserializer(@Nonnull Class<?> clazz) {
        if (!EnumFlags.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not a subclass of EnumFlags");
        }
        this.flagsType = (Class<F>) clazz;
        this.valueType = Flags.getValueType(this.flagsType);
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        if (key == null) {
            return null;
        }
        final String newKey = key.trim();
        if (newKey.isEmpty()) {
            return key;
        }
        return Flags.valueOf(this.flagsType, this.valueType, newKey, true);
    }
}
