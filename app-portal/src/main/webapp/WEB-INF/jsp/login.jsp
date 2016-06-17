<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<head>
	<%@include file="/inc/meta.jsp" %>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">
	<meta http-equiv="X-UA-Compatible" content="edge"/>
	<!-- CSS -->
	<link href="${resPrefixUrl }/portal/login/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
	<link rel="stylesheet" href="${resPrefixUrl}/portal/login/css/supersized.css">
	<link rel="stylesheet" href="${resPrefixUrl}/portal/login/css/style.css">
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
	<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<style>
		@media screen and(max-width:1000px){
			.container{width:220px}
		}
		#errorMsg{
			margin-top: 12px;
			display: block;
			margin-left: 100px;
			color: red;
		}
	</style>


	<style type="text/css">

		<c:if test="${not empty er}">
		#errorMsg{
			visibility:hidden}
		</c:if>

		#imgValidateCode{position: relative;top: 0px;left: 0px;}
		#checkedIcon{display:none}
		.lotusLogin .lotusLoginForm input{
			font-size:11pt;
		}
	</style>

	<script type="text/javascript">
		function changeCode(){
			document.getElementById("imgValidateCode").src="${ctx}/vc/get?dt="+(new Date().getTime());
		}
		function submitForm(){
			if($("#username").val() == ""){
				showError("请输入用户名！");
				$("#username").focus();
				return;
			}
			if($("#password").val() == ""){
				showError("请输入密码！");
				$("#password").focus();
				return;
			}
			if($("validateCode").val() == ""){
				showError("请输入验证码！");
				$("validateCode").focus();
				return;
			}

			loginForm.submit();
		}
		function showError(msg){
			$("#errorMsg")[0].style.visibility='visible';
			errorMsg.innerHTML=msg;
		}

	</script>
</head>

<body>
<div class="container">
	<h1>欢迎使用 云呼你</h1>
	<div class="row">
		<div class="col-lg-6">
			<div style="display:inline-block;padding-top: 255px;">
				<span >还没有账号，注册新账号</span>
				<br>
				<span>快速注册账号</span>
				<br>
				<a class="btn btn-default" href="#" role="button">注册</a>
			</div>
		</div>

		<div class="col-lg-6">
			<form:form  id="loginForm" method="post" action="${ctx}/login">
				<%--<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>--%>
				<input styletype="text"  class="username" placeholder="邮箱地址" id="username" name="username" value="${param.username }" onkeydown="if(event.keyCode == 13){document.getElementById('password').focus();}">
				<p class="tips">&nbsp;&nbsp;亲，请检查用户名或密码!</p>
				<input type="password" class="password" placeholder="密码" id="password" name="password" type="password"  value="${param.password }" >
				<input type="Captcha" class="Captcha"  placeholder="验证码" maxlength="4"  id="validateCode" name="validateCode"  value="${param.validateCode }" onkeydown="if(event.keyCode == 13){submitForm();}" >
				<img alt="" src="${ctx}/vc/get" id="imgValidateCode" style="cursor: pointer;width: 80px;padding-top: 0px;margin:28px 0px 0px 15px;" onclick="javascript:changeCode();" title="看不清？请点击图片更换验证码">
				<a class="forgot" href="${ctx}/common/forgetPassword">忘记密码？</a>
				<input class="remember" type="checkbox">
				<p class="rember">记住我</p>
				<span id="errorMsg">
					<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
						${SPRING_SECURITY_LAST_EXCEPTION.message}
					</c:if>
				</span>
				<button type="button" class="submit_button" onclick="submitForm();" >登录</button>
			</form:form>
		</div>
	</div>

	<p class="rights">Copy Rights © 2013-2014 &nbsp;版权所有 All Rights Reserved 京ICP备&nbsp;14034791号</p>
</div>

</body>
<!-- Javascript -->
<script src="${resPrefixUrl}/common/scripts/jquery/jquery-1.11.1.min.js" ></script>
<script src="${resPrefixUrl}/portal/login/js/supersized.3.2.7.min.js" ></script>
<script src="${resPrefixUrl}/portal/login/js/supersized-init.js" ></script>
<script src="${resPrefixUrl}/portal/login/js/scripts.js" ></script>


</html>
