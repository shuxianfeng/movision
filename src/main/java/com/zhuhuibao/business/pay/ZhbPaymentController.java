package com.zhuhuibao.business.pay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiOperation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wordnik.swagger.annotations.ApiParam;

import org.apache.poi.hwpf.HWPFDocument;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.service.payment.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.file.ExporDoc;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 筑慧币付款，消费流程
 *
 * @author pl
 * @version 2016/6/21 0021
 */
@RestController
@RequestMapping(value ="/rest/zhbpayment/site/base")
@Api(value="ZhbPayment", description="筑慧币付款")
public class ZhbPaymentController {
    @Autowired
    PaymentService paymentService;

    @Autowired
    ResumeService resume;
    @ApiOperation(value = "商品是否已经购买", notes = "商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_payment", method = RequestMethod.GET)
    public Response previewResume(@ApiParam(value = "商品ID") @RequestParam String GoodsID,
                                  @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
        return paymentService.viewGoodsRecord(Long.parseLong(GoodsID),type);
    }
    
    @ApiOperation(value = "简历查看", notes = "商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_paymentResume", method = RequestMethod.GET)
    public Response previewResumeNew(@ApiParam(value = "商品ID") @RequestParam String GoodsID,
                                  @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
        Response response = paymentService.viewResumeRecord(Long.parseLong(GoodsID),type);
        return response;
    }
    
    @RequestMapping(value="export_resume", method = RequestMethod.GET)
    @ApiOperation(value="定义简历模板导出简历",notes = "定义简历模板导出简历")
    public void exportResume(HttpServletRequest req, HttpServletResponse response,
                             @ApiParam(value = "简历ID") @RequestParam Long resumeID) throws IOException
    {
         
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setContentType("application/msword");
        try {
            String path = req.getSession().getServletContext().getRealPath("/");
             
            Map<String, String> resumeMap=null;
			try {
				resumeMap = resume.exportResumeNew(String.valueOf(resumeID));
			} catch (NumberFormatException e) {
				 
				e.printStackTrace();
			} catch (Exception e) { 
				e.printStackTrace();
			}
            if (!resumeMap.isEmpty()) {
                String fileName =  !StringUtils.isEmpty(resumeMap.get("title")) ? resumeMap.get("title") :"简历";
                response.setHeader("Content-disposition", "attachment; filename=\""
                        + URLEncoder.encode(fileName, "UTF-8") + ".doc\"");
                HWPFDocument document = ExporDoc.replaceDoc(path + "resumeTemplate.doc", resumeMap);
                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                if (document != null) {
                    document.write(ostream);
                }
                ServletOutputStream stream = response.getOutputStream();
                stream.write(ostream.toByteArray());
                stream.flush();
                stream.close();
                stream.close();

                Resume resumeBean=new Resume();
                resumeBean.setDownload("1");
                resumeBean.setType("1");
                resumeBean.setId(String.valueOf(resumeID));
                resume.updateResume(resumeBean);

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
