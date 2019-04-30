

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


<form:form action="rookie/edit.do" modelAttribute="rookie">
	<form:hidden path="id"/>
	<form:hidden path="version" />
	
	<acme:textbox code="rookie.name" path="name"/>
	
	<acme:textbox code="rookie.surname" path="surname"/>
	
	<acme:textbox code="rookie.email" path="email"/>
	
	<acme:textbox code="rookie.phone" path="phone"/>
	
	<acme:textbox code="rookie.address" path="address"/>
	
	<acme:textbox code="rookie.photo" path="photo"/>
	
	<acme:textbox code="rookie.vatNumber" path="vatNumber"/>
	<br />
	<br />
	
<jstl:if test="${rookie.userAccount.username == pageContext.request.userPrincipal.name}">
	<input type="submit" name="save" id="save"
		value="<spring:message code="rookie.save" />" />
</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="rookie.cancel" />"
		onclick="javascript: relativeRedir('${redirectURI}');" />
	<br />

</form:form>

<jstl:if test="${rookie.userAccount.username == pageContext.request.userPrincipal.name}">
<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='rookie.confirmationPhone'/>","${countryCode}");});
</script>
</jstl:if>