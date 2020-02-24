package com.example.demo.namespace;

import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Map;

@AllArgsConstructor
public class String2NamespaceConverter implements Converter<String, Namespace>, org.springframework.core.convert.converter.Converter<String, Namespace> {

    private Map<String, Namespace> namespaces;

    @Override
    public Namespace convert(final MappingContext<String, Namespace> context) {
        return namespaces.get(context.getSource());
    }

    @Override
    public Namespace convert(final String source) {
        return namespaces.get(source);
    }
}

