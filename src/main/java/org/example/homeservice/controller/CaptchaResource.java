package org.example.homeservice.controller;

import cn.apiclub.captcha.Captcha;
import jakarta.servlet.http.HttpServletResponse;
import org.example.homeservice.controller.config.CaptchaGenerator;
import org.example.homeservice.util.CaptchaSettings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class CaptchaResource {
//
//    @GetMapping("/verify")
//    public String register(Model model) {
//        model.addAttribute("captcha", genCaptcha());
//        return "verifyCaptcha";
//    }
//
//    @PostMapping("/verify")
//    public String verify(@ModelAttribute CaptchaSettings captchaSettings, Model model) {
//        if (captchaSettings.getCaptcha().equals(captchaSettings.getHiddenCaptcha())) {
//            model.addAttribute("message", "Captcha verified successfully");
//        } else {
//            model.addAttribute("message", "Invalid Captcha");
//            model.addAttribute("captcha", genCaptcha());
//        }
//        return "verifyCaptcha";
//    }
//
//
//    @GetMapping("/app/payment")
//    public String showPaymentPage(Model model) {
//        model.addAttribute("captcha", new CaptchaDto());
//        return "payment"; // Return the Thymeleaf template name
//    }
//
//    @PostMapping("/app/verify")
//    public String verify(@ModelAttribute("captcha") CaptchaDto captchaDto, Model model) {
//        // Handle the captcha verification logic here
//        // For example, check if the captcha matches the expected value
//        if (captchaDto.getCaptcha() == null || captchaDto.getCaptcha().isEmpty()) {
//            model.addAttribute("message", "Captcha is required");
//            return "payment"; // Return to payment page with an error message
//        }
//
//        // Add your captcha verification logic here
//
//        // If successful, redirect or show success page
//        return "redirect:/success"; // Change to your success page
//    }
//
//
//
//    private CaptchaSettings genCaptcha() {
//        CaptchaSettings captchaSettings = new CaptchaSettings();
//        Captcha captcha = CaptchaGenerator.generateCaptcha(260, 80);
//        captchaSettings.setHiddenCaptcha(captcha.getAnswer());
//        captchaSettings.setCaptcha("");
//        captchaSettings.setRealCaptcha(CaptchaGenerator.encodeCaptchaToBinary(captcha));
//        return captchaSettings;
//    }
//
//    public static boolean cardCheck(String card) {
//        if (card.trim().length() == 16) {
//            try {
//                Long.valueOf(card);
//                return true;
//            } catch (NumberFormatException | NullPointerException e) {
//                return false;
//            }
//        } else return false;
//    }
//
//
//    public class CaptchaDto {
//        private String captcha;
//        private String hiddenCaptcha;
//
//        // Getters and Setters
//        public String getCaptcha() {
//            return captcha;
//        }
//
//        public void setCaptcha(String captcha) {
//            this.captcha = captcha;
//        }
//
//        public String getHiddenCaptcha() {
//            return hiddenCaptcha;
//        }
//
//        public void setHiddenCaptcha(String hiddenCaptcha) {
//            this.hiddenCaptcha = hiddenCaptcha;
//        }
//    }
}

