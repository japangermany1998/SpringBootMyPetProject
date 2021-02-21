package vn.techmaster.authen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.techmaster.authen.service.IAuthenSerVice;

@SpringBootTest
public class TestService {
    @Autowired
    IAuthenSerVice serVice;

    @Test
    public void testPasswordhash(){
        System.out.println(serVice.hashPassword("manofsteel1998"));
    }
}
