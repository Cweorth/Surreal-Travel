<%--
    Document   : form
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <sec:authorize access="hasRole('ROLE_STAFF')">
    <tr>
        <td class="left">* <f:message key="reservation.customer"/>:</td>
        <td>
            <form:select path="customer">
                <form:options items="${customers}" itemValue="id" itemLabel="name"/>
            </form:select>
        </td>
    </tr>
    </sec:authorize>
    <tr>
        <td class="left">* <f:message key="reservation.trip"/>:</td>
        <td>
            <form:select path="trip" cssClass="selectAjax">
                <form:options items="${trips}" itemValue="id" itemLabel="destination"/>
            </form:select>
        </td>
    </tr>
    <tr>
        <td class="left"><f:message key="trip.excursions"/>:</td>
        <td id="excursionsAjaxContainer">
            <jsp:include page="excursionsAjax.jsp" />
        </td>
    </tr>
</table>

<sec:authorize access="not hasRole('ROLE_STAFF')">
    <input type="hidden" id="customer" name="customer" value="${reservationDTO.customer.id}" />
</sec:authorize>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/reservations');"><f:message key="basic.cancel" /></button>
