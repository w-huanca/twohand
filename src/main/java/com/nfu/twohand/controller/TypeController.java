package com.nfu.twohand.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nfu.twohand.pojo.Good;
import com.nfu.twohand.pojo.Student;
import com.nfu.twohand.pojo.Type;
import com.nfu.twohand.service.GoodService;
import com.nfu.twohand.service.StudentService;
import com.nfu.twohand.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private GoodService goodService;

    @Autowired
    private StudentService studentService;

    @RequestMapping("listType")
    public String listType(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "8", value = "pageSize") Integer pageSize,
                           Type type, Model model) {
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Type> qw = new QueryWrapper<>();
        if (type.getType() != null) {
            qw.like("type", type.getType());
        }
        List<Type> list = typeService.list(qw);
        PageInfo<Type> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "admin-type-list";
    }

    @RequestMapping("saveType")
    public String saveType(Type type) {
        boolean b = typeService.save(type);
        return "redirect:/type/listType";
    }

    @RequestMapping("preUpdateType/{id}")
    public String preUpdateType(@PathVariable Integer id, Model model) {
        Type type = typeService.getById(id);
        model.addAttribute("type", type);
        return "admin-type-update";
    }

    @RequestMapping("updateType")
    public String updateType(Type type) {
        boolean update = typeService.updateById(type);
        return "redirect:/type/listType";
    }

    @RequestMapping("delType/{id}")
    public String delType(@PathVariable Integer id) {
        typeService.removeById(id);
        return "redirect:/type/listType";
    }

    @ResponseBody
    @RequestMapping("batchDeleteType")
    public String batchDeleteType(String idList) {
        List<String> split = StrUtil.split(idList, ","); //将字符串转换为数组
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            if (!s.isEmpty()) {
                list.add(Integer.valueOf(s));
            }
        }
        boolean b = typeService.removeByIds(list);
        if (b) {
            return "OK";
        } else {
            return "error";
        }
    }

    @RequestMapping("check/{id}")
    public String check(@PathVariable Integer id, Model model) {
        Type type = typeService.getById(id);
        List<Good> list = goodService.list(new QueryWrapper<Good>().eq("type", type.getType()));
        for (Good good : list) {
            Student student = studentService.getById(good.getSid());
            good.setStudent(student);
        }
        model.addAttribute("goodList", list);
        return "admin-type-good";
    }
}
