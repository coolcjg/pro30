<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<style>

	</style>

<meta charset="UTF-8">
<title>사이드 메뉴</title>
</head>
<body>

	<h1>
		<!-- 
		<a href="${contextPath}/member/listMembers.do" class="no-underline">회원정보</a><br><br>
		 -->
		<a href="${contextPath}/free/list.do" class="no-underline">자유게시판</a><br><br>
		<a href="${contextPath}/board/listArticlesWithPaging.do" class="no-underline">여행정보</a><br><br>
		<a href="${contextPath}/gallery/list.do" class="no-underline">갤러리</a><br><br>
	</h1>

</body>
</html>