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
  
#reply_li{
	width:350px;
	padding:10px;
	list-style:none;
	
	text-align:left;
	border:1px solid #e9e3ed;
}

.btn_remove{
	display:none;
}

#tr_add_file{
	display:none;
}

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
	left:0%;
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

<!-- 댓글기능 추가관련 js -->
<script>
 console.log("Reply Module..........");
 
 var replyService= (function(){
	 
		function add(reply, callback, error) {
			console.log("add reply...............");

			$.ajax({
				type : 'post',
				url : '${contextPath}/replies/new',
				data : JSON.stringify(reply),
				contentType : "application/json; charset=utf-8",
				success : function(result, status, xhr) {
					if (callback) {
						callback(result);
					}
				},
				error : function(xhr, status, er) {
					if (error) {
						error(er);
					}
				}
			})
		}
		
		function getList(param, callback, error){
			var articleNO = param.articleNO;
			var page = param.page || 1;
			
			$.getJSON("${contextPath}/replies/pages/"+articleNO+"/"+page+".json", 
							function(data){
								if(callback){
									callback(data);
								}
					})
					.fail(function(xhr, status, err){
						if(error){
							error();
						}
					});
		}
		
		function remove(rno, callback, error){
			$.ajax({
				type:'delete',
				url:'${contextPath}/replies/'+rno,
				success : function(deleteResult, status, xhr){
					if(callback){
						callback(deleteResult);
					}
				},
				error: function(xhr, status, er){
					if(error){
						error(er);
					}
				}
			});
		}
		
		function update(reply, callback, error){
			console.log("RNO : " + reply.rno);
			
			$.ajax({
				type : 'put',
				url : '${contextPath}/replies/'+reply.rno,
				data : JSON.stringify(reply),
				contentType:"application/json; charset=utf-8",
				success:function(result, status, xhr){
					if(callback){
						callback(result);
					}
				},
				error: function(xhr, status, er){
					if(error){
						error(er);
					}
				}
				
			});
		}
		
		function get(rno, callback, error){
			$.get(
					"${contextPath}/replies/"+rno +".json", 
					
					function(result){
						if(callback){
							callback(result);
						}
					})
				.fail(function(xhr, status, err){
					if(error){
						error();
					}
				});
			}
		
		function displayTime(timeValue){
			var today = new Date();
			
			var gap = today.getTime() - timeValue;
			
			var dateObj = new Date(timeValue);
			var str = "";
			
			if(gap <(1000*60*60*24)){
				var hh = dateObj.getHours();
				var mi = dateObj.getMinutes();
				var ss = dateObj.getSeconds();
				
				return [(hh>9?'':'0')+hh, ':', (mi >9?'':'0')+mi, ':', (ss >9?'':'0')+ss].join('');
			}else{
				var yy = dateObj.getFullYear();
				var mm = dateObj.getMonth()+1;
				var dd = dateObj.getDate();
				
				return [yy,'/', (mm>9?'':'0')+mm, '/', (dd>9?'':'0')+dd].join('');
			}
		};
		
		
		 return {
			 add:add,
			 getList:getList,
			 remove:remove,
			 update : update,
			 get:get,
			 displayTime:displayTime
		};
 })();
 
 console.log(replyService);
</script>

<script>
/*
console.log("========================");
console.log("JS TEST");

var articleNO = '<c:out value="${article.articleNO}"/>';

//for replyService add test
replyService.add(
		{reply:"JS Test", id:"hong", articleNO:articleNO},
		function(result){
			alert("RESULT : " + result);
		}
);

replyService.getList(
		{articleNO:articleNO, page:1},
		
		function(list){
			for(var i =0, len = list.length ||0; i<len; i++){
				console.log(list[i]);
			}
		}
		
);

replyService.remove(
		23,
		
		function(count){
			console.log(count);
			if(count ==="success"){
				alert("REMOVED");
			}
		},
		
		function(err){
			alert("ERROR.....");
		}
		
);

replyService.update(
		{rno:22, articleNO:articleNO, reply:"Modified Reply......"},
		
		function(result){
			alert("수정 완료....");
		}
);


replyService.get(5, function(data){
	console.log(data);
});
*/


</script>



<script type="text/javascript">


	$(document).ready(function(){
		
		
		console.log("333");

			
		//리플 수정완료버튼 눌렀을 때 처리.
		$(document).on("click", ".rep_mod_sub", function(e){
			
			var rno = $(this).closest("li").data("rno");
			var new_content = $("#new_reply_content").val();
			
			var reply = {rno:rno, reply:new_content};
			
			replyService.update(reply, function(result){
				alert(result);
				showList(1);
			});
		});
		
		
		//댓글 삭제
		$(document).on("click", ".repDel", function(e){
			
			var rno = $(this).closest("li").data("rno");
			
			console.log(rno);
			
			replyService.remove(rno, function(result){
				alert(result);
				showList(1);
			});
			
		})
		


		       
		//댓글수정하기위해 폼 변경
		$(document).on("click", ".repMod", function(e){
			var form=$(this).closest("li");
			var rno = $(this).closest("li").data("rno");
			console.log(rno);

						
			var content = $(this).next().next().next().next().next().data("reply");

			
			
			var rep_mod_input = document.createElement("input");
			    
			rep_mod_input.setAttribute("id", "new_reply_content");
			rep_mod_input.setAttribute("type", "text");
			rep_mod_input.setAttribute("name", "mod_rep");
			rep_mod_input.setAttribute("value", content);
			
			var rep_mod_sub = document.createElement("button");
			rep_mod_sub.setAttribute("type", "button");
			rep_mod_sub.setAttribute("class", "rep_mod_sub");
			rep_mod_sub.innerHTML="댓글수정";

			form.append(rep_mod_input);
			form.append(rep_mod_sub);
			
			$(this).next().next().next().next().next().remove();
			$(this).remove();
		});	
		



				
		
		var articleNO = '<c:out value="${article.articleNO}"/>';
		var replyUL = $(".chat");
		var reply_reg_button = $("#reply_reg_btn");
		
	

		//댓글등록
		reply_reg_button.on("click", function(e){
			
			var reply = $("#reply_content").val();
			var id = '${member.id}';
			var articleNO = ${article.articleNO};
			
			var reply = {
					reply:reply,
					id : id,
					articleNO : articleNO
			};
			
			replyService.add(reply,function(result){
				alert(result);
				showList(1);
				$("#reply_content").val("");
			})
		});
		
		
		//처음 로딩할 때 리플 가져오기		
		showList(1);
				
		function showList(page){
			replyService.getList({articleNO:articleNO, page : page||1}, function(list){
				var str="";
				if(list==null|| list.length==0){
					replyUL.html("");
					return;
				}
				for(var i=0, len=list.length || 0; i<len; i++){
					
					//현재 로그인한 아이디
					var loginId = '${member.id}';
					
					//댓글 아이디
					var repId = list[i].id;
					
					
					str+="<li data-rno='"+list[i].rno+"' id='reply_li'>";
					str+="<strong>"+list[i].id+"</strong>";
					str+="&nbsp;";
					
					//로그인 아이디와 댓글의 아이디가 같을 경우 수정,삭제버튼 활성화.
					if(loginId == repId){
						str+="<a class='repMod' href='#'>수정</a> <a class='repDel' href='#'>삭제</a><br>";
					}else{
						str+="";
					}
					
					str+="<small>"+replyService.displayTime(list[i].replyDate)+"</small><br>";
					str+="&nbsp;";
					str+="<p data-reply='"+list[i].reply+"'>"+list[i].reply+"</p>";
					str+="</li>";
					str+="<br>"
				}
				
				replyUL.html(str);
				
			});
			
		}
		
		
		//첨부파일 리스트 등록
		(function(){
			
			console.log(articleNO);
			
			
			$.getJSON("${contextPath}/board/getAttachList.do", {articleNO:articleNO}, function(arr){
				
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
				
		
		
		$(".uploadResult").on("click","li", function(e){
			console.log("view image");
			
			var liObj = $(this);
			
			var path = encodeURIComponent(liObj.data("path")+"/" + liObj.data("uuid")+"_"+liObj.data("filename"));
			
			if(liObj.data("type")){
				showImage(path.replace(new RegExp(/\\/g),"/"));
			}else{
				self.location="${contextPath}/board/download.do?fileName="+path
			}
			
		});
		
		function showImage(fileCallPath){
			alert(fileCallPath);
			$(".bigPictureWrapper").css("display", "flex").show();
			
			$(".bigPicture")
			.html("<img src='${contextPath}/board/display.do?fileName="+fileCallPath+"'>")
			;
		}
		
		
		$(".bigPictureWrapper").on("click", function(e){
			$('.bigPictureWrapper').hide();
		});
	});
	
	
	
	


	


	var operForm = $('#operForm');
	
	function backToList(obj){
		obj.action="${contextPath}/board/listArticlesWithPaging.do";
		obj.submit();
	}
	
	function fn_mod(obj){
		obj.method="get";
		obj.action="${contextPath}/board/modArticleForm.do";
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
			

			<tr id="tr_add_file">
				<td width="150" align="center" bgcolor="lightgreen">첨부파일추가</td>
				
				<td>
					<div>
						<div class="form-group uploadDiv">
							<input type="file" name='uploadFile' multiple="multiple">
						</div>
						
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
			
			<tr>
				<td width="150" align="center" bgcolor="lightgreen">댓글</td>
				
				<td>
					<div>
						<ul class="chat">

						</ul>
					</div>
					
					<hr>

					<div>
						<div>
							<textarea rows="5" cols="40" id="reply_content"></textarea>
							<br>
							<button id='reply_reg_btn' type='button'>댓글등록</button>
						</div>
					</div>
				</td>
			</tr>

			<tr id="tr_btn_modify">
				<td id="td_btn_modify" colspan="2" align="center">
					<input type="button" value="수정반영하기" onClick="fn_modify_article(frmArticle)"> 
					<input type="button" value="취소" onClick="backToList(frmArticle)">
				</td>
			</tr>

			<tr id="tr_btn">
				<td colspan="2" align="center">
					<c:if test="${member.id==article.id }">
						<!-- 
						<input type="button" value="수정하기" onClick="fn_enable(this.form)">
						 -->
						 
						<input type="button" value="수정하기" onClick="fn_mod(this.form)">
						<input type="button" value="삭제하기" onClick="fn_remove_article('${contextPath}/board/remove.do',${article.articleNO},${cri.pageNum}, ${cri.amount}, '${cri.type}', ${cri.keyword})">
					</c:if>
					<input type="button" value="리스트로 돌아기기" onClick="backToList(this.form)">
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