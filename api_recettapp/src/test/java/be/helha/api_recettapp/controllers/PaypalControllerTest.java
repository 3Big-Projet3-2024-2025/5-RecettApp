package be.helha.api_recettapp.controllers;


import be.helha.api_recettapp.services.PaypalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaypalControllerTest {
    @Mock
    private PaypalService paypalService;

    @InjectMocks
    private PaypalController paypalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPay() {
        double total = 100.0;
        String redirectUrl = "https://www.paypal.com/checkoutnow?token=example";

        // Mock service behavior
        when(paypalService.createPayment(eq(total), eq("USD"), anyString(), anyString()))
                .thenReturn(redirectUrl);

        // Call the controller method
        String actualUrl = paypalController.pay(total);

        // Verify and assert
        assertEquals(redirectUrl, actualUrl);
        verify(paypalService, times(1)).createPayment(eq(total), eq("USD"), anyString(), anyString());
    }
}
