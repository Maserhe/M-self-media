import com.maserhe.UserApplication;
import com.maserhe.sms.SendSMS;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private SendSMS sendSMS;

    @Test
    public void test() {
        //System.out.println("dsfadfa");
        System.out.println(sendSMS.getSecretKey());
    }

}
