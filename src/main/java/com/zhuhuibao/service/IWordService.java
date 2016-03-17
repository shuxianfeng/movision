package com.zhuhuibao.service;

import java.util.List;

public interface IWordService {

	List<String> segWords(String s);
	
	List<String> findSimilarWords(String s);
}
