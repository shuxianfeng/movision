package com.movision.utils;

import com.movision.fsearch.utils.StringUtil;
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
            if (StringUtil.isNotEmpty(tmp.toString())) {
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
                        //判断下一张图片是否是正常尺寸图片，若不是正常尺寸图片则把此图片标记mark为1
                        if (i + 1 < jsonArray.size()) {//判断是否是最后一条
                            String ss = JSONObject.fromObject(jsonArray.get(i + 1)).get("rate").toString();
                            if (StringUtil.isNotEmpty(ss)) {
                                if (k == 1 && (Double.parseDouble(ss) <= 0.5
                                        || Double.parseDouble(ss) >= 2)) {
                                    JSONObject jso2 = setJsonObject(JSONObject.fromObject(jsonArray.get(i)), 1);
                                    ja.add(jso2);
                                    k = 0;
                                }
                            }
                        } else if (ja.size() < jsonArray.size()) {
                            JSONObject jso2 = setJsonObject(JSONObject.fromObject(jsonArray.get(i)), 1);
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
