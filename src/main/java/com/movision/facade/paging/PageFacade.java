package com.movision.facade.paging;

import com.movision.common.pojo.InstantInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/2 16:59
 */
@Service
public class PageFacade {
    /**
     * 分页
     *
     * @param list
     * @param pageNo
     * @return
     */
    public List getPageList(List list, int pageNo, int pageSize) {
        List<Object> result = new ArrayList<Object>();
        if (list != null && list.size() > 0) {
            int allCount = list.size();//总记录数
            int pageCount = allCount / pageSize + 1;//总页数
            if (pageNo > pageCount) {
                result = null;
                return result;
                //pageCount = pageNo;
            }
            int start = (pageNo - 1) * pageSize;
            int end = pageNo * pageSize;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(list.get(i));
            }
        }
        return (result != null && result.size() > 0) ? result : null;
    }

}
