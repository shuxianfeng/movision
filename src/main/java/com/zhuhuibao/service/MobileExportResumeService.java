package com.zhuhuibao.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hwpf.HWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.utils.file.ExporDoc;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 导出简历
 * @author zhuangyuhao
 * @time   2016年10月17日 下午7:27:18
 *
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class MobileExportResumeService {
	
	@Autowired
    ResumeService resumeSV;
	
	/**
	 * 导出简历
	 * @param req
	 * @param response
	 * @param resumeID
	 * @throws IOException
	 */
	public void exportResume(HttpServletRequest req, HttpServletResponse response, Long resumeID)
			throws IOException
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
				resumeMap = resumeSV.exportResumeNew(String.valueOf(resumeID));
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
                resumeSV.updateResume(resumeBean);

            }
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}
