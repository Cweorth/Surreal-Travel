$(document).ready(function() {
    
    var notification = $.getQuery('notification');
    if(notification === "success") {
        showSuccessMessage();
    }

    $( "button.new" ).button({
        icons: {
            primary: "ui-icon-document"
        }
    });
    
    $( "button.submit" ).button({
        icons: {
            primary: "ui-icon-check"
        }
    }).click(function() {
        showStatus();
    });
    
    $( "button.cancel" ).button({
        icons: {
            primary: "ui-icon-cancel"
        }
    });
    
    $( ".datepicker" ).datepicker({
        showAnim: "fadeIn",
        showOtherMonths: true,
        selectOtherMonths: true,
        changeMonth: true,
        changeYear: true
    });
    
    $( ".datepicker" ).datepicker( $.datepicker.regional[ "cs" ] );
    
    $( ".dialogDelete" ).dialog({
        height: 200,
        resizable: false,
        modal: true,
        autoOpen: false,
        closeOnEscape: true,
        closeText: ""
    });
    
});

$(document).on("click", ".dialogDeletePrompt", function(e){ 

    e.preventDefault();

    url = $(this).attr('href');
    id = $(this).parent('li').attr('id');
    element = $('#dialogDelete_' + id);
    
    var buttons = element.dialog("option", "buttons");

    if(jQuery.isEmptyObject(buttons)) {
        element.dialog( "option", "buttons", {
            "yes": function() {
                window.location=url;
            },
            "no": function() {
                $(this).dialog("close");
            }
        });
    }
    
    element.dialog( "option", "open", function() {
        
        $('.ui-dialog-buttonpane').find('button:contains("yes")').button({
            icons: {
                primary: 'ui-icon-check'
            },
            text: false
        }).attr("title", "");
        $('.ui-dialog-buttonpane').find('button:contains("no")').button({
            icons: {
                primary: 'ui-icon-closethick'
            },
            text: false
        }).attr("title", "");
    });

    element.dialog('open');

});

(function($){
    $.getQuery = function( query ) {
        query = query.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
        var expr = "[\\?&]"+query+"=([^&#]*)";
        var regex = new RegExp( expr );
        var results = regex.exec( window.location.href );
        if( results !== null ) {
            return results[1];
            return decodeURIComponent(results[1].replace(/\+/g, " "));
        } else {
            return false;
        }
    };
})(jQuery);

function showStatus() {
    $(function() {
        $('#status').show();
    });
}

function hideStatus() {
    $(function() {
        $('#status').fadeOut(200);
    });
}

function showSuccessMessage() {
    $('#successMessage').fadeIn('slow').delay(4000).fadeOut('slow');
}

function redirect(url) {
    window.location=url;
}
   