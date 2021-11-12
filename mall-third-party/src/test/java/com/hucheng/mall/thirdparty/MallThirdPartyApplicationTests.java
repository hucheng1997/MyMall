package com.hucheng.mall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MallThirdPartyApplicationTests {

    @Autowired
    OSS ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {
        FileInputStream is = new FileInputStream("D:\\MyStudyVideo\\2020谷粒商城\\docs\\pics\\5b5e74d0978360a1.jpg");
        ossClient.putObject("hucheng-mall", "5b5e74d0978360a1.jpg", is);
    }

}
