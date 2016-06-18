package com.zhuhuibao.business.oms.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;
import com.zhuhuibao.mybatis.project.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;

/**
 * 申请技术培训课程业务处理类
 *
 * @author pl
 * @create 2016/6/1 0001
 **/
@RestController
@RequestMapping("/rest/upload/oms")
@Api(value = "upload", description = "上传文件")
public class UploadFileColler {
	private static final Logger log = LoggerFactory.getLogger(UploadFileColler.class);
 
	@Autowired
	ProjectService projectService;
	
	@Autowired
    ConstantService service;
	
	@Autowired
    private UploadService uploadService;
	
	private String failReason;
	
	List fialList=new ArrayList();
	
	List<ProjectLinkman> partyBList= new ArrayList<ProjectLinkman>();

	String details="";
	String temp="";
	List<Map<String,String>> typeList;
	 
	@RequestMapping(value = "upload_project", method = RequestMethod.POST)
	@ApiOperation(value = "导入项目工程信息", notes = "导入项目工程信息", response = Response.class)
	public Response uploadProject(HttpServletRequest req) {
		Response response = new Response(); 
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null == session){
            response.setMessage("you are not login!");
        }
        else {
         //   OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
        	
        	String fileToBeRead=null;
			try {
				fileToBeRead = uploadService.upload(req,"project");
				 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           
	            
        	typeList=service.findByType("8");
             //fileToBeRead="D:/workspace/zhuhuibao/target/classes/com/zhuhuibao/business/oms/upload/南京-项目信息.xls";
    		try {
    			// 创建对Excel工作簿文件的引用
    			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
    					fileToBeRead));
    			// 创建对工作表的引用。 
    			HSSFSheet sheet = workbook.getSheetAt(0);
    			// 获取sheet页数据行
    			int rowsCount=sheet.getLastRowNum();
    			
    			 List projectList=new ArrayList();
    		    
    			if(rowsCount<=0)
    			{
    				
    			}else{

    				//解析数据
    				for(int i=0;i<rowsCount-2;i++)
    				{
    					HSSFRow row = sheet.getRow(i+2);
    					
    					
    					ProjectInfo projectInfo= this.rowToPrjectInfo(row,i+2,1);
    					
    					projectList.add(projectInfo);
    					//解析后数据入库
    					if(projectInfo!=null){
    						
    						projectService.addProjectInfo(projectInfo);
    					}
    				
    				
    				}
    				
    				System.out.println("result"+projectList.size());
    			}
    			
    	 
    			
    		} catch (Exception e) {
    			System.out.println("已运行xlRead() : " + e);
    		}
           
        }
        
		

		return response;
	}
	/**
	 *  数据规则验证处理
	 * @param row
	 * @return
	 */
	private List verificationRule(HSSFRow row)
	{
		List result=new ArrayList();
		
		return result;
	}
	/**
	 * row 转换成项目信息
	 * @return
	 */
	private ProjectInfo rowToPrjectInfo(HSSFRow row,int rowsNum,Integer id){
		
		ProjectInfo ProjectInfo= new ProjectInfo(); 
		
		ProjectInfo.setCreateid(Long.valueOf(id));
		String name;
		failReason="第"+rowsNum+"行";
		failReason="";
		if(!isEmpty(row.getCell(0))){
			name=row.getCell(0).toString();
			ProjectInfo.setName(name);
		}else{
			failReason+= ",第1列，项目名称为空，导入失败";
			return null;
		}
		
		if(!isEmpty(row.getCell(1))){
			String address=row.getCell(1).toString();
			int areaIndex=address.indexOf("县")>0?address.indexOf("县"):address.indexOf("区");
            String areaOrCity=address.substring(0,areaIndex+1);
            Map areaOrCityMap=new HashMap();
            areaOrCityMap.put("area", areaOrCity);
            areaOrCityMap.put("city", "南京市");
            Map codeMap=projectService.getAreaOrCity(areaOrCityMap);
            if(codeMap!=null){
				ProjectInfo.setArea(codeMap.get("code").toString());
				ProjectInfo.setCity(codeMap.get("cityCode").toString());
				ProjectInfo.setAddress(address.substring(areaIndex+1,address.length()));
            }else{
            	failReason+= ",第2列，项目地址不正确，导入失败";
            }
			
		} 
		
		if(!isEmpty(row.getCell(2))){
			String priceStr=row.getCell(2).toString().replace(",", "");
			try{
				Double price=Double.valueOf(priceStr)*100;
				ProjectInfo.setPrice(price);
			}catch(Exception ex){
				failReason+="第3列，造价格式不正确";
			}
		}
		
		
        if(!isEmpty(row.getCell(3))){
        	
			String category= row.getCell(3).toString();
			String typeCode=null;
			
			String [] categoryArray=category.split("/");
			for(int i=0;i<typeList.size();i++)
			{
				Map typeMap=typeList.get(i);
				String typeName=typeMap.get("name").toString();
				
				for(int j=0;j<categoryArray.length;j++)
				{
					if("教育".equals(typeName)&&("教育".equals(categoryArray[j])||"科研设施".equals(categoryArray[j])))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("医疗".equals(categoryArray[j])&&"医疗".equals(typeName))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("政府".equals(typeName)&&("公用事业设施".equals(categoryArray[j])||
							"开采".equals(categoryArray[j])||"石油化工".equals(categoryArray[j])
							||"电力".equals(categoryArray[j])||"基础设施".equals(categoryArray[j])))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("司法".equals(categoryArray[j])&&"司法".equals(typeName))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("企业".equals(typeName)&&("工业".equals(categoryArray[j])||
							"零售".equals(categoryArray[j])||"交通运输枢纽及仓储物流".equals(categoryArray[j])
							||"展览".equals(categoryArray[j])))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("酒店文体".equals(typeName)&&("酒店".equals(categoryArray[j])||
							"文娱康乐".equals(categoryArray[j])))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
					
					if("写字楼".equals(typeName)&&("办公楼".equals(categoryArray[j])))
					{
						if(typeCode==null)
						{
							typeCode=typeMap.get("code").toString();
						}else{
							typeCode+=","+typeMap.get("code").toString();
						}
					}
				}
				
				
			}
			
			try{
			    ProjectInfo.setCategory(Integer.valueOf(typeCode.substring(0,1)));
			}catch(Exception ex){
				
				failReason+="第4列,项目类别不正确";
			}
		}
        

        if(!isEmpty(row.getCell(5))){
			String description= row.getCell(5).toString();
			
			ProjectInfo.setDescription(description);
		}
        

        if(!isEmpty(row.getCell(6))){
			String startDate= row.getCell(6).toString();
			if(startDate.indexOf("年第一季度")>=0)
			{
				startDate=startDate.replace("年第一季度", "-01-01");
			}
			if(startDate.indexOf("年第二季度")>=0)
			{
				startDate=startDate.replace("年第一季度", "-04-01");
			}
			if(startDate.indexOf("年第三季度")>=0)
			{
				startDate=startDate.replace("年第一季度", "-07-01");
			}
			if(startDate.indexOf("年第四季度")>=0)
			{
				startDate=startDate.replace("年第一季度", "-11-01");
			}
			startDate=startDate.replace("年", "-").replace("月", "-");
			if(startDate.length()==8)
			{
				startDate=startDate+"01";
			}
		 
			ProjectInfo.setStartDate(startDate);
		}
        
        if(!isEmpty(row.getCell(7))){
			String endDate= row.getCell(7).toString();
			if(endDate.indexOf("年第一季度")>=0)
			{
				endDate=endDate.replace("年第一季度", "-03-31");
			}
			if(endDate.indexOf("年第二季度")>=0)
			{
				endDate=endDate.replace("年第一季度", "-06-30");
			}
			if(endDate.indexOf("年第三季度")>=0)
			{
				endDate=endDate.replace("年第一季度", "-09-30");
			}
			if(endDate.indexOf("年第四季度")>=0)
			{
				endDate=endDate.replace("年第一季度", "-12-31");
			}
			endDate=endDate.replace("年", "-").replace("月", "-");
			if(endDate.length()==8)
			{
				endDate=endDate+"01";
			}
			ProjectInfo.setEndDate(endDate);
		}
        
        if(!isEmpty(row.getCell(8))){
			String publishDate= row.getCell(8).toString();
			publishDate+=":00";
			ProjectInfo.setPublishDate(publishDate);
		}
        //甲方信息
        if(!isEmpty(row.getCell(9))){
        	
			String[] partyContent= row.getCell(9).toString().replace("\n联系人备注", "联系人备注").split("\n\n");
			System.out.println("rowsNum============"+rowsNum);
			List<ProjectLinkman> partyAList= new ArrayList<ProjectLinkman>();
			String deptType="";
			for(int i=0;i<partyContent.length;i++)
			{
				String[] partyList=partyContent[i].split("\n"); 
				ProjectLinkman linkma=new ProjectLinkman();
				linkma.setPartyType(Long.valueOf("1"));
				if(partyList.length==3)
				{
					deptType=partyList[0];
					linkma.setDeptType(deptType);
					linkma.setName(partyList[1]);
					details=partyList[2].replace("联系人备注", "备注");
					
					int index=details.indexOf(":")+1; 
					
					temp=this.subStr(details, index);
					if("联系人:".equals(temp))
					{
						details=details.replace("联系人:", "");
						index=details.indexOf(":")+1; 
						String linkName=this.subStr(details, index);
						
						temp=linkName.substring(linkName.length()-3,linkName.length());
						linkma.setLinkman(linkName.replace(temp, ""));
						details=details.replace(linkName, "");
						int result=0;
						while(result<4)
						{
							linkma=setLinkMan(linkma);
							result++;
						}
						
					} 
					partyAList.add(linkma);
				}else if(deptType!=""){
					
					linkma.setDeptType(deptType);
					linkma.setName(partyList[0]);
					
					if(partyList.length<3)
					{
					
					  linkma.setNote(partyList[1]);
					}else{
						details=partyList[1];
						int index=details.indexOf(":")+1; 
						
						temp=this.subStr(details, index);
						if("联系人:".equals(temp))
						{
							details=details.replace("联系人:", "");
							index=details.indexOf(":")+1; 
							String linkName=this.subStr(details, index);
							
							temp=linkName.substring(linkName.length()-3,linkName.length());
							linkma.setLinkman(linkName.replace(temp, ""));
							details=details.replace(linkName, "");
							int result=0;
							while(result<4)
							{
								linkma=setLinkMan(linkma);
								result++;
							}
						}
						
					}
					partyAList.add(linkma);
				}else{
					failReason="第"+rowsNum+"行，第10列，业主 / 开发商 格式不正确";
				}
			}
			ProjectInfo.setPartyAList(partyAList);
		 
		}
		
           //封装乙方信息 建筑师 / 设计师
           if(!isEmpty(row.getCell(10))){ 
        	   
        	setPartBList(row.getCell(10).toString(), rowsNum, ProjectInfo,"1"); 
		   }
           
           //封装乙方信息 工程师 / 技术顾问
           if(!isEmpty(row.getCell(11))){ 
        	setPartBList(row.getCell(11).toString(), rowsNum, ProjectInfo,"2");  
		   }
           
           //封装乙方信息 承建商
           if(!isEmpty(row.getCell(12))){ 
        	setPartBList(row.getCell(12).toString(), rowsNum, ProjectInfo,"3"); 
		   }
           
           //封装乙方信息 分包商
           if(!isEmpty(row.getCell(13))){ 
        	setPartBList(row.getCell(13).toString(), rowsNum, ProjectInfo,"4"); 
		   }
           
           ProjectInfo.setPartyBList(partyBList);
		fialList.add(failReason);
		HSSFCell cell = row.getCell((short) 0);
		return ProjectInfo; 
	}
	
	/**
	 * 封装乙方信息
	 * @param row
	 * @param rowsNum
	 * @param ProjectInfo
	 */
	private void setPartBList(String rowContetnt, int rowsNum, ProjectInfo ProjectInfo,String type) {
		String[] partyContent=rowContetnt.replace("\n联系人备注", "联系人备注").split("\n\n");
		 
		
		
		String deptType="";
		for(int i=0;i<partyContent.length;i++)
		{
			String[] partyList=partyContent[i].split("\n"); 
			ProjectLinkman linkma=new ProjectLinkman();
			linkma.setPartyType(Long.valueOf("2"));
			linkma.setTypePartyB(Integer.valueOf(type));
			if(partyList.length==3)
			{
				deptType=partyList[0];
				linkma.setDeptType(deptType);
				linkma.setName(partyList[1]);
				details=partyList[2].replace("联系人备注", "备注");
				
				int index=details.indexOf(":")+1; 
				
				temp=this.subStr(details, index);
				if("联系人:".equals(temp))
				{
					details=details.replace("联系人:", "");
					index=details.indexOf(":")+1; 
					String linkName=this.subStr(details, index);
					
					temp=linkName.substring(linkName.length()-3,linkName.length());
					linkma.setLinkman(linkName.replace(temp, ""));
					details=details.replace(linkName, "");
					int result=0;
					while(result<4)
					{
						linkma=setLinkMan(linkma);
						result++;
					}
					
				} 
				partyBList.add(linkma);
			}else if(deptType!=""){
				
				linkma.setDeptType(deptType);
				linkma.setName(partyList[0]);
				
				if(partyList.length<3)
				{
				
				  linkma.setNote(partyList[1]);
				}else{
					 details=partyList[1];
					int index=details.indexOf(":")+1; 
					
					temp=this.subStr(details, index);
					if("联系人:".equals(temp))
					{
						details=details.replace("联系人:", "");
						index=details.indexOf(":")+1; 
						String linkName=this.subStr(details, index);
						
						temp=linkName.substring(linkName.length()-3,linkName.length());
						linkma.setLinkman(linkName.replace(temp, ""));
						details=details.replace(linkName, "");
						int result=0;
						while(result<4)
						{
							linkma=setLinkMan(linkma);
							result++;
						}
					}
					
				}
				partyBList.add(linkma);
			}else{
				failReason="第"+rowsNum+"行，第11列，建筑师 / 设计师 格式不正确";
			}
		}
	 
	}
	
	/**
	 * 封装联系人信息
	 * @param linkma
	 * @param details
	 * @param temp
	 * @return 
	 */
	private ProjectLinkman setLinkMan(ProjectLinkman linkma) {
		int index;
		if("电话:".equals(temp.trim())){
			index=this.details.indexOf(":")+1; 
			if(index==0)
			{ 
				linkma.setMobile(details);
			}else{
			String mobile=this.subStr(details, index);
			details=details.replace(mobile, "");
			temp=mobile.substring(mobile.length()-3,mobile.length());
			mobile=mobile.replace(temp, "").trim();
			linkma.setMobile(mobile);
			}
		}else if("传真:".equals(temp.trim())){
			index=details.indexOf(":")+1; 
			if(index==0)
			{ 
				linkma.setFax(details);
			}else{
				String fax=this.subStr(details, index); 
				temp=fax.substring(fax.length()-3,fax.length());
				details=details.replace(fax, "");
				fax=fax.replace(temp, "").trim();
				linkma.setFax(fax);
			}
			//项目地址
		}else if("地址:".equals(temp.trim())){
			index=details.indexOf(":")+1; 
			if(index==0)
			{
				linkma.setAddress(details); 
			}else{
				String address=this.subStr(details, index);
				temp=address.substring(address.length()-3,address.length());
				details=details.replace(address, "");
				address=address.replace(temp, "").trim();
				linkma.setAddress(address);
			}
			
		}else{ 
		 
			linkma.setNote(details);
		}
		return linkma;
	}
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private boolean  isEmpty(Object obj)
	{
		if(obj==null ||obj=="")
		{
			return true;
		}
		return false;
	}
	/**
	 *  字符串截取
	 * @param str
	 * @param index
	 * @return
	 */
	private String subStr(String str,int index)
	{
		return str.substring(0, index);
	}
 
}
