package org.example.controllerWeb;

import jakarta.servlet.http.HttpSession;
import org.example.service.JudokaService;
import org.example.service.CompetenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The type Web controller.
 */
@Controller
public class WebController {

    private final JudokaService judokaService;
    private final CompetenciaService competenciaService;

    /**
     * Instantiates a new Web controller.
     *
     * @param judokaService      the judoka service
     * @param competenciaService the competencia service
     */
    public WebController(JudokaService judokaService, CompetenciaService competenciaService) {
        this.judokaService = judokaService;
        this.competenciaService = competenciaService;
    }

    //@GetMapping("/")
    //public String index() {
    //    return "index";
    //}

    /**
     * Root string.
     *
     * @return the string
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    /**
     * Show login string.
     *
     * @param session the session
     * @return the string
     */
    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String tipo = (String) session.getAttribute("tipo");
            if ("judoka".equals(tipo)) return "redirect:/judoka/home";
            if ("club".equals(tipo)) return "redirect:/club/home";
        }
        return "login";
    }

    /**
     * Do login string.
     *
     * @param username the username
     * @param tipo     the tipo
     * @param model    the model
     * @param session  the session
     * @return the string
     */
    @PostMapping("/login")
    public String doLogin(
            @RequestParam("username") String username,
            @RequestParam("tipo") String tipo,
            Model model,
            HttpSession session
    ) {
        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "Usuario vacío");
            return "login";
        }
        // Aquí podrías validar si existe el judoka/club. Por ejemplo:
        session.setAttribute("username", username);
        session.setAttribute("tipo", tipo);

        if ("judoka".equals(tipo)) {
            return "redirect:/judoka/home";
        } else if ("club".equals(tipo)) {
            return "redirect:/club/home";
        } else {
            model.addAttribute("error", "Tipo inválido");
            return "login";
        }
    }

    /**
     * Judoka home string.
     *
     * @param session the session
     * @param model   the model
     * @return the string
     */
    @GetMapping("/judoka/home")
    public String judokaHome(HttpSession session, Model model) {
        if (!esJudoka(session)) return "redirect:/login";
        model.addAttribute("username", session.getAttribute("username"));
        return "judoka_home";
    }

    /**
     * Club home string.
     *
     * @param session the session
     * @param model   the model
     * @return the string
     */
    @GetMapping("/club/home")
    public String clubHome(HttpSession session, Model model) {
        if (!esClub(session)) return "redirect:/login";
        model.addAttribute("username",session.getAttribute("username"));
        return "club_home";
    }

    private boolean esJudoka(HttpSession s) {
        return s.getAttribute("username") != null && "judoka".equals(s.getAttribute("tipo"));
    }
    private boolean esClub(HttpSession s) {
        return s.getAttribute("username") != null && "club".equals(s.getAttribute("tipo"));
    }

    /**
     * Logout string.
     *
     * @param session the session
     * @return the string
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}