package org.example.homeservice.controller.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller

public class PaymentResource {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret="6LcyY18qAAAAAOii_eFrl7pb1F_T7aQuhJgbLJ1Y";

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    @GetMapping("/payment")
    public String showPaymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        return "payment";
    }






    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("cardNumber") @NotBlank @Pattern(regexp = "\\d{16}") String cardNumber,
                                 @RequestParam("expiryDate") @NotBlank @Pattern(regexp = "(0[1-9]|1[0-2])\\/\\d{2}") String expiryDate,
                                 @RequestParam("cvv") @NotBlank @Pattern(regexp = "\\d{3}") String cvv,
                                 @RequestParam("g-recaptcha-response") String recaptchaResponse,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        Map<String, String> errors = new HashMap<>();
        System.out.println(recaptchaResponse);

        if (!cardNumber.matches("\\d{16}")) {
            errors.put("cardNumberError", "Card number must be 16 digits.");
        }

        if (!expiryDate.matches("(0[1-9]|1[0-2])\\/\\d{2}")) {
            errors.put("expiryDateError", "Expiration date must be in MM/YY format.");
        }

        if (!cvv.matches("\\d{3}")) {
            errors.put("cvvError", "CVV must be a 3-digit number.");
        }

        if (!verifyCaptcha(recaptchaResponse)) {
            redirectAttributes.addFlashAttribute("error", "Please complete the reCAPTCHA challenge.");
            return "redirect:/payment";
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "payment";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
        return "/payment_sucess";
    }

    @GetMapping("/paymentSuccess")
    public String showSuccessPage() {
        return "paymentSuccess";  // Success page HTML
    }





    private boolean verifyCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare parameters for the reCAPTCHA request
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", captchaResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Call Google's API
        ResponseEntity<Map> response = restTemplate.exchange(
                RECAPTCHA_VERIFY_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Extract the response body and check if captcha is successful
        Map<String, Object> body = response.getBody();
        System.out.println(response.getStatusCode());
        return (Boolean) body.get("success");
    }



}
@Data
 class PaymentRequest {

    @NotBlank(message = "Captcha is required")
    private String gRecaptchaResponse;

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Expiration date is required")
    private String expirationDate;

    @NotBlank(message = "CVV is required")
    @Size(min = 3, max = 3, message = "CVV must be 3 digits")
    private String cvv;

}

