import com.maserhe.UserApplication;
import com.maserhe.entity.UserDo;
import com.maserhe.mapper.UserMapper;
import com.maserhe.sms.SendSMS;
import com.maserhe.user.service.Impl.UserServiceImpl;
import com.maserhe.utils.RedisOperator;
import com.maserhe.utils.SMSUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-30 21:24
 */
@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class TestSms {

    @Autowired
    private SMSUtils sMSUtils;

    @Autowired
    private SendSMS sendSMS;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() throws Exception {

        //System.out.println(sendSMS.getSign());
        //String[] number = {"+8615839496393"};
        //String[] msg = {"123132"};
        //sMSUtils.sendMsg(number, msg);
        //boolean b = sMSUtils.sendMsg("+8615839496393", "0123");
        //System.out.println(b);

    }

    @Test
    public void test1() {
        //测试
        redisOperator.set("masg", "123456");
        String masg = redisOperator.get("masg");
        System.out.println(masg);
    }

    @Test
    public void test2() throws Exception {

        System.out.println(Math.random());
    }

    @Test
    public void test3() {

        Example example = new Example(UserDo.class);
        example.createCriteria().andEqualTo("mobile", "15839496393");
        UserDo userDo = userMapper.selectOneByExample(example);
        System.out.println(userDo);

    }

}
