package org.wflis.javaproj2.controllers;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.wflis.javaproj2.dao.userDao;
import org.wflis.javaproj2.entity.User;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private userDao dao;

    @GetMapping("/login")
    public String loginPage() {
        // zwrócenie nazwy widoku logowania - login.html
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model m, User form) {
        // dodanie do modelu nowego użytkownika
        m.addAttribute("user", new User());
        // zwrócenie nazwy widoku rejestracji - register.html
        return "register";
    }

    @PostMapping("/register")
    public String registerPagePOST(@ModelAttribute(value = "user") @Valid User user, BindingResult binding) {
        if (binding.hasErrors()) {
            return "register"; // powrót do formularza
        }
        // make sure user login is unique else return error
        if (dao.findByLogin(user.getLogin()) != null) {
            binding.rejectValue("login", "error.user", "Login musi być unikalny");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        // przekierowanie do adresu url: /login
        return "redirect:/login";
    }

    @GetMapping("/delete")
    public String deleteAccount(@RequestParam Integer userId){
        dao.deleteById(userId);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {
        // dodanie do modelu obiektu user - aktualnie zalogowanego użytkownika:
        m.addAttribute("user", dao.findByLogin(principal.getName()));
        // zwrócenie nazwy widoku profilu użytkownika - profile.html
        return "profile";
    }

    @GetMapping("/edit")
    public String editPage(Model m, Principal principal){
        m.addAttribute("user", new User());
        User userInfo = dao.findByLogin(principal.getName());
        m.addAttribute("userInfo",userInfo);
        return "edit";
    }

    @PostMapping("/edit")
    public String editPagePUT(Principal principal,@ModelAttribute(value = "user") User user){
        User updateUser = dao.findByLogin(principal.getName());
        updateUser.setName(user.getName());
        updateUser.setSurname(user.getSurname());
        updateUser.setLogin(user.getLogin());
        updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(updateUser);
        return "redirect:/profile";
    }

    @GetMapping("/users")
    // definicja metody, która zwróci do widoku users.html listę użytkowników z bd
    public String usersPage(Model m) {
        // dodanie do modelu listy użytkowników z bd
        m.addAttribute("users", dao.findAll());
        // zwrócenie nazwy widoku users.html
        return "users";
    }
}