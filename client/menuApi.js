// api for menu presntion on the user app
$(document).ready(function() {
	menu.sort(function(a, b){// menu object sortion by dish cat before building for each category a table
		return a.dish_cat > b.dish_cat;
	});
	var tempCat=menu[0]['dish_cat'];
	console.log(tempCat);
	//table creation for first category
	var tbl=$("<table/>").attr("id","mytable");
	$(".table-div").append(tbl);
	var str="<h3>"+tempCat+"</h3>";
	$(".table-div").append(str);
	var td="<tr>";
	var td1="<td>שם המנה</td>";
	var td2="<td>תיאור המנה</td>";
	var td3="<td>מחיר המנה</td></tr>";
	$(".table-div").append(td+td1+td2+td3);
	var tr="<tr>";
	//data insertion into the table
	var td1="<td>"+menu[0]['dish_name']+"</td>";
	var td2="<td>"+menu[0]["dish_description"]+"</td>";
	var td3="<td>"+menu[0]["dish_price"]+"</td></tr>";
	//appending the created table into the view file
		$(".table-div").append(tr+td1+td2+td3);
	// appendting all the other dishes for each table by its category
	for(var i=1;i<menu.length;i++)
	{
		if(typeof menu[i]!=='undefined' && typeof menu[i]['dish_cat']!=='undefined')
		{
			if(tempCat.localeCompare(menu[i]['dish_cat'])==0)//same category
			{
				var tr="<tr>";
				var td1="<td>"+menu[i]['dish_name']+"</td>";
				var td2="<td>"+menu[i]["dish_description"]+"</td>";
				var td3="<td>"+menu[i]["dish_price"]+"</td></tr>";
				$(".table-div").append(tr+td1+td2+td3);

			}
			else// case new cat
			{
				var tempCat=menu[i]['dish_cat'];
		   	var str="<h3>"+tempCat+"</h3>";
				$(".table-div").append(str);
				var td="<tr>";
				var td1="<td>שם המנה</td>";
				var td2="<td>תיאור המנה</td>";
				var td3="<td>מחיר המנה</td></tr>";
				$(".table-div").append(td+td1+td2+td3);
				var tr="<tr>";
				var td1="<td>"+menu[i]['dish_name']+"</td>";
				var td2="<td>"+menu[i]["dish_description"]+"</td>";
				var td3="<td>"+menu[i]["dish_price"]+"</td></tr>";
				$(".table-div").append(tr+td1+td2+td3);
			}
		}
	}

});
