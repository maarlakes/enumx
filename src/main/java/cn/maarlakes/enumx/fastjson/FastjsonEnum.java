package cn.maarlakes.enumx.fastjson;

import com.alibaba.fastjson2.JSON;

/**
 * @author linjpxc
 */
public final class FastjsonEnum {
    private FastjsonEnum() {
    }

    static {
        System.out.println(1111);
    }

    public static void register() {
        JSON.register(EnumObjectReaderModule.getInstance());
        JSON.register(EnumObjectWriterModule.getInstance());
    }
}
