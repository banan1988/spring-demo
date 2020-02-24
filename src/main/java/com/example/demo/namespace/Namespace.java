package com.example.demo.namespace;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
public
class Namespace {

    static final Namespace DEVELOPMENT = Namespace.builder().name("development").hasReport(false).build();
    static final Namespace PRODUCTION = Namespace.builder().name("production").build();

    @NonNull
    private final String name;

    @Accessors(fluent = true)
    @Builder.Default
    private final boolean hasReport = true;

}
