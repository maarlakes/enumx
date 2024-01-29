package cn.maarlakes.enumx.fastjson;

import cn.maarlakes.enumx.Valuable;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import com.alibaba.fastjson2.writer.ObjectWriter;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Type;

/**
 * @author linjpxc
 */
@SuppressWarnings("rawtypes")
public final class EnumObjectWriterModule implements ObjectWriterModule {
    private EnumObjectWriterModule() {
    }

    @Nonnull
    public static EnumObjectWriterModule getInstance() {
        return Helper.INSTANCE;
    }

    @Override
    public ObjectWriter getObjectWriter(Type objectType, Class objectClass) {
        if (Valuable.class.isAssignableFrom(objectClass)) {
            return ValueObjectWriter.INSTANCE;
        }
        return null;
    }

    private static final class Helper {
        static final EnumObjectWriterModule INSTANCE = new EnumObjectWriterModule();
    }
}
