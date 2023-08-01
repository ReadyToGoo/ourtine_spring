package ourtine.server.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RootController {
    @GetMapping("/health")
    public String HealthCheck() {
        return "I'm healthy!!";
    }

    @GetMapping("/test")
    public String test(){ return "Test Complete~";}
}
