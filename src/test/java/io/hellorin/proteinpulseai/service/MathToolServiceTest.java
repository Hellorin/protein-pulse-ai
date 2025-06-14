package io.hellorin.proteinpulseai.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MathToolServiceTest {

    private final MathToolService mathToolService = new MathToolService();

    @Test
    @DisplayName("Should return 0.00 when no numbers are provided")
    void addAll_WithNoNumbers_ReturnsZero() {
        String result = mathToolService.sumAll();
        assertEquals("0.00", result);
    }

    @Test
    @DisplayName("Should correctly add two positive numbers")
    void addAll_WithTwoPositiveNumbers_ReturnsCorrectSum() {
        String result = mathToolService.sumAll(1.5, 2.5);
        assertEquals("4.00", result);
    }

    @Test
    @DisplayName("Should correctly add multiple numbers including decimals")
    void addAll_WithMultipleNumbers_ReturnsCorrectSum() {
        String result = mathToolService.sumAll(1.1, 2.2, 3.3, 4.4);
        assertEquals("11.00", result);
    }

    @Test
    @DisplayName("Should handle negative numbers correctly")
    void addAll_WithNegativeNumbers_ReturnsCorrectSum() {
        String result = mathToolService.sumAll(-1.5, 2.5, -3.0);
        assertEquals("-2.00", result);
    }

    @Test
    @DisplayName("Should handle very small decimal numbers")
    void addAll_WithSmallDecimals_ReturnsCorrectSum() {
        String result = mathToolService.sumAll(0.001, 0.002, 0.003);
        assertEquals("0.01", result);
    }

    @Test
    @DisplayName("Should handle very large numbers")
    void addAll_WithLargeNumbers_ReturnsCorrectSum() {
        String result = mathToolService.sumAll(1000000.0, 2000000.0);
        assertEquals("3000000.00", result);
    }
} 