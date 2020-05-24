//package service;
//
//import base.BaseTest;
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.greatwall.component.ccyl.common.model.PageRequest;
//import com.greatwall.jhgx.domain.BizType;
//import com.greatwall.jhgx.service.BizTypeService;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author wangcan
// * @date 2020/4/25 18:47
// */
//public class BizTypeTest extends BaseTest {
//
//    @Autowired
//    BizTypeService bizTypeService;
//
//    @Test
//    public void query() {
//        PageRequest page = new PageRequest();
//        page.setPage(1);
//        page.setSize(10);
//        BizType bizType = new BizType();
//        bizType.setOrgCode("4301210021002");
//        bizType.setId(1171723101906210818L);
//        IPage iPage = bizTypeService.selectByCondition2PageData(bizType, page);
//        Assert.assertNotNull(iPage);
//        System.out.println(JSON.toJSONString(iPage));
//    }
//}
