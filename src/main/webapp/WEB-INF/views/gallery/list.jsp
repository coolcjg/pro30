<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<%
	request.setCharacterEncoding("UTF-8");
%> 


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
	function fn_writeForm(isLogOn, galleryForm, loginForm){
		if(isLogOn!='' && isLogOn!='false'){
			location.href=galleryForm;
		}else{
			alert("로그인 후 글쓰기가 가능합니다.");
			location.href=loginForm+'?action=/gallery/galleryForm.do';
		}
	}
</script>


</head>
<body>

<div style="text-align:left">
<h1>갤러리</h1>
</div>

<div style="border:2px solid black;">

	
	<c:choose>
		<c:when test="${articlesList == null }">
		
		
			
			
				<div style="border:1px solid black; padding:10px; width:305px">
					<div style="width:300px; height:300px; border:1px solid green" >
						<img src="${contextPath}/resources/image/gallery.jpg" style="width:300px; height:300px">
					</div>
					
					<div style="width:300px; text-align:left">
						등록된 글이 없습니다.
					</div>
					
					<div style="width:300px; text-align:left ">
						글쓴이
					</div>
					
					<div style="width:300px; text-align:left ">
						날짜
					</div>
				</div>
				
			
		</c:when>
		
		<c:when test="${articlesList!=null}">
			<c:forEach var="article" items="${articlesList}" varStatus="articleNum">
				
				${article.articleNO }
					${article.id }
									<a class='cls1 move' href='<c:out value="${article.articleNO}"/>'><c:out value="${article.title}"/></a>
					${article.writeDate }
					
				
			</c:forEach>

		</c:when>		
	</c:choose>

</div>

<br>
<br>

<!--  검색처리 -->
<div>
	<form id='searchForm' action="${contextPath}/board/listArticlesWithPaging.do" method="get">
		<select name='type'>
			<option value="" <c:out value="${pageMaker.cri.type==null?'selected':''}"/>>--</option>
			<option value="T" <c:out value="${pageMaker.cri.type eq 'T'?'selected':''}"/> >제목</option>
			<option value="C" <c:out value="${pageMaker.cri.type eq 'C'?'selected':''}"/> >내용</option>
			<option value="W" <c:out value="${pageMaker.cri.type eq 'W'?'selected':''}"/> >작성자</option>
			<option value="TC" <c:out value="${pageMaker.cri.type eq 'TC'?'selected':''}"/> >제목 or 내용</option>
			<option value="TW" <c:out value="${pageMaker.cri.type eq 'TW'?'selected':''}"/> >제목 or 작성자</option>
			<option value="TWC" <c:out value="${pageMaker.cri.type eq 'TWC'?'selected':''}"/> >제목 or 내용 or 작성자</option>
		</select>
		<input type="text" name="keyword" value='<c:out value="${pageMaker.cri.keyword}"/>'/>
		<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
		<input type="hidden" name="amount" value="${pageMaker.cri.amount}">
		<button>Search</button>
	</form>
</div>

<br>
<hr width="80%" color="#e9e3ed" size="1px">
<br>

<!-- 글쓰기 버튼 -->
<div id="board_function">
	<a class="cls1" href="javascript:fn_writeForm('${isLogOn}','${contextPath}/gallery/galleryForm.do','${contextPath}/member/loginForm.do')"><p class="cls2">글쓰기</p></a>
</div>




</body>
</html>