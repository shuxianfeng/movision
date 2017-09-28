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

    @Autowired
    private IJobService jobService;

    private static File DICT_FILE = null;
    private static Dictionary DICT = null;
    private static Seg COMPLEX_SEG = null;
    private static Map<String, List<String>> SIMILAR_WORD_MAP = new HashMap<String, List<String>>(
            0);

    /**
     * 加载Dictionary
     */
    static {
        //加载字典文件
        DICT_FILE = new File(G.getConfig().getString("words.tmp.path"));
        if (!DICT_FILE.exists()) {
            try {
                //确保文件存在，即创造文件
                FileUtil.ensureFile(DICT_FILE);
            } catch (IOException e) {
                L.error("Failed to init dictionary", e);
                System.exit(-1);
            }
        }
        DICT = Dictionary.getInstance(DICT_FILE.getParentFile());
        COMPLEX_SEG = new ComplexSeg(DICT);
    }

    /**
     * 更新字典
     *
     * @throws Exception
     */
    private void updateWords() throws Exception {
        /**
         * 更新商品字典表
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
         * 更新商品相似字典表
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
    @PostConstruct  //在spring容器初始化话WordService的时候，所做的操作
    public void init() {
        jobService.scheduleRepeatJob(new JobService.RepeatJob() {
            @Override
            public void run(boolean firstTime) {
                try {
                    //更新词库
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

    /**
     * 拆分词
     *
     * @param s
     * @return
     */
    @Override
    public List<String> segWords(String s) {
        //根据，、。,.空格 回车 ，分隔字符串
        String[] strs = s.split("，|、|。|,|\\.| |\t");
        //创建词集合， size<=10
        List<String> wordList = new ArrayList<String>(Math.min(strs.length, 10));

        for (String str : strs) {
            if (str.isEmpty()) {
                continue;
            }
            //调用MMseg分词算法
            MMSeg mmSeg = new MMSeg(new StringReader(str), COMPLEX_SEG);
            Word word = null;
            try {
                //把分出的词加入到wordlist
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
