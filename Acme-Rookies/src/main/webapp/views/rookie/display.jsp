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
	<img src="${rookie.photo}" class="ui mini rounded image" >
</div>

<table class="ui celled table">
	<tbody>
		<tr>
			<td><spring:message code="rookie.name" />
			<td data-label="name"><jstl:out value="${rookie.name}" /></td>
		</tr>
		<tr>
			<td><spring:message code="rookie.surname" />
			<td data-label="surname"><jstl:out value="${rookie.surname}" /></td>
		</tr>	
		<tr>
			<td><spring:message code="rookie.email" />
			<td data-label="email"><jstl:out value="${rookie.email}" /></td>
		</tr>
		<tr>
			<td><spring:message code="rookie.phone" />
			<td data-label="phone"><jstl:out value="${rookie.phone}" /></td>
		</tr>
		<tr>
			<td><spring:message code="rookie.address" />
			<td data-label="address"><jstl:out value="${rookie.address}" /></td>
		</tr>
		<tr>
			<td><spring:message code="rookie.vatNumber" />
			<td data-label="vatNumber"><jstl:out value="${rookie.vatNumber}" /></td>
		</tr>
	</tbody>
</table>

<jstl:if test="${rookie.userAccount.username == pageContext.request.userPrincipal.name}">
	<security:authorize access="hasRole('ROOKIE')">
<br/>
<br/>
<input type="button" name="save" class="ui button"
	value="<spring:message code="rookie.edit" />"
	onclick="javascript: relativeRedir('rookie/edit.do');" />
	
</security:authorize>
</jstl:if>
<br/>
<br/>
<jstl:if test="${rookie.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>
	
<br/>
<br/>
<jstl:if test="${rookie.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="rookie/delete.do" code="actor.delete"/>
</jstl:if>