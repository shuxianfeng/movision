package com.zhuhuibao.business.oms.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;
import com.zhuhuibao.mybatis.project.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.oss.ZhbOssClient;

/**
 * 申请技术培训课程业务处理类
 *
 * @author pl
 * @create 2016/6/1 0001
 **/
@RestController
@RequestMapping("/rest/upload/oms")
@Api(value = "upload", description = "上传文件")
public class UploadFileController {
	private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);
 
	@Autowired
	ProjectService projectService;
	
    @Autowired
    ApiConstants ApiConstants;
    
    @Autowired
    ZhbOssClient zhbOssClient;
	
	@Autowired
    ConstantService service;
	
	@Autowired
    private UploadService uploadService;
	
	private String failReason;
	
	List<String> fialList=null;
	
	List<ProjectLinkman> partyBList= null;

	String details="";
	String temp="";
	Map<String, String> typeList;
	
	int isAdd=0;
	int sucCount=0;
	int failCount=0;
	int rowsCount=10000;
	String fileToBeRead=null;
	@RequestMapping(value = "upload_project", method = RequestMethod.POST)
	@ApiOperation(value = "导入项目工程信息", notes = "导入项目工程信息", response = Response.class)
	public Response uploadProject(HttpServletRequest req,@RequestParam(value = "file", required = false) MultipartFile file) {
		Response response = new Response(); 
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        rowsCount=10000;
        if(null == session){
            response.setMessage("you are not login!");
        }
        else {
            
			try {
				fileToBeRead =  zhbOssClient.uploadObject(file,"doc","project");
				response.setCode(200);
		   	 } catch (Exception e1) {
				e1.printStackTrace();
			} 
           
        } 

		return response;
	}
	
	@RequestMapping(value = "upload_dataproject", method = RequestMethod.POST)
	@ApiOperation(value = "导入项目工程信息", notes = "导入项目工程信息", response = Response.class)
	public Response saveDataProject() {
		 Subject currentUser = SecurityUtils.getSubject();
         Session session = currentUser.getSession(false);
		 OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
		 Response response = new Response();
		try {
			// 创建对Excel工作簿文件的引用
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream( ApiConstants.getUploadDoc() + "/project/doc/"+
					fileToBeRead));
			// 创建对工作表的引用。 
			HSSFSheet sheet = workbook.getSheetAt(0);
			// 获取sheet页数据行
			  rowsCount=sheet.getLastRowNum();
			
			 List<ProjectInfo> projectList=new ArrayList<ProjectInfo>();
			 
			 fialList=new ArrayList<String>();
			if(rowsCount<=0)
			{
				failReason="项目格式导入内容不正确，请重与管理员联系！";
				Map<String, Object> result=new HashMap<String, Object>();
				fialList.add(failReason);
				result.put("fialList", fialList);
		        response.setCode(200);  
				response.setData(result);
			}else{

				//解析数据
				sucCount=0;
				failCount=0;
				for(int i=1;i<=rowsCount;i++)
				{
					HSSFRow row = sheet.getRow(i);
					isAdd=0;
					ProjectInfo projectInfo=null;
					try { 
					    projectInfo= this.rowToPrjectInfo(row,i,principal.getId());
					  } catch (Exception e) {
						  failCount+=1;
						  fialList.add("第"+i+"行格式转换异常");
						  log.error("projectInfo:转换异常",e);
						  continue;
				    }
				 
					projectList.add(projectInfo);
					//解析后数据入库
					if(projectInfo!=null&&isAdd==0){
						 
						try { 
						   projectService.addProjectInfo(projectInfo);
						   sucCount+=1;
						 } catch (Exception e) {
							 failCount+=1;
							 
							 fialList.add("第"+i+"行插入异常");
						     log.error("projectInfo:转换异常",e);
						     continue;
				        } 
						
					}else if(isAdd==2){
						sucCount+=1;
					}else{
						failCount+=1;
					}
				
				
				}
				
				Map<String, Object> result=new HashMap<String, Object>();
				result.put("sucCount", sucCount);
				result.put("failCount", failCount);
				result.put("msg", "本次上传:"+(rowsCount)+"条项目信息,成功:"+sucCount+"失败:"+failCount);
				result.put("fialList", fialList);
				response.setCode(200); 
				
				response.setData(result);
			}
			
  	 
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return response;
	}
	 
	/**
	 * row 转换成项目信息
	 * @return
	 */
	@SuppressWarnings("unused")
	private ProjectInfo rowToPrjectInfo(HSSFRow row,int rowsNum,Integer id){
		
		ProjectInfo projectInfo= new ProjectInfo(); 
		Map<String, Object> map = new HashMap<String, Object>();
		
		projectInfo.setCreateid(Long.valueOf(id));
		String name;
		failReason="第"+(rowsNum+1)+"行";
		if(!isEmpty(row.getCell(0))){
			name=row.getCell(0).toString();
			try {
				map.put("name", name);
				List<Map<String,String>> list=projectService.findPrjectByName(map);
				if(list!=null&&list.size()>0)
				{
					isAdd=2; 
					//failReason+= ",第1列，项目已导入"; 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			projectInfo.setName(name);
		}else{
			failReason+= ",第1列，项目名称为空，导入失败;"; 
			isAdd=1; 
		}
		
		if(!isEmpty(row.getCell(3))){
			String address=row.getCell(3).toString();
			int areaIndex=address.indexOf("市")>=0?address.indexOf("市"):10000;
			
			if(areaIndex>address.indexOf("区") && address.indexOf("区")>=0)
			{
				areaIndex=address.indexOf("区");
			}
			
			if(areaIndex>address.indexOf("县") && address.indexOf("县")>=0)
			{
				areaIndex=address.indexOf("县");
			}
			
			if(areaIndex>address.indexOf("州") && address.indexOf("州")>=0)
			{
				areaIndex=address.indexOf("州");
			}
			
			if(areaIndex>address.indexOf("盟") && address.indexOf("盟")>=0)
			{
				areaIndex=address.indexOf("盟");
			}
			
			if(areaIndex>address.indexOf("旗") && address.indexOf("旗")>=0)
			{
				areaIndex=address.indexOf("旗");
			}
			 
            String areaOrCity=address.substring(0,areaIndex+1);
            Map<String, String> areaOrCityMap=new HashMap<String, String>();
            areaOrCityMap.put("area", areaOrCity);
            areaOrCityMap.put("city", row.getCell(2).toString());
            Map<String,Object> codeMap=projectService.getAreaOrCity(areaOrCityMap);
            if(codeMap!=null){
				projectInfo.setArea(codeMap.get("code").toString());
				projectInfo.setCity(codeMap.get("cityCode").toString());
				projectInfo.setProvince(codeMap.get("provincecode").toString());
				projectInfo.setAddress(address.substring(areaIndex+1,address.length()));
            }else{
            	
            	codeMap=projectService.getCity(areaOrCityMap);
            	
            	projectInfo.setCity(codeMap.get("cityCode").toString());
				projectInfo.setProvince(codeMap.get("provincecode").toString());
            	if(codeMap!=null){
            		projectInfo.setAddress(address);
            	}else{
	            	isAdd=1;
	            	failReason+= ",第4列，项目地址不正确，导入失败;"; 
            	}
            }
			
		} 
		
		if(!isEmpty(row.getCell(4))){
			String priceStr=row.getCell(4).toString().replace(",", "");
			try{
				Double price=Double.valueOf(priceStr)*100;
				projectInfo.setPrice(price);
			}catch(Exception ex){
				isAdd=1;
				failReason+="第5列，造价格式不正确;";
			}
		}
		
		//项目类别
        if(!isEmpty(row.getCell(5))){
        	
			String [] categoryArray= row.getCell(5).toString().split("/"); 
			List<String> list=new ArrayList<String>();
			for(int i=0;i<categoryArray.length;i++)
			{
				list.add(categoryArray[i].trim());
			}
			
			 
			typeList=projectService.getCatagoryByValue(list);
			 
			try{
				if(typeList!=null)
				 {
			        projectInfo.setCategory(typeList.get("type"));
				 }
			}catch(Exception ex){
				isAdd=1;
				failReason+="第6列,项目类别不正确;"; 
			}
		}
        
        //项目描述
        if(!isEmpty(row.getCell(7))){
			String description= row.getCell(7).toString(); 
			projectInfo.setDescription(description);
		}
        
        //项目开工时间
        if(!isEmpty(row.getCell(8))){
			String startDate= row.getCell(8).toString();
			if(startDate.indexOf("年第一季度")>=0)
			{
				startDate=startDate.replace("年第一季度", "-01-01");
			}
			if(startDate.indexOf("年第二季度")>=0)
			{
				startDate=startDate.replace("年第二季度", "-04-01");
			}
			if(startDate.indexOf("年第三季度")>=0)
			{
				startDate=startDate.replace("年第三季度", "-07-01");
			}
			if(startDate.indexOf("年第四季度")>=0)
			{
				startDate=startDate.replace("年第四季度", "-11-01");
			}
			startDate=startDate.replace("年", "-").replace("月", "-");
			if(startDate.length()==8)
			{
				startDate=startDate+"01";
			}
			if(startDate.length()==5)
			{
				startDate=startDate.replace("-", "-01-01");
			}
		 
			projectInfo.setStartDate(startDate);
		}
        
        if(!isEmpty(row.getCell(9))){
			String endDate= row.getCell(9).toString();
			if(endDate.indexOf("年第一季度")>=0)
			{
				endDate=endDate.replace("年第一季度", "-03-31");
			}
			if(endDate.indexOf("年第二季度")>=0)
			{
				endDate=endDate.replace("年第二季度", "-06-30");
			}
			if(endDate.indexOf("年第三季度")>=0)
			{
				endDate=endDate.replace("年第三季度", "-09-30");
			}
			if(endDate.indexOf("年第四季度")>=0)
			{
				endDate=endDate.replace("年第四季度", "-12-31");
			}
			endDate=endDate.replace("年", "-").replace("月", "-");
			if(endDate.length()==8)
			{
				endDate=endDate+"01";
			}
			if(endDate.length()==5)
			{
				endDate=endDate.replace("-", "-12-31");
			}
			projectInfo.setEndDate(endDate);
		}
        
        if(!isEmpty(row.getCell(10))){
			String publishDate= row.getCell(10).toString();
			publishDate+=":00";
			projectInfo.setPublishDate(publishDate);
		}
        //甲方信息
        if(!isEmpty(row.getCell(11))){
        	
			String partyContent= "";
			String  rowContetnt = row.getCell(11).toString();
			
	 
			String [] partyContentList;
			if(rowContetnt.indexOf(";")>0)
			{
				String startStr= rowContetnt.substring(0,rowContetnt.indexOf(";")+1);
				String endStr= rowContetnt.substring(rowContetnt.indexOf(";")+1,rowContetnt.length()); 
				String zbTitle=	startStr.substring(startStr.lastIndexOf("\n"),startStr.length()); 
				partyContent=startStr.replace(zbTitle, "")+endStr.replace("联系人备注:","联系人备注:"+ zbTitle.replace("\n", ""));
			}else{
				partyContent=rowContetnt;
			}
					
			partyContentList=partyContent.replace("\n联系人备注", "联系人备注").split("\n\n");
			
			List<ProjectLinkman> partyAList= new ArrayList<ProjectLinkman>();
			 
			for(int i=0;i<partyContentList.length;i++)
			{
				String[] partyList=partyContentList[i].split("\n"); 
				ProjectLinkman linkma=new ProjectLinkman();
				linkma.setPartyType(Long.valueOf("1"));
				if(partyList.length==3)
				{
				 
					linkma.setDeptType(partyList[0]);
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
						while(result<5)
						{
							linkma=setLinkMan(linkma);
							result++;
						}
						
					} 
					partyAList.add(linkma);
				}else if(partyList.length==2){
					
					 
					linkma.setName(partyList[0]);
					
					if(partyList.length<3)
					{
					
						if("联系人".equals(partyList[1].substring(0,3)))
						{
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
								while(result<5)
								{
									linkma=setLinkMan(linkma);
									result++;
								}
							}
						}else{
					      linkma.setNote(partyList[1]);
						}
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
							while(result<5)
							{
								linkma=setLinkMan(linkma);
								result++;
							}
						}
						
					}
					partyAList.add(linkma);
				}else{
					failReason="第"+rowsNum+"行，第10列，业主 / 开发商 格式不正确;";
				}
			}
			projectInfo.setPartyAList(partyAList);
		 
		}
		
          partyBList=new ArrayList<ProjectLinkman>();
           //封装乙方信息 建筑师 / 设计师
           if(!isEmpty(row.getCell(12))){ 
        	   if(!"".equals(row.getCell(12).toString()))
	        	{
        		   setPartBList(row.getCell(12).toString(), rowsNum, projectInfo,"1"); 
	        	}
		   }
           
           //封装乙方信息 工程师 / 技术顾问
           if(!isEmpty(row.getCell(13))){ 
        	   if(!"".equals(row.getCell(13).toString()))
	        	{
        		   setPartBList(row.getCell(13).toString(), rowsNum, projectInfo,"3");  
	        	}
		   }
           
           //封装乙方信息 承建商
           if(!isEmpty(row.getCell(14))){ 
	        	if(!"".equals(row.getCell(14).toString()))
	        	{
	        	 setPartBList(row.getCell(14).toString(), rowsNum, projectInfo,"2"); 
	        	}
		   }
           
           //封装乙方信息 分包商
           if(!isEmpty(row.getCell(15))){ 
        	   if(!"".equals(row.getCell(15).toString()))
	        	{
        		   	setPartBList(row.getCell(15).toString(), rowsNum, projectInfo,"4"); 
	        	}
		   }
           
           projectInfo.setPartyBList(partyBList);
           if(isAdd==1)
           {
        	   fialList.add(failReason);
           }
		 
		return projectInfo; 
	}
	
	@RequestMapping(value = "sel_progress", method = RequestMethod.GET)
	@ApiOperation(value = "项目导入进度", notes = "项目导入进度", response = Response.class)
	public Response queryProgress() {
		Response response = new Response(); 
		Map<String, Object> result=new HashMap<String, Object>();
		int total=rowsCount;
		if(rowsCount==0)
		{
			total=1;
		}
		double proc=(double)(Math.round((this.sucCount+this.failCount)*100/total));
		 
		if(this.sucCount+this.failCount>=total){
			proc=100;
		}
		result.put("progress", proc); 
		response.setCode(200); 
		
		response.setData(result);
		return response;
	}
	/**
	 * 封装乙方信息
	 * @param row
	 * @param rowsNum
	 * @param ProjectInfo
	 */
	private void setPartBList(String rowContetnt, int rowsNum, ProjectInfo ProjectInfo,String type) {
		
		String partyContent="";
		String [] partyContentList =rowContetnt.replace("\n联系人备注", "联系人备注").replace("\n中标价:", "\t中标价:").split("\n\n");
		
		 
		for(int i=0;i<partyContentList.length;i++)
		{
			String[] partyList=partyContentList[i].split("\n"); 
			ProjectLinkman linkma=new ProjectLinkman();
			linkma.setPartyType(Long.valueOf("2"));
			linkma.setTypePartyB(Integer.valueOf(type));
			
			if(partyList.length==4)
			{
				List<String> list =null;
				list=new ArrayList<String>();
				list.add(partyList[0]+"/"+partyList[1]);
				list.add(partyList[2]);
				list.add(partyList[3]);
				partyList=list.toArray(new String[1]);
				 
				
			}
			if(partyList.length==3)
			{
				 
				linkma.setDeptType(partyList[0]);
				linkma.setName(partyList[1]);
				partyContent=partyList[2];
				if(partyContent.indexOf(";")>0&&partyContent.substring(partyContent.lastIndexOf(":")-3,partyContent.lastIndexOf(":"))=="中标价")
				{
					String startStr= partyContent.substring(0,partyContent.indexOf(";")+1);
					String endStr= partyContent.substring(partyContent.indexOf(";")+1,partyContent.length()); 
					String zbTitle=	startStr.substring(startStr.lastIndexOf("\t"),startStr.length()); 
					partyContent=startStr.replace(zbTitle, "")+endStr.replace("联系人备注:","联系人备注:"+ zbTitle.replace("\n", ""));
				}
				details=partyContent.replace("联系人备注", "备注").replace("\t", "");
				
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
					while(result<5)
					{
						 
						linkma=setLinkMan(linkma);
				 
						result++;
					}
					
				} 
				partyBList.add(linkma);
			}else if(partyList.length==2){
				
				 
				linkma.setName(partyList[0]);
				
				if(partyList.length<3)
				{
				
					if("联系人".equals(partyList[1].substring(0,3)))
					{
						details=partyList[1];
						
						partyContent=partyList[1];
						if(partyContent.indexOf(";")>0)
						{
							String startStr= partyContent.substring(0,partyContent.indexOf(";")+1);
							String endStr= partyContent.substring(partyContent.indexOf(";")+1,partyContent.length()); 
							String zbTitle=	startStr.substring(startStr.lastIndexOf("\t"),startStr.length()); 
							partyContent=startStr.replace(zbTitle, "")+endStr.replace("联系人备注:","联系人备注:"+ zbTitle.replace("\n", ""));
						} 
						
						details=partyContent.replace("\t", "");
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
					}else{
				      linkma.setNote(partyList[1]);
					}
				}else{
					 
					 partyContent=partyList[1];
					if(partyContent.indexOf(";")>0)
					{
						String startStr= partyContent.substring(0,partyContent.indexOf(";")+1);
						String endStr= partyContent.substring(partyContent.indexOf(";")+1,partyContent.length()); 
						String zbTitle=	startStr.substring(startStr.lastIndexOf("\t"),startStr.length()); 
						partyContent=startStr.replace(zbTitle, "")+endStr.replace("联系人备注:","联系人备注:"+ zbTitle.replace("\n", ""));
					} 
					
					details=partyContent.replace("\t", "");
					
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
				rowsNum=rowsNum+1;
				if("1".equals(type)){
				failReason="第"+rowsNum+"行，第12列:业主 / 开发商格式错误;";
				}else if("2".equals(type)){
				failReason="第"+rowsNum+"行，第13列:承建商格式错误格式错误;";
				}else if("3".equals(type)){
					failReason="第"+rowsNum+"行，第14列:工程师 / 技术顾问;";
				}else if("4".equals(type)){
					failReason="第"+rowsNum+"行，第15列:分包商格式错误;";
				}
				isAdd=1;
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
			String telephone=this.subStr(details, index);
			details=details.replace(telephone, "");
			temp=telephone.substring(telephone.length()-3,telephone.length());
			telephone=telephone.replace(temp, "").trim();
			linkma.setTelephone(telephone);
			}
		}else if("手机:".equals(temp.trim())){
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
			//传真
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
		}else if("地址:".equals(temp)){
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
			//网址
		}else if("网址:".equals(temp.trim())){
			index=details.indexOf(":")+1;  
			String interAdd=this.subStr(details, index); 
			temp=interAdd.substring(interAdd.length()-3,interAdd.length());
			details=details.replace(interAdd, ""); 
			 
		}else if("备注:".equals(temp.trim())){ 
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
