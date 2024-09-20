package com.nfu.twohand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nfu.twohand.pojo.*;
import com.nfu.twohand.service.*;
import com.nfu.twohand.service.impl.MyUserBasedRecommenderImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class AccountController {

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

    @Autowired
    private NoticeService noticeService;

    @Value("${location}")
    private String location;

    @RequestMapping("/login")
    public String login() {
        return "user-login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "user-login";
    }

    @GetMapping("/toRegister")
    public String toRegister() {
        return "user-register";
    }

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

            List<Notice> list = noticeService.list();
            model.addAttribute("noticeList", list);
            if (session.getAttribute("userId") != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                String currentUser = (String) session.getAttribute("currentUser");
                MyUserBasedRecommenderImpl muser = new MyUserBasedRecommenderImpl();
                List<RecommendedItem> listU = muser.userBasedRecommender(userId, 4);
                if (listU == null) {
                    List<Good> listByU = null;
                    model.addAttribute("recommendList", listByU);
                    model.addAttribute("msg", 0);
                } else {
                    List<Good> listByU = getRecommend(listU);
                    model.addAttribute("recommendList", listByU);
                    model.addAttribute("msg", 1);
                }
            }

            return "user-home";
        } else {
            model.addAttribute("msg", "用户名或密码错误");
            return "user-login";
        }
    }

    @RequestMapping("homeUser")
    public String homeUser(Model model, HttpSession session) {
        if (session.getAttribute("userId") != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            String currentUser = (String) session.getAttribute("currentUser");
            MyUserBasedRecommenderImpl muser = new MyUserBasedRecommenderImpl();
            List<RecommendedItem> listU = muser.userBasedRecommender(userId, 4);
            if (listU == null) {
                List<Good> listByU = null;
                model.addAttribute("recommendList", listByU);
                model.addAttribute("msg", 0);
            } else {
                List<Good> listByU = getRecommend(listU);
                model.addAttribute("recommendList", listByU);
                model.addAttribute("msg", 1);
            }
        }
        List<Notice> list = noticeService.list();
        model.addAttribute("noticeList", list);
        return "user-home";
    }

    public List<Good> getRecommend(List<RecommendedItem> list) {
        List<Good> listBook = new ArrayList<>();
        for (RecommendedItem r : list) {
            Integer id = Math.toIntExact(r.getItemID());
            Good book = goodService.getById(id);
            book.setStudent(studentService.getById(book.getSid()));
            listBook.add(book);
        }
        return listBook;
    }

    /**
     * 跳转到个人信息修改页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("profile")
    public String profile(HttpSession session, Model model) {
        String currentUser = (String) session.getAttribute("currentUser");
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", currentUser);
        User one = userService.getOne(qw);
        String password = (String) session.getAttribute("password");
        if (password != null) {
            one.setPassword(password);
        }
        model.addAttribute("user", one);
        return "admin-profile";
    }

    /**
     * 修改个人信息
     * @param user
     * @param file
     * @return
     */
    @RequestMapping("updateAdminProfile")
    public String updateAdminProfile(User user, MultipartFile file) {
        if (!file.isEmpty()) {
            transFile(user, file);
        }
        String s = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(s);
        boolean b = userService.updateById(user);
        return "redirect:/profile";
    }

    /**
     * 修改管理员头像的方法
     * @param user
     * @param file
     */
    private void transFile(User user, MultipartFile file) {
        String originalFilename = file.getOriginalFilename(); //获取文件名
        int index = 0; //获取文件名的最后一个点的位置
        if (originalFilename != null) {
            index = originalFilename.lastIndexOf(".");
        }
        String suffix = null; //获取文件名的后缀
        if (originalFilename != null) {
            suffix = originalFilename.substring(index);
        }
        String prefix = System.nanoTime() + ""; //获取当前时间的纳秒值
        String path = prefix + suffix; //拼接文件名
        File file1 = new File(location); //创建一个文件对象，用于存放图片
        if (!file1.exists()) {
            if (file1.mkdirs()) {
                System.out.println("目录已成功创建");
            } else {
                System.out.println("创建目录失败");
            }
        }
        File file2 = new File(file1, path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setImage(path);
    }

    @RequestMapping("profileUser")
    public String profileUser(HttpSession session, Model model) {
        String currentUser = (String) session.getAttribute("currentUser");
        String password = (String) session.getAttribute("password");
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("sname", currentUser);
        Student one = studentService.getOne(qw);
        one.setPassword(password);
        model.addAttribute("user", one);
        return "user-profile";
    }

    @RequestMapping("updateStudentProfile")
    public String updateStudentProfile(Student customer, MultipartFile file) {
        if (!file.isEmpty()) {
            transFile(customer, file);
        }
        String s = DigestUtils.md5Hex(customer.getPassword());
        customer.setPassword(s);
        boolean b = studentService.updateById(customer);
        return "redirect:/profileUser";
    }

    /**
     * 修改学生头像的方法
     * @param customer
     * @param file
     */
    private void transFile(Student customer, MultipartFile file) {
        String originalFilename = file.getOriginalFilename(); //获取文件名
        int index = 0; //获取文件名的最后一个点的位置
        if (originalFilename != null) {
            index = originalFilename.lastIndexOf(".");
        }
        String suffix = null; //获取文件名的后缀
        if (originalFilename != null) {
            suffix = originalFilename.substring(index);
        }
        String prefix = System.nanoTime() + ""; //获取当前时间的纳秒值
        String path = prefix + suffix; //拼接文件名
        File file1 = new File(location); //创建一个文件对象，用于存放图片
        if (!file1.exists()) {
            if (file1.mkdirs()) {
                System.out.println("目录已成功创建");
            } else {
                System.out.println("创建目录失败");
            }
        }
        File file2 = new File(file1, path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        customer.setSimage(path);
    }

    @RequestMapping("/register")
    public String register(String userName, String userPwd, String confirmPwd, Model model) {
        if (!userPwd.equals(confirmPwd)) {
            model.addAttribute("msg", "两次密码不一致");
            return "user-register";
        } else {
            Student customer = new Student();
            customer.setSname(userName);
            String s = DigestUtils.md5Hex(userPwd);
            customer.setPassword(s);
            boolean b = studentService.save(customer);
            return "user-login";
        }
    }
}
