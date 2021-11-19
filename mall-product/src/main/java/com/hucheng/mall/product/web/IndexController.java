package com.hucheng.mall.product.web;

import com.hucheng.mall.product.entity.CategoryEntity;
import com.hucheng.mall.product.service.CategoryService;
import com.hucheng.mall.product.vo.Catalogs2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MrHu
 */
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping(value = {"/", "index.html"})
    private String indexPage(Model model) {
        //1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntities);
        return "index";
    }


    /**
     * 二级、三级分类数据
     *
     * @return
     */
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }

}

