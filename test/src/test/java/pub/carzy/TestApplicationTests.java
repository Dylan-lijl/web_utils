package pub.carzy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.FileWriter;
import pub.carzy.export_file.file_export.actuator.writers.TxtFileWriter;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// @SpringBootTest
class TestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void writeFile() {
        try {
            PrintWriter writer = new PrintWriter("D:\\tmp\\files\\tmp_测试文件.txt");
            writer.print("0465465\t\n4565\n");
            writer.flush();
            writer.print('\n');
            writer.flush();
            writer.print('\t');
            writer.flush();
            writer.print("4545");
            writer.print("45455465");
            writer.print("45455465454");
            writer.print("45455465454454");
            writer.print("45455465454454");
            writer.print("\n");
            writer.flush();
            writer.print("5555");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriter() throws IOException {
        ExportActuatorParam param = new ExportActuatorParam();
        param.setPrefix("tmp_");
        param.setCommonFilePath("D:\\tmp\\files\\");
        ExportRequestParam requestParam = new ExportRequestParam();
        requestParam.setFilename("test");
        requestParam.setFileType(3);
        param.setParam(requestParam);
        FileWriter writer = new TxtFileWriter(param);
        writer.createFile();
        List<Object> values = new ArrayList<>();
        values.add("标题");
        values.add("第二标题");
        writer.writeLine(values, null);
        writer.flush();
        values = new ArrayList<>();
        values.add(56);
        values.add(895);
        writer.writeLine(values, null);
        writer.flush();
        List<List<Object>> list = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            List<Object> objects = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                objects.add(i + " * " + j + " = " + (i * j));
            }
            list.add(objects);
        }
        writer.writeMany(list,null);
        writer.flush();
    }

}
