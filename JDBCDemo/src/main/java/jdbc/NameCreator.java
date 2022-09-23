package jdbc;

import java.util.Random;

/**
 * 该案例可随机生成1-3个字的名字
 */
public class NameCreator {
    public static String createName(){
        String line = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金" +
                "魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞仁袁柳酆鲍" +
                "史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平" +
                "黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁" +
                "杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡";
        Random random = new Random();
        //以该字符串长度做索引为范围来随机返回一个字符并接收
        String name = line.charAt(random.nextInt(line.length()))+"";
        for(int i= 1+random.nextInt(2);i>0;i--){//循环次数为1或2次
            name+=line.charAt(random.nextInt(line.length()))+"";//根据循环的次数增加字符
        }
        return name;
    }
    public static void main(String[] args) {
        String name = createName();
        System.out.println(name);
    }
}
