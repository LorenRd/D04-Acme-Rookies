<%--
 * create.jsp
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

<form:form action="claseSinNombre/rookie/create.do?positionId=${param['positionId']}" modelAttribute="claseSinNombre">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
				
		<acme:textarea code="claseSinNombre.body" path="body"/>
		<br />		
		<acme:textbox code="claseSinNombre.picture" path="picture" />
		<br />
		
		<acme:submit name="saveDraft" code="claseSinNombre.saveDraft"/>
		
		<acme:submit name="saveFinal" code="claseSinNombre.save"/>
		
		<acme:cancel url="welcome/index.do" code="claseSinNombre.cancel"/>
		
</form:form>
