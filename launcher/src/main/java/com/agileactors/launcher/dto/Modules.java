package com.agileactors.launcher.dto;

import java.util.List;
import java.util.Optional;

public record Modules(List<String> paths) {

    public Optional<String> findModule(String path) {
        return paths.stream()
                .filter(path::startsWith)
                .findFirst();
    }
}
