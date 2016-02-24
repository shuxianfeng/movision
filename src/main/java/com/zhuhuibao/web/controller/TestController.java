package com.zhuhuibao.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("showcase")
public class TestController {


    @Autowired
    private HttpServletRequest request;

    /**
     * 非ajax请求,返回视图
     * @return
     */
    @RequestMapping("case1")
    public String case1(){
        return "index";
    }

    /**
     * 前台ajax参数放formData中,后台从request中获取参数
     * @return
     */
    @RequestMapping("case2")
    @ResponseBody
    public List<String> case2(@RequestBody String aa){
        String query= request.getParameter("query");
        System.out.println("aa == "+aa);
        System.out.println("query = [" + query + "]");
        List result=new ArrayList<String>();
        result.add("a");

        return result;
    }

    /**
     * ajax请求,参数在request payload 中 content-type=“application/json;charset=UTF-8”
     * @param param
     * @return
     */
    @RequestMapping("case3")
    @ResponseBody
    public List<String> case3(@RequestBody Param param){
        System.out.println("query = [" + param.getQuery() + "]");
        List result=new ArrayList<String>();
        result.add(param.getQuery());

        return result;
    }

    /**
     * ajax 请求 参数在formdata中 content-type 为默认“application/x-www-form-urlencoded; charset=UTF-8”
     * @param query
     * @return
     */
    @RequestMapping("case4")
    @ResponseBody
        public List<String> case4(@RequestParam String query){
        System.out.println("query = [" + query + "]");
        List result=new ArrayList<String>();
        result.add(query);

        return result;
    }

    /**
     *  ajax 请求 参数在formdata中 content-type 为默认“application/x-www-form-urlencoded; charset=UTF-8”
     * @param search
     * @return
     */
    /*@RequestMapping("case5")
    @ResponseBody
    public List<String> case5(@ModelAttribute Search search){
        System.out.println("query = [" + search.getQuery() + "]");
        List result=new ArrayList<String>();
        result.add(search.getQuery());

        return result;
    }*/

    public static class Param implements Serializable{

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        private String query;
    }

}
