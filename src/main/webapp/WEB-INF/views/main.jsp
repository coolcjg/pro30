<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%
	request.setCharacterEncoding("UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>


<style>


		table{
			border-collapse: collapse;
			
			border:1px solid #e9e3ed;
		}
		    
		td{
			border-top:1px solid #e9e3ed ;
			border-right:1px dotted #e9e3ed;	
			padding:5px;
		}
		
		#td_title{
			width:250px; 
			text-align:left
		}
		
		#td_name{
			width:70px; 
			text-align:left
		}
		
		#td_date{
			width:70px; 
			text-align:center;
		}
		
		#board_recent{
			box-sizing:border-box; 
			float:left; 
			background:white; 
			width:490px; 
			height:250px;  
			margin:10px; 
			border:1px solid #e9e3ed; 
			padding=15px;
		}
		
		#gallery_recent{
			box-sizing:border-box;
			clear:both; 
			background:white; 
			width:1000px; 
			height:500px;  
			margin:10px; 
			border:1px solid #e9e3ed;
		}
		
		#gallery_outline{
			box-sizing:border-box; 
			border:1px solid #e9e3ed; 
			padding:10px; 
			width:230px; 
			display:inline-block;
		}
		
		#img_outline{
			box-sizing:border-box; 
			width:210px; 
			height:210px;
		}

</style>
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>

	$(document).ready(function(){
		

	})

</script>


</head>
<body>
	<div style="padding:20px;" >
	
		<div id="board_recent">
			
			<p>자유게시판</p>
			 
			<table style="width:420px; margin:0 auto;">
				<c:choose >
					<c:when test="${freeList == null }">
						<tr height="10">
							<td colspan="4">
								<p align="center">
									<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
								</p>
							</td>
						</tr>
					
					</c:when>
					
					<c:when test="${freeList!=null }">
						<c:forEach var="article" items="${freeList}" varStatus="articleNum">
							<tr>
								<td id="td_title"><a class='move' href='${contextPath}/free/view.do?articleNO=${article.articleNO}'>${article.title}</a></td>
								<td id="td_name">${article.id }</td>
								<td id="td_date" >
									<script>
										var writeDate = "${article.writeDate}";
										var end = writeDate.length;
										var start = writeDate.indexOf("-");
										var newDateForm = writeDate.substring(start+1,end);
										document.write(newDateForm);							
									</script>
								</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</table>
		
		</div>
	
		<div id="board_recent">
		
			<p>여행정보</p>
			 
			<table style="width:420px; margin:0 auto;">
				<c:choose >
					<c:when test="${tripList == null }">
						<tr height="10">
							<td colspan="4">
								<p align="center">
									<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
								</p>
							</td>
						</tr>
					
					</c:when>
					
					<c:when test="${tripList!=null }">
						<c:forEach var="article" items="${tripList}" varStatus="articleNum">
							<tr>
								<td id="td_title"><a class='move' href='${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}'>${article.title}</a></td>
								<td id="td_name">${article.id }</td>
								<td id="td_date" >
									<script>
										var writeDate = "${article.writeDate}";
										var end = writeDate.length;
										var start = writeDate.indexOf("-");
										var newDateForm = writeDate.substring(start+1,end);
										document.write(newDateForm);							
									</script>
								</td>
							</tr>
						</c:forEach>
					</c:when>
				</c:choose>
			</table>
		
		</div>
		

		<div id="gallery_recent">

			<p>갤러리</p>

			<c:choose>
				<c:when test="${galleryList == null }">
					<p align="center">
						<b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
					</p>
				</c:when>
		
				<c:when test="${galleryList!=null}">
					<c:forEach var="article" items="${galleryList}" varStatus="articleNum">
						
						<div id="gallery_outline">
							<div id="img_outline">
								<img src='${contextPath}/gallery/displaythumb.do?articleNO=${article.articleNO}' style="width:210px; height:210px">
							</div>
							
							<div style="box-sizing:border-box; width:210px; text-align:left">
								<a class='cls1 move' href='${contextPath}/gallery/view.do?articleNO=${article.articleNO}'>${article.title}</a>
							</div>
							
							<div style="box-sizing:border-box; width:210px; text-align:left ">
								${article.id }
							</div>
							
							<div style="width:300px; text-align:left ">
									<script>
										var writeDate = "${article.writeDate}";
										var end = writeDate.length;
										var start = writeDate.indexOf("-");
										var newDateForm = writeDate.substring(start+1,end);
										document.write(newDateForm);							
									</script>
							</div>
						</div>			
		
					</c:forEach>
				</c:when>		
			</c:choose>			
		
		</div>
		
		
	
	
	
	</div>
	
	
</body>
</html>