package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AccountBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.mapper.*;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心业务处理
 * @author cxx
 *
 */
@Service
@Transactional
public class MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private CertificateMapper certificateMapper;

	@Autowired
	private WorkTypeMapper workTypeMapper;

	@Autowired
	private EnterpriseTypeMapper enterpriseTypeMapper;

	@Autowired
	private IdentityMapper identityMapper;

	@Autowired
	private ProvinceMapper provinceMapper;

	@Autowired
	private CityMapper cityMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private CertificateRecordMapper certificateRecordMapper;

	@Autowired
	private EmployeeSizeMapper employeeSizeMapper;

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	ZhbService zhbService;
	/**
	 * 会员信息更新
	 */
	public int updateMemInfo(Member member)
	{
		try{
			return memberMapper.updateMemInfo(member);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 根据会员ID查询会员信息
	 */
	public Member findMemById(String id)
	{
		try{
			return memberMapper.findMemById(id);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 新建员工
	 */
	public int addMember(Member member) throws Exception {
		int result = 0;
		try{
			boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.YGZH.toString());
			if(bool) {
				result = memberMapper.addMember(member);
				zhbService.payForGoods(Long.parseLong(member.getId()),ZhbPaymentConstant.goodsType.YGZH.toString());
			}else{//支付失败稍后重试，联系客服
				throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
			}
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 查询代理商
	 */
	public List<AccountBean> findAgentMember(String account, String type)
		{
		try{
			return memberMapper.findAgentMember(account,type);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 根据会员账号查询会员
	 */
	public Member findMember(Member member)
	{
		try{
			return memberMapper.findMember(member);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 资质类型
	 */
	public List findCertificateList(String type)
	{
		try {
			List<Certificate> certificate = certificateMapper.findCertificateList(type);
			List list = new ArrayList();
			for(int i=0;i<certificate.size();i++){
				Certificate certificate1 = certificate.get(i);
				String [] stringArr= certificate1.getDegree().split(",");
				Map map = new HashMap();
				map.put("id",certificate1.getId());
				map.put("name",certificate1.getName());
				map.put("degree",stringArr);
				list.add(map);
			}
			return list;
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 员工角色
	 */
	public List<WorkType> findWorkTypeList()
	{
		try{
			return workTypeMapper.findWorkTypeList();
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 工作类别
	 */
	public List<WorkType> findIndividualWorkTypeList()
	{
		try{
			return workTypeMapper.findIndividualWorkTypeList();
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 人员规模
	 */
	public List<EmployeeSize> findEmployeeSizeList()
	{
		try{
			return employeeSizeMapper.findEmployeeSizeList();
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 企业性质
	 */
	public List<EnterpriseType> findEnterpriseTypeList()
	{
		try{
			return enterpriseTypeMapper.findEnterpriseTypeList();
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 企业身份
	 */
	public List<Identity> findIdentityList()
	{
		try{
			return identityMapper.findIdentityList();
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 资质保存
	 */
	public int saveCertificate(CertificateRecord record)
	{
		try{
			return certificateRecordMapper.saveCertificate(record);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 资质查询
	 */
	public List<CertificateRecord> certificateSearch(CertificateRecord record)
	{
		try{
			return certificateRecordMapper.certificateSearch(record);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 资质删除
	 */
	public int deleteCertificate(String id)
	{
		try{
			return certificateRecordMapper.deleteCertificate(id);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 资质删除
	 */
	public int updateCertificate(CertificateRecord record)
	{
		try{
			return certificateRecordMapper.updateCertificate(record);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 根据父类ID查询公司下属员工
	 */
	public List findStaffByParentId(Paging<Member> pager, Map<String, Object> map)
	{
		try {
			List<Member> memberList = memberMapper.findAllByPager(pager.getRowBounds(),map);
			List list = new ArrayList();
			for(int i=0;i<memberList.size();i++){
				Map map1 = new HashMap();
				Member member1 = memberList.get(i);
				map1.put("id",member1.getId());
				if(member1.getMobile()!=null){
					map1.put("account",member1.getMobile());
				}else{
					map1.put("account",member1.getEmail());
				}

				String workTypeName = "";
				if(member1.getWorkType()!=null){
					WorkType workType = workTypeMapper.findWorkTypeById(member1.getWorkType().toString());
					workTypeName = workType.getName();
				}

				map1.put("role",workTypeName);
				map1.put("roleId",member1.getWorkType());
				map1.put("name",member1.getEnterpriseLinkman());
				list.add(map1);
			}
			return list;
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *查询最新供应商 工程商
	 * @return
     */
	public List findNewEngineerOrSupplier(String type){
		try {
			List<Member> memberList = memberMapper.findNewEngineerOrSupplier(type);
			List list = new ArrayList();
			for(int i=0;i<memberList.size();i++){
				Member member = memberList.get(i);
				Map map = new HashMap();
				map.put(Constants.id,member.getId());
				map.put(Constants.companyName,member.getEnterpriseName());
				map.put(Constants.webSite,member.getEnterpriseWebSite());
				map.put(Constants.address,member.getAddress());
				map.put(Constants.saleProductDesc,member.getSaleProductDesc());
				map.put(Constants.logo,member.getEnterpriseLogo());
				map.put(Constants.status,member.getStatus());
				list.add(map);
			}
			return list;
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}


	/**
	 *查询最新认证供应商 工程商
	 * @return
	 */
	public List findnewIdentifyEngineer(String type){
		try {
			List list = new ArrayList();
			if("2".equals(type)){
				List<Member> list1 = memberMapper.findnewIdentifyEngineer("4");
				for(int i=0;i<list1.size();i++){
					Member member = list1.get(i);
					Map map = new HashMap();
					map.put(Constants.id,member.getId());
					map.put(Constants.companyName,member.getEnterpriseName());
					list.add(map);
				}
			}else{
				List<Member> list2 = memberMapper.findnewIdentifyEngineer("2");
				List<Member> list3 = memberMapper.findnewIdentifyEngineer("1");
				List<Member> list4 = memberMapper.findnewIdentifyEngineer("3");
				list2.removeAll(list3);
				list2.addAll(list3);
				list2.removeAll(list4);
				list2.addAll(list4);
				for(int a=0;a<list2.size();a++){
					Member member = list2.get(a);
					Map map1 = new HashMap();
					map1.put(Constants.id,member.getId());
					map1.put(Constants.companyName,member.getEnterpriseName());
					list.add(map1);
				}
			}
			if(list.size()>=6){
				list = list.subList(0,6);
			}
			return list;
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *供应商 工程商简版介绍
	 * @return
	 */
	public Map introduce(String id,String type){
		try {
			Member member = memberMapper.findMemById(id);
			String enterpriseTypeName = "";
			String provinceName= "";
			String cityName="";
			String areaName="";
			String address = "";
			String employeeSizeName="";
			String identifyName = "";
			if(member.getEnterpriseType()!=null){
				EnterpriseType enterpriseType = enterpriseTypeMapper.selectByPrimaryKey(member.getEnterpriseType());
				enterpriseTypeName = enterpriseType.getName();
			}
			if(member.getProvince()!=null){
				Province province = provinceMapper.getProInfo(member.getProvince());
				provinceName = province.getName();
			}
			if(member.getCity()!=null){
				City city = cityMapper.getCityInfo(member.getCity());
				cityName = city.getName();
			}
			if(member.getArea()!=null){
				Area area = areaMapper.getAreaInfo(member.getArea());
				areaName = area.getName();
			}
			if(member.getEmployeeNumber()!=null){
				EmployeeSize employeeSize = employeeSizeMapper.selectByPrimaryKey(Integer.parseInt(member.getEmployeeNumber()));
				employeeSizeName = employeeSize.getName();
			}
			if(member.getIdentify()!=null){
				String identifys[] = member.getIdentify().split(",");
				for(int i=0;i<identifys.length;i++){
					String identify = identifys[i];
					Identity identityInfo = identityMapper.selectByPrimaryKey(Integer.parseInt(identify));
					identifyName = identityInfo.getName() + "  " + identifyName;
				}
			}
			address = provinceName + cityName + areaName;
			CertificateRecord certificateRecord = new CertificateRecord();
			certificateRecord.setMem_id(id);
			certificateRecord.setType(type);
			certificateRecord.setIs_deleted(0);
			certificateRecord.setStatus("1");
			List<CertificateRecord> certificateRecordList = certificateRecordMapper.certificateSearch(certificateRecord);
			Map map = new HashMap();
			String createTime = "";
			if(member.getEnterpriseCreaterTime()!=null){
				createTime = member.getEnterpriseCreaterTime().substring(0,10);
			}
			map.put(Constants.companyName,member.getEnterpriseName());
			map.put(Constants.enterpriseTypeName,enterpriseTypeName);
			map.put(Constants.area,address);
			map.put(Constants.enterpriseCreaterTime,createTime);
			map.put(Constants.registerCapital,member.getRegisterCapital());
			map.put(Constants.employeeNumber,employeeSizeName);
			map.put(Constants.identifyName,identifyName);
			map.put(Constants.enterpriseDesc,member.getEnterpriseDesc());
			map.put(Constants.saleProductDesc,member.getSaleProductDesc());
			map.put(Constants.address,member.getAddress());
			map.put(Constants.webSite,member.getEnterpriseWebSite());
			map.put(Constants.telephone,member.getEnterpriseTelephone());
			map.put(Constants.fax,member.getEnterpriseFox());
			map.put(Constants.certificateRecord,certificateRecordList);
			return map;
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 *名企展示
	 * @return
	 */
	public List greatCompany(String type){
		List list = new ArrayList();
		if("2".equals(type)){
			List<ResultBean> list1 = memberMapper.findGreatCompany("4");
			for(int i=0;i<list1.size();i++){
				ResultBean resultBean = list1.get(i);
				Map map = new HashMap();
				map.put(Constants.id,resultBean.getCode());
				map.put(Constants.companyName,resultBean.getName());
				map.put(Constants.logo,resultBean.getSmallIcon());
				list.add(map);
			}
		}else{
			List<ResultBean> list2 = memberMapper.findGreatCompany("2");
			List<ResultBean> list3 = memberMapper.findGreatCompany("1");
			List<ResultBean> list4 = memberMapper.findGreatCompany("3");
			list2.removeAll(list3);
			list2.addAll(list3);
			list2.removeAll(list4);
			list2.addAll(list4);
			for(int a=0;a<list2.size();a++){
				ResultBean resultBean = list2.get(a);
				Map map1 = new HashMap();
				map1.put(Constants.id,resultBean.getCode());
				map1.put(Constants.companyName,resultBean.getName());
				map1.put(Constants.logo,resultBean.getSmallIcon());
				list.add(map1);
			}
		}
		if(list.size()>=12){
			list = list.subList(0,12);
		}
		return list;
	}

	/**
	 *留言
	 * @return
	 */
	public int saveMessage(Message message){
		try {
			return messageMapper.saveMessage(message);
		}
		catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
}
