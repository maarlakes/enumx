package cn.maarlakes.enumx.fastjson;

import cn.maarlakes.enumx.EnumFlags;
import cn.maarlakes.enumx.EnumValue;
import cn.maarlakes.enumx.Flags;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;

/**
 * @author linjpxc
 */
final class FlagsObjectReader<F extends EnumFlags<F, E, V>, E extends Enum<E> & EnumValue<E, V>, V> implements ObjectReader<F> {
    private final Class<F> type;

    FlagsObjectReader(Class<F> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public F readObject(JSONReader reader, Type fieldType, Object fieldName, long features) {
        if (reader.readIfNull()) {
            return null;
        }
        if (fieldType instanceof Class) {
            return Flags.valueOf((Class<F>) fieldType, reader.readAny(), true);
        }
        return Flags.valueOf(this.type, reader.readAny(), true);
    }
}
