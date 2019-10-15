package com.example.demo;

import com.example.demo.service.TestService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private TestService ts;

    /**
     * 测试oracle库的连接
     */
    @Test
    public void test1(){
        TestCase.assertEquals(106, ts.getReVisitLen().intValue());
    }

    /**
     * 转换随访任务
     */
    @Test
    public void test2(){
        TestCase.assertEquals(1, ts.convertRevisitInfoData().intValue());
    }

    /**
     * 转换随访任务下的患者
     */
    @Test
    public void test3(){
        TestCase.assertEquals(1, ts.convertRevisitPatient().intValue());
    }

    /**
     * 转换任务分配情况
     */
    @Test
    public void test4(){
        TestCase.assertEquals(1, ts.convertRevisitEntry().intValue());
    }


    /**
     * 转换表单临时表
     */
    @Test
    public void test5(){
        TestCase.assertEquals(1, ts.convertRevisitResultTemp().intValue());
    }


    /**
     * 转换最终表单数据
     */
    @Test
    public void test6(){
        TestCase.assertEquals(1, ts.convertRevisitResult().intValue());
    }
}
