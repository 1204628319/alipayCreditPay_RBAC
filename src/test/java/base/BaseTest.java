package base;

import com.greatwall.jhgx.Application;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author wangcan
 * @date 2020/4/25 18:46
 */
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = AdminServerApplication.class)
@SpringBootTest(classes = {Application.class})
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        //指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc 逻辑哲学论
        MockMvcBuilders.webAppContextSetup(context).build();
    }

}
