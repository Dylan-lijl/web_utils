package pub.carzy.services.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pub.carzy.api.CommonResult;
import pub.carzy.services.dto.request.DownloadRequest;
import pub.carzy.services.dto.request.UploadRequest;
import pub.carzy.services.dto.response.UploadResponse;
import pub.carzy.services.service.FileService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author admin
 */
@RequestMapping("/file")
@Controller
@Api(tags = "文件管理器", description = "文件管理器")
public class FileController {
    @Resource
    private FileService fileService;

    @GetMapping()
    @ApiOperation("获取文件")
    public void download(@Valid DownloadRequest request, HttpServletResponse response) {
        fileService.download(request, response);
    }

    @PostMapping()
    @ApiOperation("上传文件")
    @ResponseBody
    public CommonResult<UploadResponse> upload(@Valid UploadRequest request) {
        UploadResponse response = fileService.upload(request);
        return CommonResult.success(response);
    }
}
