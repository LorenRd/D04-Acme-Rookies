<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="messages" id="row" pagesize="5" requestURI="${requestURI}" 
class="displaytag" keepStatus="true">
	
	<!-- Subject -->
	<spring:message code="message.subject" var="subjectHeader" />
	<display:column  property="subject" title="${subjectHeader}" />
	
	<!-- Body -->
	<spring:message code="message.body" var="bodyHeader" />
	<display:column  property="body" title="${bodyHeader}" />
	
</display:table>

<security:authorize access="hasRole('ADMIN')">
<br/>
<br/>
<a href="message/actor/warning.do"><spring:message code="message.warning" /></a>
<br/>
<jstl:if test="${rebrandMessage == false}">
<a href="message/actor/rebrand.do"><spring:message code="message.rebrand" /></a>
</jstl:if>
<br/>
</security:authorize>