请将下列内容转换成readme.md表格,并放入代码格式当中,我才能复制
类型:键值对
类型值:1
使用方法: "convertor": {
           "type": 1,
           "valueMap": {
             "key1": "value1",
             "key2": "value2"
           },
         }
说明:根据值查找这个map对应的value作为表格值

类型:时间类型
类型值:2
使用方法: "convertor": {
           "type": 2,
           "extMap":{
             "pattern": "yyyy-MM-dd"
           }
         }
说明:将内容转成时间字符串默认"yyyy-MM-dd HH:mm:ss",pattern可选参数:格式化表达式

类型: 拼接
类型值:3
使用方法: "convertor": {
           "type": 3,
           "value": "v",
           "extMap":{
             "type":"1"
           }
         }
说明:可选参数如果type为空或等于1就value+v,否则v+value

类型:字符串替换
类型值:4
使用方法: "convertor": {
           "type": 4,
           "valueMap": {
             "src": "\\s+",
             "dest": ""
           },
           "extMap":{
             "type":"2"
           }
         }
说明:可选参数如果type为空或等于1就当成字符串替换,否则当成正则表达式替换

类型:数组转成字符串
类型值:5
使用方法: "convertor": {
           "type": 5,
           "value": "-",
           "extMap":{
             "placeholder":"$tmp",
             "separator": "\n"
           }
         }
说明:将字符串数组转成根据分割符字符串,extMap.placeholder可选参数作用是如果原字符串存在分隔符,使用另一个字符代替这个,比如:123$tmp123-142,最终结果是123-123\n142,extMap.separator参数是分割后拼接的字符,默认\n

类型:JSON字符串转键值对
类型值:6
使用方法: "convertor": {
           "type": 5,
           "extMap":{
             "type":"yml"
           }
         }
说明: 可选参数extMap.type可选值有 yaml,yml(默认)和properties

类型:位运算匹配选项
类型值:7
使用方法: "convertor": {
           "type": 7,
           "extMap":{
             "operation": "<<",
             "count": 2,
             "join": ","
           }
         }
说明:  extMap可选参数:
         operation:运算符,如果没有将原值返回,可选值:[&,|,^,~,<<,>>,>>>]
         count: 次数,当操作符是<<,>>,>>>时表示位移几次,区间在[1-64],当不在这个区间将返回原值
         join: 连字符,默认空串(无符号拼接)

