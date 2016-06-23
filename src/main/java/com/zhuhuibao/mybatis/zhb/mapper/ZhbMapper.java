package com.zhuhuibao.mybatis.zhb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;

/**
 * 
 * @author tongxinglong
 *
 */
public interface ZhbMapper {

	/**
	 * 根据orderNo/type查询筑慧币记录流水信息
	 * 
	 * @param param
	 *            :orderNo/type
	 * @return
	 */
	ZhbRecord selectZhbRecordByOrderNoAndType(Map<String, String> param);

	/**
	 * 根据orderNo查询筑慧币记录流水信息
	 * 
	 * @param orderNo
	 * @return
	 */
	List<ZhbRecord> selectZhbRecordListByOrderNo(String orderNo);

	/**
	 * 查询会员筑慧币流水记录
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String, String>> selectZhbRecordList(RowBounds rowBounds, Map<String, Long> param);

	/**
	 * 增加筑慧币流水记录
	 * 
	 * @param zhbRecord
	 */
	void insertZhbRecord(ZhbRecord zhbRecord);

	/**
	 * 根据memberId查询筑慧币账户信息
	 * 
	 * @param memberId
	 * @return
	 */
	ZhbAccount selectZhbAccount(Long memberId);

	/**
	 * 增加筑慧币账号信息
	 * 
	 * @param zhbAccount
	 */
	void insertZhbAccount(ZhbAccount zhbAccount);

	/**
	 * 根据memberId修改筑慧币账号金额
	 * 
	 * @param zhbAccount
	 */
	int updateZhbAccountEmoney(ZhbAccount zhbAccount);

	/**
	 * 根据拼音查询筑慧币物品信息
	 * 
	 * @param pinyin
	 * @return
	 */
	DictionaryZhbgoods selectZhbGoodsByPinyin(String pinyin);

	/**
	 * 根据id查询筑慧币物品信息
	 * 
	 * @param pinyin
	 * @return
	 */
	DictionaryZhbgoods selectZhbGoodsById(Long id);

	/**
	 * 根据类型查询筑慧币物品信息
	 * 
	 * @param id
	 * @return
	 */
	List<DictionaryZhbgoods> selectZhbGoodsListById(String type);

}
