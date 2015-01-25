<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:bulletin>
    <jsp:attribute name="title">
        <f:message key="basic.introduction.board"/>
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Travel agency Surreal Travel is hiring new bus drivers!</h4>
        <div>
            <p>Bring at most 1 day old drug test to the interview. Driver's licence is an advantage (if you do not have one, no matter, you can be a pilot*!)</p>
            <p>* pilot license for a fellbeast can be obtained in our training center; we were forced to discard giant eagles, since apparently they are "endangered" and "on the brink of extinction". Thank you, Greenpeace.</p>
        </div>
        <h4>CEO of Surreal-Travel dies in an unfortunate accident</h4>
        <div>
            <p>With great sadness in our hearts we regret to inform you, that founder of our company, Olo Bramble of Willowbottom, died during his attempt to conquer a
            <em>bucket challenge</em>. The content of a bucket turned out to be a wildly aggresive chemical known as a <em>morgul hooch</em>, which turned poor mister
            Willowbottom to a puddle of goo.</p>
            <p>Honor his memory by buying some of our trips to fantasy lands! First twenty grieving will have 10% discount*!</p>
            <p style="font-size: 8px;">* discount is applicable only if you are a member of hobbit species and attend Mr. Willowbottom's funeral (written confirmation will be issued by a nottary next to the coffin)</p>
        </div>
        <h4>Winter offer - ticket to unknown</h4>
        <div>
            <p>The right stuff for real adventurers! Buy a special trip <strong>Wherever the wind takes me</strong> for a special price 9 999,- and we will pick your destination at random!
            You will not know your destination before departure - you will find out, once you're there. In order not to spoil a suprise, stewards may require you to be blindfolded during a journey.</p>
            <p>Do not think (seriously, don't) and take a leap into unknown with us!</p>
            <p style="font-size: 8px;">Return ticket not included.</p>
        </div>
    </jsp:attribute>
</t:bulletin>