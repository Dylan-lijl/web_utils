package pub.carzy.services.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pub.carzy.api.ResultCodeEnum;
import pub.carzy.services.dto.request.DownloadRequest;
import pub.carzy.services.dto.request.UploadRequest;
import pub.carzy.services.dto.response.UploadResponse;
import pub.carzy.services.service.FileService;
import pub.carzy.util.ExceptionHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件处理业务
 *
 * @author admin
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private Map<String, String> mimeTypeMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    void init() {
        URL url = this.getClass().getClassLoader().getResource("mime-type.json");
        try (InputStream stream = url.openStream()) {
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            JSONObject object = JSONObject.parseObject(new String(bytes));
            for (Map.Entry<String, Object> line : object.entrySet()) {
                mimeTypeMap.put(line.getKey(), line.getValue().toString());
            }
        } catch (IOException e) {
            ExceptionHandler.throwException(ResultCodeEnum.SYSTEM_EXCEPTION.getMessage() + ":启动失败,获取mimeType资源失败");
        }
    }

    @Override
    public void download(DownloadRequest request, HttpServletResponse response) {
        File file = new File(applicationContext.getEnvironment().getProperty("web.export.common-file-path"), request.getPath());
        if (!file.exists()) {
            ExceptionHandler.throwException("文件不存在");
        }
        writeFile(file, response);
        String property = applicationContext.getEnvironment().getProperty("web.export.prefix");
        //删除缓存文件
        if (property != null && file.getName().startsWith(property)) {
            file.delete();
        }
    }

    /**
     * 写文件
     *
     * @param file
     * @param response
     */
    private void writeFile(File file, HttpServletResponse response) {
        //获取文件后缀名
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index >= 0 && mimeTypeMap.get(fileName.substring(index)) != null) {
            response.setContentType(mimeTypeMap.get(fileName.substring(index)) + ";charset=UTF-8");
        } else {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=\"" + file.getName().replace(applicationContext.getEnvironment().getProperty("web.export.prefix"), "") + "\"");
        }
        try {
            ServletOutputStream stream = response.getOutputStream();
            Files.copy(file, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UploadResponse upload(UploadRequest request) {
        String replace = UUID.randomUUID().toString().replace("-", "");
        MultipartFile multipartFile = request.getFile();
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = multipartFile.getName();
        }
        int index = originalFilename.lastIndexOf(".");
        String newFilename = index > 0 ? replace + originalFilename.substring(index) : replace;
        File file = new File(applicationContext.getEnvironment().getProperty("web.export.common-file-path"), newFilename);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            ExceptionHandler.throwException("上传文件失败,请稍后重试!");
        }
        UploadResponse response = new UploadResponse();
        response.setPath("/" + newFilename);
        return response;
    }

}
