package com.movision.facade.boss;

import com.movision.mybatis.brand.entity.Brand;
import com.movision.mybatis.productcategory.entity.ProductCategory;
import com.movision.mybatis.productcategory.service.ProductCategoryService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author zhanglei
 * @Date 2017/3/1 16:48
 */
@Service
public class ProductCategoryFacade {
    @Autowired
    private ProductCategoryService productCategoryService;
    private static Logger log = LoggerFactory.getLogger(ProductCategoryFacade.class);
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;


    /**
     * 查询类别列表
     *
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategory(Paging<ProductCategory> pager) {
        return productCategoryService.findAllCategory(pager);
    }

    /**
     * 查询品牌列表
     *
     * @param pager
     * @return
     */
    public List<Brand> findAllBrand(Paging<Brand> pager) {
        return productCategoryService.findAllBrand(pager);
    }
    /**
     * 条件搜索商品分类
     *
     * @param typename
     * @param pager
     * @return
     */
    public List<ProductCategory> findAllCategoryCondition(String typename, Paging<ProductCategory> pager) {
        Map<String, Object> map = new HashedMap();
        map.put("typename", typename);

        return productCategoryService.findAllCategoryCondition(map, pager);
    }


    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    public int deleteCategory(Integer id) {
        return productCategoryService.deleteCategory(id);
    }


    /**
     * 增加商品类别
     *
     * @param typename
     * @param imgurl
     * @return
     */
    public Map<String, Integer> addCategory(HttpServletRequest request, String typename, MultipartFile imgurl) {
        Map<String, Integer> map = new HashedMap();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setTypename(typename);
        try {
            //上传图片到本地服务器
            String savedFileName = "";
            String imgurle = "";
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (imgurl != null && isMultipart) {
                if (!imgurl.isEmpty()) {
                    String fileRealName = imgurl.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/goods/coverimg/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        imgurl.transferTo(savedFile);  //转存文件
                    }
                }
                imgurle = imgdomain + savedFileName;
            }
            productCategory.setImgurl(imgurle);
            int result = productCategoryService.addCategory(productCategory);
            map.put("result", result);
        } catch (Exception e) {
            log.error("增加失败", e);
        }
        return map;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ProductCategory queryCategory(Integer id) {
        return productCategoryService.queryCategory(id);
    }

    /**
     * 修改类别
     *
     * @param request
     * @param id
     * @param typename
     * @param imgurl
     * @return
     */
    public Map<String, Integer> updateCategory(HttpServletRequest request, String id, String typename, MultipartFile imgurl) {
        Map<String, Integer> map = new HashedMap();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(Integer.parseInt(id));
        productCategory.setTypename(typename);
        try {
            //上传图片到本地服务器
            String savedFileName = "";
            String imgurle = "";
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (imgurl != null && isMultipart) {
                if (!imgurl.isEmpty()) {
                    String fileRealName = imgurl.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/goods/coverimg/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        imgurl.transferTo(savedFile);  //转存文件
                    }
                }
                imgurle = imgdomain + savedFileName;
            }
            productCategory.setImgurl(imgurle);
            int result = productCategoryService.updateCategory(productCategory);
            map.put("result", result);
        } catch (Exception e) {
            log.error("修改失败", e);
        }
        return map;
    }
}
