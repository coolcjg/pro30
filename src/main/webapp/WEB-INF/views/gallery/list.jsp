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
console.log("111");

$(document).ready(function(){
	
	//페이징처리
	var actionForm = $("#actionForm");
	
	$(".paginate_button a").on("click", function(e){
		e.preventDefault();
		console.log('click');
		actionForm.find("input[name='pageNum']").val($(this).attr("href"));
		actionForm.submit();
	});
	
	
	//검색기능
	var searchForm = $("#searchForm");

	$("#searchForm button").on("click", function(e){
		if(!searchForm.find("option:selected").val()){
			alert("검색 종류를 선택하세요");
			return false;
		}

		if(!searchForm.find("input[name='keyword']").val()){
			alert("키워드를 입력하세요");
			return false;
		}
		searchForm.find("input[name='pageNum']").val("1");
		e.preventDefault();
		searchForm.submit();
	});
	
	
	

	
});

	


function fn_writeForm(isLogOn, galleryForm, loginForm){
	if(isLogOn!='' && isLogOn!='false'){
		location.href=galleryForm;
	}else{
		alert("로그인 후 글쓰기가 가능합니다.");
		location.href=loginForm+'?action=/gallery/galleryForm.do';
	}
}
</script>


<style>

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

</style>


</head>
<body>

<div>
<h3>갤러리</h3>
</div>

<div>

	 
	<c:choose>
		<c:when test="${galleryList == null }">
				<div style="border:1px solid #e9e3ed; padding:10px; width:305px">
					<div style="width:250px; height:250px; border:1px solid green" >
						<img src="${contextPath}/resources/image/gallery.jpg" style="width:250px; height:250px">
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
		
		<c:when test="${galleryList!=null}">
			<c:forEach var="article" items="${galleryList}" varStatus="articleNum">
				
				<div style="border:1px solid #e9e3ed; padding:10px; width:255px; display:inline-block">
					<div style="width:250px; height:250px; border:1px solid #e9e3ed" >
						
						<img src='${contextPath}/gallery/displaythumb.do?articleNO=${article.articleNO}' style="width:250px; height:250px">
						
					</div>
					
					<div style="width:300px; text-align:left">
						${article.articleNO}
					</div>
					
					<div style="width:300px; text-align:left">
						<a class='cls1 move' href='${contextPath}/gallery/view.do?articleNO=${article.articleNO}'>${article.title}</a>
					</div>
					
					<div style="width:300px; text-align:left ">
						${article.id }
					</div>
					
					<div style="width:300px; text-align:left ">
						${article.writeDate }
					</div>
				</div>			

			</c:forEach>
		</c:when>		
	</c:choose>

</div>

<br>
<br>
<!-- 페이지네이션 -->
<div id="pagination">

		<ul>
			<c:if test="${pageMaker.prev}">
				<li><a href="${pageMaker.startPage-1}">Previous</a></li>
			</c:if>
			
			<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
				<li class="paginate_button ${pageMaker.cri.pageNum==num ? "active":""}"><a class="no-underline" href="${num}">${num}</a></li>
			</c:forEach>
			
			<c:if test="${pageMaker.next }">
				<li><a href="${pageMaker.endPage+1 }">Next</a></li>
			</c:if>
		
		</ul>

</div>

<!--  검색처리 -->
<div>
	<form id='searchForm' action="${contextPath}/gallery/list.do" method="get">
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

<!-- 페이지 누를때 액션 처리 -->
<form id='actionForm' action="${contextPath}/gallery/list.do" method="get">
	<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
	<input type="hidden" name="amount" value="${pageMaker.cri.amount}">
	<input type="hidden" name="type" value="<c:out value='${pageMaker.cri.type }'/>">
	<input type="hidden" name="keyword" value="<c:out value='${pageMaker.cri.keyword}'/>">
</form>

</body>
</html>