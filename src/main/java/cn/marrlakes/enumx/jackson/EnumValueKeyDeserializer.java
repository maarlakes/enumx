package cn.marrlakes.enumx.jackson;

import cn.marrlakes.enumx.EnumValue;
import cn.marrlakes.enumx.Enums;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import jakarta.annotation.Nonnull;

import java.io.IOException;

/**
 * @author linjpxc
 */
@SuppressWarnings("unchecked")
public class EnumValueKeyDeserializer<E extends Enum<E> & EnumValue<E, V>, V> extends KeyDeserializer {

    private final Class<E> enumType;
    private final Class<V> enumValueType;

    public EnumValueKeyDeserializer(@Nonnull Class<?> enumType) {
        if (!(enumType.isEnum() && EnumValue.class.isAssignableFrom(enumType))) {
            throw new IllegalArgumentException();
        }
        this.enumType = (Class<E>) enumType;
        this.enumValueType = Enums.getValueType((Class<E>) enumType);
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
        return Enums.valueOf(this.enumType, this.enumValueType, newKey, true);
    }
}
