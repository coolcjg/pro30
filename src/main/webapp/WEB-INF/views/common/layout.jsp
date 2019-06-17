<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<style>
	#container{
		width:1300px;
		margin:0px auto;
		text-align:center;
		border:0px solid #e9e3ed;
	}
	
	#header{
		padding:5px;
		margin-bottom:5px;
		border:0;
		
	}
	    
	#sidebar-left{
		width:200px;
		
		padding:5px;
		margin-right:5px;
		margin-bottom : 5px;
		float:left;
		background-color:#f8f8f8;
		border:1px solid #e9e3ed;
		font-size:10px;
	}
	#content{
		width:1071px;
		padding:5px;
		float:left;
		margin-bottom:5px;
		background-color:#f8f8f8;
		border:1px solid #e9e3ed;
	}
	#footer{
		width:1288px;
		clear:both;
		padding:5px;
		border:1px solid #e9e3ed;
		background-color:#f8f8f8;
	}
	.no-underline{
			text-decoration:none;
		}

 
</style>



<title><tiles:insertAttribute name="title"/></title>
</head>
<body>
	<div id="container">
		<div id="header">
			<tiles:insertAttribute name="header"/>
		</div>
		<div id="sidebar-left">
			<tiles:insertAttribute name="side"/>
		</div>
		
		<div id="content">
			<tiles:insertAttribute name="body"/>
		</div>
		 
	
		<div id="footer">
			<tiles:insertAttribute name="footer"/>
		</div>

	</div>
</body>
</html>