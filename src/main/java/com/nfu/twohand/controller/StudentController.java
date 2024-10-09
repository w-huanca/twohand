package com.nfu.twohand.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nfu.twohand.pojo.Student;
import com.nfu.twohand.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Value("${location}")
    private String location;

    @RequestMapping("listStudent")
    public String listStudent(@RequestParam(defaultValue = "1", value = "pageNum", required = false) Integer pageNum,
                              @RequestParam(defaultValue = "8", value = "pageSize", required = false) Integer pageSize,
                              Student student, Model model){
        if (pageNum < 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize < 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 8;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Student> qw = new QueryWrapper<>();
        if (student.getSname() != null) {
            qw.like("sname", student.getSname());
        }
        List<Student> list = studentService.list(qw);
        PageInfo<Student> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", pageInfo);
        return "admin-student-list";
    }

    @RequestMapping("saveStudent")
    public String saveStudent(Student student, MultipartFile file) {
        if (!file.isEmpty()) {
            transfile(student, file);
        }
        boolean save = studentService.save(student);
        return "redirect:/student/listStudent";
    }

    private void transfile(Student student, MultipartFile file) {
        String originalFilename = file.getOriginalFilename(); //获取文件名
        int index = originalFilename.lastIndexOf("."); //获取文件名的最后一个点的位置
        String suffix = originalFilename.substring(index); //获取文件名的后缀
        String prefix = System.nanoTime() + ""; //获取当前时间的纳秒值
        String path =  prefix + suffix; //拼接文件名
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
        student.setSimage(path);
    }

    @RequestMapping("preUpdateStudent/{id}")
    public String preUpdateStudent(@PathVariable Integer id, Model model) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        return "admin-student-update";
    }

    @RequestMapping("updateStudent")
    public String updateStudent(Student student, MultipartFile file) {
        if (!file.isEmpty()) {
            transfile(student, file);
        }
        boolean update = studentService.updateById(student);
        return "redirect:/student/listStudent";
    }

    @RequestMapping("delStudent/{id}")
    public String delStudent(@PathVariable Integer id) {
        studentService.removeById(id);
        return "redirect:/student/listStudent";
    }

    @ResponseBody
    @RequestMapping("batchDeleteStudent")
    public String batchDeleteStudent(String idList) {
        List<String> split = StrUtil.split(idList, ","); //将字符串转换为数组
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            if (!s.isEmpty()) {
                list.add(Integer.valueOf(s));
            }
        }
        boolean b = studentService.removeByIds(list);
        if (b) {
            return "OK";
        } else {
            return "error";
        }
    }

    // 启用用户
    @RequestMapping("isdisable/{id}")
    public String isDisable(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        student.setDisable(1);
        boolean update = studentService.updateById(student);
        return "redirect:/student/listStudent";
    }

    // 禁用用户
    @RequestMapping("notdisable/{id}")
    public String notDisable(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        student.setDisable(0);
        boolean update = studentService.updateById(student);
        return "redirect:/student/listStudent";
    }

    @ResponseBody
    @RequestMapping("batchIsDisable")
    public String batchIsDisable(String idList) {
        List<String> split = StrUtil.split(idList, ","); //将字符串转换为数组
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            if (!s.isEmpty()) {
                list.add(Integer.valueOf(s));
            }
        }
        List<Student> studentList = new ArrayList<>();
        for (Integer integer : list) {
            Student student = studentService.getById(integer);
            student.setDisable(1);
            studentList.add(student);
        }
        boolean b = studentService.updateBatchById(studentList);
        if (b) {
            return "OK";
        } else {
            return "error";
        }
    }
}
