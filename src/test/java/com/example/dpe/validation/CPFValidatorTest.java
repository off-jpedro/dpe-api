package com.example.dpe.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFValidatorTest {

    private CPFValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CPFValidator();
    }

    @Test
    void shouldAcceptValidCPF() {
        assertTrue(validator.isValid("52998224725", null));
    }

    @Test
    void shouldRejectInvalidCheckDigits() {
        assertFalse(validator.isValid("52998224724", null));
    }

    @Test
    void shouldRejectRepeatedDigits() {
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    void shouldRejectCPFWithLessThanElevenDigits() {
        assertFalse(validator.isValid("1234567890", null));
    }

    @Test
    void shouldRejectCPFWithLetters() {
        assertFalse(validator.isValid("12345678ABC", null));
    }

    @Test
    void shouldAcceptNullBecauseNotBlankHandlesRequiredField() {
        assertTrue(validator.isValid(null, null));
    }
}
