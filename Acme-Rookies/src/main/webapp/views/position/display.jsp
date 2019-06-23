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

		<b><spring:message code="position.title" /></b>:
		<jstl:out value="${position.title}"/><br/>
	
		<b><spring:message code="position.description" /></b>:
		<jstl:out value="${position.description }"/><br/>	
	
		<b><spring:message code="position.deadline" /></b>:
		<jstl:out value="${position.deadline }"/><br/>
	
		<b><spring:message code="position.profileRequired" /></b>:
		<jstl:out value="${position.profileRequired }"/><br/>
		
		<b><spring:message code="position.skillsRequired" /></b>:
		<br/>
		<jstl:forEach items="${position.skillsRequired}" var="skillRequired" >
			<jstl:if test="${skillRequired != null}">
	        	<jstl:out value="${skillRequired}"/>
	        </jstl:if>
		</jstl:forEach>
		<br/>
		
		<b><spring:message code="position.technologiesRequired" /></b>:
		<br/>
		<jstl:forEach items="${position.technologiesRequired}" var="technologyRequired" >
			<jstl:if test="${technologyRequired != null}">
	        	<jstl:out value="${technologyRequired}"/>
	        </jstl:if>
		</jstl:forEach>
		<br/>
		
		<b><spring:message code="position.salaryOffered" /></b>:
		<jstl:out value="${position.salaryOffered }"/><br/>
		
		<b><spring:message code="position.ticker" /></b>:
		<jstl:out value="${position.ticker }"/><br/>
		
		<jstl:if test="${position.company.userAccount.username == pageContext.request.userPrincipal.name}">
		<b><spring:message code="position.status" /></b>:
		<jstl:out value="${position.status }"/><br/>
		</jstl:if>
		
		<!-- Company -->
		<b><spring:message code="position.company" /></b>:
		<a href="company/display.do?companyId=${position.company.id}">
			<jstl:out value="${position.company.commercialName}"/>
		</a><br/>
		
		<!-- Problems -->
		
		<b><spring:message code="position.problems" /></b>:
		<br/><ul>
		<jstl:forEach items="${position.problems}" var="problem" >
			<jstl:if test="${problem != null}">
	        	<li><jstl:out value="${problem.title}"/></li>
	        </jstl:if>
		</jstl:forEach></ul>

		<!-- Audits -->
<h3> <spring:message code="position.audits" /> </h3>
<jstl:choose>
<jstl:when test="${not empty audits}">
<display:table pagesize="5" class="displaytag" name="audits" requestURI="position/display.do" id="audits">
		
		<!-- Display -->
		<display:column>
			<a href="audit/display.do?auditId=${audits.id}"><spring:message code="audit.display"/></a>
		</display:column>
		
		<spring:message code="audit.text" var="text" />
		<display:column property="text" title="${text}" sortable="text"/>
	
		<spring:message code="audit.score" var="score" />
		<display:column property="score" title="${score}" sortable="true"/>
			
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="position.audits.empty" /> 
</jstl:otherwise>
</jstl:choose>

<!-- claseSinNombres -->
<h3> <spring:message code="position.claseSinNombres" /> </h3>
<jstl:choose>
<jstl:when test="${not empty claseSinNombres}">
<display:table pagesize="5" class="displaytag" name="claseSinNombres" requestURI="position/display.do" id="claseSinNombres">
		
		<!-- Display -->
		<display:column>
			<a href="claseSinNombre/display.do?claseSinNombreId=${claseSinNombres.id}"><spring:message code="claseSinNombre.display"/></a>
		</display:column>
		
		<spring:message code="claseSinNombre.rookie" var="rookie" />
		<display:column property="rookie.userAccount.username" title="${rookie}" sortable="true"/>
	
		<spring:message code="claseSinNombre.ticker" var="tickerHeader" />
		<display:column property="ticker" title="${tickerHeader}" sortable="true" />
		
		<jstl:if test="${cookie['language'].getValue()=='es'}">
			<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    		<display:column property="publicationMoment" format="{0,date, dd-MM-yy HH:mm}" title="${publicationMomentHeader}" />
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='en'}">
			<spring:message code="claseSinNombre.publicationMoment" var="publicationMomentHeader" />
    		<display:column property="publicationMoment" format="{0,date, yy/MM/dd HH:mm}" title="${publicationMomentHeader}" />
		</jstl:if>
		
</display:table>
</jstl:when>
<jstl:otherwise>
<spring:message code="position.claseSinNombres.empty" /> 
</jstl:otherwise>
</jstl:choose>

<security:authorize access="hasRole('ROOKIE')">
	<a href="claseSinNombre/rookie/create.do?positionId=${position.id}"><spring:message code="claseSinNombre.create"/></a><br/>
</security:authorize>

<security:authorize access="hasRole('COMPANY')">
<jstl:if test="${position.company.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${position.status == 'DRAFT'}">			
		<a href="position/company/edit.do?positionId=${position.id}"><spring:message code="position.edit"/></a><br/>
	</jstl:if>
<br/>
	<jstl:if test="${position.status == 'FINAL'}">
		<a href="position/company/cancel.do?positionId=${position.id}"><spring:message code="position.saveCancel"/></a><br/>				
	</jstl:if>
<br/>
	<a href="position/company/delete.do?positionId=${position.id}"><spring:message code="position.delete"/></a><br/>
</jstl:if>
</security:authorize>
