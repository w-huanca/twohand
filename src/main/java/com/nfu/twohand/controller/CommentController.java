package com.nfu.twohand.controller;

import com.nfu.twohand.pojo.Comment;
import com.nfu.twohand.pojo.Good;
import com.nfu.twohand.service.CommentService;
import com.nfu.twohand.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private GoodService goodService;

    @RequestMapping("/listComment")
    public String comment(Model model){
        List<Comment> list = commentService.list();
        for (Comment comment : list) {
            Good good = goodService.getById(comment.getGoodId());
            comment.setGood(good);
        }
        model.addAttribute("commentList",list);
        return "admin-comment-list";
    }

    @RequestMapping("/delComment/{id}")
    public String delComment(@PathVariable Integer id){
        boolean b = commentService.removeById(id);
        return "redirect:/comment/listComment";
    }
}
