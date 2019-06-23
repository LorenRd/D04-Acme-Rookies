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

<display:table name="claseSinNombres" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="claseSinNombre/display.do?claseSinNombreId=${row.id}"><spring:message code="claseSinNombre.display"/></a>
	</display:column>

	<!-- Attributes -->
	<spring:message code="claseSinNombre.position" var="positionTitleHeader" />
	<display:column property="position.title" title="${positionTitleHeader}"
		sortable="true" />
		
	<jstl:if test="${cookie['language'].getValue()=='es'}">
		<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    	<display:column property="publicationMoment" format="{0,date, dd-MM-yy HH:mm}" title="${publicationMomentHeader}" />
	</jstl:if>
	<jstl:if test="${cookie['language'].getValue()=='en'}">
		<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    	<display:column property="publicationMoment" format="{0,date, yy/MM/dd HH:mm}" title="${publicationMomentHeader}" />
	</jstl:if>
		
	<spring:message code="claseSinNombre.ticker" var="tickerHeader" />
	<display:column property="ticker" title="${tickerHeader}"
		sortable="true" />
		
</display:table>