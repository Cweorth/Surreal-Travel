<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<f:message var="title" key="reservation.title" />

<t:layout title="${title}">
<jsp:attribute name="content">

    <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/reservations/new');"><f:message key="reservation.add"/></button>
    
    <br><br>
    
    <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
        <tr class="head">
            <td width="25">#</td>
            <td width="15%"><f:message key="customer.name"/></td>
            <td><f:message key="reservation.trip"/> (<f:message key="excursion.destination"/>)</td>
            <td width="10%"><f:message key="trip.dateFrom"/></td>
            <td width="10%"><f:message key="trip.basePrice"/></td>
            <td width="10%"><f:message key="trip.fullPrice"/></td>
            <td width="10%"><f:message key="trip.excursions"/></td>
            <td width="200" align="right"><f:message key="basic.action"/></td>
        </tr>
        <c:choose>
            <c:when test="${not empty reservations}">
                <c:forEach items="${reservations}" var="reservation" varStatus="count">
                    <tr<c:if test="${not reservation.excursions.isEmpty()}"> class="showHidden"</c:if>>
                        <td class="number">${count.index + 1}</td>
                        <td><c:out value="${reservation.customer.name}"/></td>
                        <td><c:out value="${reservation.trip.destination}"/></td>
                        <td><f:formatDate value="${reservation.trip.dateFrom}" type="date"/></td>
                        <td><c:out value="${reservation.trip.basePrice}"/></td>
                        <td><c:out value="${reservation.getTotalPrice()}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty reservation.excursions}">
                                    <c:out value="${reservation.excursions.size()}"/>&nbsp;
                                    (<b><f:message key="trip.showexcursions"/></b>)
                                </c:when>
                                <c:otherwise>
                                    <i><f:message key="trip.noexcursions"/></i>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <ul class="rowMenu">
                                <sec:authorize access="hasRole('ROLE_STAFF')">
                                <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                    <jsp:param name="url" value="${pageContext.request.contextPath}/reservations/edit/${reservation.id}" />
                                </jsp:include>
                                </sec:authorize>
                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                    <jsp:param name="id" value="${customer.id}" />
                                    <jsp:param name="url" value="${pageContext.request.contextPath}/reservations/delete/${reservation.id}" />
                                </jsp:include>
                            </ul>
                        </td>
                    </tr>
                    <c:if test="${not reservation.excursions.isEmpty()}">
                        <c:forEach items="${reservation.excursions}" var="excursion">
                            <tr class="hidden">
                                <td colspan="2" />
                                <td><c:out value="${excursion.destination}"/></td>
                                <td><f:formatDate value="${excursion.excursionDate}" type="date"/></td>
                                <td><c:out value="${excursion.price}"/></td>
                                <td/>
                                <td colspan="2"><c:out value="${excursion.description}"/></td>
                            </tr> 
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="8" class="noresult"><f:message key="basic.noresult"/></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
            
</jsp:attribute>
</t:layout>
