package com.zhuhuibao.test;

import org.junit.Test;

import com.zhuhuibao.common.constant.VipConstant;

public class CheckVipTest {

	@Test
	public void checkVip4Test() {
    	String value = "300";
		boolean suc1 = value.equals(VipConstant.VipLevel.PERSON_GOLD.toString());
	    boolean suc2 = value.equals(VipConstant.VipLevel.PERSON_PLATINUM.toString());
	    System.out.println("suc1="+suc1);
	    System.out.println("suc2="+suc2);
	    //如果suc1不是真，并且suc2不是真，则抛异常
//	    if (!(!suc1 || !suc2)) {
	    if(!suc1 && !suc2){	//!false && !false
	    	System.out.println("*********error*************");
	    }else{
	    	System.out.println("存在该VipLevel");
	    }
	    
	 }
}
