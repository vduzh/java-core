package by.duzh.springframework.core.convert.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.GenericConversionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterRegistryTest {
    private ConversionService conversionService;

    @BeforeEach
    void init() {
        final GenericConversionService conversionService = new GenericConversionService();

        ConverterRegistry converterRegistry = conversionService;
        converterRegistry.addConverter(new Converter<String, Integer>() {
            @Override
            public Integer convert(String source) {
                return Integer.parseInt(source);
            }
        });
        converterRegistry.addConverter(new Converter<String, String>() {
            @Override
            public String convert(String source) {
                return source.toUpperCase();
            }
        });

        this.conversionService = conversionService;
    }

    @Test
    void test() {
        assertEquals(123, conversionService.convert("123", Integer.class).intValue());
        assertEquals("FOO", conversionService.convert("foo", String.class));
    }
}
