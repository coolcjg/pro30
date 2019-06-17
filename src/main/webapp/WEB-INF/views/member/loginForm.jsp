<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인창</title>

<c:choose>
	<c:when test="${result =='loginFailed' }">
		<script>
			window.onload=function(){
				alert("아이디나 비밀번호가 틀립니다. 다시 로그인하세요!");
			}
		</script>
	</c:when>
</c:choose>

<style>
table{
	border-collapse: collapse;
}

td{
	border:1px solid #e9e3ed;
	padding:5px;
}


</style>

</head>
<body>
<br>
<br>
<br>
<form name="frmLogin" method="post" action="${contextPath}/member/login.do">

	<table align="center"; >
		<tr>
			<td>아이디</td>
			<td><input type="text" name="id" value="" size="20"></td>
		</tr>
		
		<tr>
			<td>비밀번호</td>
			<td><input type="password" name="pwd" value="" size="20"></td>
		
		</tr>
		
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="로그인">
				<input type="reset" value="다시 입력">
			</td>
		</tr>
	</table>
</form>
<br>
<br>
<br>
</body>
</html>