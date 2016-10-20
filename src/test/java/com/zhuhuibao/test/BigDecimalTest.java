package com.zhuhuibao.test;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalTest extends BaseSpringContext{
	
	@Test
	public void test() {
		BigDecimal payAmount = new BigDecimal(0.887);
		String total_fee = String.valueOf(payAmount.multiply(new BigDecimal(100)).longValue()); 
		System.out.println(total_fee);
	}
	
}
