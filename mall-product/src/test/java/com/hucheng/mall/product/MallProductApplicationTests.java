package com.hucheng.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hucheng.mall.product.entity.BrandEntity;
import com.hucheng.mall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    public void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("apple");
//        boolean save = brandService.save(brandEntity);
//        if (save){
//            System.out.println("保存成功");
//        }
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::println);
    }

}
