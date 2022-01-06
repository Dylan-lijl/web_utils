package pub.carzy.services.service;


import pub.carzy.services.dto.request.DownloadRequest;
import pub.carzy.services.dto.request.UploadRequest;
import pub.carzy.services.dto.response.UploadResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
public interface FileService {
    /**
     * 下载文件
     *
     * @param request  请求
     * @param response 请求
     */
    void download(DownloadRequest request, HttpServletResponse response);

    /**
     * 上传文件
     *
     * @param request 请求
     * @return 文件路径
     */
    UploadResponse upload(UploadRequest request);

}
