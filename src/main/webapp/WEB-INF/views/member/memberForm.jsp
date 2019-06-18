<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>

	table{
		border-collapse: collapse;
	}
	
	td{
		border:1px solid #e9e3ed;
		padding:5px;
	}

	.text_center{
	text-align:center;
	}
</style>

</head>
<body>
	<br>
	<form method="post" action="${contextPath}/member/addMember.do" enctype="multipart/form-data">
		<table align="center">
			<tr>
				<td>아이디</td>
				<td><input type="text" name="id"></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="pwd"></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><input type="text" name="email"></td>
			</tr>
			<tr>
				<td colspan="2" ><input type="submit" value="가입하기"> <input type="reset" value="다시입력"></td>
			</tr>
		</table>
	</form>
	<br>
</body>
</html>