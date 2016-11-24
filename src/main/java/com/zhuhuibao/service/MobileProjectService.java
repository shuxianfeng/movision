package com.zhuhuibao.service;

import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import com.zhuhuibao.mybatis.oms.service.TenderTonedService;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.service.payment.PaymentService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@Service
@Transactional
public class MobileProjectService {

    private Logger log = LoggerFactory.getLogger(MobileProjectService.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TenderTonedService tenderTonedService;

    /**
     * 根据会员ID及其他条件查询项目信息
     * 
     * @param memberId
     * @param name
     * @param city
     * @param province
     * @param category
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, String>> getMyProjectList(Long memberId, String name, String city, String province, String category, String pageNo, String pageSize) throws Exception {
        Paging<Map<String, String>> projectPager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = MapUtil.convert2HashMap("city", city, "province", province, "category", category, "viewerId", memberId, "type", ZhbConstant.ZhbGoodsType.CKXMXX.toString());
        if (name != null && !"".equals(name)) {
            map.put("name", name.replace("_", "\\_"));
        }

        List<Map<String, String>> projectList = projectService.queryOmsViewProject(map, projectPager);
        projectPager.result(projectList);

        return projectPager;
    }

    /**
     * 按条件查询项目信息
     * 
     * @param name
     * @param city
     * @param province
     * @param category
     * @param startDateA
     * @param startDateB
     * @param endDateA
     * @param endDateB
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, String>> getProjectListByConditions(String name, String city, String province, String category, String startDateA, String startDateB, String endDateA, String endDateB,
            String pageNo, String pageSize) {
        Map<String, Object> map = MapUtil.convert2HashMap("city", city, "province", province, "category", category, "startDateA", startDateA, "startDateB", startDateB, "endDateA", endDateA,
                "endDateB", endDateB);
        if (name != null && !"".equals(name)) {
            map.put("name", name.replace("_", "\\_"));
        }

        log.info("查询工程信息：queryProjectInfo", map);
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        // 调用查询接口
        List<Map<String, String>> projectList = projectService.findAllProject(map, pager);
        pager.result(projectList);

        return pager;
    }

    /**
     * 查看项目信息，根据当前会员是否购买返回加密或非加密信息
     * 
     * @param projectId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getProjectDetail(Long projectId,ZhbConstant.ZhbGoodsType zhbGoodsType) throws Exception {
        return paymentService.getChargeGoodsRecord(projectId, zhbGoodsType.toString());
    }

    /**
     * 查询招中标信息列表
     * 
     * @param map
     * @param pager
     * @return
     */
    public List<Map<String, Object>> getTenderList(Map<String, Object> map, Paging<Map<String, Object>> pager) {

        return tenderTonedService.findAllTenderTonedNPager(map, pager);
    }

    /**
     * 查询招中标信息详情
     * 
     * @param tenderId
     * @return
     * @throws Exception
     */
    public TenderToned getTenderDetail(Long tenderId) throws Exception {
        try {
            return tenderTonedService.queryTenderToneByID(tenderId);
        } catch (Exception e) {
            log.error("getTenderDetail>>>tenderId=" + tenderId, e);
            throw e;
        }
    }
}
