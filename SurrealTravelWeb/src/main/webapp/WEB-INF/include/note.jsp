<%--
    Document   : note
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="ui-widget" style="margin-bottom: 5px">
    <div style="padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all">
        <p>
            <span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
            <strong><f:message key="basic.note" />:</strong> <f:message key="${param.key}"/>
        </p>
    </div>
</div>
