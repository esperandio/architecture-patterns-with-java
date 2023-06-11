package app.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private AllocateController controller;

    @Test
    void healthcheck() {
        assertThat(controller.healthcheck()).asString().contains("ok");
    }
}
