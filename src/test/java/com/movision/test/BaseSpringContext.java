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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"//,
//        "classpath:spring/applicationContext-shiro.xml",
//        "file:src/main/webapp/WEB-INF/spring-mvc.xml"
})
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
