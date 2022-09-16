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
    private static Map<String, Method> mapping = new HashMap<>();

    static {
        initMapping();//在静态块中调用完成初始化
    }

    private static void initMapping(){
        //定位类加载路径
        try {
            File rootDir= new File(
                    HandlerMapping.class.getClassLoader().getResource(".").toURI()
            );
            //定位controller包
            File dir = new File(rootDir,"com/webserver/controller");
            if (dir.exists()){//确保controller目录存在
                File[] subs = dir.listFiles(f->f.getName().endsWith(".class"));
                for (File sub : subs){
                    //将.class替换为空串留下类名
                    String className = sub.getName().replace(".class","");
                    Class cls = Class.forName("com.webserver.controller."+className);//加载类,并传入完全限定名
                    if (cls.isAnnotationPresent(Controller.class)){//判断该类是否被注解@Controller标注
                        Method[] methods = cls.getDeclaredMethods();//利用类加载后的引用来获取这些类的所有方法(包含私有)
                        for (Method method : methods){//遍历方法数组
                            if (method.isAnnotationPresent(RequestMapping.class)){//判断方法是否被注解@RequestMapping标注
                                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                                String value = rm.value();//获取@RequestMapping上的参数,该参数记录着该方法处理的请求路径
                                mapping.put(value,method);//将表单action参数作为key,获取的方法作为value放入Map键值对中
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
     * 根据请求路径过去对应的业务处理方法
     * @param uri
     * @return
     */
    public static Method getMethod(String uri){
        return mapping.get(uri);//传key返回value并用Method接收
    }

    public static void main(String[] args) {
        System.out.println(mapping.size());
        System.out.println(mapping);
    }
}
