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

<div class="content">
	<img src="${provider.photo}" class="ui mini rounded image" >
</div>

<table class="ui celled table">
	<tbody>
		<tr>
			<td><spring:message code="provider.name" />
			<td data-label="name"><jstl:out value="${provider.name}" /></td>
		</tr>
		<tr>
			<td><spring:message code="provider.surname" />
			<td data-label="surname"><jstl:out value="${provider.surname}" /></td>
		</tr>	
		<tr>
			<td><spring:message code="provider.email" />
			<td data-label="email"><jstl:out value="${provider.email}" /></td>
		</tr>
		<tr>
			<td><spring:message code="provider.phone" />
			<td data-label="phone"><jstl:out value="${provider.phone}" /></td>
		</tr>
		<tr>
			<td><spring:message code="provider.address" />
			<td data-label="address"><jstl:out value="${provider.address}" /></td>
		</tr>
		<tr>
			<td><spring:message code="provider.vatNumber" />
			<td data-label="vatNumber"><jstl:out value="${provider.vatNumber}" /></td>
		</tr>
		<tr>
			<td><spring:message code="provider.make" />
			<td data-label="make"><jstl:out value="${provider.make}" /></td>
		</tr>
	</tbody>
</table>

<!-- Items -->
<h3> <spring:message code="provider.items" /> </h3>
<jstl:choose>
<jstl:when test="${not empty items}">
<display:table pagesize="5" class="displaytag" name="items" requestURI="provider/display.do" id="items">
		
		<!-- Display -->
		<display:column>
			<a href="item/display.do?itemId=${items.id}"><spring:message code="provider.display"/></a>
		</display:column>
		
		<spring:message code="provider.items.name" var="name" />
		<display:column property="name" title="${name}" sortable="name"/>
	
		<spring:message code="provider.items.description" var="description" />
		<display:column property="description" title="${description}" sortable="true"/>
			
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="provider.items.empty" /> 
</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('PROVIDER')">
<br/>
<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
<acme:button url="item/provider/create.do" code="item.create"/>
</jstl:if>
</security:authorize>

<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
	<security:authorize access="hasRole('PROVIDER')">
<br/>
<br/>
<input type="button" name="save" class="ui button"
	value="<spring:message code="provider.edit" />"
	onclick="javascript: relativeRedir('provider/edit.do');" />
	
</security:authorize>
</jstl:if>
<br/>
<br/>
<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>
	
<br/>
<br/>
<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="provider/delete.do" code="actor.delete"/>
</jstl:if>