<%-- 
    Document   : entryEditButton
    Author     : Jan Klimeš
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${param.inactive}">
        <li class="ui-corner-all inactive">
            <span class="ui-icon ui-icon-pencil"></span><span class="text"><f:message key="basic.edit" /></span>
        </li>
    </c:when>
    <c:otherwise>
        <li class="ui-corner-all">
            <a href="${param.url}" class="noPropagation">
                <span class="ui-icon ui-icon-pencil"></span><span class="text"><f:message key="basic.edit" /></span>
            </a>
        </li>
    </c:otherwise>
</c:choose>
