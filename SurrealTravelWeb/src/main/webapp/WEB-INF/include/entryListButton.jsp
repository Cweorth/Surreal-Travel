<%--
    Document   : entryListButton
    Author     : Jan Klimeš
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${param.inactive}">
        <li class="ui-corner-all inactive">
            <span class="ui-icon ui-icon-link"></span><span class="text"><f:message key="basic.list.${param.key}" /></span>
        </li>
    </c:when>
    <c:otherwise>
        <li class="ui-corner-all">
            <a href="${param.url}" class="noPropagation">
                <span class="ui-icon ui-icon-link"></span><span class="text"><f:message key="basic.list.${param.key}" /></span>
            </a>
        </li>
    </c:otherwise>
</c:choose>
