package pecunia_22.controllers.viewControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import utils.JsonUtils;

import java.time.LocalDateTime;

@Controller
public class AppErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(AppErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");
        Object exception = request.getAttribute("javax.servlet.error.exception");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (uri == null) uri = request.getRequestURI();
        System.out.println("+++++++++++++++++++ERROR++++++++++++++++++++++++++++");
//        System.out.println(JsonUtils.gsonPretty(request));
        System.out.println(JsonUtils.gsonPretty(request.getTrailerFields()));
        System.out.println(JsonUtils.gsonPretty(logger));
        System.out.println("+++++++++++++++++++ERROR++++++++++++++++++++++++++++");
        System.out.println(uri.toString());

        // ✅ logowanie do konsoli / pliku
        logger.error("""

                🚨 BŁĄD SYSTEMU — PE CUNIA ALERT 🚨
                ================================
                Data i czas: {}
                Status: {}
                Adres URL: {}
                Wiadomość: {}
                Wyjątek: {}
                ================================
                """,
                LocalDateTime.now(),
                statusCode != null ? statusCode : "Brak statusu",
                uri,
                message != null ? message : "Brak wiadomości",
                exception != null ? exception : "Brak szczegółów"
        );

        // przekazanie do modelu dla widoku error.html
        model.addAttribute("status", statusCode != null ? statusCode : "Nieznany");
        model.addAttribute("message", message != null ? message : "Wystąpił nieoczekiwany błąd");
        model.addAttribute("exception", exception);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", uri);

        return "error";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // szablon access-denied.html w templates/
    }

//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        // Możesz też wyciągnąć status np. 404, 500 itd.
//        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
//        return "error"; // thymeleaf template: error.html
//    }
}
