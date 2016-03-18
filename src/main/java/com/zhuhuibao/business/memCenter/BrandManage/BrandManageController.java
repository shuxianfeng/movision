package com.zhuhuibao.business.memCenter.BrandManage;

import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by cxx on 2016/3/7 0007.
 */
@RestController
public class BrandManageController {
    private static final Logger log = LoggerFactory
            .getLogger(BrandManageController.class);

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    ApiConstants ApiConstants;
    /**
     * 新建品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/addBrand", method = RequestMethod.POST)
    public void upload(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
        JsonResult result = new JsonResult();
        brand.setPublishTime(new Date());
        int isAdd = brandMapper.addBrand(brand);
        if(isAdd==0){
            result.setCode(400);
            result.setMessage("添加失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 更新品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/updateBrand", method = RequestMethod.POST)
    public void updateBrand(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
        JsonResult result = new JsonResult();
        //如果是未通过的品牌进行更新，则状态变为待审核
        if(brand.getStatus()==0){
            brand.setStatus(2);
        }
        brand.setLastModifyTime(new Date());
        int isUpdate = brandMapper.updateBrand(brand);
        if(isUpdate==0){
            result.setCode(400);
            result.setMessage("更新失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 删除品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/deleteBrand", method = RequestMethod.POST)
    public void deleteBrand(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
        JsonResult result = new JsonResult();
        int isDelete = brandMapper.deleteBrand(brand);
        if(isDelete==0){
            result.setCode(400);
            result.setMessage("删除失败");
        }else{
            result.setCode(200);
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/searchBrand", method = RequestMethod.GET)
    public void searchBrand(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
        List<Brand> brandList = brandMapper.searchBrandByStatus(brand);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brandList);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 品牌数量
     */
    @RequestMapping(value = "/rest/searchBrandSize", method = RequestMethod.GET)
    public void searchBrandSize(HttpServletRequest req, HttpServletResponse response, Brand brand) throws IOException {
        int size = brandMapper.searchBrandSize(brand);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(size);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查看品牌详情
     */
    @RequestMapping(value = "/rest/brandDetails", method = RequestMethod.GET)
    public void brandDetails(HttpServletRequest req, HttpServletResponse response,int id) throws IOException {
        Brand brand = brandMapper.brandDetails(id);
        brand.setViews(brand.getViews()+1);
        int isUpdate = brandMapper.updateBrand(brand);
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setData(brand);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 上传图片，返回url
     */
    @RequestMapping(value = "/rest/uploadImg", method = RequestMethod.POST)
    public void uploadImg(HttpServletRequest req, HttpServletResponse response) throws IOException {
        //指定所上传的文件，上传成功后，在服务器的保存位置
        String saveDirectory = ApiConstants.getUploadDir();

        //指定所上传的文件最大上传文件大小
        int maxPostSize = ApiConstants.getUploadMaxPostSize();

        //指定所上传的文件命名规则
        RandomFileNamePolicy rfnp = new RandomFileNamePolicy();

        String url = "";
        //完成文件上传
        JsonResult result = new JsonResult();
        try {
            MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8", rfnp);
            Enumeration fileNames = multi.getFileNames();

            while(fileNames.hasMoreElements()){
                String fileName = (String)fileNames.nextElement();
                if(null != multi.getFile(fileName)){
                    String lastFileName = multi.getFilesystemName(fileName);
                    url =  "http://192.168.1.100:8080/upload/" + lastFileName;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("文件大小超限");
        }
        result.setCode(200);
        result.setData(url);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
