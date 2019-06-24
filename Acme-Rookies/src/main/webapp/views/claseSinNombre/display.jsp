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

<jstl:choose>
<jstl:when test="${(claseSinNombre.position.company.userAccount.username == pageContext.request.userPrincipal.name) || (claseSinNombre.rookie.userAccount.username == pageContext.request.userPrincipal.name)}">

		<b><spring:message code="claseSinNombre.position" /></b>:
		<jstl:out value="${claseSinNombre.position.title}"/><br/>
		
		<b><spring:message code="claseSinNombre.rookie" /></b>:
		<jstl:out value="${claseSinNombre.rookie.userAccount.username}"/><br/>
	
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
	
		<div class="content">
			<img src="${claseSinNombre.picture}" class="ui mini rounded image" >
		</div>
	
		<jstl:if test="${claseSinNombre.isDraft}">
		<b><spring:message code="claseSinNombre.isDraft.draft" /></b>
		</jstl:if>
<br/>

<jstl:if test="${claseSinNombre.rookie.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${claseSinNombre.isDraft == true}">
		<a href="claseSinNombre/rookie/edit.do?claseSinNombreId=${claseSinNombre.id}"><spring:message code="claseSinNombre.edit"/></a><br/>
		<br/>
		<a href="claseSinNombre/rookie/delete.do?claseSinNombreId=${claseSinNombre.id}"><spring:message code="claseSinNombre.delete"/></a><br/>
	</jstl:if>
</jstl:if>

</jstl:when>
<jstl:otherwise>
<spring:message code="claseSinNombre.notYours" />
</jstl:otherwise>
</jstl:choose>