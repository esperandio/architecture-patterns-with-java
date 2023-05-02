package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTests {
    @Test
    @DisplayName("1 + 1 = 2")
    void addsTwoNumbers() {
        Hello hello = new Hello();
        assertEquals(2, hello.add(1, 1), "1 + 1 should equal 2");
    }
}
