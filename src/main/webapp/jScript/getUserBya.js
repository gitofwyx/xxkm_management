HashMap.prototype.constructor;
var hashMap = new HashMap();
HashMap.
$(function(){
	$.post("/route/addUserRouteRecord",map,function(data,status){
		if(data){
//			var user=data.user;
//			var picdir=data.picdir;
//			var name="";
//			//user=$.parseJSON(user); 
//			$.each($.parseJSON(user), function (n, value) {
//				$("#account").val(value.account);
//				$("#name").val(value.name);
//				$("#birth").val(value.birth);
//				var sex =value.sex;
//                $(":radio[name='sex'][value='" + sex + "']").prop("checked", "checked");
//                $("#nickName").val(value.nickName);
//                $("#qq").val(value.qq);
//                $("#phone").val(value.phone);
//                $("#education").val(value.education);
//                var marriage =value.marriage;
//                $(":radio[name='marriage'][value='" + marriage + "']").prop("checked", "checked");
//                $("#address").val(value.address);
//                $("#password").val(value.password);
//	        });
		}
		//alert("name:"+name);
	});
});