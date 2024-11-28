package com.nfu.twohand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nfu.twohand.pojo.Good;
import com.nfu.twohand.pojo.Student;
import com.nfu.twohand.pojo.Type;
import com.nfu.twohand.service.GoodService;
import com.nfu.twohand.service.StudentService;
import com.nfu.twohand.service.TypeService;
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
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TypeService typeService;

    @Value("${location}")
    private String location;

    @RequestMapping("/listGood")
    public String ListGood(@RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "8", value = "pageSize") Integer pageSize,
                           Good good, Model model) {
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Good> qw = new QueryWrapper<>();
        if (good.getGname() != null) {
            qw.like("sname", good.getGname());
        }
        List<Good> list = goodService.list(qw);
        for (Good good1 : list) {
            Student student = studentService.getById(good1.getSid());
            good1.setStudent(student);
        }

        // mybatis plus 分页
        Page<Good> goodPage = goodService.getGoodsByPage(new Page<>(pageNum, pageSize), qw);
        model.addAttribute("page", goodPage);

        PageInfo<Good> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);

        List<Student> list1 = studentService.list(null);
        model.addAttribute("studentList", list1);

        List<Type> list2 = typeService.list(null);
        model.addAttribute("typeList", list2);
        return "admin-good-list";
    }

    @RequestMapping("saveGood")
    public String saveGood(Good good, MultipartFile file) {
        if (!file.isEmpty()) {
            transfile(good, file);
        }
        boolean save = goodService.save(good);
        return "redirect:/good/listGood";
    }

    @RequestMapping("preUpdateGood/{id}")
    public String preUpdateGood(@PathVariable Integer id, Model model) {
        Good byId = goodService.getById(id);
        model.addAttribute("good", byId);

        List<Student> list1 = studentService.list(null);
        model.addAttribute("studentList", list1);

        List<Type> list2 = typeService.list(null);
        model.addAttribute("typeList", list2);
        return "admin-good-update";
    }

    @RequestMapping("updateGood")
    public String updateGood(Good good, MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            transfile(good, file);
        }
        if (good.getDiscount() < 0) {
            model.addAttribute("msg", "商品折扣不能小于0");
            return "admin-good-update";
        }

        boolean save = goodService.updateById(good);
        return "redirect:/good/listGood";
    }

    @RequestMapping("delGood/{id}")
    public String delGood(@PathVariable Integer id) {
        boolean remove = goodService.removeById(id);
        return "redirect:/good/listGood";
    }

    @ResponseBody
    @RequestMapping("batchDeleteGood")
    public String batchDeleteGood(String idList) {
        String[] split = idList.split(",");
        List<Integer> list = new ArrayList<>();
        for (String id : split) {
            if (!id.isEmpty()) {
                list.add(Integer.valueOf(id));
            }
        }
        boolean b = goodService.removeByIds(list);
        if (b) {
            return "OK";
        } else {
            return "ERROR";
        }
    }

    private void transfile(Good good, MultipartFile file) {
        String originalFilename = file.getOriginalFilename(); //获取文件名
        int index = originalFilename.lastIndexOf("."); //获取文件名的最后一个点的位置
        String suffix = originalFilename.substring(index); //获取文件名的后缀
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
}
