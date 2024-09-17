package com.nfu.twohand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nfu.twohand.pojo.GOrder;
import com.nfu.twohand.pojo.Good;
import com.nfu.twohand.pojo.Student;
import com.nfu.twohand.service.GOrderService;
import com.nfu.twohand.service.GoodService;
import com.nfu.twohand.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private GOrderService orderService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GoodService goodService;

    @RequestMapping("/listOrder")
    public String listOrder(@RequestParam(defaultValue = "1", value = "pageNum", required = false) Integer pageNum,
                            @RequestParam(defaultValue = "8", value = "pageSize", required = false) Integer pageSize,
                            GOrder order, Model model) {
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        if (order.getId() != null) {
            qw.like("id", order.getId());
        }
        List<GOrder> list = orderService.list(qw);
        for (GOrder order1 : list) {
            Student student = studentService.getById(order1.getSid());
            Good good = goodService.getById(order1.getGid());
            order1.setStudent(student);
            order1.setGood(good);
        }

        PageInfo<GOrder> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "admin-order-list";
    }

    @RequestMapping("/delOrder/{id}")
    public String delOrder(@PathVariable Integer id) {
        boolean b = orderService.removeById(id);
        return "redirect:/order/listOrder";
    }

    @RequestMapping("/sendMessage/{id}")
    public String sendMessage(@PathVariable Integer id){
        GOrder byId = orderService.getById(id);
        byId.setStatus(1);
        orderService.updateById(byId);
        return "redirect:/order/listOrder";
    }

    @ResponseBody
    @RequestMapping("batchDeleteOrder")
    public String batchDeleteOrder(String idList) {
        String[] split = idList.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            if (!s.isEmpty()) {
                list.add(Integer.valueOf(s));
            }
        }
        boolean b = orderService.removeByIds(list);
        if (b) {
            return "OK";
        } else {
            return "ERROR";
        }
    }
}
