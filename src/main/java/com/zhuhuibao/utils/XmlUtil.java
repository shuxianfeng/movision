package com.zhuhuibao.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ch.qos.logback.core.joran.spi.XMLUtil;

public class XmlUtil {
	
	/**
	 * 把SortedMap转化成xml格式
	 * @param parameters
	 * @return
	 */
	public static String getRequestXml(SortedMap<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if("sign".equalsIgnoreCase(k)){

            }
            else if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }
            else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("<"+"sign"+">"+"<![CDATA["+parameters.get("sign")+"]]></"+"sign"+">");
        sb.append("</xml>");
        return sb.toString();
    }
		
	
		/**
		    * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
		    * @param strxml
		    * @return
		    * @throws JDOMException
		    * @throws IOException
		    */
	   public static Map doXMLParse(String strxml) throws JDOMException, IOException {
	      strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

	      if(null == strxml || "".equals(strxml)) {
	         return null;
	      }

	      Map m = new HashMap();

	      InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
	      SAXBuilder builder = new SAXBuilder();
	      Document doc = builder.build(in);
	      Element root = doc.getRootElement();
	      List list = root.getChildren();
	      Iterator it = list.iterator();
	      while(it.hasNext()) {
	         Element e = (Element) it.next();
	         String k = e.getName();
	         String v = "";
	         List children = e.getChildren();
	         if(children.isEmpty()) {
	            v = e.getTextNormalize();
	         } else {
	            v = XmlUtil.getChildrenText(children);
	         }

	         m.put(k, v);
	      }

	      //关闭流
	      in.close();

	      return m;
	   }
	
	   /**
	    * 获取子结点的xml
	    * @param children
	    * @return String
	    */
	   public static String getChildrenText(List children) {
	      StringBuffer sb = new StringBuffer();
	      if(!children.isEmpty()) {
	         Iterator it = children.iterator();
	         while(it.hasNext()) {
	            Element e = (Element) it.next();
	            String name = e.getName();
	            String value = e.getTextNormalize();
	            List list = e.getChildren();
	            sb.append("<" + name + ">");
	            if(!list.isEmpty()) {
	               sb.append(XmlUtil.getChildrenText(list));
	            }
	            sb.append(value);
	            sb.append("</" + name + ">");
	         }
	      }

	      return sb.toString();
	   }
	
	   /**
	    * 获取xml编码字符集
	    * @param strxml
	    * @return
	    * @throws IOException
	    * @throws JDOMException
	    */
	   /*public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
	      InputStream in = HttpClientUtil.String2Inputstream(strxml);
	      SAXBuilder builder = new SAXBuilder();
	      Document doc = builder.build(in);
	      in.close();
	      return (String)doc.getProperty("encoding");
	   }*/
	
	/**
	 * Converter Map<Object, Object> instance to xml string. Note: currently,
	 * we aren't consider more about some collection types, such as array,list,
	 *
	 * @param dataMap  the data map
	 *
	 * @return the string
	 */
	public static String converter(SortedMap<Object,Object> dataMap)
	{
	    synchronized (XmlUtil.class)
	    {
	        StringBuilder strBuilder = new StringBuilder();
	        strBuilder.append("<xml>");
	        Set<Object> objSet = dataMap.keySet();
	        for (Object key : objSet)
	        {
	            if (key == null)
	            {
	                continue;
	            }
//	            strBuilder.append("\n");
	            strBuilder.append("<").append(key.toString()).append(">");
	            Object value = dataMap.get(key);
	            strBuilder.append(coverter(value));
	            strBuilder.append("</").append(key.toString()).append(">\n");
	        }
	        strBuilder.append("</xml>");
	        return strBuilder.toString();
	    }
	}
	
	public static String coverter(Object[] objects) {
        StringBuilder strBuilder = new StringBuilder();
        for(Object obj:objects) {
            strBuilder.append("<item className=").append(obj.getClass().getName()).append(">\n");
            strBuilder.append(coverter(obj));
            strBuilder.append("</item>\n");
        }
        return strBuilder.toString();
    }
	
	public static String coverter(Collection<?> objects)
    {
        StringBuilder strBuilder = new StringBuilder();
        for(Object obj:objects) {
            strBuilder.append("<item className=").append(obj.getClass().getName()).append(">\n");
            strBuilder.append(coverter(obj));
            strBuilder.append("</item>\n");
        }
        return strBuilder.toString();
    }
	
	/**
     * Coverter.
     *
     * @param object the object
     * @return the string
     */
    public static String coverter(Object object)
    {
    	//处理数组
        if (object instanceof Object[])
        {
            return coverter((Object[]) object);
        }
        //处理集合
        if (object instanceof Collection)
        {
            return coverter((Collection<?>) object);
        }
        StringBuilder strBuilder = new StringBuilder();
        //处理对象
        if (isObject(object))
        {
            Class<? extends Object> clz = object.getClass();
            Field[] fields = clz.getDeclaredFields();

            for (Field field : fields)
            {
                field.setAccessible(true);
                if (field == null)
                {
                    continue;
                }
                String fieldName = field.getName();
                Object value = null;
                try
                {
                    value = field.get(object);
                }
                catch (IllegalArgumentException e)
                {
                    continue;
                }
                catch (IllegalAccessException e)
                {
                    continue;
                }
                /*strBuilder.append("<").append(fieldName)
                        .append(" className=\"").append(
                                value.getClass().getName()).append("\">");*/
                strBuilder.append("<").append(fieldName)
                .append(" className=\"").append(
                        value.getClass().getName()).append("\">\n");
                
                if (isObject(value))
                {
                    strBuilder.append(coverter(value));
                }
                else if (value == null)
                {
//                    strBuilder.append("null\n");
                	strBuilder.append("null");
                }
                else
                {
//                    strBuilder.append(value.toString() + "\n");
                    strBuilder.append(value.toString());
                }
                strBuilder.append("</").append(fieldName).append(">\n");
            }
        }
        else if (object == null)
        {
//            strBuilder.append("null\n");
            strBuilder.append("null");
        }
        else
        {
//            strBuilder.append(object.toString() + "\n");
            strBuilder.append(object.toString());
        }
        return strBuilder.toString();
    }
    
    /**
     * Checks if is object.
     *
     * @param obj the obj
     *
     * @return true, if is object
     */
    private static boolean isObject(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof String)
        {
            return false;
        }
        if (obj instanceof Integer)
        {
            return false;
        }
        if (obj instanceof Double)
        {
            return false;
        }
        if (obj instanceof Float)
        {
            return false;
        }
        if (obj instanceof Byte)
        {
            return false;
        }
        if (obj instanceof Long)
        {
            return false;
        }
        if (obj instanceof Character)
        {
            return false;
        }
        if (obj instanceof Short)
        {
            return false;
        }
        if (obj instanceof Boolean)
        {
            return false;
        }
        return true;
    }
}
