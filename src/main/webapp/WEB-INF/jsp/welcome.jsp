<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="mainTitle" /></title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/style.css" />
</head>

<c:set var="clickToContinue" > 
	<spring:message code="clickToContinue" />
</c:set>

<body class="home">
	<br><br><br><br><br><br><br>
	<div style="text-align: center;">
		<span class="biggerGreyLetters"><spring:message code="welcome" /></span> 
		<br><br><br><br>
		<span class="biggerGreyLetters"><spring:message code="mainDesc" /></span><br><br>
		<p><spring:message code="desc2" /></p>
		<p><spring:message code="desc3" /></p>
		<p><spring:message code="desc4" /></p>
	</div>
	
	<br>
	<br>
	<br>
	<div style="text-align: center;">
		<form method="GET"
			action="${pageContext.servletContext.contextPath}/album">
			<input type="submit" class="button" style="width: 180px; height: 35px;"
				value="${ clickToContinue }" />
		</form>
	</div>
</body>

<%@include file="footer.jsp"%>

</html>