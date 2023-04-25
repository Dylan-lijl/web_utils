package pub.carzy.services.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import pub.carzy.api.CommonPage;
import pub.carzy.services.dto.request.TestRequest;
import pub.carzy.services.dto.response.TestResponse;
import pub.carzy.services.mapper.HelpKeywordMapper;
import pub.carzy.services.model.HelpKeyword;
import pub.carzy.services.service.TestService;
import pub.carzy.util.BeanHandler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private HelpKeywordMapper helpKeywordMapper;

    @Override
    public IPage<TestResponse> pageTest(TestRequest request) {
        Page<HelpKeyword> page = helpKeywordMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()), new QueryWrapper<>());
        return CommonPage.transformClass(page, TestResponse.class);
    }

    @Override
    public List<TestResponse> pageList(TestRequest request) {
        List<HelpKeyword> list = helpKeywordMapper.selectList(new QueryWrapper<>());
        List<TestResponse> responses = new ArrayList<>();
        list.forEach(item -> responses.add(BeanHandler.copy(item, TestResponse.class)));
        return responses;
    }

    @Override
    public List<TestResponse> pageTestOther(TestRequest request) {
        List<TestResponse> list = new ArrayList<>();
        TestResponse testResponse = new TestResponse();
        testResponse.setHelpKeywordId(1);
        testResponse.setName("测式回车\n内容");
        list.add(testResponse);

        testResponse = new TestResponse();
        testResponse.setHelpKeywordId(2);
        testResponse.setName("测式空格   内容");
        list.add(testResponse);

        testResponse = new TestResponse();
        testResponse.setHelpKeywordId(3);
        testResponse.setName("测式逗号,内容");
        list.add(testResponse);

        testResponse = new TestResponse();
        testResponse.setHelpKeywordId(4);
        testResponse.setName("测式引号\"内容");
        list.add(testResponse);

        testResponse = new TestResponse();
        testResponse.setHelpKeywordId(5);
        testResponse.setName("测式复杂{\"key\":{\"kkk\":555566952}\n,\"value\":{\"v\":\"123456,4541548\"}}内容");
        list.add(testResponse);

        testResponse = new TestResponse();
        testResponse.setHelpKeywordId(6);
        testResponse.setName("测式复杂{\"key\":{\"kkk\":555566952}\n,\"value\":{\"v\":\"123456,4541548\"}}内容");
        list.add(testResponse);
        return list;
    }
}
