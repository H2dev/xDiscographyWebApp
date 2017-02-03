<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<%@include file="header.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="mainTitle" /></title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/style.css" />
</head>

<c:set var="delete">
	<spring:message code="delete" />
</c:set>
<c:set var="goBack">
	<spring:message code="goBack" />
</c:set>
<c:set var="edit">
	<spring:message code="edit" />
</c:set>

<body>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>

	<table style="margin-left: auto; margin-right: auto;">
		<tr>
			<td class="label"><spring:message code="form.album.id" /></td>
			<td class="albumData"><c:out value="${album.id}" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="form.album.title" /></td>
			<td class="albumData" style="font-weight: bold"><c:out
					value="${album.title}" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="form.album.artist" /></td>
			<td class="albumData" style="font-weight: bold"><c:out
					value="${album.artist}" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="form.album.year" /></td>
			<td class="albumData" style="font-weight: bold"><c:out
					value="${album.year}" /></td>
		</tr>
		<c:forEach var="track" items="${album.tracklistJavaList}"
			varStatus="counter">
			<tr>
				<c:choose>
					<c:when test="${ counter.index != 0 }">
						<td class="label"></td>
					</c:when>
					<c:otherwise>
						<td class="label"><spring:message code="form.album.tracklist" /></td>
					</c:otherwise>
				</c:choose>
				<td class="albumData"><c:out value="${track}" /></td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<div style="text-align: center;">
		<form style="display: inline" method="GET"
			action="${pageContext.servletContext.contextPath}/album/">
			<input type="hidden" name="returningFromView" value="true">
			<input type="submit" class="button" style="width: 100px; height: 28px;"
				value="${ goBack }" />
		</form>
		<form style="display: inline" method="GET"
			action="${pageContext.servletContext.contextPath}/editAlbum/${album.id}">
			<input type="submit" class="button" style="width: 100px; height: 28px;"
				value="${ edit }" />
		</form>
		<form style="display: inline" method="POST"
			action="${pageContext.servletContext.contextPath}/albumDelete/${album.id}"
			onsubmit="return confirmDialogDelete()">
			<input type="submit" class="button" style="width: 100px; height: 28px;"
				value="${ delete }" />
		</form>
	</div>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
</body>

<%@include file="footer.jsp"%>

<script type="text/javascript">
	function confirmDialogDelete() {
		var retVal = confirm("Are you sure you want to delete this album?");
		if (retVal == true) {
			return true;
		} else {
			return false;
		}
	}
</script>

</html>