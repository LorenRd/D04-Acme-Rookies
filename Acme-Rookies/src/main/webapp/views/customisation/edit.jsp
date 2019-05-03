<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>



<form:form action="customisation/administrator/edit.do" modelAttribute="customisation">

	<form:hidden path="id" />
	
	<form:hidden path="version" />
	
	<form:label path="systemName">
		<spring:message code="customisation.systemName" />:
	</form:label>
	<form:input path="systemName" />
	<form:errors cssClass="error" path="systemName" />
	<br />
	<br />


	<form:label path="welcomeBanner">
		<spring:message code="customisation.welcomeBanner" />:
	</form:label>
	<form:input path="welcomeBanner" />
	<form:errors cssClass="error" path="welcomeBanner" />
	<br />
	<br />

	<form:label path="welcomeMessageEn">
		<spring:message code="customisation.welcomeMessageEn" />:
	</form:label>
	<form:textarea path="welcomeMessageEn" />
	<form:errors cssClass="error" path="welcomeMessageEn" />
	<br />
	<br />
	
	<form:label path="welcomeMessageEs">
		<spring:message code="customisation.welcomeMessageEs" />:
	</form:label>
	<form:textarea path="welcomeMessageEs" />
	<form:errors cssClass="error" path="welcomeMessageEs" />
	<br />
	<br />
	<form:label path="countryCode">
		<spring:message code="customisation.countryCode" />:
	</form:label>
	<form:input path="countryCode" />
	<form:errors cssClass="error" path="countryCode" />
	<br />
	<br />
	

	
	


	
	
	<spring:message code="customisation.save" var="saveCustomisation"  />
	<spring:message code="customisation.cancel" var="cancelCustomisation"  />
	
	<input type="submit" name="save"
		value="${saveCustomisation}" />&nbsp; 
				
	
	<input type="button" name="cancel"
		value="${cancelCustomisation}"
		onclick="javascript: relativeRedir('customisation/administrator/display.do');" />
	<br />


</form:form>

