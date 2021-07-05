$(function(){
	$.post("/getUserById",null,function(data,status){
		if(data){
			var user=data.user;
			var picdir=data.picdir;
			var name="";
			//user=$.parseJSON(user);
//			$.each(user, function (n, value) {
				//$("#account").val(value.account);
				var name="";
				$("#name").val(data.user.name);
				$("#birth").val(data.user.birth);
				var sex =data.user.sex;
                $(":radio[name='sex'][value='" + sex + "']").prop("checked", "checked");
                $("#nickName").val(data.user.nickName);
                $("#qq").val(data.user.qq);
                $("#phone").val(data.user.phone);
                $("#education").val(data.user.education);
                var marriage =data.user.marriage;
                $(":radio[name='marriage'][value='" + marriage + "']").prop("checked", "checked");
                $("#address").val(data.user.address);
                $("#password").val(data.user.password);
//	        });
		}
		//alert("name:"+name);
	});
});