package com.zhuhuibao.mybatis.project.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.constants.service.ConstantService;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Area;
import com.zhuhuibao.mybatis.memCenter.entity.City;
import com.zhuhuibao.mybatis.memCenter.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.ProjectConstant;
import com.zhuhuibao.mybatis.oms.service.OmsMemService;
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;
import com.zhuhuibao.mybatis.project.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.project.mapper.ProjectMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 项目信息业务处理类
 *
 * @author Created by gmli on 2016/5/10
 * @created 2016-05-13
 */
@Service
@Transactional
public class ProjectService {
    private static final Logger log = LoggerFactory
            .getLogger(OmsMemService.class);
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectLinkmanService linkmanService;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    ConstantService constantService;

    /**
     * 查询项目信息
     *
     * @param projectID 项目信息ID
     * @return 项目信息
     * @throws SQLException
     */
    public ProjectInfo queryProjectInfoByID(Long projectID) {
        log.info("query project info by id " + projectID);
        ProjectInfo projectInfo;
        try {
            projectInfo = projectMapper.queryProjectInfoByID(projectID);

        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;

        }
        return projectInfo;
    }

    /**
     * OMS查询项目信息
     *
     * @param projectID 项目信息ID
     * @return 项目信息
     * @throws SQLException
     */
    public ProjectInfo queryOmsProjectInfoByID(Long projectID) {
        log.info("query project info by id " + projectID);
        ProjectInfo projectInfo;
        try {
            projectInfo = projectMapper.queryOmsProjectInfoByID(projectID);

        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;

        }
        return projectInfo;
    }


    /**
     * 查询项目信息详情
     *
     * @param projectID 项目信息ID
     * @return 项目信息
     */
    public Map<String, Object> queryProjectDetail(Long projectID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        log.info("query project detail info projectId = " + projectID);
        try {
            ProjectInfo projectInfo = queryProjectInfoByID(projectID);
            //项目信息
            map.put("project", projectInfo);
            //根据项目ID查询联系人信息
            List<ProjectLinkman> linkmanList = linkmanService.queryProjectLinkmanByProjectID(projectID);
            if (!linkmanList.isEmpty()) {
                //甲方信息
                List<ProjectLinkman> partyAList = new ArrayList<>();
                //乙方中的设计师信息
                List<ProjectLinkman> partyBDesignList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBFirstList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBWorkList = new ArrayList<>();
                //乙方中的分包商信息
                List<ProjectLinkman> partyBSecondList = new ArrayList<>();
                int size = linkmanList.size();
                for (ProjectLinkman linkman : linkmanList) {
                    //甲方信息
                    if (linkman.getPartyType() == 1) {
                        partyAList.add(linkman);
                    } else//乙方信息
                    {
                        //1:设计师，2：总包商，3：工程商，4:分包商
                        if (linkman.getTypePartyB() == 1) {
                            partyBDesignList.add(linkman);
                        } else if (linkman.getTypePartyB() == 2) {
                            partyBFirstList.add(linkman);
                        } else if (linkman.getTypePartyB() == 3) {
                            partyBWorkList.add(linkman);
                        } else if (linkman.getTypePartyB() == 4) {
                            partyBSecondList.add(linkman);
                        }

                    }
                }
                //乙方信息
                Map<String, Object> partyB = new TreeMap<>();
                partyB.put("design", partyBDesignList);
                partyB.put("first", partyBFirstList);
                partyB.put("engineering", partyBWorkList);
                partyB.put("second", partyBSecondList);

                map.put("partyB", partyB);

                map.put("partyA", partyAList);

            }
        } catch (Exception e) {
            log.error("query project detail info error!");
            throw e;
        }
        return map;
    }

    /**
     * OMS查询项目信息详情
     *
     * @param projectID 项目信息ID
     * @return 项目信息
     */
    public Map<String, Object> queryOmsProjectDetail(Long projectID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        log.info("query project detail info projectId = " + projectID);
        try {
            ProjectInfo projectInfo = queryOmsProjectInfoByID(projectID);
            //项目信息
            map.put("project", projectInfo);
            //根据项目ID查询联系人信息
            List<ProjectLinkman> linkmanList = linkmanService.queryProjectLinkmanByProjectID(projectID);
            if (!linkmanList.isEmpty()) {
                //甲方信息
                List<ProjectLinkman> partyAList = new ArrayList<>();
                //乙方中的设计师信息
                List<ProjectLinkman> partyBDesignList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBFirstList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBWorkList = new ArrayList<>();
                //乙方中的分包商信息
                List<ProjectLinkman> partyBSecondList = new ArrayList<>();
                for (ProjectLinkman linkman : linkmanList) {
                    //甲方信息
                    if (linkman.getPartyType() == 1) {
                        partyAList.add(linkman);
                    } else//乙方信息
                    {
                        //1:设计师，2：总包商，3：工程商，4:分包商
                        if (linkman.getTypePartyB() == 1) {
                            partyBDesignList.add(linkman);
                        } else if (linkman.getTypePartyB() == 2) {
                            partyBFirstList.add(linkman);
                        } else if (linkman.getTypePartyB() == 3) {
                            partyBWorkList.add(linkman);
                        } else if (linkman.getTypePartyB() == 4) {
                            partyBSecondList.add(linkman);
                        }

                    }
                }
                //乙方信息
                Map<String, Object> partyB = new TreeMap<>();
                partyB.put("design", partyBDesignList);
                partyB.put("first", partyBFirstList);
                partyB.put("engineering", partyBWorkList);
                partyB.put("second", partyBSecondList);

                map.put("partyB", partyB);

                map.put("partyA", partyAList);

            }
        } catch (Exception e) {
            log.error("query project detail info error!");
            throw e;
        }
        return map;
    }

    /**
     * 查询未登录的项目信息详情
     *
     * @param projectID 项目信息ID
     * @return 项目信息
     */
    public Map<String, Object> previewUnLoginProject(Long projectID) throws Exception {
        Map<String, Object> map = new HashMap<>();
        log.info("query project detail info projectId = " + projectID);
        try {
            ProjectInfo projectInfo = queryProjectInfoByID(projectID);
            projectInfo.setAddress(ProjectConstant.HiddenStar.TEN.toString());
            //项目信息
            map.put("project", projectInfo);
            //根据项目ID查询联系人信息
            List<ProjectLinkman> linkmanList = linkmanService.queryProjectLinkmanByProjectID(projectID);
            if (!linkmanList.isEmpty()) {
                //甲方信息
                List<ProjectLinkman> partyAList = new ArrayList<>();
                //乙方中的设计师信息
                List<ProjectLinkman> partyBDesignList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBFirstList = new ArrayList<>();
                //乙方中的工程商信息
                List<ProjectLinkman> partyBWorkList = new ArrayList<>();
                //乙方中的分包商信息
                List<ProjectLinkman> partyBSecondList = new ArrayList<>();
                for (ProjectLinkman linkman : linkmanList) {
                    //甲方信息
                    if (linkman.getPartyType() == 1) {
                        hideLinkman(linkman);
                        partyAList.add(linkman);
                    } else//乙方信息
                    {
                        //1:设计师，2：总包商，3：工程商，4:分包商
                        if (linkman.getTypePartyB() == 1) {
                            hideLinkman(linkman);
                            partyBDesignList.add(linkman);
                        } else if (linkman.getTypePartyB() == 2) {
                            hideLinkman(linkman);
                            partyBFirstList.add(linkman);
                        } else if (linkman.getTypePartyB() == 3) {
                            hideLinkman(linkman);
                            partyBWorkList.add(linkman);
                        } else if (linkman.getTypePartyB() == 4) {
                            hideLinkman(linkman);
                            partyBSecondList.add(linkman);
                        }

                    }
                }
                //乙方信息
                Map<String, Object> partyB = new TreeMap<>();
                partyB.put("design", partyBDesignList);
                partyB.put("first", partyBFirstList);
                partyB.put("engineering", partyBWorkList);
                partyB.put("second", partyBSecondList);

                map.put("partyB", partyB);

                map.put("partyA", partyAList);

            }
        } catch (Exception e) {
            log.error("query project detail info error!");
            throw e;
        }
        return map;
    }

    /**
     * 隐藏联系人部分信息
     *
     * @param linkman 联系人信息
     */
    private void hideLinkman(ProjectLinkman linkman) {
        StringBuilder sb;
        linkman.setName(ProjectConstant.HiddenStar.TEN.toString());
        linkman.setNote(ProjectConstant.HiddenStar.TEN.toString());
        linkman.setAddress(ProjectConstant.HiddenStar.TEN.toString());
        //联系人
        String lman = linkman.getLinkman();
        if (!StringUtils.isEmpty(lman)) {
            linkman.setLinkman(linkman.getLinkman().substring(0, 1) + ProjectConstant.HiddenStar.THREE.toString());
        }
        //手机
        String mobile = linkman.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            sb = new StringBuilder(mobile);
            sb.replace(4, sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
            if(mobile.length() >= 4){
                  linkman.setMobile(sb.toString());
            }else{
                 linkman.setMobile(ProjectConstant.HiddenStar.TEN.toString());
            }
        }
        //座机
        String tel = linkman.getTelephone();
        if (!StringUtils.isEmpty(tel)) {
            if(tel.length() >= 4) {
                sb = new StringBuilder(tel);
                sb.replace(4, sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
                linkman.setTelephone(sb.toString());
            } else{
                  linkman.setTelephone( ProjectConstant.HiddenStar.TEN.toString());
            }

        }
        //传真
        String fax = linkman.getFax();
        if (!StringUtils.isEmpty(fax)) {
            if(fax.length() >=4 ) {
                sb = new StringBuilder(fax);
                sb.replace(4, sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
                linkman.setFax(sb.toString());
            } else{
                linkman.setFax( ProjectConstant.HiddenStar.TEN.toString());
            }

        }
    }

    /**
     * 添加项目工程信息
     *
     * @param projectInfo 项目工程信息
     * @return
     */
    public int addProjectInfo(ProjectInfo projectInfo) throws SQLException {
        int result = 0;
        try {
            result = projectMapper.addProjectInfo(projectInfo);
            Long projectId = projectInfo.getId();
            //甲方信息
            List<ProjectLinkman> partyAlist = projectInfo.getPartyAList();
            insertProjectLinkman(projectId, partyAlist);
            //乙方信息
            List<ProjectLinkman> partyBlist = projectInfo.getPartyBList();
            insertProjectLinkman(projectId, partyBlist);

        } catch (Exception e) {
            log.error("add project error!", e);
            throw new SQLException();

        }
        return result;
    }

    /**
     * 插入项目联系人信息 甲方乙方信息
     *
     * @param projectId 项目ID
     * @param partylist 联系人信息
     * @throws Exception
     */
    private void insertProjectLinkman(Long projectId, List<ProjectLinkman> partylist) throws Exception {
        log.info("projectId = " + projectId);
        try {
            if (!partylist.isEmpty()) {
                int partyASize = partylist.size();
                for (ProjectLinkman partyA : partylist) {
                    partyA.setProjectid(projectId);
                    linkmanService.addProjectLinkmanInfo(partyA);
                }
            }
        } catch (Exception e) {
            log.error("insert project linkman info error!");
            throw e;
        }
    }


    /**
     * 修改项目信息
     *
     * @param projectInfo
     * @return
     * @throws SQLException
     */
    public int updateProjectInfo(ProjectInfo projectInfo) throws SQLException {
        log.info("update project info =" + StringUtils.beanToString(projectInfo));
        int result = 0;
        try {
            result = projectMapper.updateProjectInfo(projectInfo);
            List<ProjectLinkman> partyAList = projectInfo.getPartyAList();
            List<ProjectLinkman> partyBList = projectInfo.getPartyBList();
            if (partyAList != null && !partyAList.isEmpty()) {
                for (ProjectLinkman aPartyAList : partyAList) {

                    linkmanService.updateByPrimaryKeySelective(aPartyAList);
                }

            }
            if (partyBList != null && !partyBList.isEmpty()) {
                for (ProjectLinkman aPartyBList : partyBList) {
                    linkmanService.updateByPrimaryKeySelective(aPartyBList);
                }
            }
        } catch (Exception e) {
            log.error("upate project error!", e);
            throw new SQLException();

        }
        return result;
    }

    /**
     * 根据条件查询项目分页信息
     *
     * @param map 项目信息搜素条件
     * @return
     */
    public List<Map<String, String>> findAllPrjectPager(Map<String, Object> map, Paging<Map<String, String>> page) throws Exception {
        log.info("search project info for pager condition = " + StringUtils.mapToString(map));
        List<Map<String, String>> projectList;
        try {
            projectList = projectMapper.findAllPrjectPager(map, page.getRowBounds());
        } catch (Exception e) {
            log.error("search project info for pager error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 根据条件查询最新项目信息
     *
     * @param map 项目信息搜素条件 count：指定项目信息条数
     * @return
     */
    public List<Map<String, String>> queryLatestProject(Map<String, Object> map) {
        log.info("query latest project info condition = " + StringUtils.mapToString(map));
        List<Map<String, String>> projectList = null;
        try {
            projectList = projectMapper.queryLatestProject(map);
        } catch (Exception e) {
            log.error("query latest project info error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 首页查询最新项目信息
     *
     * @param map 项目信息搜素条件 count：指定项目信息条数
     * @return
     */
    public List<Map<String, String>> queryHomepageLatestProject(Map<String, Object> map) {
        log.info("query homepage latest project info condition = " + StringUtils.mapToString(map));
        List<Map<String, String>> projectList = null;
        try {
            projectList = projectMapper.queryHomepageLatestProject(map);
        } catch (Exception e) {
            log.error("query homepage latest project info error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 根据条件查询项目分页信息
     *
     * @param map 查询条件
     * @return
     */
    public List<Map<String, String>> queryOmsViewProject(Map<String, Object> map, Paging<Map<String, String>> page) throws Exception {
        log.info("search my viewer project info viewerId = " + StringUtils.mapToString(map));
        List<Map<String, String>> projectList;
        try {
            projectList = projectMapper.findAllOmsViewProject(map, page.getRowBounds());
        } catch (Exception e) {
            log.error("search my viewer project info viewerId error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 获取地区
     *
     * @param areaOrCityMap
     * @return
     */
    public Map<String, Object> getAreaOrCity(Map areaOrCityMap) {
        Map<String, Object> codeMap;
        try {
            codeMap = projectMapper.getAreaOrCity(areaOrCityMap);

        } catch (Exception e) {
            log.error("check isview project error!");
            throw e;
        }
        return codeMap;
    }

    /**
     * 项目类别
     *
     * @param list
     * @return
     */
    public Map<String, String> getCatagoryByValue(List list) {
        Map<String, String> codeMap;
        try {
            codeMap = projectMapper.getCatagoryByValue(list);

        } catch (Exception e) {
            log.error("check isview project error!");
            throw e;
        }
        return codeMap;
    }

    public List<Map<String, String>> findPrjectByName(Map<String, Object> map) {
        log.info("search my viewer project info viewerId = " + StringUtils.mapToString(map));
        List<Map<String, String>> projectList;
        try {
            projectList = projectMapper.findPrjectByName(map);
        } catch (Exception e) {
            log.error("search my viewer project info viewerId error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 获取省市
     *
     * @param areaOrCityMap
     * @return
     */
    public Map<String, Object> getCity(Map<String, String> areaOrCityMap) {
        Map<String, Object> codeMap;
        try {
            codeMap = projectMapper.getCity(areaOrCityMap);

        } catch (Exception e) {
            log.error("check isview project error!");
            throw e;
        }
        return codeMap;
    }

    /**
     * 条件 分页查询项目信息
     *
     * @param map   conditions
     * @param pager pager
     * @return
     */
    public List<Map<String, String>> findAllProject(Map<String, Object> map, Paging<Map<String, String>> pager) {
        List<Map<String, String>> projectList;

        try {
            List<Map<String, String>> records = projectMapper.findAllPrject(map, pager.getRowBounds());
            projectList = processProjectList(records);

        } catch (Exception e) {
            log.error("search project info for pager error!");
            throw e;
        }
        return projectList;
    }

    /**
     * 项目信息记录解析组装
     *
     * @param records
     * @return
     */
    private List<Map<String, String>> processProjectList(List<Map<String, String>> records) {
        List<Map<String, String>> projectList = new ArrayList<>();
        for (Map<String, String> map : records) {
            Map<String, String> result = new HashMap<>();
            result.put("id", map.get("id"));
            result.put("name", map.get("name"));
            result.put("publishDate", map.get("publishDate"));
            result.put("updateDate", map.get("updateDate"));
            result.put("price", map.get("price"));
            //今天心情不好就写到这里了  2016-07-5  >>>>>>>>____ _<<<<<<<<<<<<
            result.put("startDate", map.get("startDate"));

            //address city  categoryName
            //address <-- provinceName + cityName + areaName + address
            //city <-- cityName
            String provinceCode = map.get("province");
            String cityCode = map.get("city");
            String areaCode = map.get("area");
            String address;

            if (!StringUtils.isEmpty(provinceCode)) {
                Province province = dictionaryService.selectProvinceByCode(provinceCode);
                String provinceName = StringUtils.isEmpty(province.getName()) ? "" : province.getName();

                String cityName = "";
                if (!StringUtils.isEmpty(cityCode)) {
                    City city = dictionaryService.selectCityByCode(cityCode);
                    cityName = city.getName();
                }
                result.put("city", cityName);

                String areaName = "";
                if (!StringUtils.isEmpty(areaCode)) {
                    Area area = dictionaryService.selectAreaByCode(areaCode);
                    areaName = area.getName();
                }
                String addressO = StringUtils.isEmpty(map.get("address")) ? "" : map.get("address");
                address = provinceName + cityName + areaName + addressO;
                result.put("address", address);
            } else {
                result.put("address", map.get("address"));
            }

            String category = map.get("category");
            if (!StringUtils.isEmpty(category)) {
                String categoryName = constantService.selectNameByJoinCode(category, Constants.ConstantType.XMXXLB.toString());
                result.put("categoryName", categoryName);
            } else {
                result.put("categoryName", "");
            }

            projectList.add(result);
        }
        return projectList;
    }
}
