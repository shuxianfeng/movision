package com.zhuhuibao.mybatis.dictionary.mapper;

public interface DictionaryMapper {
	//根据会员邮箱地址找到主邮箱地址
	String findMailAddress(String memberMail);
}
