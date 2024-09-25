package com.nfu.twohand.controller;

import cn.hutool.crypto.digest.DigestUtil;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // 查询用户名是否存在
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        User user = userService.getOne(qw);
        if (user == null) {
            // 用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "index";
        }

        // 用户名存在，验证密码
        boolean passwordCorrect = userService.login(username, password);
        if (passwordCorrect) {
            // 登录成功，保存用户信息到 session
            session.setAttribute("currentUser", user.getUsername());
            session.setAttribute("password", password);
            session.setAttribute("userId", user.getId());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("phone", user.getPhone());
            session.setAttribute("image", user.getImage());

            // 查询其他信息
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
            // 密码错误
            model.addAttribute("msg", "密码错误");
            return "index";
        }
    }

    @RequestMapping("/userlogin")
    public String userlogin(String username, String password, Model model, HttpSession session) {
        // 检查用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("msg", "用户名不能为空");
            return "user-login";
        }

        // 检查密码是否为空
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("msg", "密码不能为空");
            return "user-login";
        }

        // 查询用户名是否存在
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("sname", username);
        Student student = studentService.getOne(qw);
        if (student == null) {
            // 用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "user-login";
        }

        // 验证密码
        boolean passwordCorrect = studentService.login(username, password);
        if (passwordCorrect) {
            // 登录成功，将用户信息存入 session
            session.setAttribute("currentUser", student.getSname());
            session.setAttribute("password", password);
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
            // 密码错误
            model.addAttribute("msg", "密码错误");
            return "user-login";
        }
    }

    /**
     * 发送短信验证码
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/sendVerificationCode")
    public String sendVerificationCode(HttpSession session, Model model) {
        // 生成验证码
        String verificationCode = studentService.generateVerificationCode();

        // 将验证码保存到 session
        session.setAttribute("verificationCode", verificationCode);

        // 模拟发送验证码到控制台
        System.out.println("【二手交易平台】验证码：" + verificationCode);

        model.addAttribute("msg", "验证码已发送，发送成功");
        return "user-login";  // 返回登录页面
    }

    /**
     * 短信登录
     *
     * @param phone
     * @param smsCode
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/smsLogin")
    public String smsLogin(String phone, String smsCode, Model model, HttpSession session) {
        // 检查手机号是否为空
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("msg", "手机号不能为空");
            return "user-login";
        }

        // 检查验证码是否为空
        if (smsCode == null || smsCode.trim().isEmpty()) {
            model.addAttribute("msg", "验证码不能为空");
            return "user-login";
        }

        // 获取 session 中存储的验证码
        String generatedCode = (String) session.getAttribute("verificationCode");

        // 验证验证码是否过期或不存在
        if (generatedCode == null) {
            model.addAttribute("msg", "验证码不存在或已过期，请重新获取");
            return "user-login";
        }

        // 验证手机号是否存在于数据库中
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("phone", phone);
        Student student = studentService.getOne(qw);

        if (student == null) {
            // 手机号不存在
            model.addAttribute("msg", "手机号不存在");
            return "user-login";
        }

        // 验证用户输入的验证码是否正确
        if (!smsCode.equals(generatedCode)) {
            // 验证码错误
            model.addAttribute("msg", "验证码错误");
            return "user-login";
        } else {
            // 验证码正确，登录成功
            session.setAttribute("currentUser", student.getSname());
            session.setAttribute("password", student.getPassword());
            session.setAttribute("userId", student.getId());
            session.setAttribute("email", student.getEmail());
            session.setAttribute("phone", student.getPhone());
            session.setAttribute("image", student.getSimage());
            model.addAttribute("msg", "登录成功");
            return "user-home";  // 登录成功，返回用户主页
        }
    }

    /**
     * 忘记密码
     *
     * @return
     */
    @RequestMapping("/forgotPassword")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    /**
     * 重置密码
     *
     * @param sname
     * @param newPassword
     * @param confirmPassword
     * @param phone
     * @param smsCode
     * @param model
     * @param session
     * @return
     */
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam(required = false) String sname,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String smsCode,
                                Model model,
                                HttpSession session) {

        // 检查所有字段是否为空
        if (sname == null || sname.trim().isEmpty()) {
            model.addAttribute("msg", "账号不能为空");
            return "forgot-password"; // 返回重置密码页面
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            model.addAttribute("msg", "新密码不能为空");
            return "forgot-password"; // 返回重置密码页面
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("msg", "确认密码不能为空");
            return "forgot-password"; // 返回重置密码页面
        }
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("msg", "手机号不能为空");
            return "forgot-password"; // 返回重置密码页面
        }
        if (smsCode == null || smsCode.trim().isEmpty()) {
            model.addAttribute("msg", "短信验证码不能为空");
            return "forgot-password"; // 返回重置密码页面
        }

        // 检查新密码和确认密码是否匹配
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("msg", "新密码与确认密码不匹配");
            return "forgot-password"; // 返回重置密码页面
        }

        // 验证账号和手机号
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sname", sname)
                .eq("phone", phone);  // 使用账号和手机号联合查询
        Student student = studentService.getOne(queryWrapper);

        if (student == null) {
            model.addAttribute("msg", "账号或手机号不存在");
            return "forgot-password"; // 返回重置密码页面
        }

        // 验证短信验证码
        String generatedCode = (String) session.getAttribute("verificationCode");
        if (generatedCode == null || !generatedCode.equals(smsCode)) {
            model.addAttribute("msg", "验证码错误或已过期，请重新获取");
            return "forgot-password"; // 返回重置密码页面
        }

        // 更新密码，使用DigestUtil.md5Hex加密新密码
        student.setPassword(DigestUtil.md5Hex(newPassword));
        boolean updated = studentService.updateById(student);

        if (updated) {
            model.addAttribute("msg", "密码重置成功，请重新登录");
            return "user-login"; // 返回登录页面
        } else {
            model.addAttribute("msg", "密码重置失败，请稍后再试");
            return "forgot-password"; // 返回重置密码页面
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
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("profile")
    public String profile(HttpSession session, Model model) {
        String currentUser = (String) session.getAttribute("currentUser");
        String password = (String) session.getAttribute("password");
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", currentUser);
        User one = userService.getOne(qw);
        one.setPassword(password);
        model.addAttribute("user", one);
        return "admin-profile";
    }

    /**
     * 修改个人信息
     *
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
     *
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
     *
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
    public String register(String userName, String userPwd, String confirmPwd,
                           String phone, String smsCode, HttpSession session, Model model) {
        // 输入不能为空
        if (userName == null || userName.trim().isEmpty()) {
            model.addAttribute("msg", "用户名不能为空");
            return "user-register";
        }
        if (userPwd == null || userPwd.trim().isEmpty()) {
            model.addAttribute("msg", "密码不能为空");
            return "user-register";
        }
        if (confirmPwd == null || confirmPwd.trim().isEmpty()) {
            model.addAttribute("msg", "确认密码不能为空");
            return "user-register";
        }
        // 手机验证
        // 检查手机号是否为空
        if (phone == null || phone.trim().isEmpty()) {
            model.addAttribute("msg", "手机号不能为空");
            return "user-register";
        }
        // 检查手机号是否已经存在
        if (studentService.getOne(new QueryWrapper<Student>().eq("phone", phone)) != null) {
            model.addAttribute("msg", "手机号已存在");
            return "user-register";
        }
        // 检查验证码是否为空
        if (smsCode == null || smsCode.trim().isEmpty()) {
            model.addAttribute("msg", "验证码不能为空");
            return "user-register";
        }
        // 获取 session 中存储的验证码
        String generatedCode = (String) session.getAttribute("verificationCode");
        // 验证验证码是否过期或不存在
        if (generatedCode == null) {
            model.addAttribute("msg", "验证码不存在或已过期，请重新获取");
            return "user-register";
        }
        // 验证用户输入的验证码是否正确
        if (!smsCode.equals(generatedCode)) {
            // 验证码错误
            model.addAttribute("msg", "验证码错误");
            return "user-register";
        }
        // 密码和确认密码是否一致
        if (!userPwd.equals(confirmPwd)) {
            model.addAttribute("msg", "两次密码不一致");
            return "user-register";
        } else {
            Student customer = new Student();
            customer.setSname(userName);
            String s = DigestUtils.md5Hex(userPwd);
            customer.setPassword(s);
            customer.setPhone(phone);
            boolean b = studentService.save(customer);
            return "user-login";
        }
    }
}
