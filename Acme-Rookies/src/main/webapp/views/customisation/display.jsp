
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<table class="displayStyle">


<tr>

<th> <spring:message code="customisation.parameters"/>  </th>
<th> <spring:message code="customisation.values"/>  </th>
</tr>

<tr>
<td> <strong> <spring:message code="customisation.welcomeBanner" /> : </strong> </td>
<td><a href="${customisation.welcomeBanner}"> <jstl:out value="${customisation.welcomeBanner}" />  </a></td>
</tr>

<tr>
<td> <strong> <spring:message code="customisation.welcomeMessageEn" /> : </strong> </td>
<td> <jstl:out value="${customisation.welcomeMessageEn}" /> </td>
</tr>

<tr>
<td> <strong> <spring:message code="customisation.welcomeMessageEs" /> : </strong> </td>
<td> <jstl:out value="${customisation.welcomeMessageEs}" /> </td>
</tr>


<tr>
<td> <strong> <spring:message code="customisation.countryCode" /> : </strong> </td>

<td> <jstl:out value="${customisation.countryCode}" /> </td>
</tr>







</table>

<div>

<a href="customisation/administrator/edit.do"> <spring:message code="customisation.edit"/> </a>

</div>



