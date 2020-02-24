package com.example.demo.namespace;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Configuration
class NamespaceConfiguration {

    private final List<String> namespaces;

    public NamespaceConfiguration(@NotEmpty @Value("${namespaces}") final List<String> namespaces) {
        this.namespaces = namespaces;
    }

    @Bean
    public Map<String, Namespace> getNamespaces() {
        return Set.of(Namespace.DEVELOPMENT, Namespace.PRODUCTION).stream()
                .filter(namespace -> namespaces.contains(namespace.getName()))
                .collect(Collectors.toMap(Namespace::getName, namespace -> namespace));
    }

}
