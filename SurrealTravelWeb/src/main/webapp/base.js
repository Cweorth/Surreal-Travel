customerCheckPath = "form input#customer";

$(document).ready(function() {
    
    var notification = $.getQuery('notification');
    if(notification === "success") {
        showSuccessMessage();
    } else if(notification === "failure") {
        showFailureMessage();
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
    
    // localize datepicker
    if((window.navigator.userLanguage || window.navigator.language) === "cs")
        $( ".datepicker" ).datepicker($.datepicker.regional[ "cs" ]);
    
    $( ".dialogDelete" ).dialog({
        height: 200,
        resizable: false,
        modal: true,
        autoOpen: false,
        closeOnEscape: true,
        closeText: ""
    });
    
    toggleCustomer(customerCheckPath);
   
});

$(document).on("change", "form .selectAjax", function(e){ 
    $.ajax({
        type: "GET",
        url: "/pa165/reservations/ajaxGetExcursions/"  + $(this).val(),
        data: "",
        async: true,
        success: function (data) {
            $("#excursionsAjaxContainer").html(data);
        }
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

$(document).on("click", ".showHidden", function(e) {
    elem = $(this).closest("tr");
    while(elem.next("tr").attr("class") === "hidden") {
        elem = elem.next("tr");
        elem.toggle();
    }
});

$(document).on("click", ".noPropagation", function(e) {
    e.stopPropagation();
});

$(document).on("click", customerCheckPath, function () {
    toggleCustomer(customerCheckPath);
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

function showFailureMessage() {
    $('#failureMessage').fadeIn('slow').delay(4000).fadeOut('slow');
}

function redirect(url) {
    window.location=url;
}

function toggleCustomer(selector) {
    disable = !$(selector).prop('checked');
    $("form input#customer-name, form input#customer-address").attr('readonly', disable).val(disable ? "" : $(this).val());
}

