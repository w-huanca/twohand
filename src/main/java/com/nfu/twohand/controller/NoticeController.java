package com.nfu.twohand.controller;

import com.nfu.twohand.pojo.Notice;
import com.nfu.twohand.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/listNotice")
    public String listNotice(Model model){
        List<Notice> list = noticeService.list();
        model.addAttribute("noticeList", list);
        return "admin-notice-list";
    }

    @RequestMapping("/saveNotice")
    public String addNotice(Notice notice){
        boolean save = noticeService.save(notice);
        return "redirect:/notice/listNotice";
    }

    @RequestMapping("/delNotice/{id}")
    public String delNotice(@PathVariable Integer id){
        boolean del = noticeService.removeById(id);
        return "redirect:/notice/listNotice";
    }
}
