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
	<style>
		.cls1{text-decoration:none;}
		.cls2{text_align:center; font-size:30px;}
		
		
		#pagination{
			
			margin:0px auto;

		}
		
		#pagination ul{
			display:table;
			margin:0px auto;
		}
				
		#pagination ul li{
			list-style:none;
			float:left;
			margin-right:5px;
		}
		
		#board_function{
			width:80%;
			margin:0px auto;
		}
		table{
			border-collapse: collapse;
		}
		
		td{
			border:1px solid #e9e3ed;
			padding:5px;
		}
		
		
		

	</style>

<meta charset="UTF-8">
<title>Insert title here</title>

<script src="http://code.jquery.com/jquery-latest.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	//페이징처리
	var actionForm = $("#actionForm");
	
	$(".paginate_button a").on("click", function(e){
		e.preventDefault();
		console.log('click');
		actionForm.find("input[name='pageNum']").val($(this).attr("href"));
		actionForm.submit();
	});
	
	//게시글리스트에서 제목 눌렀을때 페이지 이동 함수
	$(".move").on("click", function(e){
		e.preventDefault();
		actionForm.append("<input type='hidden' name='articleNO' value='"+$(this).attr("href")+"'>");
		actionForm.attr("action","${contextPath}/board/viewArticle.do");
		actionForm.submit();
	})
	
	
	
	

	
	
	
	
});
</script>

<script>
	function fn_articleForm(isLogOn, articleForm, loginForm){
		if(isLogOn!='' && isLogOn!='false'){
			location.href=articleForm;
		}else{
			alert("로그인 후 글쓰기가 가능합니다.");
			location.href=loginForm+'?action=/board/articleForm.do';
		}
	}
</script>

  

</head>
<body>
<br>
<table align="center" border="1" width="80%">
	<tr height="10" align="center" bgcolor="lightgreen">
		<td>글번호</td>
		<td>작성자</td>
		<td>제목</td>
		<td>작성일</td>
	</tr>
	
	<c:choose>
		<c:when test="${articlesList == null }">
			<tr height="10">
				<td colspan="4">
					<p align="center">
						<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
					</p>
				</td>
			</tr>
		</c:when>
		
		<c:when test="${articlesList!=null}">
			<c:forEach var="article" items="${articlesList}" varStatus="articleNum">
				<tr align="center">
					<td width="5%">${article.articleNO }</td>
					<td width="10%">${article.id }</td>
					
					<td align='left' width="35%">
						<span style="padding-right:30px"></span>
						
						<a class='cls1 move' href='<c:out value="${article.articleNO}"/>'><c:out value="${article.title}"/></a>
						
					</td>
					<td width="10%">${article.writeDate }</td>
					
				</tr>
			</c:forEach>

		</c:when>		
	</c:choose>
</table>
<br>


<!-- 페이지네이션 -->
<div id="pagination">

		<ul>
			<c:if test="${pageMaker.prev}">
				<li><a href="${pageMaker.startPage-1}">Previous</a></li>
			</c:if>
			
			<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
				<li class="paginate_button"><a class="no-underline" href="${num}">${num}</a></li>
			</c:forEach>
			
			<c:if test="${pageMaker.next }">
				<li><a href="${pageMaker.endPage+1 }">Next</a></li>
			</c:if>
		
		</ul>

</div>

<hr width="80%" color="#e9e3ed" size="1px">

<div id="board_function">
	<a class="cls1" href="javascript:fn_articleForm('${isLogOn}','${contextPath}/board/articleForm.do','${contextPath}/member/loginForm.do')"><p class="cls2">글쓰기</p></a>
</div>

<!-- 페이지 누를때 액션 처리 -->
<form id='actionForm' action="${contextPath}/board/listArticlesWithPaging.do" method="get">
	<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
	<input type="hidden" name="amount" value="${pageMaker.cri.amount}">
</form>





</body>
</html>