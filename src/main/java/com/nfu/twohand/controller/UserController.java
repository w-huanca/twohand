package com.nfu.twohand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nfu.twohand.pojo.*;
import com.nfu.twohand.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private GOrderService orderService;

    @RequestMapping("/home")
    public String home(Model model) {
        List<Student> studentList = studentService.list();
        List<Good> goodList = goodService.list();
        List<Type> typeList = typeService.list();
        List<GOrder> orderList = orderService.list(new QueryWrapper<GOrder>().eq("isorder", 1));
        model.addAttribute("peopleCount", studentList.size());
        model.addAttribute("goodCount", goodList.size());
        model.addAttribute("typeCount", typeList.size());
        model.addAttribute("orderCount", orderList.size());
        model.addAttribute("goodList", goodList);
        return "admin-home";
    }

    @RequestMapping("/login")
    public String login() {
        return "user-login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "index";
    }

    @RequestMapping("/adminlogin")
    public String adminlogin(String username, String password, Model model, HttpSession session) {
        boolean count = userService.login(username, password);
        if (count) {
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("username", username);
            User user = userService.getOne(qw);
            session.setAttribute("currentUser", user.getUsername());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("userId", user.getId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("phone", user.getPhone());
            session.setAttribute("image", user.getImage());

            List<Student> studentList = studentService.list();
            List<Good> goodList = goodService.list();
            List<Type> typeList = typeService.list();
            List<GOrder> orderList = orderService.list(new QueryWrapper<GOrder>().eq("isorder", 1));
            model.addAttribute("peopleCount", studentList.size());
            model.addAttribute("goodCount", goodList.size());
            model.addAttribute("typeCount", typeList.size());
            model.addAttribute("orderCount", orderList.size());
            model.addAttribute("goodList", goodList);

            return "admin-home";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "index";
        }
    }

    @RequestMapping("/userlogin")
    public String userlogin(String username, String password, Model model, HttpSession session) {
        boolean count = studentService.login(username, password);
        if (count) {
            QueryWrapper<Student> qw = new QueryWrapper<>();
            qw.eq("sname", username);
            Student student = studentService.getOne(qw);
            session.setAttribute("currentUser", student.getSname());
            session.setAttribute("password", student.getPassword());
            session.setAttribute("userId", student.getId());
            session.setAttribute("email", student.getEmail());
            session.setAttribute("phone", student.getPhone());
            session.setAttribute("image", student.getSimage());
            return "user-home";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "user-login";
        }
    }
}
