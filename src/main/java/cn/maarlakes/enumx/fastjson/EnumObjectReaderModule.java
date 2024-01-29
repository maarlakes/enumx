package cn.maarlakes.enumx.fastjson;

import cn.maarlakes.enumx.EnumFlags;
import cn.maarlakes.enumx.EnumValue;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.reader.ObjectReader;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Type;

/**
 * @author linjpxc
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class EnumObjectReaderModule implements ObjectReaderModule {

    private EnumObjectReaderModule() {
    }

    @Nonnull
    public static EnumObjectReaderModule getInstance() {
        return Helper.INSTANCE;
    }

    @Override
    public ObjectReader getObjectReader(Type type) {
        if (type instanceof Class) {
            final Class clazz = (Class) type;
            if (EnumFlags.class.isAssignableFrom(clazz)) {
                return new FlagsObjectReader<>(clazz);
            }
            if (clazz.isEnum() && EnumValue.class.isAssignableFrom(clazz)) {
                return new EnumObjectReader(clazz);
            }
        }
        return null;
    }

    private static final class Helper {
        static final EnumObjectReaderModule INSTANCE = new EnumObjectReaderModule();
    }
}
