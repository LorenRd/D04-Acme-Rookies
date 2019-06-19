<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->

<display:table name="clasesSinNombres" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="claseSinNombre/rookie/display.do?claseSinNombreId=${row.id}"><spring:message code="claseSinNombre.display"/></a>
	</display:column>

	<!-- Attributes -->
	<spring:message code="claseSinNombre.position" var="positionTitleHeader" />
	<display:column property="position.title" title="${positionTitleHeader}"
		sortable="true" />
		
	<spring:message code="claseSinNombre.publicationMoment" var="momentHeader" />
	<display:column property="publicationMoment" title="${momentHeader}"
		sortable="true" />
		
	<spring:message code="claseSinNombre.ticker" var="tickerHeader" />
	<display:column property="ticker" title="${tickerHeader}"
		sortable="true" />
		
</display:table>
<br />
<acme:button url="claseSinNombre/rookie/create.do" code="claseSinNombre.create"/>