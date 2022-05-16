package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers (Model model) {
        model.addAttribute("allUsers", userService.listUser());
        return "/admin";
    }

    @GetMapping("/new")
    public String newUser (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getRoleList());
        return "new";
    }

    @PostMapping()
    public String create (@ModelAttribute ("user") User user, @RequestParam("role") List<String> role) {
        Collection<Role> roleList = userService.getSetOfRoles(role);
        user.setRoles(roleList);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping ("{id}")
    public String updateUserForm (@PathVariable ("id") int id, Model model) {
        model.addAttribute("update", userService.getUserById(id));
        model.addAttribute("allRole", roleService.getRoleList());
        return "update";
    }

    @PatchMapping ("{id}")
    public String update (@ModelAttribute ("update") User user, @RequestParam ("roleList") List<String> role) {
        user.setRoles(userService.getSetOfRoles(role));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("delete/{id}")
    public String delete (@PathVariable ("id") int id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

}
