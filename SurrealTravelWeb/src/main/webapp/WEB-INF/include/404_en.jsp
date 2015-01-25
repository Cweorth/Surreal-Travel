<%-- 
    Document   : 404_en
    Created on : Jan 25, 2015, 11:10:09 AM
    Author     : Roman Lacko [396157]
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<p style="text-align: center">
    <span style="font-size: 200%">
        We know about <b><span style="font-size: 150%">404</span></b> planets that have what you are looking for.
    </span>
    <br/>
    It's just not this one.
</p>

<br/>
<t:bulletin>
    <jsp:attribute name="title">
        What to do next?
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Are You from Earth?</h4>
        <div>
            <ul>
                <li>You can return to the <a href="/">main page</a></li>
                <li>Check that you typed the URL correctly</li>
            </ul>
        </div>
        <h4>Are You from a planet that has a <i>StarGate</i>?</h4>
        <div>
            <ul>
                <li>Dial another planet and try there again</li>
            </ul>
        </div>
        <h4>Are You capable of multidimensional travel?</h4>
        <div>
            <ul>
                <li>Go to the dimension where there is no need of this page</li>
            </ul>
        </div>
    </jsp:attribute>
</t:bulletin>

<br/>
<small>
    The Surreal-Travel does not own rights for the image material on this page.<br/>
    The content from <i>2001: A Space Odyssey</i> (film) is owned by Metro-Goldwyn-Mayer.
</small>