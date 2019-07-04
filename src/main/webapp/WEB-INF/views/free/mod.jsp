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
		
		
		(function(){
			var articleNO = '<c:out value="${article.articleNO}"/>';
			console.log(articleNO);
			
			
			$.getJSON("${contextPath}/free/getAttachList.do", {articleNO:articleNO}, function(arr){
				
				console.log(arr);
				
				var str="";
				
				$(arr).each(function(i, attach){
					
					//image type
					if(attach.fileType){
						var fileCallPath = encodeURIComponent(attach.uploadPath+"/s_"+attach.uuid+"_"+attach.fileName);
						
						str+="<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
						str+="<span>" +attach.fileName+"</span>";
						str+="<button class='btn_remove' type='button' data-file=\'"+fileCallPath+"\' data-type='image' visibility='hidden'><i>x</i></button><br>"
						str+="<img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'>";
						str+="</div>";
						str+="</li>";
					}else{
						str+="<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"' ><div>";
						str+="<span> " + attach.fileName+"</span>";
						str+="<button class='btn_remove' type='button' data-file=\'"+fileCallPath+"\' data-type='image'><i>x</i></button><br>"
						str+="<img src='${contextPath}/resources/image/doc.jpg'>";
						str+="</div>";
						str+="</li>";
					}
				});
				
				$(".uploadResult ul").html(str);

			});
			
			
			
		})();
		
		$(".uploadResult").on("click", "button", function(e){
			console.log("delete file");
			
			if(confirm("파일을 삭제하시겠습니까? ")){
				var targetLi = $(this).closest("li");
				targetLi.remove();
			}
			
			
		});
		
		
		
		
		
		function showImage(fileCallPath){
			alert(fileCallPath);
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture")
			.html("<img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'>")
			.animate({width:'100%', height:'100%'}, 1000);
		}
		
		
		$(".bigPictureWrapper").on("click", function(e){
			$('.bigPictureWrapper').hide();
		});
		
		
	});


	var operForm = $('#operForm');
	
	function backToList(obj){
		obj.action="${contextPath}/free/list.do";
		obj.submit();
	}
	
	//수정하기 눌렀을 때 폼을 활성화
	function fn_enable(obj){
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 
		 document.getElementById("tr_btn_modify").style.display="table-row";
		 
		 document.getElementById("tr_add_file").style.display="table-row";
		 
		 
		 var btn_remove = document.getElementsByClassName("btn_remove");
		 var i=0;
		 for(i=0; i<btn_remove.length; i++){
			 btn_remove[i].style.display="initial";
		 }
	 
		 	 
		 document.getElementById("tr_btn").style.display="none";
		 
		 
	 }
	
	
	//수정 반영하기 클릭할 때 반응.
	function fn_modify_article(obj){
		
		var formObj=$("form");
		
		console.log("수정 클릭");
		
		var str="";
		
		$(".uploadResult ul li").each(function(i, obj){
			var jobj = $(obj);
			console.dir(jobj);
			
			var filename = jobj.data("filename");
			var uuid = jobj.data("uuid");
			var path = jobj.data("path");
			var type = jobj.data("type");
			
			
			str+="<input type='hidden' name='attachList["+i+"].fileName' value='"+filename+"'>";
			str+="<input type='hidden' name='attachList["+i+"].uuid' value='"+uuid+"'>";
			str+="<input type='hidden' name='attachList["+i+"].uploadPath' value='"+path+"'>";
			str+="<input type='hidden' name='attachList["+i+"].fileType' value='"+type+"'>";
		});
		
		formObj.append(str);
		obj.action="${contextPath}/free/modArticle.do";
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
	
	
	//파일 확장자, 크기 확인
	var regex=new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	var maxSize = 10485760; //10MB


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
				<td><input type="text" value="${article.title }" name="title" id="i_title"  /></td>
			</tr>

			<tr>
				<td width="150" align="center" bgcolor="lightgreen">내용</td>
				<td><textarea rows="20" cols="60" name="content" id="i_content">${article.content}</textarea></td>
			</tr>
			
			
			
			
			<tr id="tr_add_file">
				<td width="150" align="center" bgcolor="lightgreen">첨부파일추가</td>
				
				<td>
					<div>

						<div class='newUploadResult'>
							<ul>
							
							</ul>
						</div>
					</div>
				</td>
			</tr>
			
			
			
			<tr>
				<td width="150" align="center" bgcolor="lightgreen">첨부파일</td>
				
				<td>
					<div class="form-group uploadDiv">
						<input type="file" name='uploadFile' multiple="multiple">
					</div>				
				
					<div class='uploadResult'>
						<ul>
						
						</ul>
					</div>
					
					<div class='bigPictureWrapper'>
						<div class='bigPicture'>
						
						</div>
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
				<td id="td_btn_modify" colspan="2" align="center">
					<input type="button" value="수정반영하기" onClick="fn_modify_article(frmArticle)"> 
					<input type="button" value="취소" onClick="backToList(frmArticle)">
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