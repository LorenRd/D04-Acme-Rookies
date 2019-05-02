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

		<b><spring:message code="item.name" /></b>:
		<jstl:out value="${item.name}"/><br/>
	
		<b><spring:message code="item.description" /></b>:
		<jstl:out value="${item.description }"/><br/>	
	
		<b><spring:message code="item.informationLink" /></b>:
		<jstl:out value="${item.informationLink }"/><br/>
	
		<jstl:if test="${item.pictures != null && (not empty item.pictures)}">
		<b><spring:message code="item.pictures" /></b>:
		<br/>
			<jstl:forEach items="${item.pictures}" var="picture" >
				<jstl:if test="${picture != null}">
	        		<acme:image src="${picture}" cssClass="external-image-landscape"/>
	        	</jstl:if>
			</jstl:forEach>
			<br/>
		</jstl:if>
		
		<!-- Provider -->
		<b><spring:message code="item.provider" /></b>:
		<a href="provider/display.do?providerId=${item.provider.id}">
			<jstl:out value="${item.provider.userAccount.username}"/>
		</a><br/>

<security:authorize access="hasRole('PROVIDER')">
<jstl:if test="${item.provider.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<a href="item/provider/edit.do?itemId=${item.id}"><spring:message code="item.edit"/></a><br/>
	<a href="item/provider/delete.do?itemId=${item.id}"><spring:message code="item.delete"/></a><br/>
</jstl:if>
</security:authorize>