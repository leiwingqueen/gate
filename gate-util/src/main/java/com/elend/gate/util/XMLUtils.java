package com.elend.gate.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * dom4j操作xml工具类
 * @author mgt
 *
 */
public class XMLUtils {
	
	/**
	 * 由对象生成Document
	 * 对象必须使用注解NodeAnatation
	 * 对象的属性可以是普通类型（int,String, ...）,
	 * 也可以是对象（同样要使用注解），
	 * 同时也能是Collection的子类，不过集合的内容必须是一个对象（包含注解），否则无法生成XML标签
	 * @param o
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
        public static Document generateDocument(Object o, String charset) throws IllegalArgumentException, IllegalAccessException {
            //没有注解，直接返回null（根节点）
            NodeAnnotation node = o.getClass().getAnnotation(NodeAnnotation.class);
            if(node == null) {
                    return null;
            }
            Document document =  DocumentHelper.createDocument();
            document.setXMLEncoding(charset);  
            //遍历注解生成xml
            addElement(document, o);
            
            return document;
        }
	
	/**
	 * 遍历对象的注解生成xml
	 * @param branch
	 * @param o
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void addElement(Branch branch, Object o) throws IllegalArgumentException, IllegalAccessException {
            // 空对象直接返回
            if (o == null) {
                return;
            }
            // 类上注解
            NodeAnnotation nodeAn = o.getClass().getAnnotation(NodeAnnotation.class);
            if (nodeAn != null) {
                branch = branch.addElement(nodeAn.name());
            }

            // 遍历属性
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field f : fields) {
                nodeAn = f.getAnnotation(NodeAnnotation.class);
                if (nodeAn != null) {
                    // 一般属性
                    if (nodeAn.type() == NodeType.SIMPLE_FIELD) {
                        // 直接添加String内容标签
                        Element e = branch.addElement(nodeAn.name());
                        e.addText(f.get(o) == null ? "" : f.get(o) + "");
                    }
                    // 类属性
                    else if (nodeAn.type() == NodeType.CLASS_FIELD) {
                        // 递归遍历
                        addElement(branch, f.get(o));
                    }
                    // Collection属性
                    else if (nodeAn.type() == NodeType.LIST_FIELD) {
                        // 遍历每一个元素
                        Element e = branch.addElement(nodeAn.name());
                        for (Object o2 : (Collection<Object>) f.get(o)) {
                            // 递归遍历
                            addElement(e, o2);
                        }
                    }
                }
            }
	}
	
	/**
	 * 由对象生成Document
	 * 对象必须使用注解NodeAnatation
	 * 对象的属性可以是普通类型（int,String, ...）,
	 * 也可以是对象（同样要使用注解），
	 * 同时也能是Collection的子类，不过集合的内容必须是一个对象（包含注解），否则注入内容
	 * @param o
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Object parseDocument(Class clazz, Document document) throws InstantiationException, IllegalAccessException {
		String xpath = "";
		
		//没有注解，直接返回null（根节点）
		NodeAnnotation nodeAn = (NodeAnnotation) clazz.getAnnotation(NodeAnnotation.class);
		if(nodeAn == null) {
			return null;
		}
		
		Object o = clazz.newInstance();
		
		//遍历注入属性值
		addField(document, o, xpath); 
		
		return o;
	}
	
	/**
	 * 遍历对象的注解注入属性值
	 * @param branch
	 * @param o
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	private static void addField(Document document, Object o, String xpath) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		NodeAnnotation nodeAn = o.getClass().getAnnotation(NodeAnnotation.class);
		if(nodeAn != null) {
			xpath += "/" + nodeAn.name();
		} 
		
		//遍历属性
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field f : fields) {
			nodeAn = f.getAnnotation(NodeAnnotation.class);
			if(nodeAn != null) {
				//一般属性
				if(nodeAn.type() == NodeType.SIMPLE_FIELD) {
					//直接添加String内容
					String xpath2 = xpath + "/" + nodeAn.name();
					Element fieldNamElement = (Element) document.selectSingleNode(xpath2);
					if (fieldNamElement != null){
						String s = fieldNamElement.getStringValue() == null ? null: fieldNamElement.getStringValue().trim();
						//判断具体的类型，转化注入
						if(s != null) {
							if(f.getType().getSimpleName().equalsIgnoreCase("Double")) {
								Double v = Double.parseDouble(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Integer")) {
								Integer v = Integer.parseInt(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Byte")) {
								Byte v = Byte.parseByte(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Short")) {
								Short v = Short.parseShort(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Long")) {
								Long v = Long.parseLong(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Float")) {
								Float v = Float.parseFloat(s.trim());
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("char")) {
								char v = s.trim().charAt(0);
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Character")) {
								char v = s.trim().charAt(0);
								f.set(o, v);
							} else if(f.getType().getSimpleName().equalsIgnoreCase("Boolean")) {
								Boolean v = Boolean.parseBoolean(s.trim());
								f.set(o, v);
							} else {
								f.set(o, s);
							}
						}
			        }
				} 
				//类属性
				else if(nodeAn.type() == NodeType.CLASS_FIELD) {
					//新建对象
					Object o2 = f.getType().newInstance();
					//递归遍历
					addField(document, o2, xpath);
					//设置值
					f.set(o, o2);
				} 
				//Collection属性
				else if(nodeAn.type() == NodeType.LIST_FIELD) {
					Class t = f.getType();
					//新建集合对象
					Object o2 = f.getType().newInstance();
					//设置值
					f.set(o, o2);
					
					String xpath2 = xpath + "/" + nodeAn.name();
					//获取集合的泛型
					Class clazz = ClassUtils.getParameterizedType(f);
					NodeAnnotation nodeAn2 = (NodeAnnotation) clazz.getAnnotation(NodeAnnotation.class);
					if(nodeAn2 == null) {
						return ;
					}
					String xpath3 = xpath2 + "/" + nodeAn2.name();
					List<Element> eles = document.selectNodes(xpath3);
					for(Element e : eles) {
						Object o3 = clazz.newInstance();
						((Collection<Object>) o2).add(o3);
						addField(document, o3, xpath2);
						//删除节点，避免下一个集合内容获取到相同的值
						e.getParent().remove(e);
					}
				}
			}
		}
	}
}
