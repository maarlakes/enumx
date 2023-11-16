package cn.marrlakes.enumx;

import jakarta.annotation.Nonnull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Objects;

public final class Flags {
    private Flags() {
    }


    @Nonnull
    public static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> Class<V> getValueType(@Nonnull Class<F> clazz) {
        return ValueUtils.getValueType(clazz);
    }

    public static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> F valueOf(@Nonnull Class<F> flagsType, @Nonnull Object value) {
        return valueOf(flagsType, getValueType(flagsType), value, false);
    }

    public static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> F valueOf(@Nonnull Class<F> flagsType, @Nonnull Class<V> valueType, @Nonnull Object value) {
        return valueOf(flagsType, valueType, value, false);
    }

    @Nonnull
    public static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> F valueOf(@Nonnull Class<F> flagsType, @Nonnull Object value, boolean primitiveConvert) {
        return valueOf(flagsType, getValueType(flagsType), value, primitiveConvert);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> F valueOf(
            @Nonnull Class<F> flagsType, @Nonnull Class<V> valueType, @Nonnull Object value, boolean primitiveConvert) {
        final Class<?> primitiveType = ClassUtils.getPrimitiveType(valueType);
        final Method createMethod = findCreateMethod(flagsType, valueType, primitiveType);
        if (createMethod != null) {
            final Object obj = invokeMethod(createMethod, value, primitiveConvert);
            if (obj != null) {
                return (F) obj;
            }
        }

        final Constructor<?> constructor = findConstructor(flagsType, valueType, primitiveType);
        if (constructor != null) {
            final Object obj = invokeConstructor(constructor, value, primitiveConvert);
            if (obj != null) {
                return (F) obj;
            }
        }
        throw new IllegalArgumentException("No flags value  " + flagsType.getName());
    }

    @SuppressWarnings("unchecked")
    static <F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> boolean equals
            (F left, Object right) {
        if (left == right) {
            return true;
        }
        if (left == null) {
            return false;
        }
        if (right == null) {
            return false;
        }
        if (left.getClass() != right.getClass()) {
            return false;
        }
        final V rightValue = ((F) right).value();
        return Objects.equals(left.value(), rightValue);
    }

    private static Object invokeMethod(@Nonnull Method method, @Nonnull Object value, boolean primitiveConvert) {
        final Object param = castValue(value, method.getParameterTypes()[0], value.getClass(), primitiveConvert);
        if (param != null) {
            try {
                AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    method.setAccessible(true);
                    return null;
                });
                return method.invoke(null, param);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

    private static Method findCreateMethod(@Nonnull Class<?> flagsType, @Nonnull Class<?> valueType, Class<?>
            primitiveType) {
        return Arrays.stream(flagsType.getDeclaredMethods())
                .filter(item -> Modifier.isStatic(item.getModifiers()))
                .filter(item -> item.getParameterCount() == 1)
                .filter(item -> {
                    final Class<?> parameterType = item.getParameterTypes()[0];
                    if (parameterType.isAssignableFrom(valueType)) {
                        return true;
                    }
                    return primitiveType == parameterType;
                }).findFirst().orElse(null);
    }

    private static Object invokeConstructor(@Nonnull Constructor<?> constructor, @Nonnull Object value,
                                            boolean primitiveConvert) {
        final Object param = castValue(value, constructor.getParameterTypes()[0], value.getClass(), primitiveConvert);
        if (param != null) {
            try {
                AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    constructor.setAccessible(true);
                    return null;
                });
                return constructor.newInstance(param);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    private static Constructor<?> findConstructor(@Nonnull Class<?> flagsType, @Nonnull Class<?> valueType, Class<?>
            primitiveType) {
        return Arrays.stream(flagsType.getDeclaredConstructors())
                .filter(item -> item.getParameterCount() == 1)
                .filter(item -> {
                    final Class<?> parameterType = item.getParameterTypes()[0];
                    if (parameterType.isAssignableFrom(valueType)) {
                        return true;
                    }
                    return primitiveType == parameterType;
                }).findFirst().orElse(null);
    }

    private static Object castValue(@Nonnull Object value, @Nonnull Class<?> parameterType, Class<?> valueType,
                                    boolean primitiveConvert) {
        if (parameterType.isAssignableFrom(valueType)) {
            return value;
        }
        if (parameterType.isPrimitive() && ClassUtils.getPrimitiveType(valueType) == parameterType) {
            return value;
        }
        if (valueType.isPrimitive() && ClassUtils.getPrimitiveType(parameterType) == valueType) {
            return value;
        }

        if (primitiveConvert) {
            final Object castValue = ClassUtils.convertPrimitive(parameterType, value);
            if (castValue != null) {
                return castValue;
            }
        }
        return null;
    }
}
