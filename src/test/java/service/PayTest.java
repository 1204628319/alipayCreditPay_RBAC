package service;

import base.BaseTest;
import com.greatwall.jhgx.rest.PayController;
import org.springframework.beans.factory.annotation.Autowired;

public class PayTest extends BaseTest {

    @Autowired
    PayController payController;

//    @Test
//    public void testPay() {
//           SystemOrder systemOrder = new SystemOrder();
//        systemOrder.setAuthCode("287017386304843375");
//        systemOrder.setNote("test");
//        systemOrder.setOrderAmt("2");
//        payController.pay(systemOrder);
//    }
}
