> enumx 提供枚举值的统一接口，枚举值扩展接口，以及一些常见的枚举工具方法。

## 依赖

```xml

<dependency>
    <groupId>cn.maarlakes</groupId>
    <artifactId>enumx</artifactId>
    <version>1.0.0</version>
</dependency>
```

- 统一的枚举值接口 EnumValue， 并提供默认的 Jackson 序列号模块 EnumModule，可以通过 SPI 注册
- Enums 提供一组 valueOf 方法以简化枚举值查找

### EnumValue  示例

```java
import cn.marrlakes.enumx.EnumValue;

public enum ExampleEnum implements EnumValue<ExampleEnum, Integer> {
    A(1), B(2);

    private final int value;

    private ExampleEnum(int value) {
        this.value = value;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    public static ExampleEnum valueOf(int value) {
        return EnumValue.valueOf(ExampleEnum.class, value);
    }
}
```
