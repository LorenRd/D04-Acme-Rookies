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

<display:table name="audits" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="audit/auditor/display.do?auditId=${row.id}"><spring:message code="audit.display"/></a>
	</display:column>

	<!-- Attributes -->
	<spring:message code="audit.position" var="positionTitleHeader" />
	<display:column property="position.title" title="${positionTitleHeader}"
		sortable="true" />
		
	<spring:message code="audit.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" />
		
	<spring:message code="audit.text" var="textHeader" />
	<display:column property="text" title="${textHeader}"
		sortable="true" />
		
	<spring:message code="audit.score" var="scoreHeader" />
	<display:column property="score" title="${scoreHeader}"
		sortable="true" />
		
</display:table>
<br />
<acme:button url="audit/auditor/create.do" code="audit.create"/>