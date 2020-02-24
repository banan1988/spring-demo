package com.example.demo;

import com.example.demo.namespace.Namespace;
import com.example.demo.namespace.String2NamespaceConverter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@AllArgsConstructor
@Configuration
class ModelMapperConfiguration implements WebMvcConfigurer {

    private final Map<String, Namespace> namespaces;

    @Bean
    public ModelMapper getModelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new String2NamespaceConverter(namespaces));
        return modelMapper;
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new String2NamespaceConverter(namespaces));
    }
}
