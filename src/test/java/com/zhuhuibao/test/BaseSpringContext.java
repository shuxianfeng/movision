/*
 * 文 件 名:  BaseSpringContext.java
 * 描    述:  <描述>
 * 修 改 人:  王翔
 * 修改时间:  2013-4-21
 * 修改内容:  <修改内容>
 */
package com.zhuhuibao.test;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王翔
 * @version [版本号, 2013-4-21]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml",
        "classpath:spring/applicationContext-shiro.xml",
        "file:/src/main/webapp/WEB-INF/spring-mvc.xml"})
public class BaseSpringContext {// extends
    // AbstractTransactionalJUnit4SpringContextTests
    // {

//    @Autowired
//    private WebApplicationContext wac;
//
//    public MockMvc mockMvc;
//
//    @Before
//    public void setUp() throws Exception {
//        webAppContextSetup(this.wac).addFilter(new DelegatingFilterProxy(),
//                "/*").build();
//        this.mockMvc = webAppContextSetup(this.wac).build();
//    }

}
