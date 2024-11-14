package com.nfu.twohand.controller;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nfu.twohand.pojo.*;
import com.nfu.twohand.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class UserController {

    @Value("${location}")
    private String location;

    @Autowired
    private GoodService goodService;

    @Autowired
    private GOrderService orderService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ScoreGoodService scoreGoodService;

    @RequestMapping("listGood")
    public String listGood(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "8", value = "pageSize") Integer pageSize,
                           Model model, Good good, HttpSession session) {
        session.removeAttribute("msg");
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Good> qw = new QueryWrapper<>();
        // 搜索框查询
        if (good.getGname() != null) {
            qw.like("gname", good.getGname());
        }
        if (good.getType() != null) {
            qw.like("type", good.getType());
        }
        // 不显示库存为0的商品
        qw.ne("stock", 0);
        List<Good> list = goodService.list(qw);
        for (Good good1 : list) {
            Student student = studentService.getById(good1.getSid());
            good1.setStudent(student);
        }
        PageInfo<Good> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);

        List<Type> list1 = typeService.list();
        model.addAttribute("typeList", list1);
        return "user-good-list";
    }

    @RequestMapping("toGoodSingle/{id}")
    public String toGoodSingle(@PathVariable Integer id, Model model, HttpSession session) {
        session.setAttribute("goodId", id);
        Good good = goodService.getById(id);
        Student student = studentService.getById(good.getSid());
        good.setStudent(student);
        model.addAttribute("good", good);

        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("good_id", id);
        List<Comment> list = commentService.list(qw);
        model.addAttribute("commentList", list);
        return "user-good-single";
    }

    /**
     * 添加评论
     *
     * @param comment
     * @param session
     * @return
     */
    @RequestMapping("addComment")
    public String addComment(Comment comment, HttpSession session) {
        if (comment.getStar() > 5) {
            comment.setStar(5);
        } else if (comment.getStar() < 0) {
            comment.setStar(0);
        }
        String currentUser = (String) session.getAttribute("currentUser");
        String image = (String) session.getAttribute("image");
        Integer goodId = (Integer) session.getAttribute("goodId");
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/toGoodSingle/" + goodId;
        }
        comment.setCustomer(currentUser);
        comment.setCimage(image);
        comment.setCommentTime(new Date());
        commentService.save(comment);

        ScoreGood scoreGood = new ScoreGood();
        scoreGood.setUserId(userId);
        scoreGood.setGoodId(goodId);
        if (comment.getStar() > 5) {
            scoreGood.setScore(5);
        } else if (comment.getStar() < 0) {
            scoreGood.setScore(0);
        } else {
            scoreGood.setScore(comment.getStar());
        }
        scoreGood.setTime(new Date());
        boolean save = scoreGoodService.save(scoreGood);

        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("good_id", comment.getGoodId());
        List<Comment> commentList = commentService.list(qw);
        int a = 0;  // 评分
        double b = 0;   // 数量
        for (Comment comment1 : commentList) {
            log.info(comment1.getStar() + "");
            a = a + comment1.getStar();
            b = b + 1;
        }
        log.info(a + "");
        log.info(b + "");
        String formattedResult = String.format("%.2f", a / b);  // 保留两位小数
        log.info(formattedResult);
        int round = (int) Math.round(Double.parseDouble(formattedResult));  // 四舍五入
        log.info(round + "");
        Good good = goodService.getById(comment.getGoodId());
        good.setStar(round);
        goodService.updateById(good);

        return "redirect:/toGoodSingle/" + goodId;
    }

    /**
     * 查看收藏商品列表
     *
     * @param pageNum
     * @param pageSize
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/collection")
    public String collection(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                             @RequestParam(required = false, defaultValue = "8", value = "pageSize") Integer pageSize,
                             Model model, HttpSession session) {
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        }
        Student student = studentService.getById(userId);
        // 获取当前用户的收藏的商品
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("isorder", 2);
        qw.eq("sid", userId);
        List<GOrder> list = orderService.list(qw);
        for (GOrder order : list) {
            Good good = goodService.getById(order.getGid());
            order.setGood(good);
        }
        PageInfo<GOrder> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "user-collection";
    }

    /**
     * 收藏商品
     *
     * @param foodId
     * @param session
     * @return
     */
    @RequestMapping("goodToCollection/{foodId}")
    public String foodToCollection(@PathVariable Integer foodId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/toGoodSingle/" + foodId;
        } else {
            System.out.println(">>>>>>>>>> 当前用户为：" + userId);
        }
        Integer goodId = (Integer) session.getAttribute("goodId");
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("sid", userId);
        qw.eq("gid", goodId);
        qw.eq("isorder", 2);
        GOrder order = orderService.getOne(qw);
        if (order != null) {
            session.setAttribute("msg", "该商品已收藏");
            return "redirect:/toGoodSingle/" + goodId;
        }
        Good food = goodService.getById(foodId);
        GOrder order1 = new GOrder();
        order1.setSid(userId);
        order1.setGid(foodId);
        order1.setIsorder(1);
        order1.setTotal(food.getPrice());
        order1.setIsorder(2);
        order1.setOrderTime(new Date());
        orderService.save(order1);
        session.removeAttribute("msg");
        return "redirect:/collection";
    }

    /**
     * 取消收藏
     */
    @RequestMapping("delCollection/{orderId}")
    public String delCollection(@PathVariable Integer orderId, HttpSession session) {
        boolean remove = orderService.removeById(orderId);
        return "redirect:/collection";
    }

    /**
     * 购物车
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("cart")
    public String cart(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        } else {
            System.out.println(">>>>>>>>>> 当前用户为：" + userId);
        }
        Student student = studentService.getById(userId);
        // 获取当前用户的购物车商品
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("isorder", 0);
        qw.eq("sid", userId);
        List<GOrder> list = orderService.list(qw);
        for (GOrder order : list) {
            Good good = goodService.getById(order.getGid());
            order.setGood(good);
        }
        // 计算购物车总金额
        double a = 0;
        for (GOrder order : list) {
            a += order.getTotal();
        }

        // 创建BigDecimal对象
        BigDecimal bd = new BigDecimal(Double.toString(a));
        // 设置小数点后两位
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        // 转换回double
        double roundedValue = bd.doubleValue();

        PageInfo<GOrder> pageInfo = new PageInfo<>(list);
        model.addAttribute("totalMoney", roundedValue);
        model.addAttribute("pageInfo", pageInfo);
        return "user-cart";
    }

    @RequestMapping("/payOrder")
    public String payOrder(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        }
        Student student = studentService.getById(userId);
        // 获取当前用户的购物车商品
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("isorder", 0);
        qw.eq("sid", userId);
        List<GOrder> list = orderService.list(qw);
        // 更改订单状态为已支付
        for (GOrder order : list) {
            Good good = goodService.getById(order.getGid());
            order.setGood(good);
            order.setIsorder(1);
            orderService.updateById(order);
        }
        return "redirect:/listOrder";
    }

    /**
     * 购物车添加商品
     *
     * @param goodId
     * @param count
     * @param session
     * @return
     */
    @RequestMapping("/goodToCart")
    public String goodToCart(Integer goodId, Integer count, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/toGoodSingle/" + goodId;
        }
        // 检查商品是否已在购物车中
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("sid", userId);
        qw.eq("gid", goodId);
        qw.eq("isorder", 0);
        GOrder one = orderService.getOne(qw);
        if (one != null) {
            session.setAttribute("msg", "该商品已经在购物车中");
            return "redirect:/toGoodSingle/" + goodId;
        }
        // 检查库存
        Good byId = goodService.getById(goodId);
        if (count > byId.getStock()) {
            session.setAttribute("msg", "库存不足");
            return "redirect:/toGoodSingle/" + goodId;
        } else if (count <= 0) {
            session.setAttribute("msg", "数量不能小于等于0");
            return "redirect:/toGoodSingle/" + goodId;
        }
        // 添加商品到购物车
        Good food = goodService.getById(goodId);
        GOrder order = new GOrder();
        order.setSid(userId);
        order.setGid(goodId);
        order.setCount(count);
        order.setTotal(food.getPrice() * count);
        orderService.save(order);

        // 更新库存
        food.setStock(food.getStock() - count);
        goodService.updateById(food);
        // 将新添加商品的ID存储在session中
        session.setAttribute("newGoodId", goodId);
        return "redirect:/cart";
    }

    /**
     * 加购/减购
     *
     * @param orderId
     * @param count
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateOrder")
    public String updateOrder(String orderId, String count) {
        log.info("a");
        // 当前订单对象
        GOrder order = orderService.getById(orderId);
        // 订单的原加购数量
        Integer oldCount = order.getCount();
        // 订单的商品id
        Integer gid = order.getGid();
        // 根据订单中商品id获取的商品对象
        Good good = goodService.getById(gid);
        // 商品的单价
        Double price = good.getPricen();
        // 新的加购数量
        Integer a = Integer.valueOf(count);
        // 返回的状态结果
        String result = "";
        order.setId(Integer.valueOf(orderId));
        order.setCount(a);
        order.setTotal(price * a);
        boolean b = orderService.updateById(order);

        // 修改库存
        log.info(oldCount + "");
        log.info(count + "");
        if (oldCount > a) {
            good.setStock(good.getStock() + (oldCount - a));
            goodService.updateById(good);
        } else if (oldCount < a) {
            good.setStock(good.getStock() - (a - oldCount));
            goodService.updateById(good);
        }

        if (b) {
            return "OK";
        } else {
            return "ERROR";
        }
    }

    /**
     * 删除购物车
     *
     * @param orderId
     * @param session
     * @return
     */
    @RequestMapping("/delCart/{orderId}")
    public String delCart(@PathVariable Integer orderId, HttpSession session) {
        GOrder byId = orderService.getById(orderId);
        Good good = goodService.getById(byId.getGid());
        good.setStock(good.getStock() + byId.getCount());
        goodService.updateById(good);
        boolean remove = orderService.removeById(orderId);
        return "redirect:/cart";
    }

    /**
     * 查看当前用户订单以及已出售商品
     *
     * @param pageNum
     * @param pageSize
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/listOrder")
    public String myOrders(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "12", value = "pageSize") Integer pageSize,
                           Model model, HttpSession session) {
        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 12;
        }
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        }
        PageHelper.startPage(pageNum, pageSize);

        // 查询当前用户的全部订单
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("isorder", 1);
        qw.eq("sid", userId);
        List<GOrder> list = orderService.list(qw);
        for (GOrder order : list) {
            Good good = goodService.getById(order.getGid());
            order.setGood(good);
            Student student = studentService.getById(good.getSid());
            order.setStudent(student);
        }
        PageInfo<GOrder> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);

        // 查询当前用户已卖出的商品
        // 1. 查询已经支付的订单
        QueryWrapper<GOrder> qw1 = new QueryWrapper<>();
        qw1.eq("isorder", 1);
        List<GOrder> list1 = orderService.list(qw1);
        // 2. 根据订单的用户ID查询用户（买家）
        for (GOrder order : list1) {
            Student student = studentService.getById(order.getSid());
            order.setStudent(student);
        }
        // 3. 根据订单的商品ID查询商品
        for (GOrder order : list1) {
            Good good = goodService.getById(order.getGid());
            order.setGood(good);
        }
        // 4. 根据商品的用户ID与当前用户ID进行对比，如果相等，则添加到list2中
//        List<GOrder> list2 = new ArrayList<>();
        for (GOrder order : list1) {
            if (!userId.equals(order.getGood().getSid())) {
//                list2.add(order);
                list.remove(order);
            }
        }
        PageInfo<GOrder> pageInfo1 = new PageInfo<>(list1);
        model.addAttribute("pageInfo1", pageInfo1);

        return "user-order-list";
    }

    /**
     * @param orderId
     * @return
     */
    @RequestMapping("confirmOrder/{orderId}")
    public String confirmOrder(@PathVariable Integer orderId) {
        GOrder order = orderService.getById(orderId);
        order.setIsorder(2);
        orderService.updateById(order);
        return "redirect:/listOrder";
    }

    /**
     * @param orderId
     * @return
     */
    @RequestMapping("fahuo/{orderId}")
    public String fahuo(@PathVariable Integer orderId) {
        GOrder order = orderService.getById(orderId);
        order.setStatus(1);
        orderService.updateById(order);
        return "redirect:/listOrder";
    }

    /**
     * 删除我的订单
     *
     * @param id
     * @return
     */
    @RequestMapping("delMyOrder/{id}")
    public String delMyOrder(@PathVariable Integer id) {
        boolean b = orderService.removeById(id);
        return "redirect:/listOrder";
    }

    /**
     * 我发布的商品
     *
     * @param pageNum
     * @param pageSize
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/listMyGood")
    public String listMyGood(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                             @RequestParam(required = false, defaultValue = "8", value = "pageSize") Integer pageSize,
                             Model model, HttpSession session) {
        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Good> qw = new QueryWrapper<>();
        qw.eq("sid", userId);
        List<Good> list = goodService.list(qw);

        PageInfo<Good> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);

        return "user-my-good";
    }

    private void transfile(Good good, MultipartFile file) {
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
        good.setGimage(path);
    }

    @RequestMapping("preUpdateGood/{id}")
    public String preUpdateGood(@PathVariable Integer id, Model model, HttpSession session) {
        List<Type> typeList = typeService.list();
        model.addAttribute("typeList", typeList);
        Good student = goodService.getById(id);
        model.addAttribute("good", student);
        return "user-good-update";
    }

    @RequestMapping("updateGood")
    public String updateStudent(Good student, MultipartFile file, HttpSession session) {
        if (!file.isEmpty()) {
            transfile(student, file);
        }
        student.setPricen(student.getPricen() * student.getDiscount());
        boolean b = goodService.updateById(student);
        return "redirect:/listMyGood";
    }

    /**
     * 删除我的商品
     *
     * @param id
     * @return
     */
    @RequestMapping("delMyGood/{id}")
    public String delMyGood(@PathVariable Integer id) {
        boolean b = goodService.removeById(id);
        return "redirect:/listMyGood";
    }

    @RequestMapping("preSaveGood")
    public String preSaveGood(Model model) {
        List<Type> typeList = typeService.list();
        model.addAttribute("typeList", typeList);
        return "user-good-save";
    }

    @RequestMapping("saveGood")
    public String saveGood(Good good, MultipartFile file, HttpSession session) {
        if (!file.isEmpty()) {
            transfile(good, file);
        }
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("msg", "您暂未登录，请先登录！");
            return "redirect:/login";
        }
        if (good.getPricen() != null && good.getDiscount() != null) {
            good.setPricen(good.getPricen() * good.getDiscount());
        }
        good.setSid(userId);
        good.setStar(0);
        boolean b = goodService.save(good);
        return "redirect:/listMyGood";
    }
}
