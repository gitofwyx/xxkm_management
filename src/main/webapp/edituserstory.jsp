<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
      <form action="/userstory/edituserstory" method="post" enctype="multipart/form-data">
          	故事类别:
			<select name="type" size="1">
			      <option value="1"  selected="selected">惊险</option>
			      <option value="2">风景</option>
			      <option value="3">大山</option>
			      <option value="4">美食</option>
			</select>
			<br>
			故事：<textarea type="text" name="remark"></textarea>
			<br>
      		上传图片:<input type="file" name="userStoryPic" multiple/> 
          
          <input type="submit" value="提交">
      </form>
     
</body>
</html>