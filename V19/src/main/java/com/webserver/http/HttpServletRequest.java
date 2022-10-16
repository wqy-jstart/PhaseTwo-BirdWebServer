package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象,该类的每一个实例用于标识HTTP协议规定的请求内容
 * 一个请求由三部分构成:
 * 1:请求行
 * 2:消息头
 * 3:消息正文
 */
public class HttpServletRequest {//面向对象思想,做到实例化该类即可完成请求业务
    private Socket socket;
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径(该抽象路径是用户输入包含与请求行中的)
    private String protocol;//协议版本

    private String requestURI;//uri中的请求部分,即:"?"左侧的内容
    private String queryString;//uri中的参数部分,即:"?"右侧部分
    private Map<String,String> parameters = new HashMap<>();//定义一个Map存放uri右侧的每一组参数

    //消息头的相关信息
    private Map<String,String> headers = new HashMap<>();//创建一个Map散列表,用来存储消息头和内容

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket=socket;
        /*在构造方法中调用解析请求相关方法*/
        //1:解析请求行
        parseRequestLine();
        //2:解析消息头
        parseHeaders();
        //3:解析消息正文
        parseContent();
    }

    /**
     * 1.该方法用来解析请求行
     * @throws IOException
     * @throws EmptyRequestException
     */
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readline();//调用该方法完成读取请求行内容
        if (line.isEmpty()){//如果请求的信息为空
            //抛出空请求异常
            throw new EmptyRequestException();
        }
        System.out.println("请求行内容: "+line);
        //将请求行内容按照空格来拆分,并返回一个字符串数组
        String[] data = line.split("\\s");
        method = data[0];//获取元素1,赋值给请求方式
        uri = data[1];//获取元素2,赋值给抽象路径
        protocol = data[2];//获取元素3,赋值给协议版本
        //根据获取的uri值,进一步解析uri
        parseURI();//调用解析uri的方法
        //打桩输出
        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);
    }

    /**
     * 该方法用来进一步解析请求行中的uri
     */
    private void parseURI(){
        /*
            uri有两种情况:
            1:不含有参数的
              例如: /index.html
              直接将uri的值赋值给requestURI即可.
            2:含有参数的
              例如:/regUser?username=fancq&password=&nickname=chuanqi&age=22
              将uri中"?"左侧的请求部分赋值给requestURI
              将uri中"?"右侧的参数部分赋值给queryString
              将参数部分首先按照"&"拆分出每一组参数，再将每一组参数按照"="拆分为参数名与参数值
              并将参数名作为key，参数值作为value存入到parameters中。
         */
        String[] data = uri.split("\\?");//如果uri为有参类型,根据正则按照"?"拆分,返回字符串数组,第一个\用来转义第二个\
        requestURI = data[0];//将?左边的参数,即uri赋给requestURI变量
        if (data.length>1){ //判断,如果字符串输出的长度>1,说明?后面有参,则进行下一步处理参数
            queryString = data[1];
            parseParameters(queryString);//将?后面的参数传入parseParameters()方法中进行转码处理
        }
        //打桩输出uri中参数的对应值
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);//输出进一步解析uri的Map
    }

    /**
     * 解析参数。参数可能来自于抽象路径urL中或者正文中(也起到重用的效果)
     * @param line 字符串格式应当是name=value&name=value&...
     */
    private void parseParameters(String line){//返回值为void,将内容转码后拆分放入parameters里
        //对参数部分进行转码
        try {
            line = URLDecoder.decode(line,"UTF-8");//传入需要转码的字符串内容,确定转换的格式UTF-8
        } catch (UnsupportedEncodingException e) { //该异常为不支持的字符集
            e.printStackTrace();
        }
        String[] data = line.split("&");//根据含参uri中?右边参数特点,每组参数被"&"隔开,则使用&拆分,返回存放每组参数的字符串数组
        System.out.println("URI拆分后为:"+Arrays.toString(data));
        for (String para : data){//遍历按"&"拆分后的每一组参数
            String[] arr = para.split("=",2);//将每一组参数按照"="拆分成两项
            parameters.put(arr[0],arr[1]);//将拆分后的两部分put到Map中(key,value)
            //或        if (arr.length>1) {
//三目运算      parameters.put(arr[0], arr.length > 1 ? arr[1] : "");
//
        }
    }

    /**
     * 2.该方法用来解析消息头
     * @throws IOException
     */
    private void parseHeaders() throws IOException {
        //读取消息头
        while (true){
            String line = readline();//循环调用readline()方法读一行
            //因为请求到最后浏览器会单独发送一个回车加换行,故如果读取到的是空字符串,说明单独读取到了CRLF
            if (line.isEmpty()){
                break;//读取到最后为空时说明读取结束,跳出循环
            }
            System.out.println("消息头: "+line);
            //将消息头按照":"拆分名字和值并作为key,value存入到headers键值对中
            String[] data = line.split(":\\s");
            //System.out.println(Arrays.toString(data));//输出拆分后的消息头
            headers.put(data[0],data[1]);//将截取的两部分分为key和value
        }
        System.out.println("headers: "+headers);//Map输出的格式为{key=value,key=value......}
    }

    /**
     * 该方法用来解析消息正文(用户使用Post请求时会有需要解析的消息正文)
     * @throws IOException
     */
    private void parseContent() throws IOException {
        //判断请求方式是否为post请求,如果是post请求,该业务中用户上传的信息会包含在消息正文里
        if ("post".equalsIgnoreCase(method)){ //判断请求方式是否为post请求
            //通过消息头Content-Length来确定正文的长度,containsKey()该方法返回boolean值
            if (headers.containsKey("Content-Length")){//查看存放消息头和正文的Map中是否包含该key值
                int contentLength = Integer.parseInt(headers.get("Content-Length"));//若包含,将字符串转换为整数
                //基于正文长度创建一个等长数组，用于块读正文数据。
                byte[] data = new byte[contentLength];
                InputStream in = socket.getInputStream();//通过socket获取输入流读这个消息正文
                in.read(data);

                //获取消息头Content-Type
                String contentType = headers.get("Content-Type");
                //判断正文类型进行不同处理(若正文类型有该种情况,说明用户提交了表单数据,并不含附件)
                if ("application/x-www-form-urlencoded".equals(contentType)){
                    //表单数据,不含附件的。格式是一个字符串,就是原GET请求中在抽象路径"?"右侧内容
                    String line = new String(data, StandardCharsets.ISO_8859_1);//将读取的内容按照ISO_8859_1的字符集转给line
                    System.out.println("正文内容："+line);
                    parseParameters(line);//将正文内容进行转码拆分
                }
//                else if(){}//后期可以再自行扩展判断其他类型并解析正文
            }
        }
    }

    //该方法实现调用一次读一行读到CRLF(回车换行)时一行结束
    public String readline() throws IOException { //该部分代码需要重用,故异常需调用者处理
        //调用同一个socket对象若干次getInputStream()方法返回的始终是同一条输入流
        InputStream in = socket.getInputStream();//通过socket获取输入流
        StringBuilder builder = new StringBuilder();//利用StringBuilder可以实现对读取的字符进行追加操作
        char pre='a',cur='a';//pre表示上次读取的字符,cur表示本次读取的字符
        int d;//接收read()方法返回读取1个字节二进制低八位内容(一次读一个)
        while ((d = in.read()) !=-1){//当为-1时说明流读取到了末尾
            cur = (char) d;//赋给本次读取的字符
            if (pre==13&cur==10){//若上一个读取的是回车符并且本次读取的是换行符
                break;//跳出循环
            }
            builder.append(cur);//追加本次读取到的字符
            pre = cur;//进入下一个循环前将本次读取的字符记作上次读取的字符
        }
        return builder.toString().trim();//这里StringBuilder重写了toString()方法,调用并去除空格赋给line
    }

    //因为浏览器请求的内容是规定的,通常不需要修改,所以只需提供get方法即可
    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 根据消息头名字来获取对应的值
     * @param name 消息头
     * @return 返回消息头后面的内容
     */
    public String getHeader(String name) {//根据传进来的消息头
        return headers.get(name);//利用Map中的get方法来获取对应key的value
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    /**
     * 根据含参uri中"?"后的parameters键值对参数部分的key获取value
     * @param name 抽象路径后的参数名例如username=...
     * @return 返回参数内容
     */
    public String getParameters(String name) {//传入用户参数中的key
        return parameters.get(name);//利用Map中的get方法获取value并返回
    }
}
