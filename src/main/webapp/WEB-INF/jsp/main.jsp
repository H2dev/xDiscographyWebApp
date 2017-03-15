<%@page import="hr.razv.h2.discography.model.Album"%>
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
<spring:url value="/resources/pictures" var="pics" />
</head>

<c:set var="fillDb">
	<spring:message code="fillDb" />
</c:set>
<c:set var="filterAlbums">
	<spring:message code="filterAlbums" />
</c:set>
<c:set var="clearFilters">
	<spring:message code="clearFilters" />
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
<c:set var="artist_field">
	<%=Album.ARTIST_FIELD%>
</c:set>
<c:set var="title_field">
	<%=Album.TITLE_FIELD%>
</c:set>
<c:set var="year_field">
	<%=Album.YEAR_FIELD%>
</c:set>

<body>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<div style="text-align: center;">
		<c:choose>
			<c:when test="${albumDbEntriesCount < 1}">
				<br>
				<p>
					<spring:message code="noAlbums" />
				</p>
				<p>
					<spring:message code="mockData" />
				</p>
				<br>
				<form method="POST"
					action="${pageContext.servletContext.contextPath}/fillInWithMockData">
					<input type="submit" class="button"
						style="width: 180px; height: 35px;" value="${ fillDb }" />
				</form>
				<br>
				<br>
				<br>
				<br>
			</c:when>
			<c:otherwise>
				<table style="margin-left: auto; margin-right: auto;">
					<form:form method="get" name="filtersForm" id="filtersForm"
						modelAttribute="filteringCriteria"
						action="${pageContext.servletContext.contextPath}/albumsUriBuilder">
						<input type="hidden" id="page" name="page" value="0">
						<tr style="vertical-align: top; height: 50px;">
							<td class="filterLabel"><spring:message
									code="form.album.title" /></td>
							<td class="filter"><form:input class="filterInputText"
									type="text" id="titleFilterInput" path="title"
									value="${ param.title }" /></td>
							<td style="width: 0px;"></td>
							<td class="filterLabel"><spring:message
									code="form.album.year" /></td>
							<td class="filter"><form:input class="filterInputNumber"
									type="number" id="yearFilterInput" path="year"
									value="${ param.year }" /></td>
							<td style="width: 17px;"></td>
							<td class="filter">
								<button type="submit" class="buttonFilter">${ filterAlbums }</button>
							</td>
						</tr>
						<tr style="vertical-align: top; height: 50px;">
							<td class="filterLabel"><spring:message
									code="form.album.artist" /></td>
							<td class="filter"><form:input class="filterInputText"
									type="text" id="artistFilterInput" path="artist"
									value="${ param.artist }" /></td>
							<td style="width: 0px;"></td>
							<td class="filterLabel"><spring:message
									code="form.album.track" /></td>
							<td class="filter"><form:input class="filterInputText"
									type="text" id="trackFilterInput" path="track"
									value="${ param.track }" /></td>
							<td style="width: 17px;"></td>
							<td class="filter">
								<button class="buttonFilter" onclick="clearFilterInputs()">${ clearFilters }</button>
							</td>
						</tr>
						<form:input type="hidden" id="orderByInput" name="orderByInput"
							path="orderBy" value="${ param.orderBy }" />
						<form:input type="hidden" id="sortAscInput" name="sortAscInput"
							path="sortAsc" value="${ param.sortAsc }" />
					</form:form>
					<tr>
						<td style="height: 0px;"></td>
					</tr>
				</table>
				<c:choose>
					<c:when test="${empty album_list}">
						<br>
						<p>
							<spring:message code="noMatchingResults" />
						</p>
						<br>
						<br>
						<br>
					</c:when>
					<c:otherwise>
						<table style="margin-left: auto; margin-right: auto;">
							<tr style="vertical-align: middle; height: 45px;">
								<td colspan="8" style="font-style: italic;"><spring:message
										code="listAlbums" /></td>
							</tr>
							<tr
								style="vertical-align: bottom; height: 35px; font-weight: bold;">
								<td class="titles"><spring:message code="titleAlbum" /></td>
								<td class="titles"><spring:message code="artistAlbum" /></td>
								<td class="titles"><spring:message code="yearAlbum" /></td>
							</tr>
							<tr style="vertical-align: top; height: 25px;">
								<td class="arrows"><c:choose>
										<c:when
											test="${ param.orderBy != title_field || ( param.sortAsc != 'true' && param.sortAsc != 'false' ) }">
											<input type="image" src="${pics}/arrow-black-up.png"
												onclick="setOrderByParam('${title_field}', 'true')" />
											<input type="image" src="${pics}/arrow-black-down.png"
												onclick="setOrderByParam('${title_field}', 'false')" />
										</c:when>
										<c:otherwise>
											<c:if test="${ param.sortAsc == 'false' }">
												<input type="image" src="${pics}/arrow-black-up.png"
													onclick="setOrderByParam('${title_field}', 'true')" />
												<input type="image" src="${pics}/arrow-orange-down.png" />
											</c:if>
											<c:if test="${ param.sortAsc == 'true' }">
												<input type="image" src="${pics}/arrow-orange-up.png" />
												<input type="image" src="${pics}/arrow-black-down.png"
													onclick="setOrderByParam('${title_field}', 'false')" />
											</c:if>
										</c:otherwise>
									</c:choose></td>
								<td class="arrows"><c:choose>
										<c:when
											test="${ param.orderBy != artist_field || ( param.sortAsc != 'true' && param.sortAsc != 'false' ) }">
											<input type="image" src="${pics}/arrow-black-up.png"
												onclick="setOrderByParam('${artist_field}', 'true')" />
											<input type="image" src="${pics}/arrow-black-down.png"
												onclick="setOrderByParam('${artist_field}', 'false')" />
										</c:when>
										<c:otherwise>
											<c:if test="${ param.sortAsc == 'false' }">
												<input type="image" src="${pics}/arrow-black-up.png"
													onclick="setOrderByParam('${artist_field}', 'true')" />
												<input type="image" src="${pics}/arrow-orange-down.png" />
											</c:if>
											<c:if test="${ param.sortAsc == 'true' }">
												<input type="image" src="${pics}/arrow-orange-up.png" />
												<input type="image" src="${pics}/arrow-black-down.png"
													onclick="setOrderByParam('${artist_field}', 'false')" />
											</c:if>
										</c:otherwise>
									</c:choose></td>
								<td class="arrows"><c:choose>
										<c:when
											test="${ param.orderBy != year_field || ( param.sortAsc != 'true' && param.sortAsc != 'false' ) }">
											<input type="image" src="${pics}/arrow-black-up.png"
												onclick="setOrderByParam('${year_field}', 'true')" />
											<input type="image" src="${pics}/arrow-black-down.png"
												onclick="setOrderByParam('${year_field}', 'false')" />
										</c:when>
										<c:otherwise>
											<c:if test="${ param.sortAsc == 'false' }">
												<input type="image" src="${pics}/arrow-black-up.png"
													onclick="setOrderByParam('${year_field}', 'true')" />
												<input type="image" src="${pics}/arrow-orange-down.png" />
											</c:if>
											<c:if test="${ param.sortAsc == 'true' }">
												<input type="image" src="${pics}/arrow-orange-up.png" />
												<input type="image" src="${pics}/arrow-black-down.png"
													onclick="setOrderByParam('${year_field}', 'false')" />
											</c:if>
										</c:otherwise>
									</c:choose></td>
							</tr>
							<c:forEach var="album" items="${album_list}">
								<tr>
									<td class="Padding8px"><c:out value="${album.title}" /></td>
									<td class="Padding8px"><c:out value="${album.artist}" /></td>
									<td class="Padding8px"><c:out value="${album.year}" /></td>
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
			</c:otherwise>
		</c:choose>
		<c:if test="${not empty album_list}">
			<br>
			<div style="text-align: center; font-weight: bold">
				<c:if test="${not empty album_list}">
					<span><spring:message code="page" /> ${ currentPage } / ${ maxNumberOfPages }
					</span>
					<br>
				</c:if>
				<br>
				<c:if test="${ currentPage > 1 }">
					<button class="link" onclick="pageFuction(${currentPage - 1})">
						<spring:message code="previousPage" />
					</button>
				</c:if>
				<c:if test="${ currentPage < maxNumberOfPages }">
					<button class="link" onclick="pageFuction(${currentPage + 1})">
						<spring:message code="nextPage" />
					</button>
				</c:if>
				<br>
			</div>
			<br>
			<br>
		</c:if>
		<div style="text-align: center;">
			<form:form style="display: inline" method="get"
				action="${pageContext.servletContext.contextPath}/addNewAlbum">
				<button type="submit" class="button"
					style="width: 200px; height: 35px;">
					<spring:message code="addNewAlbum" />
				</button>
			</form:form>
			<c:if test="${albumDbEntriesCount > 0}">
				<form:form style="display: inline" method="post"
					action="${pageContext.servletContext.contextPath}/deleteAllAlbums"
					onsubmit="return confirmDialogDeleteAll()">
					<button type="submit" class="button"
						style="width: 200px; height: 35px;">
						<spring:message code="deleteAll" />
					</button>
				</form:form>
			</c:if>
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

	function confirmDialogDeleteAll() {
		var retVal = confirm("Are you sure you want to delete all albums?");
		if (retVal == true) {
			return true;
		} else {
			return false;
		}
	}
	
	function pageFuction(page) {
		document.forms["filtersForm"].elements["page"].value =	page;
		document.forms["filtersForm"].submit(); 
	}
	
	function clearFilterInputs(link) {
		var form = document.forms["filtersForm"];
		var textInputs = document.querySelectorAll('input[type=text]');
		for (var i = 0; i < textInputs.length; i++) {
			textInputs[i].value = "";
		}
		form.elements["page"].value = 0;
		form.elements["yearFilterInput"].value = "";
		form.elements["orderByInput"].value = "";
		form.elements["sortAscInput"].value = "";
		form.submit(); 
		
		return true;
	}
	
	function setOrderByParam(orderBy, sortAsc) {
		document.forms["filtersForm"].elements["orderByInput"].value = orderBy;
		document.forms["filtersForm"].elements["sortAscInput"].value = sortAsc;
		document.forms["filtersForm"].submit(); 
	}
	
</script>
</html>