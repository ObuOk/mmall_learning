<%--
  Created by IntelliJ IDEA.
  User: guojianzhong
  Date: 2017/6/18
  Time: 下午6:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>HelloWorld</h1>

    springmvc上传文件
    <form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file" />
        <input type="submit" value="springmvc上传文件" />
    </form>


    富文本图片上传文件
    <form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file" />
        <input type="submit" value="富文本图片上传文件" />
    </form>


</body>
</html>
