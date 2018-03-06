


$(document).ready(function() {
    var max_fields      = 100; //maximum input boxes allowed
    var wrapper         = $(".fieldList"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID

    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
		 
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            $(wrapper).append("<input type='text' name='menu["+x+"][dish_name]' placeholder='שם המנה'><br>"); //add input box	
			$(wrapper).append("<label for='menu["+x+"][dish_name]'>:מנה</label>");
			$(wrapper).append("<input type='text' name='menu["+x+"][dish_description]'placeholder='תיאור המנה'><br>"); //add input box
			$(wrapper).append("<label for='menu["+x+"][dish_description]'>:תיאור המנה</label>");				
			$(wrapper).append("<input type='text' name='menu["+x+"][dish_price]'placeholder='מחיר המנה'><br>"); //add input box	
			$(wrapper).append("<label for='menu["+x+"][dish_price]'>:מחיר המנה</label>");
			$(wrapper).append("<br>");
			   console.log("balls");
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('div').remove(); x--;
    })
});