<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message var="title" key="index.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <h2><f:message key="basic.introduction.title"/></h2>
    <p><f:message key="basic.introduction"/></p>
    
    <h3><f:message key="basic.introduction.board"/></h3>
    <ul>
        <li><strong><f:message key="basic.introduction.board.title"/></strong><br><f:message key="basic.introduction.board.content"/></li>
    </ul>
</jsp:attribute>
</t:layout>
