package com.zhuhuibao.mybatis.oms.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 平台信息统计Bean
 * 
 * @author Administrator
 *
 */
public class PlatformStatistics {
	@ApiModelProperty(value = "统计ID，主键",required = true)
	private Long id;
	@ApiModelProperty(value = "统计类型")
	private Integer type;
	@ApiModelProperty(value = "总计")
	private Integer total;
	@ApiModelProperty(value = "今日增加")
	private Integer today_add_count;
	@ApiModelProperty(value = "最近7天增加")
	private Integer seven_days_add_count;
	@ApiModelProperty(value = "最近30天增加")
	private Integer thirty_days_add_count;
	@ApiModelProperty(value = "报表分类")
	private String stat_type;
	@ApiModelProperty(value = "统计时间")
	private String stat_date;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getToday_add_count() {
		return today_add_count;
	}
	public void setToday_add_count(Integer today_add_count) {
		this.today_add_count = today_add_count;
	}
	public Integer getSeven_days_add_count() {
		return seven_days_add_count;
	}
	public void setSeven_days_add_count(Integer seven_days_add_count) {
		this.seven_days_add_count = seven_days_add_count;
	}
	public Integer getThirty_days_add_count() {
		return thirty_days_add_count;
	}
	public void setThirty_days_add_count(Integer thirty_days_add_count) {
		this.thirty_days_add_count = thirty_days_add_count;
	}
	public String getStat_type() {
		return stat_type;
	}
	public void setStat_type(String stat_type) {
		this.stat_type = stat_type;
	}
	public String getStat_date() {
		return stat_date;
	}
	public void setStat_date(String stat_date) {
		this.stat_date = stat_date;
	}
	
	

}
