<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />

<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<style>
#tr_file_upload {
	display: none;
}

#tr_btn_modify {
	display: none;
}	

table{
	border-collapse: collapse;
}

td{
	border:1px solid #e9e3ed;
	padding:5px;
}

<!-- 첨부파일부분 css -->
.uploadResult{
	width:100%;
	background-color:gray;
}

.uploadResult ul{
	display:flex;
	flex-flow:row;
	justify-content:center;
	align-items:center;
}
.uploadResult ul li{
	list-style:none;
	padding:10px;
	align-content: center;
	text-align:center;
}

.uploadResult ul li{
	list-style:none;
	padding:10px;
	align-content: center;
	text-align:center;
}

.uploadResult ul li img{
	width:100px;
}

.bigPictureWrapper{
	position:absolute;
	display:none;
	justify-content:center;
	align-items:center;
	top:0%;
	width:100%;
	height:100%;
	background-color:gray;
	z-index:100;
	background:rgba(255,255,255,0.5);
}

.bigPicture{
	position:relative;
	display:flex;
	justify-content:center;
	align-items:center;
}

.bigPicture img{
	width:600px;
}

</style>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<script type="text/javascript">


	$(document).ready(function(){
		(function(){
			var articleNO = '<c:out value="${article.articleNO}"/>';
			console.log(articleNO);
			
			
			$.getJSON("${contextPath}/board/getAttachList.do", {articleNO:articleNO}, function(arr){
				
				console.log(arr);
				
				var str="";
				
				$(arr).each(function(i, attach){
					
					//image type
					if(attach.fileType){
						var fileCallPath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
						
						str+="<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
						str+="<img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'>";
						str+="</div>";
						str+="</li>";
					}else{
						str+="<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
						str+="<span> " + attach.fileName+"</span><br>";
						str+="<img src='${contextPath}/resources/image/doc.jpg'>";
						str+="</div>";
						str+="</li>";
					}
				});
				
				$(".uploadResult ul").html(str);

			});
			
			
			
		})();
	});


	var operForm = $('#operForm');
	
	function backToList(obj){
		obj.action="${contextPath}/board/listArticlesWithPaging.do";
		obj.submit();
	}
	
	 function fn_enable(obj){
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 document.getElementById("i_imageFileName").disabled=false;
		 document.getElementById("tr_btn_modify").style.display="block";
		 document.getElementById("tr_file_upload").style.display="block";
		 document.getElementById("tr_btn").style.display="none";
	 }
	
	function fn_modify_article(obj){
		obj.action="${contextPath}/board/modArticle.do";
		obj.submit();
	}
	
	function fn_remove_article(url, articleNO, pageNum, amount,type,keyword){
		var form=document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", url);
		var articleNOInput = document.createElement("input");
		var pageNumInput = document.createElement("input");
		var amountInput = document.createElement("input");
		var typeInput = document.createElement("input");
		var keywordInput = document.createElement("input");
		
		articleNOInput.setAttribute("type", "hidden");
		articleNOInput.setAttribute("name", "articleNO");
		articleNOInput.setAttribute("value", articleNO);

		pageNumInput.setAttribute("type", "hidden");
		pageNumInput.setAttribute("name", "pageNum");
		pageNumInput.setAttribute("value", pageNum);
		
		amountInput.setAttribute("type", "hidden");
		amountInput.setAttribute("name", "amount");
		amountInput.setAttribute("value", amount);
		
		typeInput.setAttribute("type", "hidden");
		typeInput.setAttribute("name", "type");
		typeInput.setAttribute("value", type);
		
		keywordInput.setAttribute("type", "hidden");
		keywordInput.setAttribute("name", "keyword");
		keywordInput.setAttribute("value", keyword);			
		
		
		form.appendChild(articleNOInput);
		form.appendChild(pageNumInput);
		form.appendChild(amountInput);
		form.appendChild(typeInput);
		form.appendChild(keywordInput);
		
		document.body.appendChild(form);
		form.submit();
	}
	
	
	
	function readURL(input){
		if(input.files && input.files[0]){
			var reader = new FileReader();
			reader.onload = function(e){
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>

</head>
<body>
 
	<form name="frmArticle" method="post" action="${contextPath}"
		enctype="multipart/form-data">
		<table border="1" align="center">
			<tr>
				<td width="150" align="center" bgcolor="lightgreen">글번호</td>
				<td><input type="text" value="${article.articleNO }" disabled />
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
				</td>
			</tr>

			<tr>
				<td width="150" align="center" bgcolor="lightgreen">작성자 아이디</td>
				<td><input type="text" value="${article.id }" name="writer"
					disabled /></td>
			</tr>

			<tr>
				<td width="150" align="center" bgcolor="lightgreen">글제목</td>
				<td><input type="text" value="${article.title }" name="title"
					id="i_title" disabled /></td>
			</tr>

			<tr>
				<td width="150" align="center" bgcolor="lightgreen">내용</td>
				<td><textarea rows="20" cols="60" name="content" id="i_content"
						disabled>${article.content}</textarea></td>
			</tr>
			
			<tr>
				<td width="150" align="center" bgcolor="lightgreen">첨부파일</td>
				
				<td>
					<div class='bigPictureWrapper'>
						<div class='bigPicture'>
						
						</div>
					</div>
					
					<div class='uploadResult'>
						<ul>
						
						</ul>
					</div>
				
				</td>
			</tr>

			


			<tr>
				<td width="150" align="center" bgcolor="lightgreen">등록일자</td>
				<td><input type="text"
					value="<fmt:formatDate value="${article.writeDate }"/>" disabled />
				</td>
			</tr>

			<tr id="tr_btn_modify">
				<td colspan="2" align="center">
					<input type="button" value="수정반영하기" onClick="fn_modify_article(frmArticle)"> 
					<input type="button" value="취소" onClick="backToList(frmArticle)">
				</td>
			</tr>

			<tr id="tr_btn">
				<td colspan="2" align="center">
					<c:if test="${member.id==article.id }">
						<input type="button" value="수정하기" onClick="fn_enable(this.form)">
						<input type="button" value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do',${article.articleNO},${cri.pageNum}, ${cri.amount}, '${cri.type}', ${cri.keyword})">
					</c:if>
					<input type="button" value="리스트로 돌아기기" onClick="backToList(this.form)"> <input type="button" value="답글쓰기" onClick="fn_reply_form('${contextPath}/board/replyForm.do',${article.articleNO})">
				</td>
			</tr>
		</table>

		<!-- 게시글을 봤을 때 페이지번호 유지를 위한 부분 -->
		<input type='hidden' name='pageNum' value='<c:out value="${cri.pageNum}"/>'>
		<input type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
		<input type="hidden" name="type" value="<c:out value='${cri.type }'/>">
		<input type="hidden" name="keyword" value="<c:out value='${cri.keyword}'/>">		
	</form>
	

	
	
	
</body>
</html>