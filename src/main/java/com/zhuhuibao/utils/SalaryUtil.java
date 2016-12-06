package com.zhuhuibao.utils;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/30
 * Time: 16:46
 */
public class SalaryUtil {

    public static   String  convertSalary(String n){
        String s = "";
        switch(n){
            case "1000元/月以下":
                s = "1千以下";
                break;
            case "1001-2000元/月":
                s = "1千-2千";
                break;
            case "2001-4000元/月":
                s = "2千-4千";
                break;
            case "4001-6000元/月":
                s = "4千-6千";
                break;
            case "6001-8000元/月":
                s = "6千-8千";
                break;
            case "8001-10000元/月":
                s = "8千-1万";
                break;
            case "10001-15000元/月":
                s = "1万-1.5万";
                break;
            case "15001-25000元/月":
                s = "1.5万-2.5万";
                break;
            case "25000元/月以上":
                s = "2.5万以上";
                break;
            case "面议":
                s = "面议";
                break;
            default:
                s = "面议";
                break;
        }
        return s;
    }

    public static void main(String[] args) {
        System.err.println(convertSalary("15001-25000元/月"));
    }
}
