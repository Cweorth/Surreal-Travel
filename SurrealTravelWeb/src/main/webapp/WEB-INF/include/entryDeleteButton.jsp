<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${param.inactive}">
        <li class="ui-corner-all inactive">
            <span class="ui-icon ui-icon-trash"></span><span class="text"><f:message key="basic.delete" /></span>
        </li>
    </c:when>
    <c:otherwise>
        <li class="ui-corner-all" id="${param.id}">
            <a href="${param.url}" class="dialogDeletePrompt noPropagation">
                <span class="ui-icon ui-icon-trash"></span><span class="text"><f:message key="basic.delete" /></span>
            </a>
            <div class="dialogDelete" id="dialogDelete_${param.id}" title="<f:message key="basic.delete" />?">
                <p><f:message key="basic.delete.genericMessage" /></p>
            </div>
        </li>
    </c:otherwise>
</c:choose>