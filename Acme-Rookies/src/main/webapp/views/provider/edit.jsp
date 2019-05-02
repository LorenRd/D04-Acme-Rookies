

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="provider/edit.do" modelAttribute="provider">
	<form:hidden path="id"/>
	<form:hidden path="version" />
	
	<acme:textbox code="provider.name" path="name"/>
	
	<acme:textbox code="provider.surname" path="surname"/>
	
	<acme:textbox code="provider.make" path="make"/>
	
	<acme:textbox code="provider.email" path="email"/>
	
	<acme:textbox code="provider.phone" path="phone"/>
	
	<acme:textbox code="provider.address" path="address"/>
	
	<acme:textbox code="provider.photo" path="photo"/>
	
	<acme:textbox code="provider.vatNumber" path="vatNumber"/>
	<br />
	<br />
	
<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
	<input type="submit" name="save" id="save"
		value="<spring:message code="provider.save" />" />
</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="provider.cancel" />"
		onclick="javascript: relativeRedir('${redirectURI}');" />
	<br />

</form:form>

<jstl:if test="${provider.userAccount.username == pageContext.request.userPrincipal.name}">
<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='provider.confirmationPhone'/>","${countryCode}");});
</script>
</jstl:if>