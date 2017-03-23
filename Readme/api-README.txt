使用说明
所有api返回格式都是json格式。

1.列表获取
访问链接http://网址/?/api/article_list/page/per_page/tagid
page,per_page,tagid为参数,用实际数字代替
如获取标签id为1的第一页,每页数量为2,则访问
http://网址/?/api/article_list/1/2/1
返回结构示例与说明:
[{"id":"1","title":"1","content":"1","createtime":"1468572999","tagid":"0","reading":"1","praise":"0","imagelink":"","source":""},{"id":"2","title":"2","content":"2","createtime":"1468572616","tagid":"0","reading":"1","praise":"0","imagelink":"","source":""}]
结构为文章内容对象数组,各内容具体含义参照文章内容的含义.

2.文章内容获取
访问链接http://网址/?/api/view/id
id为参数,即列表中的id,用实际数字代替
如获取id为1的文章内容,则访问
http://网址/?/api/view/1
返回结构示例与说明:
{"id":"1","title":"1","content":"1","createtime":"1468572999","tagid":"0","reading":"7","praise":"0","imagelink":"","source":"","tags":[null]}
id为文章唯一序列号,title为文章标题,content为文章内容,createtime为unix时间戳,tagid为文章所属标签的id,reading为阅读量,praise为点赞数,tags为对象数组,当其为[null]表示其属于全部文章列表,当其不为空时将给出一个包含所属标签和父标签的对象数组,如:[{"id":"3","tag":"\u6807\u7b7e3","fid":"0"}]  tag为标签名.
imagelink为文章配图地址，为空表示无图，显示默认配图。source为文章来源，为空表示无来源，显示成匿名来源或直接留空。

3.热门列表获取
访问链接改为http://网址/?/api/article_hotlist/page/per_page/tagid
其它同列表获取

4.内容搜索
访问链接http://网址/?/api/search/page/per_page
page,per_page意义同上
搜索内容需要通过post方式提交，name为value。
返回的内容结构同列表。

5.获取全部标签
访问链接http://网址/?/api/get_all_tag
返回结构示例与说明:
[{"id":"1","tag":"\u6807\u7b7e1","fid":"0"},{"id":"2","tag":"\u6807\u7b7e2","fid":"1"}]
id为标签id，tag为标签名，fid为父标签id(可以无视)。

6.apk更新
访问链接http://网址/?/api/update/version
version为当前版本，如9或10。
返回结构示例与说明
若当前版本低于服务器版本将返回新版apk下载链接
如："http:\/\/192.168.0.149\/\/apk\/antinetfraud_10.apk"
否则将返回null