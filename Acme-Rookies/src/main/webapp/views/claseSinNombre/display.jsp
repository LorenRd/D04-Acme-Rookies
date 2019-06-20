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

		<b><spring:message code="claseSinNombre.ticker" /></b>:
		<jstl:out value="${claseSinNombre.ticker}"/><br/>
	
		<b><spring:message code="claseSinNombre.publicationMoment" /></b>:
		<jstl:if test="${cookie['language'].getValue()=='es'}">
				<fmt:formatDate value="${claseSinNombre.publicationMoment}" pattern="dd-MM-yy HH:mm"/>
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='en'}">
				<fmt:formatDate value="${claseSinNombre.publicationMoment}" pattern="yy/MM/dd HH:mm"/>
		</jstl:if>
		<br>
		<b><spring:message code="claseSinNombre.body" /></b>:
		<jstl:out value="${claseSinNombre.body }"/><br/>
	
		<b><spring:message code="claseSinNombre.picture" /></b>:
		<br><br><img width="250px" src="${claseSinNombre.picture }"/>

		<br/>
	
		<!-- Company -->
		<b><spring:message code="claseSinNombre.company" /></b>:
		<a href="company/display.do?companyId=${claseSinNombre.audit.position.company.id}">
			<jstl:out value="${claseSinNombre.audit.position.company.commercialName}"/>
		</a><br/>

		<!-- Audit -->
		<a href="audit/display.do?auditId=${claseSinNombre.audit.id}">
			<b><spring:message code="claseSinNombre.audit" /></b>
		</a><br/>
		
<jstl:if test="${claseSinNombre.audit.position.company.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${claseSinNombre.isDraft == true}">
		<a href="claseSinNombre/company/edit.do?claseSinNombreId=${claseSinNombre.id}"><spring:message code="claseSinNombre.edit"/></a><br/>
<br/>	
		<a href="claseSinNombre/company/delete.do?claseSinNombreId=${claseSinNombre.id}"><spring:message code="claseSinNombre.delete"/></a><br/>
	</jstl:if>
</jstl:if>

	