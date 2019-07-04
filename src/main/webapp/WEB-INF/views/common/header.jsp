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
				<a href="${contextPath }/member/info.do?id=${member.id}" class="no-underline">정보수정</a>
				<a href="${contextPath }/member/logout.do" class="no-underline">로그아웃</a>
			</c:when>
			<c:otherwise>
				<a href="${contextPath}/member/memberForm.do" class="no-underline">회원가입</a>
				<a href="${contextPath }/member/loginForm.do" class="no-underline">로그인</a>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div style="clear:both">
	</div>
	</body>
</html>