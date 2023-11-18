package cn.maarlakes.enumx.jackson;

import cn.maarlakes.enumx.Valuable;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author linjpxc
 */
public final class EnumModule extends SimpleModule {
    private static final long serialVersionUID = -7253581390300586451L;

    public EnumModule() {
        this.addSerializer(new ValueSerializer());
        this.addKeySerializer(Valuable.class, new ValueKeySerializer<>(Valuable.class));

        this.setDeserializers(new ValueDeserializers());
        this.setKeyDeserializers(new ValueKeyDeserializers());
    }
}
