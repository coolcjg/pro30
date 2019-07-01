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
<title>글쓰기 창</title>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>



<script type="text/javascript">
	function readURL(input){
		if(input.files && input.files[0]){
			var reader = new FileReader();
			reader.onload = function(e){
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	
	function backToList(obj){
		obj.action="${contextPath}/board/listArticles.do";
		obj.submit();
	}
	
	var cnt=1;
	function fn_addFile(){
		$("#d_file").append("<br>"+"<input type='file' name='file"+cnt+"'/>");
		cnt++;
	}
	
	


</script>


<script>
$(document).ready(function(e){
	
	//x버튼 클릭 시 이벤트처리
	$(".uploadResult").on("click", "button", function(e){
		console.log("delete file");
		
		var targetFile = $(this).data("file");
		var type= $(this).data("type");
		
		var targetLi = $(this).closest("li");
		
		$.ajax({
			url:'${contextPath}/board/deleteFile.do',
			data:{fileName : targetFile, type:type},
			dataType:'text',
			type:'POST',
			success:function(result){
				alert(result);
				targetLi.remove();
			}
		});
	});
	
	
	
	//파일 input 변경시 자동 파일 업로드
	$("input[type='file']").change(function(e){
		var formData = new FormData();
		
		var inputFile = $("input[name='uploadFile']");
		
		//배열 files
		var files = inputFile[0].files;
		
		//add filedate to formdata
		for(var i=0; i<files.length; i++){
			
			//파일 확장자, 크기 확인
			if(!checkExtension(files[i].name, files[i].size)){
				return false;
			}
			
			formData.append("uploadFile", files[i]);
		}
		
		$.ajax({
			url:'${contextPath}/board/uploadAjaxAction.do',
			processData:false,
			contentType:false, 
			data:formData,
			type:'POST',
			dataType:'json',  //서버에서 받아올 데이터를 json데이터타입으로 지정
			success:function(result){
				console.log(result);
				showUploadResult(result);
			}
		});
	});
	
	
	
	
	
	//기본동작 막기
	var formObj = $("form[role='form']");
	
	$("input[type='submit']").on("click", function(e){
		e.preventDefault();
		console.log("submit clicked");
		
		var str="";
		
		$(".uploadResult ul li").each(function(i, obj){
			var jobj = $(obj);
			
			console.dir(jobj);
			
			str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
		});
		formObj.append(str).submit();
	});
	
	
});

//업로드 결과 표시
function showUploadResult(uploadResultArr){
	if(!uploadResultArr|| uploadResultArr.length ==0){return;}
	
	var uploadUL = $(".uploadResult ul");
	
	var str="";
	
	$(uploadResultArr).each(function(i, obj){
		
		//image type
		if(obj.image){
			var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
			str +="<li data-path='"+obj.uploadPath+"'";
			str +=" data-uuid='"+obj.uuid+"'";
			str +=" data-filename='"+obj.fileName+"'";
			str +=" data-type='"+obj.image+"'><div>";
			str +="<span>" + obj.fileName+"</span>";
			str +="<button type='button' data-file=\'"+fileCallPath+"\' data-type='image'><i>x</i></button><br>";
			str +="<img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'>";
			str +="</div></li>";
		}else{
			var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
			var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
			
			str +="<li data-path='"+obj.uploadPath+"'";
			str +=" data-uuid='"+obj.uuid+"'";
			str +=" data-filename='"+obj.fileName+"'";
			str +=" data-type='"+obj.image+"'><div>";
			str +="<span>" + obj.fileName+"</span>";
			str +="<button type='button' data-file=\'"+fileCallPath+"\' data-type='file'><i>x</i></button><br>";
			str +="<img src='${contextPath}/resources/image/doc.jpg'></a>";
			str +="</div></li>";
		}
	});
	uploadUL.append(str);
}








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







</script>





<style>

	table{
		border-collapse: collapse;
	}
	
	td{
		border:1px solid #e9e3ed;
		padding:5px;
	}

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



</head>
<body>

<h1 style="text-align:center">글쓰기</h1>
	<form role="form" name="articleForm" method="post" action="${contextPath}/board/addNewArticle.do" enctype="multipart/form-data" >
		
		<table border="1" align="center">
			<tr>
				<td bgcolor="lightgreen">
					작성자
				</td>
					 
				<td>
				 <input type="text" size="20" maxlength="100" value="${member.name}" readonly/>
				</td>
			</tr>
			
			<tr>
				<td bgcolor="lightgreen">
					글 제목
				</td>
				
				<td>
					<input type="text" size="67" maxlength="500" name="title"/>
				</td>			
			
			</tr>
			
			<tr>
				<td bgcolor="lightgreen">
					글 내용
				</td>
					
				<td>
					<textarea name="content" rows="10" cols="65" maxlength="2000"></textarea>
				</td>			
			
			</tr>						
		
			<tr>
				<td bgcolor="lightgreen">
				 파일 첨부
				</td>
				
				<td>
					<div class="uploadDiv">
						<input type="file" name='uploadFile' multiple>
					</div>
				
					<div class='uploadResult'>
						<ul>
						</ul>
					</div>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<input type="submit" value="글쓰기"/>
					<input type="button" value="목록보기" onClick="backToList(this.form)"/>
					<br>
				</td>
			</tr>
		</table>
	</form>
	

	 

</body>
</html>