<%--
 * edit.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
 
 <%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="audit/auditor/edit.do" modelAttribute="audit">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<b><spring:message code="audit.position" /></b>:
		<jstl:out value="${audit.position.title}"/><br/>
		
		<acme:textarea code="audit.text" path="text"/>
		<br />		
		<acme:textbox code="audit.score" path="score" placeholder="0.0 - 10.0"/>
		<br />
		
		<acme:submit name="saveDraft" code="audit.saveDraft"/>
		
		<acme:submit name="saveFinal" code="audit.save"/>
		
		<acme:cancel url="welcome/index.do" code="audit.cancel"/>
		
</form:form>
