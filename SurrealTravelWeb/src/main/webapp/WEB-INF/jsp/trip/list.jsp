<%--
    Document   : list
    Created on : Nov 25, 2014, 4:09:14 PM
    Author     : Roman Lacko [396157]
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<f:message var="title" key="trip.title"/>

<t:layout title="${title}">
    <jsp:attribute name="content">
        <c:if test="${not empty excursion}">
            <b><f:message key="trip.filter"/></b>: <c:out value="${excursion.destination}"/>
            <br/><br/>
        </c:if>
        <sec:authorize access="hasRole('ROLE_STAFF')">
            <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/trips/new');"><f:message key="trip.add"/></button>
            <br/>
            <c:if test="${not empty reserved}">
                <br/>
                <jsp:include page="/WEB-INF/include/note.jsp">
                    <jsp:param name="key" value="trip.message.undeletable"/>
                </jsp:include>
            </c:if>
            <br/>
        </sec:authorize>

        <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
            <tr class="head">
                <td width="25">#</td>
                <td><f:message key="trip.destination"/></td>
                <td width="10%"><f:message key="trip.dateFrom"/></td>
                <td width="10%"><f:message key="trip.dateTo"/></td>
                <td width="10%"><f:message key="trip.capacity"/></td>
                <td width="10%"><f:message key="trip.basePrice"/></td>
                <td width="10%"><f:message key="trip.fullPrice"/></td>
                <td width="10%"><f:message key="trip.excursions"/></td>
                <td width="200" align="right"><f:message key="basic.action"/></td>
            </tr>

            <c:choose>
                <c:when test="${not empty trips}">
                    <c:forEach items="${trips}" var="trip" varStatus="count">
                        <tr class="showHidden">
                            <td class="number">${count.index + 1}</td>
                            <td><c:out value="${trip.destination}"/></td>
                            <td><f:formatDate value="${trip.dateFrom}" type="date"/></td>
                            <td><f:formatDate value="${trip.dateTo}"   type="date"/></td>
                            <td><c:out value="${trip.capacity}"/></td>
                            <td><c:out value="${trip.basePrice}"/></td>
                            <td><c:out value="${trip.fullPrice}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty trip.excursions}">
                                        <c:out value="${trip.excursions.size()}"/>&nbsp;
                                        (<b><f:message key="trip.showexcursions"/></b>)
                                    </c:when>
                                    <c:otherwise>
                                        <i><f:message key="trip.noexcursions"/></i>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <sec:authorize access="hasRole('ROLE_STAFF')">
                                    <ul class="rowMenu">
                                        <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                            <jsp:param name="url" value="${pageContext.request.contextPath}/trips/edit/${trip.id}" />
                                        </jsp:include>
                                        <c:choose>
                                            <c:when test="${(not empty reserved) && reserved.contains(trip)}">
                                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                    <jsp:param name="inactive" value="true" />
                                                </jsp:include>
                                            </c:when>
                                            <c:otherwise>
                                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                    <jsp:param name="id"  value="${trip.id}" />
                                                    <jsp:param name="url" value="${pageContext.request.contextPath}/trips/delete/${trip.id}" />
                                                </jsp:include>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </sec:authorize>
                            </td>
                        </tr>
                        <c:if test="${not trip.excursions.isEmpty()}">
                            <c:forEach items="${trip.excursions}" var="excursion">
                                <tr class="hidden">
                                    <td/>
                                    <td><c:out value="${excursion.destination}"/></td>
                                    <td colspan="2"><f:formatDate value="${excursion.excursionDate}" type="date"/></td>
                                    <td/>
                                    <td><c:out value="${excursion.price}"/></td>
                                    <td/>
                                    <td><c:out value="${excursion.description}"/></td>
                                    <td/>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="9" class="noresult"><f:message key="basic.noresult"/></td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
    </jsp:attribute>
</t:layout>
