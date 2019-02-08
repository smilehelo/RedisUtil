import cn.smilehelo.redisUtil.RedisUtil;

/**
 * @program: RedisUtil
 * @description:
 * @author: HeLO
 * @create: 2019-02-08 19:13
 **/
public class RedisTest {

    public static void main(String[] args) {
/*        RedisUtil.setString("Test","Hello World");
        System.out.println(RedisUtil.getString("Test"));*/
        testBean();
    }

    public static void testBean(){
        BeanTest1 beanTest1 = new BeanTest1("test1",11,"测试1");
        System.out.println(RedisUtil.setBean("testBean1",beanTest1));
        System.out.println(RedisUtil.getBean("testBean1"));
    }
}
