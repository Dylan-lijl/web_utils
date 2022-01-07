= 工程简介

此工具功能是通用导出简单文件,看完文档不理解可以看test项目,里面有完整的示例

### 1.使用方法

- 在配置类上添加WebExportFileEnable注解
- 配置Bean->ExportAopCallback,继承DefaultExportAopCallback类按需重写方法
- 配置Bean->ExportFiler编写自己业务规则(根据拦截的方法返回类型来配置)
- 添加异常拦截器拦截异常返回自定义数据: 可以直接拦截异常公共父类ExportBaseException,也可以分开拦截:ExportFileEmptyException,ExportNotSupportedException,SystemErrorException
- 配置文件更改:

    web:
      export:
        # 切点表达式,指定扫描哪些类,默认是扫描RestController和Controller
        aop-expression: "execution(public * pub.carzy.services..*.*(..))&&(@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller))"
        # 文件名前缀,可以不需要
        prefix: tmp_
        # 创建文件所在文件夹的绝对路径,文件夹必须存在
        common-file-path: D:\\tmp\\files

- 在需要导出的方法添加注解,ExportMethod
- 前端请求格式要求, 以json请求为例:

```
    #最外层的json对象
    {
        "_export": {
            #文件名称
            "filename": "测试文件",
            #文件类型, 默认提供:1->csv格式,2->excel格式,3->文本格式,可以使用扩展点进行自定义(必填)
            "fileType": 3,
            #标题(必填)
            "titles": [
                {
                    #字段名称
                    "name": "helpKeywordId",
                    #文件标题名称
                    "title": "帮助主键"
                },
                {
                    "name": "name",
                    "title": "名称"
                }
            ]
        }
    }
```

### 2.高级使用
- 类型转换器,此工具默认提供一些简单的类型处理器:

|功能|前端对应的值|
|:--:|:--:|
|键值对|1|
|时间类型转换(yyyy-MM-dd HH:mm:ss)格式字符串|2|
|前缀拼接|3|
|后缀拼接|4|
|数组转字符串|5|

    自定转换器:1.实现ExportFileValueConvertor接口,实现match方法以及formatValue方法;2.使用SPI加载(自行百度)或使用Spring加载
- 文件处理器,默认提供三种类型

|类型|前端对应的值|
|:--:|:--:|
|csv|1|
|excel|2|
|文本|3|
    自定义文件处理器:1.继承AbstractFileWriter,重写对应的方法,2.创建工厂类实现FileWriteFactory,重写方法,将这个factory添加到spring容器,也可以使用SPI加载
- 前端参数:

```
    {
        "_export": {
            "filename": "测试文件",
            #这个来决定使用哪个文件处理器
            "fileType": 3,
            "titles": [
                {
                    "name": "helpKeywordId",
                    "title": "帮助主键"
                },
                {
                    "name": "name",
                    "title": "名称"
                },
                {
                    "name": "helpKeywordId",
                    "title": "帮助主键映射类型",
                    #转换器
                    "convertor": {
                        #映射转换器
                        "type": 1,
                        #有三个属性提供,value,values,valueMap,由子类扩展使用
                        "valueMap": {
                            "1": "***",
                            "10": "1515",
                            "100":"6565",
                            "default": "未知"
                        }
                    }
                },
                {
                    "name": "name",
                    "title": "名称拼接",
                    "convertor": {
                        "type": 3,
                        "value": "拼接_"
                    }
                }
            ]
        }
    }
```
公共样式:

```
    {
        "_export": {
            "filename": "测试文件",
            #这个来决定使用哪个文件处理器
            "fileType": 3,
            "titles": [
                {
                    "name": "helpKeywordId",
                    "title": "帮助主键"
                },
                {
                    "name": "name",
                    "title": "名称"
                }
            ],
        "titleStyle":{
            # 可选值:居中,居右
            "对齐方式": "居中",
            # 字体
            "字体": "黑体",
            "字体大小": 18,
            "粗体":true,
            "斜体":true,
            "删除线":true,
            "下划线":true,
        }
        #规则一样
        "valueStyle":{

        }
        }
    }
```
= 延伸阅读
