<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상단부</title>
</head>
<body>

		<a href="${contextPath}/main.do">
			<img src="${contextPath}/resources/image/logo.jpg"/>
		</a>


	<div style="float:right">
		<c:choose>
			<c:when test="${isLogOn == true && member!=null}">
				<h3>환영합니다. ${member.name }님!</h3>
				<a href="${contextPath }/member/info.do?id=${member.id}" class="no-underline"><h3>정보수정</h3></a>
				<a href="${contextPath }/member/logout.do" class="no-underline"><h3>로그아웃</h3></a>
			</c:when>
			<c:otherwise>
				<a href="${contextPath}/member/memberForm.do" class="no-underline"><h3>회원가입</h3></a>
				<a href="${contextPath }/member/loginForm.do" class="no-underline"><h3>로그인</h3></a>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div style="clear:both">
	</div>
	</body>
</html>