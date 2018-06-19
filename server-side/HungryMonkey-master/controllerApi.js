
$(document).ready(function() {
  var max_fields      = 100; //maximum input boxes
  var ajaxResults=[];
  var wrapper         = $(".fieldList"); //Fields wrapper
  var add_button      = $("#add_field_button"); //Add button ID
  var done_button      = $(".done_button"); //Add button ID
  var x = 0; //initlal text box count
  var y = 0;// counter for images
  var formDataMenu = new FormData();
  var tempDishNumber;
  var tempChar;
  var temp;
  $('.showMenu').hide();
  $(add_button).click(function(e){ //on add input button click
    e.preventDefault();
    $('.showMenu').hide();
    if(x < max_fields){ //max input box allowed
      x++; //text box increment
      $(wrapper).append("	<br><span name='"+x+"'><p></p><label for='menu["+x+"][dish_cat]'>קטגוריה</label>"
      +"<input class='form-control' type='text' name='menu["+x+"][dish_cat]' placeholder='קטגוריה'>"
      +"<label for='menu["+x+"][dish_name]'>מנה</label>"
      +"<input class='form-control' type='text' name='menu["+x+"][dish_name]' placeholder='שם המנה'>"
      +"<label for='menu["+x+"][dish_description]'>תיאור המנה</label>"
      +"<input class='form-control' type='text' name='menu["+x+"][dish_description]'placeholder='תיאור המנה'>"
      +"<label for='menu["+x+"][dish_price]'>מחיר המנה</label>"
      +"<input class='form-control' type='text' name='menu["+x+"][dish_price]'placeholder='מחיר המנה'>"
      +"<label>תמונה</label>"
      +"<input class='form-control' type='file' id='image_file' name='menu["+x+"][imgUploader]'>"
      +"<img id='img"+x+"' src='#'/><br>"
      +"<button id='removebb' type='button' class='btn icon-btn btn-danger' href='#'><span class='glyphicon btn-glyphicon glyphicon-trash img-circle text-danger'></span>הסר</button></span>");
    }
  });
  $(wrapper).on("click", "#removebb", function(){ //user click on remove text
    if(x>0)
    {
      $(this).parent().prev('br').addBack().detach();
      x--;
    }
  });
  $(wrapper).on('change','#image_file',function(ev){// upload image and store the url in array
    tempDishNumber=$(this).closest('span').attr('name');
    console.log("y: "+y);
    console.log("x: "+x);
    console.log("dish number:"+tempDishNumber);
    if(x==0){// case we upload image for first dish
        var image_file = $("[name='menu["+0+"][imgUploader]'").get(0).files[0];
        temp=0;
    }
    else{// case not
          var image_file = $("[name='menu["+tempDishNumber+"][imgUploader]'").get(0).files[0];
          temp=tempDishNumber;
    }
    readURL(this,temp);
    $("#img"+temp).css('visibility','visible');
      $("#img"+temp).css('width','25%');
    var formData = new FormData();
    formData.append("imgUploader", image_file);
    formData.append("flag",1);
    $.ajax({
      url: "http://localhost:3000/api/v1/set_menu/"+restid, // Url to which the request is send
      type: "POST",             // Type of request to be send, called as method
      data: formData, // Data sent to server, a set of key/value pairs (i.e. form fields and values)
      contentType: false,       // The content type used when sending data to the server.
      cache: false,             // To unable request pages to be cached
      processData:false,        // To send DOMDocument or non processed data file it is set to false
      success: function  (res) {
        //	console.log(res.location);
        if(x==0)
        {
          res.location+='0';
        }
        else
        {
          res.location+=tempDishNumber.toString();
        }
        console.log(res.location);
        ajaxResults.push(res.location);
        console.log(ajaxResults);
      },
      async: false,
      crossDomain:true
    });
    y++;
  });
  (function($){
    $.fn.serializeObject = function(){

      var self = this,
      json = {},
      push_counters = {},
      patterns = {
        "validate": /^[a-zA-Z][a-zA-Z0-9_]*(?:\[(?:\d*|[a-zA-Z0-9_]+)\])*$/,
        "key":      /[a-zA-Z0-9_]+|(?=\[\])/g,
        "push":     /^$/,
        "fixed":    /^\d+$/,
        "named":    /^[a-zA-Z0-9_]+$/
      };


      this.build = function(base, key, value){
        base[key] = value;
        return base;
      };

      this.push_counter = function(key){
        if(push_counters[key] === undefined){
          push_counters[key] = 0;
        }
        return push_counters[key]++;
      };

      $.each($(this).serializeArray(), function(){

        // skip invalid keys
        if(!patterns.validate.test(this.name)){
          return;
        }

        var k,
        keys = this.name.match(patterns.key),
        merge = this.value,
        reverse_key = this.name;

        while((k = keys.pop()) !== undefined){

          // adjust reverse_key
          reverse_key = reverse_key.replace(new RegExp("\\[" + k + "\\]$"), '');

          // push
          if(k.match(patterns.push)){
            merge = self.build([], self.push_counter(reverse_key), merge);
          }

          // fixed
          else if(k.match(patterns.fixed)){
            merge = self.build([], k, merge);
          }

          // named
          else if(k.match(patterns.named)){
            merge = self.build({}, k, merge);
          }
        }

        json = $.extend(true, json, merge);
      });

      return json;
    };
  })(jQuery);

  $('.container').on('click','.showMenu',function(ev){
    ev.preventDefault();

    $('#myModal').modal('show');
  });

  $('#confirm-save-button').on('click', function(ev) {
    ev.preventDefault();// cancel form submission
    //alert('Saved!!');
    $.ajax({
      url: "http://localhost:3000/api/v1/set_menu/"+restid, // Url to which the request is send
      type: "POST",             // Type of request to be send, called as method
      data: formDataMenu, // Data sent to server, a set of key/value pairs (i.e. form fields and values)
      contentType: false/*'application/json'*/,       // The content type used when sending data to the server.
      cache: false,             // To unable request pages to be cached
      processData:false,        // To send DOMDocument or non processed data file it is set to false
      success: function  (res) {  },
      //async: false,
      crossDomain:true
    });
    $('#myModal').modal('hide');
  });

  $('#confirm-cancel-button').on('click', function(ev) {
    ev.preventDefault();// cancel form submission
    $('#myModal').val("");
    $('.showMenu').hide();
    $('#myModal').data('modal', null);
  });

  $(function(){
    $('#set-menu').submit(function(ev){
      $.post($(this).attr('action'));
      $('.showMenu').show();
      var temp=$(this).serializeObject();
      temp.menu.sort(function(a, b){
        return a.dish_cat > b.dish_cat;
      });
      var tempCat=temp.menu[0]['dish_cat'];
      //console.log("ajax array"+ajaxResults);
      //first table for the first category
      var tbl=$("<table/>").attr("id","mytable");
      $(".modal-body").append(tbl);
      var str="<h3>"+tempCat+"</h3>";
      $(".modal-body").append(str);
      var td="<tr>";
      var td1="<td>שם המנה</td>";
      var td2="<td>תיאור המנה</td>";
      var td3="<td>מחיר המנה</td></tr>";
      $(".modal-body").append(td+td1+td2+td3);
      var tr="<tr>";
      var td1="<td>"+temp.menu[0]['dish_name']+"</td>";
      var td2="<td>"+temp.menu[0]["dish_description"]+"</td>";
      var td3="<td>"+temp.menu[0]["dish_price"]+"</td></tr>";
      $(".modal-body").append(tr+td1+td2+td3);
      // sort all the dishes by category into tables
      for(var i=1;i<temp.menu.length;i++)
      {
        if(typeof temp.menu[i]!=='undefined' && typeof temp.menu[i]['dish_cat']!=='undefined')
        {
          if(tempCat.localeCompare(temp.menu[i]['dish_cat'])==0)//same category
          {
            var tr="<tr>";
            var td1="<td>"+temp.menu[i]['dish_name']+"</td>";
            var td2="<td>"+temp.menu[i]["dish_description"]+"</td>";
            var td3="<td>"+temp.menu[i]["dish_price"]+"</td></tr>";
            $(".modal-body").append(tr+td1+td2+td3);
          }
          else// case new cat
          {
            var tempCat=temp.menu[i]['dish_cat'];
            var str="<h3>"+tempCat+"</h3>";
            $(".modal-body").append(str);
            var td="<tr>";
            var td1="<td>שם המנה</td>";
            var td2="<td>תיאור המנה</td>";
            var td3="<td>מחיר המנה</td></tr>";
            $(".modal-body").append(td+td1+td2+td3);
            var tr="<tr>";
            var td1="<td>"+temp.menu[i]['dish_name']+"</td>";
            var td2="<td>"+temp.menu[i]["dish_description"]+"</td>";
            var td3="<td>"+temp.menu[i]["dish_price"]+"</td></tr>";
            $(".modal-body").append(tr+td1+td2+td3);
          }
        }
      }
      var tempUrlsObj=JSON.stringify(ajaxResults);
      var tempFormObj=JSON.stringify(temp);
      var tempFormObj=JSON.parse(tempFormObj);
      var tempUrlsObj=JSON.parse(tempUrlsObj);
      for (var i = 0; i < tempFormObj.menu.length; i++) {
        for(var j=0;j<tempUrlsObj.length;j++){
          if(typeof temp.menu[i]!=='undefined')
          {
            tempChar=tempUrlsObj[j].slice(-1);//get the dish number which the images belongs
            if(i.toString()==tempChar)
            {
              tempFormObj.menu[i].imgUrl=tempUrlsObj[j].substring(0,tempUrlsObj[j].length-1);//redusing the number of dish from each dis before we submiting the form
            }
          }
        }
      }
      console.log(tempFormObj.menu);
      formDataMenu.set("menu",JSON.stringify(tempFormObj));
      return false;
    });
  });
  $("#myModal").on("hidden.bs.modal", function(){
    //alert("in here");
    $('.showMenu').hide();
    $(".modal-body").html("");
    $('#myModal').data('modal', null);
  });


function readURL(input,temp){
  if (input.files && input.files[0]) {
    var tempDishNumber;
    console.log(temp);
    var reader = new FileReader();
    reader.onload = function(e) {
      var img="#img"+temp;
      console.log(img);
      $(img).attr('src', e.target.result);
    }
    reader.readAsDataURL(input.files[0]);
  }
}
});