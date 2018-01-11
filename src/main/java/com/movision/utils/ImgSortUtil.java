package com.movision.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2018/1/11 9:40
 */
@Service
public class ImgSortUtil {

    public String mergePicture(String content) throws Exception {
        //转换帖子内容为数组格式
        JSONArray jsonArray = JSONArray.fromObject(content);
        //标记，用于记录两张图片是否连贯
        int k = 0;
        JSONArray ja = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));
            Object tmp = jsonObject.get("rate");
            if (tmp != null) {
                Double rate = Double.valueOf(tmp.toString());
                if (rate != null) {
                    //长图或宽图 数字为图片的宽高比例
                    if (rate >= 2 || rate <= 0.5 || rate == 0.0) {
                        JSONObject jso = setJsonObject(jsonObject, 1);
                        ja.add(jso);
                        k = 0;
                    } else {//普通图片
                        k++;
                        //为两张连贯图片赋值mark为2代表两张图片可以合并,数字2代表几张图片合并
                        if (k == 2) {
                            //封装成JSON数组
                            JSONObject jso1 = setJsonObject(JSONObject.fromObject(jsonArray.get(i - 1)), 2);
                            ja.add(jso1);
                            JSONObject jso2 = setJsonObject(jsonObject, 2);
                            ja.add(jso2);
                            k = 0;
                        }
                    }
                } else {
                    JSONObject jso = setJsonObject(jsonObject, 1);
                    ja.add(jso);
                    k = 0;
                }
            } else {
                JSONObject jso = setJsonObject(jsonObject, 1);
                ja.add(jso);
                k = 0;
            }
        }
        return ja.toString();
    }

    /**
     * 封装json对象
     *
     * @param jsonObject
     * @param mark
     * @return
     */
    private JSONObject setJsonObject(JSONObject jsonObject, int mark) {
        JSONObject jso = new JSONObject();
        jso = jsonObject;
        jso.put("type", jso.get("type"));
        jso.put("orderid", jso.get("orderid"));
        jso.put("value", jso.get("value"));
        jso.put("wh", jso.get("wh"));
        jso.put("dir", jso.get("dir"));
        jso.put("rate", jso.get("rate"));
        jso.put("mark", mark);
        return jso;
    }
}
