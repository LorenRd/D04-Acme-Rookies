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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="auditor/edit.do" modelAttribute="auditor">
	<form:hidden path="id" />
	<form:hidden path="version"/>

	<div class="ui equal width form">
		<div class="fields">
			<!-- Name -->
			<acme:textbox code="auditor.name" path="name"/>
			<!-- Surname -->
			<acme:textbox code="auditor.surname" path="surname"/>
			<!-- Photo -->
			<acme:textbox code="auditor.photo" path="photo"/>
			<!-- Phone -->
			<acme:textbox code="auditor.phone" path="phone"/>
			<!-- Address -->
			<acme:textbox code="auditor.address" path="address"/>
			<!-- VAT number -->
			<acme:textbox code="auditor.vatNumber" path="vatNumber"/>
		</div>
		<div class="fields">
			<!-- Email -->
			<acme:textbox code="auditor.email" path="email"/>
		</div>
	</div>

	
	<input type="submit" name="save" id="save"
		value="<spring:message code="auditor.save" />" />
	
	<acme:cancel url="auditor/viewProfile.do" code="auditor.cancel"/>


</form:form>


<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='auditor.confirmationPhone'/>","${countryCode}");});
</script>
