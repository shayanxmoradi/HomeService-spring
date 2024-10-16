package org.example.homeservice.controller;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.review.ReviewRequest;
import org.example.homeservice.service.WalletService;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.review.ReviewService;
import org.example.homeservice.service.user.customer.CustomerService;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PaymentResource {


    private final OrderService orderService;
    private final CustomerService customerService;

    private final RestTemplate restTemplate;

   private final WalletService walletService;


    private final ReviewService reviewService;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret ;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String ERROR_PAGE = "error";
    private static final String SUCCESS_PAGE = "payment_success";


    @GetMapping("/payment")
    public String showPaymentForm(@RequestParam Long orderId, Model model) {
        // Fetch the order details using the orderId
        Optional<OrderResponse> orderResponse = orderService.findById(orderId);
        if (orderResponse.get().status() != OrderStatus.DONE) {
            model.addAttribute(ERROR_PAGE, "order is not in done status! you cant pay now");
            return ERROR_PAGE;
        }
        if (orderResponse.isPresent()) {
            model.addAttribute("orderId", orderId);

            return "payment";
        }

        model.addAttribute(ERROR_PAGE, "Order not found.");
        return ERROR_PAGE;
    }

    @GetMapping("/paywithwallet")
    public String processPayment(@RequestParam Long orderId, Model model) {

        OrderResponse orderResponse = orderService.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Long customerId = orderResponse.customerId();
        CustomerResponseDto customerResponseDto = customerService.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Long walletId = customerResponseDto.walletId();
        boolean hasEnoughCreditCards = false;
        try {

            walletService.removeMoneyFromWallet(walletId,orderResponse.offeredPrice());
            hasEnoughCreditCards = true;
        }catch (Exception e){

        }

        if (hasEnoughCreditCards) {
            paiedOrderSetup(orderId,OrderStatus.PAIED_WITH_WALLET);
//        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
//        redirectAttributes.addFlashAttribute("orderId", orderId);
            model.addAttribute("orderId", orderId);

            return "/payment_sucess";
        }
        System.out.println("not enough credit cards");
        model.addAttribute("errorMessage", "not enough money in wallet!");
        model.addAttribute("orderId", orderId);
        return ERROR_PAGE;
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

        System.out.println(expiryDate);
        System.out.println("20" + expiryDate.substring(3, 5));
        System.out.println(expiryDate.substring(0, 2));

        Card cardRequest = new Card(cardNumber, LocalDate.of(Integer.parseInt("20" + expiryDate.substring(3, 5)), Integer.parseInt(expiryDate.substring(0, 2)), 20), cvv); // Create a DTO for the request
        System.out.println("checkedCard" + cardRequest);
        // Call Bank API to validate the card
        String bankApiUrl = "http://localhost:8085/bank/valid";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(bankApiUrl, cardRequest, Boolean.class);
        System.out.println(response.getBody());


        if (response.getBody() == null || !response.getBody()) {
            System.out.println("errorMessage");
            model.addAttribute("errorMessage", "Invalid card details. Please check your information.");
            model.addAttribute("orderId", orderId);
            return ERROR_PAGE;
        }


        if (!errors.isEmpty()) {
            model.addAttribute("errorMessage", errors);
            model.addAttribute("orderId", orderId);
            return ERROR_PAGE;
        }
        System.out.println(orderId);
        paiedOrderSetup(orderId,OrderStatus.PAID);
//        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
//        redirectAttributes.addFlashAttribute("orderId", orderId);
        model.addAttribute("orderId", orderId);


        return "/payment_sucess";
    }


    @GetMapping("/paymentSuccess")
    public String showSuccessPage(Model model, @RequestParam Long orderId) {
        model.addAttribute("orderId", orderId);
        System.out.println("asldkfjasldkfj" + orderId);

        return "payment_sucess";
    }

    @GetMapping("/submitRating")
    public String getRatingPage(Model model, @RequestParam Long orderId) {
        System.out.println("asldkfjasldkfj" + orderId);
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


        ReviewRequest reviewRequest = new ReviewRequest(
                orderId,
                rating,
                comments
        );


        try {
            System.out.println(orderId + "\n" + rating + comments);
            reviewService.addReview(reviewRequest);
            modelAndView.addObject("message", "Thank you for your feedback!"); // Successful submission message
        } catch (DataIntegrityViolationException e) {
            // This exception is thrown when there's a duplicate key violation
            modelAndView.setViewName(ERROR_PAGE); // You can redirect to an error page or stay on the same page
            modelAndView.addObject("errorMessage", "A review for this order has already been submitted.");
        }

        return modelAndView;
    }


    private void paiedOrderSetup(Long orderId,OrderStatus orderStatus) {
        //first set status to online paied
        orderService.onlinePayment(orderId,orderStatus);

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

@AllArgsConstructor
@Setter
@Getter
class Card {


    //    @Length(min = 16, max = 16, message = "should be 16 digits")
    private String cardNumber;
    //    @NotBlank(message = "cant be null")

    private LocalDate expirationDate;

    private String cvv;

    @Override
    public String toString() {
        return "Card{" +
               "cardNumber='" + cardNumber + '\'' +
               ", expirationDate=" + expirationDate +
               ", cvv='" + cvv + '\'' +
               '}';
    }
}
