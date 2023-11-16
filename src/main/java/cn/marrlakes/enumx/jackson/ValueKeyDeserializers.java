package cn.marrlakes.enumx.jackson;

import cn.marrlakes.enumx.EnumFlags;
import cn.marrlakes.enumx.EnumValue;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;

class ValueKeyDeserializers extends SimpleKeyDeserializers {

    @Override
    public KeyDeserializer findKeyDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) {
        final Class<?> rawType = type.getRawClass();
        if (rawType.isEnum() && EnumValue.class.isAssignableFrom(rawType)) {
            return new EnumValueKeyDeserializer<>(rawType);
        }
        if (EnumFlags.class.isAssignableFrom(rawType)) {
            return new FlagsValueKeyDeserializer<>(rawType);
        }
        return super.findKeyDeserializer(type, config, beanDesc);
    }
}
