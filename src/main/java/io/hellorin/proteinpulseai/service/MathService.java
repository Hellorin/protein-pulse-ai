package io.hellorin.proteinpulseai.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * Service class that provides mathematical operations.
 * This service is designed to be used with Spring AI tools for performing
 * basic mathematical calculations.
 */
@Service
public class MathService {

    /**
     * Adds all the provided double numbers and returns the sum formatted to 2 decimal places.
     * This method is annotated with @Tool to be used as a Spring AI tool.
     * 
     * Note: This implementation was created to handle Double precision issues that were
     * encountered when letting OpenAI perform the calculations directly. The method ensures
     * consistent and accurate results by using Java's built-in Double operations and proper
     * decimal formatting.
     *
     * @param numbers Variable number of double values to be added
     * @return String representation of the sum formatted to 2 decimal places, or "0.00" if no numbers provided
     */
    @Tool(description = "Add all double passed in parameters")
    public String addAll(Double... numbers) {
        return Stream.of(numbers).reduce(Double::sum).map(sum -> String.format("%.2f", sum)).orElse("0.00");
    }
}
