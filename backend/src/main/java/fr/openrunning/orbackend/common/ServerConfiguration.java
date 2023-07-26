package fr.openrunning.orbackend.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class ServerConfiguration {
    @Value("${openrunning.frontend.url}")
    @Getter
    private String frontendUrl;
}
