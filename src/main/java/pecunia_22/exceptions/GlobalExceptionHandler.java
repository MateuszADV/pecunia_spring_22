package pecunia_22.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CountryNotFoundException.class)
    public String handleCountryNotFound(CountryNotFoundException ex, Model model) {

        model.addAttribute("errorMessage", ex.getMessage());
//        return "error/404";
        return "error";
    }
}
