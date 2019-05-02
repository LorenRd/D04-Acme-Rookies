<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->

<display:table name="items" id="row" requestURI="item/list.do"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="item/display.do?itemId=${row.id}"><spring:message code="item.display"/></a>
	</display:column>

	<!-- Attributes -->

	<spring:message code="item.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}"
		sortable="true" />
		
	<spring:message code="item.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />
		
	<spring:message code="item.informationLink" var="informationLinkHeader" />
	<display:column property="informationLink" title="${informationLinkHeader}"
		sortable="true" />
		
	<strong> <spring:message code="item.pictures" /> : </strong>

	<ul>
	<jstl:forEach items="${item.pictures}" var="pictures"><img src='<jstl:out value="${pictures}"></jstl:out>'>
	<br />
	</jstl:forEach>
	</ul>
		
	<!-- Provider -->
	<spring:message code="item.provider" var="providerHeader"/>
	<display:column title="${providerHeader}">
			<a href="provider/display.do?providerId=${row.provider.id}">
				<jstl:out value="${row.provider.userAccount.username}"/>
			</a>
	</display:column>

</display:table>

<!-- Create item -->
<security:authorize access="hasRole('PROVIDER')">
		<acme:button url="item/provider/create.do" code="item.create"/>
	
</security:authorize> 
