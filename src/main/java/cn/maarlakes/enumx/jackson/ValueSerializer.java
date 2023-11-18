package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.Valuable;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author linjpxc
 */
public class ValueSerializer extends StdSerializer<Valuable<?>> {

    protected ValueSerializer() {
        super(Valuable.class, true);
    }

    @Override
    public void serialize(Valuable<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeObject(null);
        } else {
            gen.writeObject(value.value());
        }
    }
}
