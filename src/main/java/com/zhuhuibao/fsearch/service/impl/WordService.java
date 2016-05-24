package com.zhuhuibao.fsearch.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.zhuhuibao.fsearch.repository.JdbcRepository;
import com.zhuhuibao.fsearch.service.IJobService;
import com.zhuhuibao.fsearch.service.IWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhuhuibao.fsearch.repository.db.MapHandler;
import com.zhuhuibao.fsearch.repository.db.StringPropertyHandler;
import com.zhuhuibao.fsearch.utils.FileUtil;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.fsearch.service.impl.JobService.RepeatJob;
import com.zhuhuibao.utils.G;
import com.zhuhuibao.utils.L;
import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

@Component
public class WordService extends JdbcRepository implements IWordService {

	private static File DICT_FILE = null;
	private static Dictionary DICT = null;
	private static Seg COMPLEX_SEG = null;
	private static Map<String, List<String>> SIMILAR_WORD_MAP = new HashMap<String, List<String>>(
			0);

	static {
		DICT_FILE = new File(G.getConfig().getString("words.tmp.path"));
		if (!DICT_FILE.exists()) {
			try {
				FileUtil.ensureFile(DICT_FILE);
			} catch (IOException e) {
				L.error("Failed to init dictionary", e);
				System.exit(-1);
			}
		}
		DICT = Dictionary.getInstance(DICT_FILE.getParentFile());
		COMPLEX_SEG = new ComplexSeg(DICT);
	}

	@Autowired
	private IJobService jobService;

	private void updateWords() throws Exception {
		{
			FileUtil.write(DICT_FILE, StringUtil.EMPTY);
			List<String> words = getJdbcTemplate().findList(
					"select word from t_p_words", null, 0, 0,
					StringPropertyHandler.getInstance());
			for (String word : words) {
				FileUtil.append(DICT_FILE, word + FileUtil.LINE);
			}
			DICT.reload();
		}
		{
			List<Map<String, Object>> items = getJdbcTemplate().findList(
					"select w,s from t_p_word_similar", null, 0, 0,
					MapHandler.CASESENSITIVE);
			Map<String, List<String>> similarMap = new HashMap<String, List<String>>(
					0);
			for (Map<String, Object> item : items) {
				String w = item.get("w").toString();
				String s = item.get("s").toString();
				List<String> words = StringUtil.split(s, ",");
				words.add(w);
				for (String word : words) {
					similarMap.put(word, words);
				}
			}
			SIMILAR_WORD_MAP = similarMap;
		}
	}

	@PostConstruct
	public void init() {
		jobService.scheduleRepeatJob(new RepeatJob() {
			@Override
			public void run(boolean firstTime) {
				try {
					updateWords();
				} catch (Exception e) {
					L.error(e);
					if (firstTime) {
						System.exit(-1);
					}
				}
			}
		}, "words.cache.minutes", true);
	}

	@Override
	public List<String> segWords(String s) {
		String[] strs = s.split("，|、|。|,|\\.| |\t");
		List<String> wordList = new ArrayList<String>(Math.min(strs.length, 10));
		for (String str : strs) {
			if (str.isEmpty()) {
				continue;
			}
			MMSeg mmSeg = new MMSeg(new StringReader(str), COMPLEX_SEG);
			Word word = null;
			try {
				while ((word = mmSeg.next()) != null) {
					String w = word.getString();
					if (!wordList.contains(w)) {
						wordList.add(w);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return wordList;
	}

	@Override
	public List<String> findSimilarWords(String s) {
		if (s == null) {
			return null;
		}
		List<String> words = SIMILAR_WORD_MAP.get(s);
		if (words == null) {
			return null;
		}
		return new ArrayList<String>(words);
	}

}
