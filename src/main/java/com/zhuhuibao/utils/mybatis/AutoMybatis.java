package com.zhuhuibao.utils.mybatis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 目前无法使用
 * @author zhuangyuhao
 * @time   2016年10月18日 下午8:09:15
 *
 */
public class AutoMybatis {
	public static final String cp = "src/main/resources/mybatis/generatorConfig.xml";
	
	public static void main(String[] args) throws IOException {
		
		
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		
//		File configFile = new File(AutoMybatis.class.getResource("/").getFile());
		URL base = AutoMybatis.class.getResource("");
		System.out.println(base);	//file:/D:/workspace/zhuhuibao/target/classes/com/zhuhuibao/utils/mybatis/
		String path = new File(base.getFile(), "../../../../../../"+cp).getCanonicalPath();
		System.out.println("path>>>"+path);	//D:\workspace\zhuhuibao\src\main\resources\mybatis\generatorConfig.xml
		File configFile = new File(path);	
		
//		System.out.println(configFile.exists());
		
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config;
		try {
			config = cp.parseConfiguration(configFile);

			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator;
			try {
				myBatisGenerator = new MyBatisGenerator(config, callback,
						warnings);
				myBatisGenerator.generate(null);
				System.out.println("success generate!");
			} catch (Exception e) {

				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
