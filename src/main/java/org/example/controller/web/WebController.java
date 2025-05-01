package org.example.controller.web;

import jakarta.servlet.http.HttpSession;
import org.example.service.JudokaService;
import org.example.service.CompetenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final JudokaService atletaService;
    private final CompetenciaService competenciaService;

    public WebController(JudokaService atletaService, CompetenciaService competenciaService) {
        this.atletaService = atletaService;
        this.competenciaService = competenciaService;
    }

    //@GetMapping("/")
    //public String index() {
    //    return "index";
    //}

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String tipo = (String) session.getAttribute("tipo");
            if ("judoka".equals(tipo)) return "redirect:/judoka/home";
            if ("club".equals(tipo)) return "redirect:/club/home";
        }
        return "login";
    }

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

    @GetMapping("/judoka/home")
    public String judokaHome(HttpSession session, Model model) {
        if (!esJudoka(session)) return "redirect:/login";
        model.addAttribute("username", session.getAttribute("username"));
        return "judoka_home";
    }

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}