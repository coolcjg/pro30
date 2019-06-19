<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>

<script>
$(document).ready(function(){
	
	//파일 확장자, 크기 확인
	var regex=new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 5242880; //5MB
	
	function checkExtension(fileName, fileSize){
		if(fileSize >= maxSize){
			alert("파일 사이즈 초과");
			return false;
		}
		
		if(regex.test(fileName)){
			alert("해당 종류의 파일은 업로드할 수 없습니다.");
			return false;
		}
		return true;
	}
	
	//파일업로드시 입력폼을 초기화하기 위해 미리 복사해놓음
	var cloneObj = $(".uploadDiv").clone();
	
	$("#uploadBtn").on("click", function(e){
		var formData = new FormData();
		var inputFile = $("input[name='uploadFile']");
		var files = inputFile[0].files;
		console.log(files);
		
		//add filedate to formdata
		for(var i=0; i<files.length; i++){
			
			//파일 확장자, 크기 확인
			if(!checkExtension(files[i].name, files[i].size)){
				return false;
			}
			
			formData.append("uploadFile", files[i]);
		}
		
		$.ajax({
			url:'/pro30/board/uploadAjaxAction.do',
			processData:false,
			contentType:false,
			data:formData,
			type:'POST',
			dataType:'json',
			success:function(result){
				console.log(result);
				
				showUploadedFile(result);
				
				$(".uploadDiv").html(cloneObj.html());
			}
		});
	});
	
	
	//파일업로드 결과 출력
	var uploadResult = $(".uploadResult ul");
	
	function showUploadedFile(uploadResultArr){
		var str = "";
		
		$(uploadResultArr).each(function(i, obj){
			if(!obj.image){
				
				var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
				
				str+="<li><a href='${contextPath}/board/download?fileName="+fileCallPath+"'><img src='${contextPath}/resources/image/doc.jpg'>"+obj.fileName+"</a></li>";
			}else{
								
				var fileCallPath=encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
				
				str += "<li><img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'></li>";
			}
		});
		uploadResult.append(str);
	}
	
	
	
	
});
</script>

<style>
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
	}
	
	.uploadResult ul li img{
	width:20px;
	}
</style>

</head>
<body>

<h1>Upload with Ajax</h1>

<div class='uploadDiv'>
	<input type='file' name='uploadFile' multiple>
</div>

<div class='uploadResult'>
	<ul>
	
	</ul>
</div>

<button id='uploadBtn'>Upload</button>

</body>
</html>