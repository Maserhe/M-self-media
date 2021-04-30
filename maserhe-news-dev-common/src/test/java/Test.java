import com.maserhe.sms.SendSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-30 21:32
 */
@SpringBootTest
public class Test {

    @Autowired
    private SendSMS sendSMS;

    @org.junit.Test
    public void test() {
        System.out.println(sendSMS.getSecretKey());
    }
}
