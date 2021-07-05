<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="jScript/jquery.js" type="text/javascript" charset="utf-8"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
      <form action="/regist" method="post" enctype="multipart/form-data">
          姓名:<input type="text" name="name"><br>
          生日:<input type="text" name="birth"><br>
          性别:<br>
          女<input type="radio" name="sex" checked="checked" value="0">
          男<input type="radio" name="sex" value="1"><br>
          昵称:<input type="text" name="nickName"><br>
          QQ:<input type="text" name="qq"><br>
          电话:<input type="text" id="phone" name="phone"><a href="#" onclick="a()">检验是否可用</a><br>
          学历:<input type="text" name="education"><br>
           婚姻:<br>
          已婚<input type="radio" name="marriage" checked="checked" value="1">
          未婚<input type="radio" name="marriage" value="0"><br>
        <input type="file" multiple name="userPic"><br>
          地址:<input type="text" name="address"><br>
          帐号:<input type="text" name="account"><br>
          密码:<input type="password" name="password"><br>
          用户身份:
          <select class="selector" name="roles">
          </select>
          
          
          <input type="submit" value="提交">
      </form>
      <form action="/addUserPic" method="post">
          <input type="file" multiple value="pic">
      </form>
</body>
<script type="text/javascript">
function a(){
	var phone=document.getElementById("phone").value;
	window.open("/checkUserUnique?phone="+phone);
}

$.get("/getRoles",null,function(data){
	var roles=data.roles;
	$(".selector").append("<option value="+roles[0]+">"+roles[0]+"</option>"); 
	$(".selector").append("<option value="+roles[1]+">"+roles[1]+"</option>"); 
	//$(".selector option[0]").text(roles[0]);
	//$(".selector option[1]").text(roles[1]);
	//$("#user").val(roles[0]);
	//$("#admin").val(roles[1]);
});  

</script>
</html>