
exports.createObj = (obj,id) =>

	var tempbol=false;
	var kosher='כשר';	
	var tempKosher=req.body.rest_Kosher;
	if(kosher.localeCompare(req.body.rest_Kosher)==0)
	{
		tempbol=true;
	}
	var menu={'menus'};
	menu.forEach(item => {
      menu.push({
        index: {
          _index: index,
          _type: type,
          _id: item.id
        }
      });
					:{
								'dish': {
									'dish_name' : req.body.dish_1,
									'dish_description' : req.body.dish1_desc,
									'dish_price':req.body.dish1_price,
									'dish_id_inRest':id.1
								}
					}};


	