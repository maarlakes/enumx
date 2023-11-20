package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.Valuable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.annotation.Nonnull;

import java.io.IOException;

/**
 * @author linjpxc
 */
abstract class BaseValueDeserializer<T extends Valuable<V>, V> extends StdDeserializer<T> {

    protected final Class<V> valueType;

    @SuppressWarnings("unchecked")
    protected BaseValueDeserializer(Class<?> valueType) {
        super(valueType);
        if (Valuable.class.isAssignableFrom(valueType)) {
            throw new IllegalArgumentException();
        }
        this.valueType = (Class<V>) valueType;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (Number.class.isAssignableFrom(this.valueType)) {
            final Number number = p.getNumberValue();
            if (number == null) {
                return null;
            }
            return this.deserializer(convertValue(number));
        }
        String text = p.getText();
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.isEmpty()) {
            return null;
        }
        return this.deserializer(text);
    }

    protected abstract T deserializer(@Nonnull Object value);

    private Object convertValue(@Nonnull Number number) {
        if (Integer.class.isAssignableFrom(this.valueType)) {
            return number.intValue();
        }
        if (Long.class.isAssignableFrom(this.valueType)) {
            return number.longValue();
        }
        if (Short.class.isAssignableFrom(this.valueType)) {
            return number.shortValue();
        }
        if (Byte.class.isAssignableFrom(this.valueType)) {
            return number.byteValue();
        }
        if (Float.class.isAssignableFrom(this.valueType)) {
            return number.floatValue();
        }
        if (Double.class.isAssignableFrom(this.valueType)) {
            return number.doubleValue();
        }
        return number;
    }
}
