package pub.carzy.services.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import pub.carzy.services.dto.request.TestRequest;
import pub.carzy.services.dto.response.TestResponse;

import java.util.List;

/**
 * @author admin
 */
public interface TestService {
    IPage<TestResponse> pageTest(TestRequest request);

    List<TestResponse> pageList(TestRequest request);
}
