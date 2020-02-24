package com.example.demo.controller;

import com.example.demo.namespace.Namespace;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@RestController
class NamespaceController {

    private final Map<String, Namespace> namespaces;

    @GetMapping("/namespaces")
    public Collection<Namespace> namespaces() {
        return namespaces.values();
    }

    @GetMapping("/namespaces/{namespace}")
    public Namespace namespace(@PathVariable("namespace") final Namespace namespace) {
        return namespace;
    }

}
