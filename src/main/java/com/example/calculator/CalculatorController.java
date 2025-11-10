package com.example.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CalculatorController {

    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam double number1,
                            @RequestParam double number2,
                            @RequestParam String operation,
                            Model model) {

        double result = 0;
        String message = "";

        switch (operation) {
            case "addition" -> result = number1 + number2;
            case "subtract" -> result = number1 - number2;
            case "multiply" -> result = number1 * number2;
            case "divide" -> {
                if (number2 == 0) message = "Division by zero not allowed!";
                else result = number1 / number2;
            }
            default -> message = "Invalid operation!";
        }

        model.addAttribute("result", message.isEmpty() ? result : message);
        return "index";
    }

    @PostMapping("/fibonacci")
    public String generateFibonacci(@RequestParam int n, Model model) {
        if (n <= 0) {
            model.addAttribute("error", "Please enter a positive number!");
            return "index";
        }
        String svg = FibonacciCurve.generateFibonacciSVG(n);
        model.addAttribute("svgData", svg);
        model.addAttribute("n", n);
        return "index";
    }
}
