<%-- 
    Document   : 404_cs
    Created on : Jan 25, 2015, 11:10:20 AM
    Author     : Roman Lacko [396157]
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<p style="text-align: center">
    <span style="font-size: 200%">
        Evidujeme přesně <b><span style="font-size: 150%">404</span></b> planet, které mají to, co hledáte.
    </span>
    <br/>
    Právě tato mezi ně nepatří.
</p>

<br/>
<t:bulletin>
    <jsp:attribute name="title">
        Co dál?
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Jste ze Země?</h4>
        <div>
            <ul>
                <li>Můžete se vrátit na <a href="/">hlavní stránku</a></li>
                <li>Zkontrolujte URL</li>
            </ul>
        </div>
        <h4>Jste z planety s <i>Hvězdnou Bránou</i>?</h4>
        <div>
            <ul>
                <li>Vytočte jinou planetu a zkuste to odtamtud</li>
            </ul>
        </div>
        <h4>Jste schopni interdimenzionálního cestování?</h4>
        <div>
            <ul>
                <li>Přesuňte se do dimenze, kde tuto stránku nebudete potřebovat</li>
            </ul>
        </div>
    </jsp:attribute>
</t:bulletin>

<br/>
<small>
    Surreal-Travel není vlastníkem grafického materiálu použitého na této stránce.<br/>
    Obsah z <i>2001: A Space Odyssey</i> (film) vlastní společnost Metro-Goldwyn-Mayer.
</small>