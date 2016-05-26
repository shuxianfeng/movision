package com.zhuhuibao.mybatis.oms.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 平台公告Bean
 * 
 * @author Administrator
 *
 */
public class Notice {
	@ApiModelProperty(value = "公告ID，主键",required = true)
	private Long id;
	@ApiModelProperty(value = "公告标题")
	private String title;
	@ApiModelProperty(value = "公告内容")
	private String content;
	@ApiModelProperty(value = "公告状态")
	private String status;
	@ApiModelProperty(value = "公告发布时间")
	private String time;
	@ApiModelProperty(value = "公告发布者ID")
	private Long createId;
	@ApiModelProperty(value = "用户名称")
	private String userName;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
