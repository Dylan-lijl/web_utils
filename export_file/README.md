# 项目名称

项目描述

## 使用环境

- SpringBoot
- JDK8

## 使用方法

（可以直接参考 test 项目）

### 基础使用方法

#### 后端

1. 添加依赖

    ```
    <dependency>
         <groupId>pub.carzy</groupId>
         <artifactId>export_file</artifactId>
         <version>2.0</version>
    </dependency>
    ```

2. 添加注解 `WebExportFileEnable` 开启服务，在配置文件指定配置 `web.export.prefix` 和 `web.export.common-file-path`。

3. 基础对应的类来映射对象类型：

   3.1. 继承 `DefaultExportAopCallback` 对象，根据自己的需求来重写对应的方法。推荐所有接口都使用实体类来接收参数，并继承一个基础类，在放入 `ExportRequestParam`。

        - `getExportParam` 方法来获取前端传入对应的导出参数实体，传入的参数是拦截接口的方法的方法参数。
        - `updatePageSize` 方法来修改每页查询条数，来优化多次访问。
        - `responseResult` 方法在导出成功之后需要给前端返回数据，其结果必须要与方法返回值一致。比如方法返回 `String`，你的结果就只能返回 `String`，但这是一种缺陷，所以应该使用一个包装类，里面使用一个泛型来避免这个问题，或者接口全部返回 `Object` 类型（这是你不愿意的）。

4. 编写对应的解析类：

   4.1. 基础 `AbstractExportFiler` 类

        - `match` 方法来匹配对应的类型或规则。
        - `getOrder` 方法来排序解析类顺序。
        - `createExportActuator` 创建导出执行器，实现 `transformContent` 方法来根据你返回的内容来选择数据。

5. 在需要导出的接口添加 `ExportMethod` 注解。

#### 前端

1. 访问接口传递参数传入 `ExportRequestParam` 对象。

2. 接收 `responseResult` 方法返回的参数进行下一步。

### 高阶使用

#### 过滤扫描对应的接口

自定义 aop 表达式：`web.export.aop-expression`。

#### 添加转换器

实现 `ExportFileValueConvertor` 接口并重写对应的方法：

   - 第一种使用 SPI 注入，在项目资源文件夹下新建 `META-INF/services` 文件夹，新建 `pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor` 文件将扩展的类的全路径复制进去。
   - 第二种使用 Spring 注入...

#### 使用 factory 来创建文件导出类

实现 `FileWriteFactory` 接口并重写对应的方法：

   - 第一种使用 SPI 注入，在项目资源文件夹下新建 `META-INF/services` 文件夹，新建 `pub.carzy.export_file.template.FileWriteFactory` 文件将扩展的类的全路径复制进去。
   - 第二种使用 Spring 注入...
### 参数说明

###### 前端文件参数

```
//ExportRequestParam
{
  "filename": "文件名称",
  //目前内置 1:csv,2:excel,3:txt
  "fileType": 1,
  //有哪些title就导出对应的title
  "titles": [
    //ExportTitle
    {
      //跟接口返回的实体类字段对应
      "name": "字段名称",
      //前端字段名称(表头名称)
      "title": "标题",
      //表头顺序
      "sort": 1,
      //转换器 ExportValueFormat
      "convertor": {
        //可以查看对应内置的列表
        "type": 1,
        //键值对内容,根据类型来决定使用
        "valueMap": {
          "key1": "value1",
          "key2": "value2"
        },
        //单值内容,根据类型来决定使用
        "value": "单值",
        //多值内容,根据类型来决定使用
        "values": ["多值"],
        //扩展键值对,根据类型来决定使用
        "extMap": {
          "key1": "value1",
          "key2": "value2"
        }
      }
    }
  ],
  //表头样式,内置未实现,可以自己实现
  "titleStyle": {
    "key1": "value1",
    "key2": "value2"
  },
  //表格内容样式,内置未实现
  "valueStyle": {
    "key1": "value1",
    "key2": "value2"
  }
}

```
##### 支持的类型

| 类型 | 类型值 | 使用方法 | 说明 |
| ------ | ------ | ------ | ------ |
| 键值对 | 1 | ```"convertor": { "type": 1, "valueMap": { "key1": "value1", "key2": "value2" }, }``` | 根据值查找这个map对应的value作为表格值 |
| 时间类型 | 2 | ```"convertor": { "type": 2, "extMap":{ "pattern": "yyyy-MM-dd" } }``` | 将内容转成时间字符串默认"yyyy-MM-dd HH:mm:ss",pattern可选参数:格式化表达式 |
| 拼接 | 3 | `"convertor": { "type": 3, "value": "v", "extMap":{ "type":"1" } }` | 可选参数如果type为空或等于1就value+v,否则v+value |
| 字符串替换 | 4 | ```"convertor": { "type": 4, "valueMap": { "src": "\\s+", "dest": "" }, "extMap":{ "type":"2" } }``` | 可选参数如果type为空或等于1就当成字符串替换,否则当成正则表达式替换 |
| 数组转成字符串 | 5 | ```"convertor": { "type": 5, "value": "-", "extMap":{ "placeholder":"$tmp", "separator": "\n" } }``` | 将字符串数组转成根据分割符字符串,extMap.placeholder可选参数作用是如果原字符串存在分隔符,使用另一个字符代替这个,比如:123$tmp123-142,最终结果是123-123\n142,extMap.separator参数是分割后拼接的字符,默认\n |
| JSON字符串转键值对 | 6 | ```"convertor": { "type": 5, "extMap":{ "type":"yml" } }``` | 可选参数extMap.type可选值有 yaml,yml(默认)和properties |
| 位运算匹配选项 | 7 | ```"convertor": { "type": 7, "extMap":{ "operation": "<<", "count": 2, "join": "," } }``` | extMap可选参数: operation:运算符,如果没有将原值返回,可选值:[&,\|,^,~,<<,>>,>>>], count: 次数,当操作符是<<,>>,>>>时表示位移几次,区间在[1-64],当不在这个区间将返回原值, join: 连字符,默认空串(无符号拼接) |
