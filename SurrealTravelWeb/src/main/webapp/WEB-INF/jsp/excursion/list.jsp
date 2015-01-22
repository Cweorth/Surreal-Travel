<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<f:message var="title" key="excursion.title" />

<t:layout title="${title}">
<jsp:attribute name="content">

    <sec:authorize access="hasRole('ROLE_STAFF')">
    <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/excursions/new');"><f:message key="excursion.add"/></button>
    <br/>
    <c:if test="${excursionsOccurence.contains(excursionsOccurenceCheck)}">
        <br/>
        <jsp:include page="/WEB-INF/include/note.jsp">
            <jsp:param name="key" value="excursion.message.undeletable"/>
        </jsp:include>
    </c:if>
    <br/>
    </sec:authorize>
    
    <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
        <tr class="head">
            <td width="25">#</td>
            <td><f:message key="excursion.destination"/></td>
            <td><f:message key="excursion.description"/></td>
            <td><f:message key="excursion.excursionDate"/></td>
            <td><f:message key="excursion.duration"/></td>
            <td><f:message key="excursion.price"/></td>
            <td width="200" align="right"><sec:authorize access="hasRole('ROLE_STAFF')"><f:message key="basic.action"/></sec:authorize></td>
        </tr>
        <c:choose>
            <c:when test="${not empty excursions}">
                <c:forEach items="${excursions}" var="excursion" varStatus="count">
                    <tr>
                        <td class="number">${count.count}</td>
                        <td><c:out value="${excursion.destination}"/></td>
                        <td><c:out value="${excursion.description}"/></td>
                        <td><f:formatDate value="${excursion.excursionDate}" type="date"/></td>
                        <td><c:out value="${excursion.duration}"/></td>
                        <td><c:out value="${excursion.price}"/></td>
                        <td>
                            <ul class="rowMenu">
                                <c:choose>
                                    <c:when test="${excursionsOccurence.get(count.index) == 0}">
                                        <jsp:include page="/WEB-INF/include/entryListButton.jsp">
                                            <jsp:param name="key" value="trips"/>
                                            <jsp:param name="inactive" value="true"/>
                                        </jsp:include>
                                    </c:when>
                                    <c:otherwise>
                                        <jsp:include page="/WEB-INF/include/entryListButton.jsp">
                                            <jsp:param name="key" value="trips"/>
                                            <jsp:param name="url" value="${pageContext.request.contextPath}/trips?eid=${excursion.id}" />
                                        </jsp:include>
                                    </c:otherwise>
                                </c:choose>
                                <sec:authorize access="hasRole('ROLE_STAFF')">
                                    <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                        <jsp:param name="url" value="${pageContext.request.contextPath}/excursions/edit/${excursion.id}" />
                                    </jsp:include>
                                    <c:choose>
                                        <c:when test="${excursionsOccurence.get(count.index) == 0}">
                                            <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                <jsp:param name="id" value="${excursion.id}" />
                                                <jsp:param name="url" value="${pageContext.request.contextPath}/excursions/delete/${excursion.id}" />
                                            </jsp:include>
                                        </c:when>
                                        <c:otherwise>
                                            <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                <jsp:param name="inactive" value="true" />
                                            </jsp:include>
                                        </c:otherwise>
                                    </c:choose>
                                </sec:authorize>
                            </ul>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="7" class="noresult"><f:message key="basic.noresult"/></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
            
</jsp:attribute>
</t:layout>
