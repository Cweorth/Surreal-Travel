<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:errors path="excursions" cssClass="formError" element="p"/>
<c:choose>
    <c:when test="${excursions.isEmpty()}">
        <b><f:message key="trip.noexcursions"/></b>
    </c:when>
    <c:otherwise>
        <table cellspacing="0" cellpadding="3" border="0">
            <tr class="head">
                <th/>
                <th><f:message key="excursion.destination"/></th>
                <th><f:message key="excursion.description"/></th>
                <th><f:message key="excursion.excursionDate"/></th>
                <th><f:message key="excursion.duration"/></th>
                <th><f:message key="excursion.price"/></th>
            </tr>
            <c:forEach items="${excursions}" var="excursion" varStatus="state">
                <tr>
                    <!--<td><form:checkbox value="${excursion.id}"  path="excursions"/></td>-->
                    <td><input type="checkbox" value="${excursion.id}" name="excursions"></td>
                    <td><c:out value="${excursion.destination}"/></td>
                    <td><c:out value="${excursion.description}"/></td>
                    <td><f:formatDate value="${excursion.excursionDate}" type="date"/></td>
                    <td><c:out value="${excursion.duration}"/></td>
                    <td><c:out value="${excursion.price}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>