<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="customer.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <table>
        <tr>
            <td>#</td>
            <td><f:message key="customer.name"/></td>
            <td><f:message key="customer.address"/></td>
            <td><f:message key="basic.action"/></td>
        </tr>
        <c:forEach items="${customers}" var="customer" varStatus="count">
            <tr>
                <td>${count.index + 1}</td>
                <td><c:out value="${customer.name}"/></td>
                <td><c:out value="${customer.address}"/></td>
                <td></td>
            </tr>
        </c:forEach>
    </table>
</jsp:attribute>
</t:layout>
