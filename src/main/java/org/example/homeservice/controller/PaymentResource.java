package org.example.homeservice.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.validator.ReviewRequest;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PaymentResource {

    @Autowired
    private OrderService orderService;

    private final ReviewService reviewService;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret = "6LcyY18qAAAAAOii_eFrl7pb1F_T7aQuhJgbLJ1Y";

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
//    @GetMapping("/payment/{id}")
//    public String showPaymentForm(Model model) {
//        model.addAttribute("paymentRequest", new PaymentRequest());
//        return "payment";
//    }

    @GetMapping("/payment")
    public String showPaymentForm(@RequestParam Long orderId, Model model) {
        // Fetch the order details using the orderId
        Optional<OrderResponse> orderResponse = orderService.findById(orderId);
        if (orderResponse.get().status() != OrderStatus.DONE) {
            model.addAttribute("error", "order is not in done status! you cant pay now");
            return "error";
        }
        if (orderResponse.isPresent()) {
            model.addAttribute("orderId", orderId);

            return "payment";
        }

        model.addAttribute("error", "Order not found.");
        return "error";
    }


    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("cardNumber") @NotBlank @Pattern(regexp = "\\d{16}") String cardNumber,
                                 @RequestParam("expiryDate") @NotBlank @Pattern(regexp = "(0[1-9]|1[0-2])\\/\\d{2}") String expiryDate,
                                 @RequestParam("cvv") @NotBlank @Pattern(regexp = "\\d{3}") String cvv,
                                 @RequestParam("g-recaptcha-response") String recaptchaResponse,
                                 @RequestParam("orderId") Long orderId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        Map<String, String> errors = new HashMap<>();

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
        paiedOrderSetup(orderId);
        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
        redirectAttributes.addFlashAttribute("orderId", orderId); // Add the order ID here

        return "/payment_sucess";
    }


    @GetMapping("/paymentSuccess")
    public String showSuccessPage(Model model, @RequestParam Long orderId) {
        model.addAttribute("orderId", orderId);
        return "payment_sucess";
    }

    @GetMapping("/submitRating")
    public String getRatingPage(Model model, @RequestParam Long orderId) {
        model.addAttribute("orderId", orderId);
        return "rating";
    }


    @PostMapping("/submitRating")
    public ModelAndView submitRating(
            @RequestParam("orderId") Long orderId,
            @RequestParam("rating") int rating,
            @RequestParam(value = "comments", required = false) String comments) {

        ModelAndView modelAndView = new ModelAndView("thankYou");
        modelAndView.addObject("orderId", orderId);


        ReviewRequest reviewRequest= new ReviewRequest(
                orderId,
                rating,
                comments
        )        ;


        try {
            System.out.println(orderId + "\n" + rating + comments);
            reviewService.addReview(reviewRequest);
            modelAndView.addObject("message", "Thank you for your feedback!"); // Successful submission message
        } catch (DataIntegrityViolationException e) {
            // This exception is thrown when there's a duplicate key violation
            modelAndView.setViewName("error"); // You can redirect to an error page or stay on the same page
            modelAndView.addObject("errorMessage", "A review for this order has already been submitted.");
        }

        return modelAndView;
    }


    private void paiedOrderSetup(Long orderId) {
        //first set status to online paied
        orderService.onlinePayment(orderId);
        // add 70% money to specilist wallet
        //todo wallte
        //reduce specilist rating if delayed
    }






    private boolean verifyCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", captchaResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                RECAPTCHA_VERIFY_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

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

@Data
class ReceiptDto implements Serializable {
    private Double price;
    private Long orderId;
    private Long customerId;
    private Long specialistId;
}
