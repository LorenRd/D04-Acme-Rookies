<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!-- Listing grid -->

<display:table name="providers" id="row" requestURI="provider/list.do"
	pagesize="5" class="displaytag">

	<!-- Display -->
	<display:column>
		<a href="provider/display.do?providerId=${row.id}"><spring:message code="provider.display"/></a>
	</display:column>

	<!-- Attributes -->

	<spring:message code="provider.username" var="usernameHeader" />
	<display:column property="userAccount.username" title="${usernameHeader}"
		sortable="true" />

</display:table>