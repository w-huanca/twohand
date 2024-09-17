package com.nfu.twohand.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nfu.twohand.pojo.CountNumber;
import com.nfu.twohand.pojo.GOrder;
import com.nfu.twohand.pojo.MainMenu;
import com.nfu.twohand.service.GOrderService;
import com.nfu.twohand.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/main")
public class MainMenuController {

    @Autowired
    private GoodService goodService;

    @Autowired
    private GOrderService orderService;

    @RequestMapping("/mainMenu")
    public List<MainMenu> mainMenu(){
        // 获取已支付的订单
        QueryWrapper<GOrder> qw = new QueryWrapper<>();
        qw.eq("isorder", 1);
        List<GOrder> orderList = orderService.list(qw);
        // 存放商品的名称和出售数量
        List<CountNumber> list = new ArrayList<>();
        for (GOrder order : orderList) {
            // 获取当前订单的商品名称
            String gname = goodService.getById(order.getGid()).getGname();
            // 判断商品名称在 list 中是否存在
            if (list.stream().anyMatch(item -> item.getName().equals(gname))) {
                continue;
            }
            // 获取当前订单的商品ID
            int gid = order.getGid();
            // 统计当前商品出售数量
            int count = orderList.stream()
                    .filter(order1 -> order1.getGid().equals(gid))
                    .mapToInt(GOrder::getCount)
                    .sum();
            // 保存商品名称和出售数量
            CountNumber countNumber = new CountNumber();
            countNumber.setName(gname);
            countNumber.setCount(String.valueOf(count));
            list.add(countNumber);
        }
        //根据出售数量降序排序
        list.sort((o1, o2) -> o2.getCount().compareTo(o1.getCount()));
//        // 获取前10个商品
//        list = list.subList(0, Math.min(10, list.size()));
        List<MainMenu> mainMenuList = new ArrayList<>();
        for (CountNumber number : list) {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setType(number.getName());
            mainMenu.setMount(Integer.valueOf(number.getCount()));
            mainMenuList.add(mainMenu);
        }
        return mainMenuList;
    }
}
