<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%=session.getAttribute("token")%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%=basePath%>jScript/jquery.js" type="text/javascript" charset="utf-8"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
      <form action="/updateUser" method="post" enctype="multipart/form-data">
  
      <input type="hidden" id="account" name="account"><br>
          姓名:<input type="text" id="name" name="name"><br>
          生日:<input type="text" id="birth" name="birth"><br>
          性别:<br>
          女<input type="radio" id="sexwom" name="sex" checked="checked" value="0">
          男<input type="radio" id="sexm" name="sex" value="1"><br>
          昵称:<input type="text" id="nickName" name="nickName"><br>
          QQ:<input type="text" id="qq" name="qq"><br>
          电话:<input type="text" id="phone" name="phone"><br>
          学历:<input type="text" id="education" name="education"><br>
           婚姻:<br>
          已婚<input type="radio" id="married" name="marriage" checked="checked" value="1">
          未婚<input type="radio" id="single" name="marriage" value="0"><br>
        <input type="file" id="userPic" multiple name="userPic"><br>
          地址:<input type="text" id="address" name="address"><br>
          密码:<input type="password" id="password" name="password"><br>
          
          <input type="submit" value="修改">
      </form>
</body>
<script type="text/javascript" src="<%=basePath%>jScript/getUserBya.js" charset="utf-8"></script>
</html>