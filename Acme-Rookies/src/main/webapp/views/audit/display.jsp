<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

		<b><spring:message code="audit.position" /></b>:
		<jstl:out value="${audit.position.title}"/><br/>
	
		<b><spring:message code="audit.moment" /></b>:
		<jstl:out value="${audit.moment }"/><br/>	
	
		<b><spring:message code="audit.text" /></b>:
		<jstl:out value="${audit.text }"/><br/>
	
		<b><spring:message code="audit.score" /></b>:
		<jstl:out value="${audit.score }"/><br/>
	
		<jstl:if test="${audit.isDraft}">
		<b><spring:message code="audit.isDraft.draft" /></b>
		</jstl:if>
		<jstl:if test="${!audit.isDraft}">
		<b><spring:message code="audit.isDraft.final" /></b>
		</jstl:if>
<br/>
<security:authorize access="hasAnyRole('COMPANY','AUDITOR')">
		<!-- claseSinNombre -->
<h3><spring:message code="claseSinNombre.claseSinNombre" /></h3>
<jstl:choose>
<jstl:when test="${not empty claseSinNombre}">
<display:table pagesize="5" class="displaytag" name="claseSinNombre" requestURI="audit/display.do" id="claseSinNombre">
		
		<!-- Display -->
	<display:column>
		<a href="claseSinNombre/display.do?claseSinNombreId=${claseSinNombre.id}"><spring:message code="claseSinNombre.display"/></a>
	</display:column>
		
		<!-- Attributes -->
		<!-- Colors -->
			<jstl:choose>
				<jstl:when test="${row.publicationMoment le dateOneMonth}">
					<jstl:set var="background" value="Indigo" />
				</jstl:when>
	
				<jstl:when test="${row.publicationMoment le dateTwoMonths}">
					<jstl:set var="background" value="DarkSlateGrey" />
				</jstl:when>
		
				<jstl:otherwise>
					<jstl:set var="background" value="PapayaWhip" />
				</jstl:otherwise>
			</jstl:choose>
		<!--  -->
		
	<spring:message code="claseSinNombre.ticker" var="tickerHeader" />
	<display:column property="ticker" title="${tickerHeader}"
		sortable="true" />
	<jstl:if test="${cookie['language'].getValue()=='es'}">
		<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    	<display:column class="${background}" property="publicationMoment" format="{0,date, dd-MM-yy HH:mm}" title="${publicationMomentHeader}" />
	</jstl:if>
	<jstl:if test="${cookie['language'].getValue()=='en'}">
		<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    	<display:column class="${background}" property="publicationMoment" format="{0,date, yy/MM/dd HH:mm}" title="${publicationMomentHeader}" />
	</jstl:if>

			
	<spring:message code="claseSinNombre.body" var="bodyHeader" />
	<display:column property="body" title="${bodyHeader}"
		sortable="true" />
		
			
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="claseSinNombre.audits.empty" /> 
</jstl:otherwise>
</jstl:choose>
<br/>
</security:authorize>

<jstl:if test="${audit.position.company.id == principal.id}">
<jstl:if test="${!audit.isDraft}">
		<acme:button url="claseSinNombre/company/create.do?auditId=${audit.id}" code="claseSinNombre.create"/>
</jstl:if>
</jstl:if>
<jstl:if test="${audit.auditor.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${audit.isDraft == true}">
		<a href="audit/auditor/edit.do?auditId=${audit.id}"><spring:message code="audit.edit"/></a><br/>
		<br/>
		<a href="audit/auditor/delete.do?auditId=${audit.id}"><spring:message code="audit.delete"/></a><br/>
	</jstl:if>
</jstl:if>




	