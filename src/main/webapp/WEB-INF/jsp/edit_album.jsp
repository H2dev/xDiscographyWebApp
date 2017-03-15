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
<c:set var="numberOfAdditionalTracks" scope="session" value="30" />

<c:set var="save">
	<spring:message code="save" />
</c:set>
<c:set var="goBack">
	<spring:message code="goBack" />
</c:set>

<body>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<div style="text-align: center;">
		<span style="font-weight: bold; font-style: italic;"><spring:message
				code="editAlbum" /></span>
	</div>
	<br>
	<form:form method="post"
		action="${pageContext.servletContext.contextPath}/updateAlbum"
		modelAttribute="album" id="editForm"
		onsubmit="return formValidation()">
		<table style="margin-left: auto; margin-right: auto;">
			<form:input type="hidden" path="id" name="id" id="idEntry"
				value="${album.id}" />
			<tr>
				<td class="label" style="margin: 5x; padding: 5px;"><spring:message
						code="form.album.title" /></td>
				<td class="albumData" style="margin: 5x; padding: 5px;"><form:input
						path="title" id="titleEntry" name="title" class="inputText"
						value="${album.title}" /></td>
			</tr>
			<tr>
				<td class="label" style="margin: 5x; padding: 5px;"><spring:message
						code="form.album.artist" /></td>
				<td class="albumData" style="margin: 5x; padding: 5px;"><form:input
						path="artist" id="artistEntry" name="artist" class="inputText"
						value="${album.artist}" /></td>
			</tr>
			<tr>
				<td class="label" style="margin: 5x; padding: 5px;"><spring:message
						code="form.album.year" /></td>
				<td class="albumData" style="margin: 5x; padding: 5px;"><form:input
						path="year" id="yearEntry" name="year" type="number"
						class="inputNumber" value="${album.year}" /></td>
			</tr>
			<c:forEach var="track" items="${album.tracklist}"
				varStatus="counter1">
				<tr>
					<c:choose>
						<c:when test="${ counter1.index != 0 }">
							<td class="label"></td>
						</c:when>
						<c:otherwise>
							<td class="label" style="margin: 5x; padding: 5px;"><spring:message
									code="form.album.tracklist" /></td>
						</c:otherwise>
					</c:choose>
					<td class="albumData" style="margin: 5x; padding: 5px;"><form:input
							path="tracklist" id="tracklistEntry" name="tracklist"
							class="inputText"
							value="${album.tracklist[counter1.index]}" /></td>
				</tr>
			</c:forEach>
			<c:forEach begin="1" end="${numberOfAdditionalTracks}"
				varStatus="counter2">
				<tr id="additionalTrackRow${counter2.index}" style="display: none;">
					<td class="label"></td>
					<td class="albumData" style="margin: 5x; padding: 5px;"><form:input
							path="tracklist" class="inputText"
							id="additionalTrack${counter2.index}" name="additionalTrack"
							value=" " /></td>
				</tr>
			</c:forEach>
		</table>
	</form:form>
	<div style="text-align: center; display: block;"
		id="addAnotherTrackButtonDiv">
		<br>
		<button onclick="addAnotherTrack();" name="addAnotherTrackButton"
			class="button" style="width: 162px; height: 30px;">
			<spring:message code="addAnotherTrack" />
		</button>
	</div>
	<br>
	<br>
	<div style="text-align: center;">
		<form style="display: inline" method="GET"
			action="${pageContext.servletContext.contextPath}/albumsUriBuilder">
			<input type="submit" class="button"
				style="width: 80px; height: 48px;" value="${ goBack }" />
		</form>
		<input type="submit" class="button" form="editForm"
			style="width: 80px; height: 48px;" value="${ save }">
	</div>

</body>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<%@include file="footer.jsp"%>

<script type="text/javascript">
	(function() {
		var inputs = document.querySelectorAll('input[id^="additionalTrack"]');
		for (var i = 0; i < inputs.length; i++) {
			inputs[i].value = '';
		}
	})();

	var counter = 1;
	var numberOfAdditionalTracks = '${numberOfAdditionalTracks}';

	function addAnotherTrack() {
		var element = document.getElementById("additionalTrackRow" + counter);
		element.style = 'display:table-row;';
		if (counter >= numberOfAdditionalTracks) {
			var buttonDiv = document.getElementById("addAnotherTrackButtonDiv");
			buttonDiv.style = 'display:none;';
		}
		counter++;
	}

	function formValidation() {
		var title = document.getElementById('titleEntry').value;
		title = title.replace(/^\s+/, '').replace(/\s+$/, '');
		if (title == '') {
			alert('Title must be entered.');
			return false;
		} else {
			return true;
		}
	}
</script>

</html>