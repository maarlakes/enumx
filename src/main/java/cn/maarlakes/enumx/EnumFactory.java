package cn.maarlakes.enumx;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linjpxc
 */
final class EnumFactory<E extends Enum<E>> {
    private static final Logger log;

    static {
        Logger logger = null;
        try {
            logger = LoggerFactory.getLogger(EnumFactory.class);
        } catch (Exception ignored) {
        }
        log = logger;
    }

    private final Class<E> enumType;

    private final Map<Object, E> enumValues = new ConcurrentHashMap<>();

    EnumFactory(@Nonnull Class<E> enumType) {
        this.enumType = enumType;
    }


    @Nonnull
    @SuppressWarnings("unchecked")
    public E createEnum(@Nonnull Object value) {
        return this.enumValues.computeIfAbsent(value, k -> {
            if (log != null && log.isWarnEnabled()) {
                log.warn("Note that the enum type value does not exist and will be created dynamically. enum type: {}, enum value: {}", this.enumType, value);
            }
            final Constructor<?> constructor = this.enumType.getDeclaredConstructors()[0];
            final Object[] args = new Object[constructor.getParameterCount()];
            args[0] = value.toString();
            args[1] = this.enumType.getEnumConstants().length + this.enumValues.size();
            if (args.length > 2) {
                args[2] = value;
            }

            try {
                return (E) ReflectionFactory.getReflectionFactory().newConstructorAccessor(constructor).newInstance(args);
            } catch (Exception e) {
                throw new IllegalStateException("动态创建枚举失败", e);
            }
        });
    }
}
