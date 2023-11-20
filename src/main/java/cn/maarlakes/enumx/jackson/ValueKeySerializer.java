package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.Valuable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author linjpxc
 */
public class ValueKeySerializer<V extends Valuable<?>> extends StdSerializer<V> {
    protected ValueKeySerializer(Class<?> enumType) {
        super(enumType, true);
        if (!Valuable.class.isAssignableFrom(enumType)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void serialize(V value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeFieldName(value.value().toString());
        }
    }
}
