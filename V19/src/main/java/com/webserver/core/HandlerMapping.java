package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于维护请求路径与业务处理方法的对应关系
 */
public class HandlerMapping {
    /**
     * key:请求路径
     * value:处理该请求的方法
     * 例如:
     *  key->/regUser
     *  value->Method对象，表示UserController中的reg方法
     */
    private static Map<String, Method> mapping = new HashMap<>();//创建一个Hash散列表,防止重复

    static {
        initMapping();//在静态块中调用来完成初始化(保证只扫描一次controller,提高性能减少消耗)
    }
    private static void initMapping(){
        try {
            File rootDir = new File(
                    HandlerMapping.class.getClassLoader().getResource(".").toURI()
            );
            File dir = new File(rootDir,"com/webserver/controller");//定位类加载路径下的controller包
            if (dir.exists()){//确保controller目录存在
                File[] subs = dir.listFiles(f->f.getName().endsWith(".class"));//将符合条件的.class文件放到文件数组中
                for (File sub : subs){ //增强for循环遍历.class文件数组
                    String fileName = sub.getName();//获取文件名
                    String className = fileName.substring(0,fileName.lastIndexOf("."));//截取从头到"."的部分(不含.)
                    Class cls = Class.forName("com.webserver.controller."+className);//传入完全限定名
                    //查看是否被注解@Controller标注了
                    if (cls.isAnnotationPresent(Controller.class)){
                        //扫描所有方法,查看是否为处理本次请求的方法
                        Method[] methods = cls.getDeclaredMethods();//利用加载该类的引用获取类中的所有方法(包括私有)
                        for (Method method : methods){//遍历获取的方法数组(包括私有)
                            //判断是否被注解@RequestMapping标注了
                            if (method.isAnnotationPresent(RequestMapping.class)){
                                RequestMapping rm = method.getAnnotation(RequestMapping.class);//返回RequestMapping的实例
                                String value = rm.value();//利用RequestMapping实例来获取注解中的参数值
                                mapping.put(value,method);//调用Map中的put()方法,添加注解中的地址与对应的方法名
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法对外界提供,根据请求抽象路径获取对应的业务处理方法
     * @param uri 请求抽象路径Key
     * @return 返回处理对应请求抽象路径的方法
     */
    public static Method getMethod(String uri){
        return mapping.get(uri);//传入key利用Map中的get()方法来获取value值
    }

   //通过main方法来测试扫描controller下处理业务的数量和信息
    public static void main(String[] args) {
        System.out.println(mapping.size());
        System.out.println(mapping);
    }
}
