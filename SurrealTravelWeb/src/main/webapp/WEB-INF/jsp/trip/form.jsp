<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="trip.destination"/>:</td>
        <td><form:input path="destination" cssClass="text"/><form:errors path="destination" cssClass="formError"/></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="trip.dateFrom"/>:</td>
        <td><form:input path="dateFrom" cssClass="text datepicker"/><form:errors path="dateFrom" cssClass="formError"/></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="trip.dateTo"/>:</td>
        <td><form:input path="dateTo" cssClass="text datepicker"/><form:errors path="dateTo" cssClass="formError"/></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="trip.basePrice"/>:</td>
        <td><form:input path="basePrice" cssClass="text"/><form:errors path="basePrice" cssClass="formError"/></td>
    </tr>
    <tr>
        <td class="left"><f:message key="trip.excursions"/>:</td>
        <td>
            <form:errors path="excursions" cssClass="formError" element="p"/>
            <c:choose>
                <c:when test="${tripdata.allExcursions.isEmpty()}">
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
                        <c:forEach items="${tripdata.allExcursions}" var="excursion" varStatus="state">
                            <tr>
                                <td><form:checkbox value="${excursion.id}" path="excursionIDs"/></td>
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
        </td>
    </tr>
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/trips');"><f:message key="basic.cancel" /></button>

