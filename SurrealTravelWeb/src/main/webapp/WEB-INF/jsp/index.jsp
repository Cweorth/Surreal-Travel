<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message var="title" key="index.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <h2><f:message key="basic.introduction.title"/></h2>
    <div class="ui-widget">
        <div style="padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all"> 
            <p><span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
            <f:message key="basic.introduction"/></p>
        </div>
    </div>
        
    <br><br>
    
    <c:choose>
        <c:when test="${pageContext.request.locale == 'cs'}">
            <jsp:include page="/WEB-INF/include/bulletin_cs.jsp" />
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/include/bulletin_en.jsp" />
        </c:otherwise>
    </c:choose>
</jsp:attribute>
</t:layout>
