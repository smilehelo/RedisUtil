/**
 * @program: RedisUtil
 * @description:
 * @author: HeLO
 * @create: 2019-02-08 19:16
 **/
public class BeanTest1 {

    private String name;

    private int age;

    private String remark;

    public BeanTest1(String name, int age, String remark) {
        this.name = name;
        this.age = age;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
