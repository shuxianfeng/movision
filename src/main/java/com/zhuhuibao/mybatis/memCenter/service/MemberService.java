package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AccountBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.mapper.*;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.convert.BeanUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AccountBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Area;
import com.zhuhuibao.mybatis.memCenter.entity.Certificate;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.City;
import com.zhuhuibao.mybatis.memCenter.entity.EmployeeSize;
import com.zhuhuibao.mybatis.memCenter.entity.EnterpriseType;
import com.zhuhuibao.mybatis.memCenter.entity.Identity;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.MemRealCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.entity.Province;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;
import com.zhuhuibao.mybatis.memCenter.mapper.AreaMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateRecordMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CityMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.EmployeeSizeMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.EnterpriseTypeMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.IdentityMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MessageMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.WorkTypeMapper;
import com.zhuhuibao.mybatis.oms.entity.MemberSucCase;
import com.zhuhuibao.mybatis.oms.service.MemberSucCaseService;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.convert.BeanUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 会员中心业务处理
 *
 * @author cxx
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

    @Autowired
    SiteMailService siteMailService;

    @Autowired
    MemShopService shopService;

    @Autowired
    MemInfoCheckService infoCheckService;

    @Autowired
    MemRealCheckService realCheckService;
    
    @Autowired
    VipInfoService vipInfoService;
    
    @Autowired
	private MemberSucCaseService memberSucCaseService;
    /**
     * 会员信息更新
     */
    public void updateMemInfo(Member member) {
        try {

            memberMapper.updateMemInfo(member);
            memberMapper.updateSubMemInfo(member);
            //资料审核已拒绝 or 实名认证已拒绝
            if ("7".equals(member.getStatus()) || "11".equals(member.getStatus())) {
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(), Long.parseLong(member.getId()),
                        member.getReason());
            }
        } catch (Exception e) {
            log.error("updateMemInfo error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据会员ID查询会员信息
     */
    public Member findMemById(String id) {
        try {
            return memberMapper.findMemById(id);
        } catch (Exception e) {
            log.error("findMemById error,id=" + id + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 新建员工
     */
    public int addMember(Member member) throws Exception {
        int result = 0;
        try {
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.YGZH.toString());
            if (bool) {
                result = memberMapper.addMember(member);
                zhbService.payForGoods(Long.parseLong(member.getId()), ZhbPaymentConstant.goodsType.YGZH.toString());
            } else {// 支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String
                        .valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        } catch (Exception e) {
            log.error("addMember error >>>", e);
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * 查询代理商
     */
    public List<AccountBean> findAgentMember(String account, String type) {
        try {
            return memberMapper.findAgentMember(account, type);
        } catch (Exception e) {
            log.error("findAgentMember error,account=" + account + ",type=" + type + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据会员账号查询会员
     * 激活并且非注销的状态
     */
    public Member findMember(Member member) {
        try {
            return memberMapper.findMember(member);
        } catch (Exception e) {
            log.error("findMember error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 资质类型
     */
    public List findCertificateList(String type) {
        try {
            List<Certificate> certificate = certificateMapper.findCertificateList(type);
            return certificate;
        } catch (Exception e) {
            log.error("findCertificateList error,type=" + type + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 员工角色
     */
    public List<WorkType> findWorkTypeList() {
        try {
            return workTypeMapper.findWorkTypeList();
        } catch (Exception e) {
            log.error("findWorkTypeList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public String queryWorkTypeById(String id) {
        try {
            WorkType workType = workTypeMapper.selectByPrimaryKey(id);
            String name = workType.getName();
            return name;
        } catch (Exception e) {
            log.error("queryWorkTypeById error,id=" + id + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 工作类别
     */
    public List<WorkType> findIndividualWorkTypeList() {
        try {
            return workTypeMapper.findIndividualWorkTypeList();
        } catch (Exception e) {
            log.error("findIndividualWorkTypeList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 人员规模
     */
    @Cacheable(value = "common", key = "#root.methodName")
    public List<EmployeeSize> findEmployeeSizeList() {
        try {
            return employeeSizeMapper.findEmployeeSizeList();
        } catch (Exception e) {
            log.error("findEmployeeSizeList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 企业性质
     */
    @Cacheable(value = "common", key = "#root.methodName")
    public List<EnterpriseType> findEnterpriseTypeList() {
        try {
            return enterpriseTypeMapper.findEnterpriseTypeList();
        } catch (Exception e) {
            log.error("findEnterpriseTypeList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 企业身份
     */
    @Cacheable(value = "common", key = "#root.methodName")
    public List<Identity> findIdentityList() {
        try {
            return identityMapper.findIdentityList();
        } catch (Exception e) {
            log.error("findIdentityList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 资质保存
     */
    public int saveCertificate(CertificateRecord record) {
        try {
            return certificateRecordMapper.saveCertificate(record);
        } catch (Exception e) {
            log.error("saveCertificate error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 资质查询
     */
    public List<CertificateRecord> certificateSearch(CertificateRecord record) {
        try {
            return certificateRecordMapper.certificateSearch(record);
        } catch (Exception e) {
            log.error("certificateSearch error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 资质删除
     */
    public int deleteCertificate(String id) {
        try {
            return certificateRecordMapper.deleteCertificate(id);
        } catch (Exception e) {
            log.error("deleteCertificate error ,id=" + id + ">>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 更新资质
     */
    public void updateCertificate(CertificateRecord record) {
        try {
            certificateRecordMapper.updateCertificate(record);
            if ("2".equals(record.getStatus())) {
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(), Long.parseLong(record.getMem_id()),
                        record.getReason());
            }
        } catch (Exception e) {
            log.error("updateCertificate error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据父类ID查询公司下属员工
     */
    public List findStaffByParentId(Paging<Member> pager, Map<String, Object> map) {
        try {
            List<Member> memberList = memberMapper.findAllByPager(pager.getRowBounds(), map);
            List list = new ArrayList();
            for (Member aMemberList : memberList) {
                Map map1 = new HashMap();
                Member member1 = aMemberList;
                map1.put("id", member1.getId());
                if (member1.getMobile() != null && !"".equals(member1.getMobile())) {
                    map1.put("account", member1.getMobile());
                } else {
                    map1.put("account", member1.getEmail());
                }

                String workTypeName = "";
                if (member1.getWorkType() != null) {
                    WorkType workType = workTypeMapper.findWorkTypeById(member1.getWorkType().toString());
                    workTypeName = workType.getName();
                }

                map1.put("role", workTypeName);
                map1.put("roleId", member1.getWorkType());
                map1.put("name", member1.getEnterpriseLinkman());
                list.add(map1);
            }
            return list;
        } catch (Exception e) {
            log.error("findStaffByParentId error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询最新供应商 工程商
     *
     * @return
     */
    public List findNewEngineerOrSupplier(String type) {
        try {
            List<Member> memberList = memberMapper.findNewEngineerOrSupplier(type);
            List list = new ArrayList();
            for (Member member : memberList) {
                Map map = new HashMap();
                map.put(Constants.id, member.getId());
                map.put(Constants.companyName, member.getEnterpriseName());
                map.put(Constants.webSite, member.getEnterpriseWebSite());
                map.put(Constants.address, member.getAddress());
                map.put(Constants.saleProductDesc, member.getSaleProductDesc());
                map.put(Constants.logo, member.getEnterpriseLogo());
                map.put(Constants.status, member.getStatus());
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            log.error("findNewEngineerOrSupplier error,type=" + type + ">>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 查询最新认证供应商 工程商
     *
     * @return
     */
    public List findnewIdentifyEngineer(String type) {
        try {
            List list = new ArrayList();
            if ("2".equals(type)) {
                List<Member> list1 = memberMapper.findnewIdentifyEngineer("4");
                for (Member member : list1) {
                    Map map = new HashMap();
                    map.put(Constants.id, member.getId());
                    map.put(Constants.companyName, member.getEnterpriseName());
                    list.add(map);
                }
            } else {
                List<Member> list2 = memberMapper.findnewIdentifyEngineer("2");
                List<Member> list3 = memberMapper.findnewIdentifyEngineer("1");
                List<Member> list4 = memberMapper.findnewIdentifyEngineer("3");
                list2.removeAll(list3);
                list2.addAll(list3);
                list2.removeAll(list4);
                list2.addAll(list4);
                for (Member member : list2) {
                    Map map1 = new HashMap();
                    map1.put(Constants.id, member.getId());
                    map1.put(Constants.companyName, member.getEnterpriseName());
                    list.add(map1);
                }
            }
            if (list.size() >= 6) {
                list = list.subList(0, 6);
            }
            return list;
        } catch (Exception e) {
            log.error("findnewIdentifyEngineer error,type=" + type + ">>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 供应商 工程商简版介绍
     *
     * @return
     */
    public Map introduce(String id, String type) {
        try {
            Member member = memberMapper.findMemById(id);
            String enterpriseTypeName = "";
            String provinceName = "";
            String cityName = "";
            String areaName = "";
            String address = "";
            String employeeSizeName = "";
            String identifyName = "";
            if (!StringUtils.isEmpty(member.getEnterpriseType())) {
                EnterpriseType enterpriseType = enterpriseTypeMapper.selectByPrimaryKey(member.getEnterpriseType());
                enterpriseTypeName = enterpriseType.getName();
            }
            if (!StringUtils.isEmpty(member.getProvince())) {
                Province province = provinceMapper.getProInfo(member.getProvince());
                provinceName = province.getName();
            }
            if (!StringUtils.isEmpty(member.getCity())) {
                City city = cityMapper.getCityInfo(member.getCity());
                cityName = city.getName();
            }
            if (!StringUtils.isEmpty(member.getArea())) {
                Area area = areaMapper.getAreaInfo(member.getArea());
                areaName = area.getName();
            }
            if (!StringUtils.isEmpty(member.getEmployeeNumber())) {
                EmployeeSize employeeSize = employeeSizeMapper.selectByPrimaryKey(Integer.parseInt(member
                        .getEmployeeNumber()));
                if (employeeSize == null) {
                    employeeSizeName = member.getEmployeeNumber();
                } else {
                    employeeSizeName = employeeSize.getName();
                }
            }
            if (!StringUtils.isEmpty(member.getIdentify())) {
                String identifys[] = member.getIdentify().split(",");
                for (String identify : identifys) {
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
            List<CertificateRecord> certificateRecordList = certificateRecordMapper
                    .certificateSearch(certificateRecord);
            Map map = new HashMap();
            String createTime = "";
            if (!StringUtils.isEmpty(member.getEnterpriseCreaterTime())) {
                createTime = member.getEnterpriseCreaterTime().substring(0, 10);
            }
            map.put(Constants.companyName, member.getEnterpriseName());
            map.put(Constants.enterpriseTypeName, enterpriseTypeName);
            map.put(Constants.area, address);
            map.put(Constants.enterpriseCreaterTime, createTime);
            if("2".equals(member.getCurrency())&&!"".equals(member.getRegisterCapital()))
            {
            map.put(Constants.registerCapital, member.getRegisterCapital()==null?"":member.getRegisterCapital()+"万美元");
            }else if("1".equals(member.getCurrency())&&!"".equals(member.getRegisterCapital())){
            	map.put(Constants.registerCapital,member.getRegisterCapital()==null?"":member.getRegisterCapital()+"万人民币");
            }else{
            	map.put(Constants.registerCapital,"");
            }
            map.put(Constants.employeeNumber, employeeSizeName);
            map.put(Constants.identifyName, identifyName);
            map.put(Constants.enterpriseDesc, member.getEnterpriseDesc());
            map.put(Constants.saleProductDesc, member.getSaleProductDesc());
            map.put(Constants.address, member.getAddress());
            map.put(Constants.webSite, member.getEnterpriseWebSite());
            map.put(Constants.telephone, member.getEnterpriseTelephone());
            map.put(Constants.fax, member.getEnterpriseFox());
            map.put(Constants.vipLevel, member.getVipLevel());
            map.put(Constants.certificateRecord, certificateRecordList);
            return map;
        } catch (Exception e) {
            log.error("introduce error,id=" + id + ",type=" + type + ">>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 名企展示
     *
     * @return
     */
    @Deprecated
    public List greatCompany(String type) {
        List list = new ArrayList();
        if ("2".equals(type)) {
            List<ResultBean> list1 = memberMapper.findGreatCompany("4");
            for (ResultBean resultBean : list1) {
                Map map = new HashMap();
                map.put(Constants.id, resultBean.getCode());
                map.put(Constants.companyName, resultBean.getName());
                map.put(Constants.logo, resultBean.getSmallIcon());
                list.add(map);
            }
        } else {
            List<ResultBean> list2 = memberMapper.findGreatCompany("2");
            List<ResultBean> list3 = memberMapper.findGreatCompany("1");
            List<ResultBean> list4 = memberMapper.findGreatCompany("3");
            list2.removeAll(list3);
            list2.addAll(list3);
            list2.removeAll(list4);
            list2.addAll(list4);
            for (ResultBean resultBean : list2) {
                Map map1 = new HashMap();
                map1.put(Constants.id, resultBean.getCode());
                map1.put(Constants.companyName, resultBean.getName());
                map1.put(Constants.logo, resultBean.getSmallIcon());
                list.add(map1);
            }
        }
        if (list.size() >= 12) {
            list = list.subList(0, 12);
        }
        return list;
    }

    /**
     * 留言
     *
     * @return
     */
    public int saveMessage(Message message) {
        try {
            return messageMapper.saveMessage(message);
        } catch (Exception e) {
            log.error("saveMessage error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, String>> queryCompanyByKeywords(String keywords) {
        try {
            return memberMapper.queryCompanyByKeywords(keywords);
        } catch (Exception e) {
            log.error("queryCompanyByKeywords error,keywords=" + keywords + " >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, String>> queryCompanyList(Map<String, Object> map) {
        try {
            return memberMapper.queryCompanyList(map);
        } catch (Exception e) {
            log.error("queryCompanyList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, String>> queryGreatCompany(Map<String, Object> map) {
        try {
            return memberMapper.queryGreatCompany(map);
        } catch (Exception e) {
            log.error("queryGreatCompany error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 完善资料审核
     * 修改审核子表状态
     * 审核通过之后同步用户主表信息
     *
     * @param member
     */
    public void updateMemData(MemInfoCheck member) {
        try {
            String status = String.valueOf(member.getStatus());

            //更新审核子表信息
            infoCheckService.update(member);

            if (status.equals(MemberConstant.MemberStatus.WSZLYSH.toString())) { // 完善审核通过
                //1.同步主表信息       {如果完善资料已审核,保持原来状态不变}
                Member mem = findMemById(String.valueOf(member.getId()));
                String memStatus = mem.getStatus();
                BeanUtils.copyProperties(member, mem);
                if (memStatus.equals(MemberConstant.MemberStatus.SMRZDSH.toString()) ||
                        memStatus.equals(MemberConstant.MemberStatus.SMRZYRZ.toString()) ||
                        memStatus.equals(MemberConstant.MemberStatus.SMRZYJJ.toString())) {
                    mem.setStatus(memStatus);
                } else {
                    mem.setStatus(status);
                }

                updateMemInfo(mem);

                //2.给用户创建商铺
                // 查询该用户是否存在商铺
                String companyId = mem.getId();
                MemberShop shop = shopService.findByCompanyID(Long.valueOf(companyId));
                if (shop == null) {
                    shop = new MemberShop();
                    shop.setCompanyId(Integer.valueOf(companyId));
                    shop.setCompanyName(mem.getEnterpriseName() != null ? mem.getEnterpriseName() : "");
                    shop.setShopName(mem.getEnterpriseName() != null ? mem.getEnterpriseName() : "");
                    shop.setCompanyAccount(mem.getAccount());
                    shop.setUpdateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    shop.setStatus(MemberConstant.ShopStatus.YSH.toString());
                    shopService.insert(shop);
                } else {
                    shop.setStatus(MemberConstant.ShopStatus.YSH.toString());
                    shopService.update(shop);
                }
            }


        } catch (Exception e) {
            log.error("updateMemData error >>>", e);
            throw e;
        }
    }

    /**
     * 更新子账户状态
     *
     * @param member
     */
    private void updateSubMemInfo(Member member) {
        try {
            memberMapper.updateSubMemInfo(member);
        } catch (Exception e) {
            log.error("updateSubMemInfo error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<String, String>> queryEngineerList(Map<String, Object> map) {
        List<Map<String, String>> list;
        try {
            list = memberMapper.queryEngineerList(map);
            for (Map<String, String> item : list) {
                VipMemberInfo vip = vipInfoService.findVipMemberInfoById(Long.valueOf(item.get("id")));
                item.put("vipLevel", vip != null ? String.valueOf(vip.getVipLevel()) : "");
            }
            return list;
        } catch (Exception e) {
            log.error("queryEngineerList error >>>", e);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 基本资料审核详情
     *
     * @param id 会员ID
     * @return
     */
    public Map<String, Object> findMeminfoCheck(String id) {
        MemInfoCheck infoCheck;
        try {
            infoCheck = infoCheckService.findMemById(id);
            String enterpriseCreaterTime = infoCheck.getEnterpriseCreaterTime();
            infoCheck.setEnterpriseCreaterTime(DateUtils.str2DateFormat(enterpriseCreaterTime, "yyyy-MM-dd"));

        } catch (Exception e) {
            log.error("findMeminfoCheck() 查询异常>>>", e);
            e.printStackTrace();
            throw e;
        }
        return BeanUtil.objectToMap(infoCheck);
    }

    /**
     * 实名认证审核详情
     *
     * @param id 会员ID
     * @return
     */
    public Map<String, Object> findMemrealCheck(String id) {
        Map<String, Object> map;
        try {
            map = realCheckService.findMemrealCheck(id);
        } catch (Exception e) {
            log.error("findMemrealCheck()查询异常>>>", e);
            e.printStackTrace();
            throw e;
        }
        return map;
    }

    /**
     * 实名认证审核     *需要先判断是否基本资料审核已通过
     * 修改审核子表状态
     * 审核通过之后同步用户主表信息
     *
     * @param member
     */
    public void updateMemRealData(MemRealCheck member) {
        try {
            //先判断基本资料是否审核通过
            String infoStatus = infoCheckService.getStatusById(member.getId());
            if (!infoStatus.equals(MemberConstant.MemberStatus.WSZLYSH.toString())) {
                throw new BusinessException(MsgCodeConstant.SMRZSH_ERROR, "基本资料未审核通过");
            }

            Integer status = member.getStatus();

            realCheckService.update(member);

            //审核通过 同步信息到主表
            if (status == MemberConstant.MemberStatus.SMRZYRZ.intValue()) {
                Member mem = new Member();
                BeanUtils.copyProperties(member, mem);
                mem.setId(String.valueOf(member.getId()));
                mem.setStatus(String.valueOf(status));
                updateMemInfo(mem);

                //同步公司名称到基本信息审核表
                MemInfoCheck icheck = new MemInfoCheck();
                icheck.setId(member.getId());
                icheck.setEnterpriseName(member.getEnterpriseName());
                infoCheckService.update(icheck);

            }

        } catch (Exception e) {
            log.error("操作异常>>>", e);
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * VIP 工程商简介
     * @param id
     * @param string
     * @return
     */
	public Map vipIntroduce(String id, String type) {
		Map map= this.introduce(id, type);
	    List<MemberSucCase> sucCase=memberSucCaseService.queryMemberSucCaseList(id);
	    map.put("sucCaseList", sucCase);
	  
		return map;
	}
	
	/**
     * 新建员工
     */
    public int omsAddMember(Member member) throws Exception {
        int result = 0;
        try { 
              result = memberMapper.addMember(member); 
        } catch (Exception e) {
            log.error("omsAddMember error >>>", e);
            e.printStackTrace();
            throw e;
        }
        return result;
    }
}
