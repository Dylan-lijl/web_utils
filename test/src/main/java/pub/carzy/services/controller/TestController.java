package pub.carzy.services.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.carzy.api.CommonPage;
import pub.carzy.api.CommonResult;
import pub.carzy.export_file.file_export.ExportMethod;
import pub.carzy.services.dto.request.TestRequest;
import pub.carzy.services.dto.response.TestResponse;
import pub.carzy.services.service.TestService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author admin
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("/pageTest")
    @ExportMethod(filename = "测试分页列表")
    public CommonResult<CommonPage<TestResponse>> pageTest(@RequestBody @Valid TestRequest request) {
        IPage<TestResponse> page = testService.pageTest(request);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @RequestMapping("/pageList")
    @ExportMethod(filename = "测试列表")
    public CommonResult<List<TestResponse>> pageList(@RequestBody @Valid TestRequest request) {
        List<TestResponse> list = testService.pageList(request);
        return CommonResult.success(list);
    }

    @RequestMapping("/pageTestOther")
    @ExportMethod(filename = "测试列表")
    public CommonResult<List<TestResponse>> pageTestOther(@RequestBody @Valid TestRequest request) {
        List<TestResponse> list = testService.pageTestOther(request);
        return CommonResult.success(list);
    }
}
