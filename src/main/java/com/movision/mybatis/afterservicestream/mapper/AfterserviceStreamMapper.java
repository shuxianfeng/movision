package com.movision.mybatis.afterservicestream.mapper;

import com.movision.mybatis.afterservicestream.entity.AfterserviceStream;

public interface AfterserviceStreamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AfterserviceStream record);

    int insertSelective(AfterserviceStream record);

    AfterserviceStream selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AfterserviceStream record);

    int updateByPrimaryKey(AfterserviceStream record);
}