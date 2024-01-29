package cn.maarlakes.enumx.fastjson;

import cn.maarlakes.enumx.Valuable;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;

/**
 * @author linjpxc
 */
final class ValueObjectWriter<T extends Valuable<T>> implements ObjectWriter<T> {

    static final ValueObjectWriter<?> INSTANCE = new ValueObjectWriter<>();

    @Override
    @SuppressWarnings("rawtypes")
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (object == null) {
            jsonWriter.writeNull();
        } else {
            jsonWriter.writeAny(((Valuable) object).value());
        }
    }
}
