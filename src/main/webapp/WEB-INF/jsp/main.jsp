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

<c:set var="fillDb">
	<spring:message code="fillDb" />
</c:set>
<c:set var="view">
	<spring:message code="view" />
</c:set>
<c:set var="edit">
	<spring:message code="edit" />
</c:set>
<c:set var="delete">
	<spring:message code="delete" />
</c:set>
<c:set var="json">
	<spring:message code="json" />
</c:set>
<c:set var="json">
	<spring:message code="json" />
</c:set>
<c:set var="addNewAlbum">
	<spring:message code="addNewAlbum" />
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
	<div style="text-align: center;">
		<form method="GET"
			action="${pageContext.servletContext.contextPath}/addNewAlbum">
			<input type="submit" class="button"
				style="width: 180px; height: 35px;" value="${ addNewAlbum }" />
		</form>
		<br> <br> <br> <span
			style="font-weight: bold; font-style: italic;"> <spring:message
				code="listAlbums" />
		</span> <br> <br> <br>
		<c:choose>
			<c:when test="${empty album_list}">
				<p>
					<spring:message code="noAlbums" />
				</p>
				<p>
					<spring:message code="mockData" />
				</p>
				<form method="POST"
					action="${pageContext.servletContext.contextPath}/fillInWithMockData">
					<input type="submit" class="button"
						style="width: 180px; height: 35px;" value="${ fillDb }" />
				</form>
			</c:when>
			<c:otherwise>
				<table style="margin-left: auto; margin-right: auto;">
					<tr style="vertical-align: top; height: 50px; font-weight: bold;">
						<td><spring:message code="titleAlbum" /></td>
						<td><spring:message code="artistAlbum" /></td>
						<td><spring:message code="yearAlbum" /></td>
					</tr>
					<c:forEach var="album" items="${album_list}">
						<tr>
							<td><c:out value="${album.title}" /></td>
							<td><c:out value="${album.artist}" /></td>
							<td><c:out value="${album.year}" /></td>
							<td>&nbsp;&nbsp;</td>
							<td class="noPadding">
								<form method="GET"
									action="${pageContext.servletContext.contextPath}/album/${album.id}">
									<input type="submit" class="button"
										style="height: 20px; font-size: 14px;" value="${ view }" />
								</form>
							</td>
							<td class="noPadding">
								<form method="GET"
									action="${pageContext.servletContext.contextPath}/editAlbum/${album.id}">
									<input type="submit" class="button"
										style="height: 20px; font-size: 14px;" value="${ edit }" />
								</form>
							</td>
							<td class="noPadding">
								<form method="GET"
									action="${pageContext.servletContext.contextPath}/albumAsJson/${album.id}">
									<input type="submit" class="button"
										style="height: 20px; font-size: 14px;" value="${ json }" />
								</form>
							</td>
							<td class="noPadding"><form:form method="POST"
									action="${pageContext.servletContext.contextPath}/albumDelete/${album.id}"
									onsubmit="return confirmDialogDelete()">
									<button type="submit" class="button"
										style="height: 20px; font-size: 14px;">
										<spring:message code="delete" />
									</button>
								</form:form></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
		<br>
		<div style="text-align: center; font-weight: bold">
			<c:if test="${not empty album_list}">
				<span><spring:message code="page" /> ${ currentPage } / ${ maxNumberOfPages } </span>
				<br>
			</c:if>
			<br>
			<c:if test="${ currentPage > 1 }">
				<a
					href="${pageContext.servletContext.contextPath}/album?page=${currentPage - 1}"
					style="color: #6f6f6f"><spring:message code="previousPage" /></a>
			</c:if>

			<c:if test="${ currentPage < maxNumberOfPages }">
				<a
					href="${pageContext.servletContext.contextPath}/album?page=${currentPage + 1}"
					style="color: #6f6f6f"><spring:message code="nextPage" /></a>
			</c:if>
			<br>
		</div>
		<br><br>
		<div style="text-align: center;">
			<form:form style="display: inline" method="get"
				action="${pageContext.servletContext.contextPath}/album">
				<button type="submit" class="button"
					style="width: 200px; height: 35px;">
					<spring:message code="refresh" />
				</button>
			</form:form>
			<form:form style="display: inline" method="post"
				action="${pageContext.servletContext.contextPath}/deleteAllAlbums"
				onsubmit="return confirmDialogDeleteAll()">
				<button type="submit" class="button"
					style="width: 200px; height: 35px;">
					<spring:message code="deleteAll" />
				</button>
			</form:form>
		</div>
		<br> <br> <br> <br> <br> <br>
	</div>

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

<script type="text/javascript">
	function confirmDialogDeleteAll() {
		var retVal = confirm("Are you sure you want to delete all albums?");
		if (retVal == true) {
			return true;
		} else {
			return false;
		}
	}
</script>
</html>