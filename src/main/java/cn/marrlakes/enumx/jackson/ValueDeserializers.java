package cn.marrlakes.enumx.jackson;

import cn.marrlakes.enumx.EnumFlags;
import cn.marrlakes.enumx.EnumValue;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ReferenceType;

/**
 * @author linjpxc
 */
class ValueDeserializers extends SimpleDeserializers {

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        if (type.isEnum() && EnumValue.class.isAssignableFrom(type)) {
            return new EnumValueDeserializer<>(type);
        }
        return super.findEnumDeserializer(type, config, beanDesc);
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        final Class<?> rawType = type.getRawClass();
        if (EnumFlags.class.isAssignableFrom(rawType)) {
            return new FlagsValueDeserializer<>(rawType);
        }
        return super.findBeanDeserializer(type, config, beanDesc);
    }

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {
        final Class<?> rawType = refType.getContentType().getRawClass();
        if (rawType.isEnum() && EnumValue.class.isAssignableFrom(rawType)) {
            return new EnumValueDeserializer<>(rawType);
        }
        if (EnumFlags.class.isAssignableFrom(rawType)) {
            return new FlagsValueDeserializer<>(rawType);
        }

        return super.findReferenceDeserializer(refType, config, beanDesc, contentTypeDeserializer, contentDeserializer);
    }

    @Override
    public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
        return (valueType.isEnum() && EnumValue.class.isAssignableFrom(valueType)) || EnumFlags.class.isAssignableFrom(valueType) || super.hasDeserializerFor(config, valueType);
    }
}
