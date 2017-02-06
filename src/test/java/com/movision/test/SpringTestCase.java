/*
 * 文 件 名:  BaseSpringContext.java
 * 描    述:  <描述>
 * 修 改 人:  王翔
 * 修改时间:  2013-4-21
 * 修改内容:  <修改内容>
 */
package com.movision.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//使用标准的JUnit @RunWith注释来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
//指定bean注入的配置文件
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"//,
//        "classpath:spring/applicationContext-shiro.xml",
//        "file:src/main/webapp/WEB-INF/spring-mvc.xml"
})
public class SpringTestCase extends AbstractJUnit4SpringContextTests {// extends
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
