package com.movision.fsearch.service.impl;

import com.chenlb.mmseg4j.*;
import com.movision.fsearch.repository.JdbcRepository;
import com.movision.fsearch.repository.db.MapHandler;
import com.movision.fsearch.repository.db.StringPropertyHandler;
import com.movision.fsearch.service.IJobService;
import com.movision.fsearch.service.IWordService;
import com.movision.fsearch.utils.FileUtil;
import com.movision.fsearch.utils.StringUtil;
import com.movision.utils.G;
import com.movision.utils.L;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WordService extends JdbcRepository implements IWordService {

    private static File DICT_FILE = null;
    private static Dictionary DICT = null;
    private static Seg COMPLEX_SEG = null;
    private static Map<String, List<String>> SIMILAR_WORD_MAP = new HashMap<String, List<String>>(
            0);

    /**
     * 加载Dictionary
     */
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

    /**
     * 更新字典
     *
     * @throws Exception
     */
    private void updateWords() throws Exception {
        /**
         * 更新品牌字典表
         */
        {
            FileUtil.write(DICT_FILE, StringUtil.EMPTY);

            List<String> words = getJdbcTemplate().findList(
                    "select word from t_goods_words", null, 0, 0,
                    StringPropertyHandler.getInstance());
            for (String word : words) {
                FileUtil.append(DICT_FILE, word + FileUtil.LINE);
            }
            DICT.reload();
        }

        /**
         * 更新相似字典表
         */
        {
            List<Map<String, Object>> items = getJdbcTemplate().findList(
                    "select w,s from t_goods_words_similar", null, 0, 0,
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

    /**
     * 每3小时更新字典
     */
    @PostConstruct
    public void init() {
        jobService.scheduleRepeatJob(new JobService.RepeatJob() {
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
