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

<jstl:if test="${audit.auditor.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${audit.isDraft == true}">
		<a href="audit/auditor/edit.do?auditId=${audit.id}"><spring:message code="audit.edit"/></a><br/>
		<br/>
		<a href="audit/auditor/delete.do?auditId=${audit.id}"><spring:message code="audit.delete"/></a><br/>
	</jstl:if>
</jstl:if>

	