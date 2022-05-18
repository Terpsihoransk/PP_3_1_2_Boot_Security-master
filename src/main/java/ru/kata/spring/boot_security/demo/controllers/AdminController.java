package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    public final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping
    public String getAllUsers (Model model) {
        model.addAttribute("allUsers", userServiceImpl.listUser());
        return "/admin";
    }
    @GetMapping ("/delete/{id}")
    public String deleteUser (@PathVariable ("id") int id) {
        userServiceImpl.removeUser(id);
        return "redirect:/admin";
    }
    @GetMapping ("/update/{id}")
    public String updateUserForm (@PathVariable ("id") int id, Model model) {
        model.addAttribute("update", userServiceImpl.getUserById(id));
        model.addAttribute("allRole", roleServiceImpl.getRoleList());
        return "update";
    }
    @PostMapping ("/update")
    public String updateUser (@ModelAttribute ("update") User user, @RequestParam ("roleList") List<String> role) {
        user.setRoles(userServiceImpl.getSetOfRoles(role));
        userServiceImpl.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping ("/registration")
    public String registration (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleServiceImpl.getRoleList());
        return "registration";
    }
    @PostMapping ("/registration")
    public String addUser (@ModelAttribute ("user") User user, @RequestParam ("role") List<String> role) {
        Collection<Role> roleList = userServiceImpl.getSetOfRoles(role);
        user.setRoles(roleList);
        userServiceImpl.addUser(user);
        return "redirect:/admin";
    }

}
