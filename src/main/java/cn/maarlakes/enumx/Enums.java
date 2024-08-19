package cn.maarlakes.enumx;


import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Enums {
    private Enums() {
    }

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final Map<Class<?>, EnumFactory<?>> NEW_ENUM_VALUES = new ConcurrentHashMap<>();

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> Class<V> getValueType(@Nonnull Class<E> clazz) {
        return ValueUtils.getValueType(clazz);
    }

    /**
     * 返回指定的枚举常量。可以是枚举值，也可以是枚举名称(忽略大小写敏感)的枚举常量。不自动转换基础类型。
     *
     * @param enumType 枚举类型class
     * @param value    自定的值
     * @param <E>      枚举类型
     * @param <V>      值类型
     * @return 返回枚举常量
     */
    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E valueOf(@Nonnull Class<E> enumType, @Nonnull Object value) {
        return valueOf(enumType, value, false);
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E valueOf(@Nonnull Class<E> enumType, @Nonnull Class<V> valueType, @Nonnull Object value) {
        return valueOf(enumType, valueType, value, false);
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E valueOf(@Nonnull Class<E> enumType, @Nonnull Object value, boolean primitiveConvert) {
        return valueOf(enumType, getValueType(enumType), value, primitiveConvert);
    }

    /**
     * 返回指定的枚举值，也可以是枚举名称(不区分大小写)的枚举常量。
     *
     * @param enumType         枚举类型class
     * @param value            自定的值
     * @param <E>              枚举类型
     * @param <V>              值类型
     * @param primitiveConvert 基础类型是否自动转换
     * @return 返回枚举常量
     */
    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E valueOf(@Nonnull Class<E> enumType, @Nonnull Class<V> valueType, @Nonnull Object value, boolean primitiveConvert) {
        final E e = valueOf0(enumType, valueType, value, primitiveConvert);
        if (e != null) {
            return e;
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + value);
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E getOrCreate(@Nonnull Class<E> enumType, @Nonnull Object value) {
        return getOrCreate(enumType, value, false);
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E getOrCreate(@Nonnull Class<E> enumType, @Nonnull Class<V> valueType, @Nonnull Object value) {
        return getOrCreate(enumType, valueType, value, false);
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> E getOrCreate(@Nonnull Class<E> enumType, @Nonnull Object value, boolean primitiveConvert) {
        return getOrCreate(enumType, getValueType(enumType), value, primitiveConvert);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E> & EnumValue<E, V>, V> E getOrCreate(@Nonnull Class<E> enumType, @Nonnull Class<V> valueType, @Nonnull Object value, boolean primitiveConvert) {
        final E e = valueOf0(enumType, valueType, value, primitiveConvert);
        if (e != null) {
            return e;
        }
        return (E) NEW_ENUM_VALUES.computeIfAbsent(enumType, k -> new EnumFactory<>(enumType)).createEnum(value);
    }


    /**
     * 返回指定名称的枚举常量。该名称与此类型中声明的枚举常量的标识，忽略大小写比较（不允许使用多余的空白字符）。
     *
     * @param enumType 枚举类型的 class
     * @param name     枚举常量的名称
     * @param <E>      枚举类型
     * @return 具有指定名称的枚举常量
     */
    @Nonnull
    public static <E extends Enum<E>> E valueOfIgnoreCase(@Nonnull Class<E> enumType, @Nonnull String name) {
        if (!enumType.isEnum()) {
            throw new IllegalArgumentException("class not enum.");
        }
        final E[] values = enumType.getEnumConstants();
        for (E item : values) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + name);
    }

    /**
     * 判断指定枚举类型的枚举常量是否存在。
     *
     * @param enumType 枚举类型 class。
     * @param name     枚举常量名称。
     * @param <E>      枚举类型
     * @return 若常量存在，则返回true，否则返回false。
     */
    public static <E extends Enum<E>> boolean exists(@Nonnull Class<E> enumType, @Nonnull String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        final E[] enumConstants = enumType.getEnumConstants();
        for (E item : enumConstants) {
            if (item.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定枚举类型的枚举常量是否存在。忽略大小写。
     *
     * @param enumType 枚举类型 class。
     * @param name     枚举常量名称。
     * @param <E>      枚举类型
     * @return 若常量存在，则返回true，否则返回false。
     */
    public static <E extends Enum<E>> boolean existsIgnoreCase(@Nonnull Class<E> enumType, @Nonnull String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        final E[] enumConstants = enumType.getEnumConstants();
        for (E item : enumConstants) {
            if (item.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定枚举类型的枚举值是否存在。
     *
     * @param enumType 枚举类型 class。
     * @param value    枚举值
     * @param <E>      枚举类型
     * @param <V>      枚举值类型
     * @return 若枚举值存在，则返回true，否则返回false。
     */
    public static <E extends Enum<E> & EnumValue<E, V>, V> boolean exists(@Nonnull Class<E> enumType, @Nonnull V value) {
        final E[] enumConstants = enumType.getEnumConstants();
        for (E item : enumConstants) {
            if (item.value().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static <E extends Enum<E>> boolean exists(@Nonnull Class<E> enumType, @Nonnull E value) {
        final E[] enumConstants = enumType.getEnumConstants();
        for (E enumConstant : enumConstants) {
            if (enumConstant == value) {
                return true;
            }
        }
        return false;
    }

    @Nonnull
    public static <E extends Enum<E>> List<E> list(@Nonnull Class<E> enumType) {
        return new ArrayList<>(Arrays.asList(enumType.getEnumConstants()));
    }

    @Nonnull
    public static <E extends Enum<E>> Map<String, E> map(@Nonnull Class<E> enumType) {
        final E[] enumConstants = enumType.getEnumConstants();
        final Map<String, E> map = new HashMap<>(enumConstants.length);
        for (E item : enumConstants) {
            map.put(item.name(), item);
        }
        return map;
    }

    @Nonnull
    public static <E extends Enum<E> & EnumValue<E, V>, V> Map<V, E> valueMap(@Nonnull Class<E> enumType) {
        final E[] enumConstants = enumType.getEnumConstants();
        final Map<V, E> map = new HashMap<>(enumConstants.length);
        for (final E enumConstant : enumConstants) {
            map.put(enumConstant.value(), enumConstant);
        }
        return map;
    }

    static boolean isPrimitiveOrEnumValueType(Class<?> valueType, boolean primitiveConvert, Object value) {
        return (ClassUtils.isPrimitiveWrapper(valueType) && primitiveConvert)
                || valueType == value.getClass()
                || valueType.isAssignableFrom(value.getClass());
    }

    static Object convertPrimitive(Class<?> clazz, Object value, boolean primitiveConvert) {
        if (!primitiveConvert) {
            return null;
        }
        try {
            return ClassUtils.convertPrimitive(clazz, value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static <E extends Enum<E> & EnumValue<E, V>, V> E valueOf0(@Nonnull Class<E> enumType, @Nonnull Class<V> valueType, @Nonnull Object value, boolean primitiveConvert) {
        final E[] enumConstants = enumType.getEnumConstants();
        if (isPrimitiveOrEnumValueType(valueType, primitiveConvert, value)) {
            final Object primitiveValue = convertPrimitive(valueType, value, primitiveConvert);
            for (E item : enumConstants) {
                if (item.value().equals(value)) {
                    return item;
                } else if (primitiveValue != null && primitiveValue.equals(item.value())) {
                    return item;
                }
            }
        }

        final String name = value.toString();
        for (E item : enumConstants) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        for (E item : enumConstants) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}