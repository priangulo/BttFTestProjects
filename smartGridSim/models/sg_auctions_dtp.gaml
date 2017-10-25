/**
 *  CircleGrid
 *  Author: Priscila Angulo
 *			Carl W. Handlin
 *  Description:
 * 	Icons made by Freepik from www.flaticon.com is licensed under CC BY 3.0
 *	https://creativecommons.org/licenses/by/3.0/
 */

model sg_auctions_dtp

/* Insert your model definition here */

global {
	//int debug <- 1;
	int cycle_length <- 1439;
	int debug_agentdb <- 0;
	int debug_house <- 0;
	int debug_transformer <- 0;
	int debug_powerline <- 0;
	int debug_generator <- 0;
	int print_results <- 1;
	
	graph general_graph;
	float totalenergy_smart <- 0.0;
	float totalenergy_nonsmart <- 0.0;
	int time_step <- 0;
	
	int grid_width <- 200;
	int grid_height <- 200;

    int num_houses <- 27;
    int num_transformers <- 9;
    int num_lines <- 3;
    int num_generator <- 1;
    
    float degree_house <- (360 / num_houses); 
	float degree_transformer <- (360 / num_transformers);
	float degree_lines <- (360 / num_lines);
	
    int radius_house <- 50;
    int radius_transformer <- 30;
    int radius_lines <- 15;
    int radius_appliance <- 4;

    int min_household_profile_id;
    int max_household_profile_id;
    
    float base_price <- 1.00; //per kwh
    float power_excess_smart <- 0.00;
    float power_excess_nonsmart <- 0.00;

    float transformer_power_capacity_smart <- 10.0; //KW
    float transformer_power_capacity_nonsmart <- 10.0; //KW
    float powerline_power_capacity_smart <- 30.0; //KW
    float powerline_power_capacity_nonsmart <- 30.0; //KW
    float generator_max_production_smart <- 60.0; //KW
    float generator_max_production_nonsmart <- 60.0; //KW

    float generator_current_production_smart <- 40.0; //KW
    float generator_current_production_nonsmart <- 40.0; //KW
    
    string production_function_smart <- "Max";
    string production_function_nonsmart <- "Max";
    
    string price_function <- "Cosine";
    
    //for cosine price function
    float price_cosine_base <- 1.25;
    float price_cosine_bound <- 0.25;
    
    //for constant price function
    float price_constant <- 1.0;
    
    // MySQL connection parameter
	map<string, string>  MySQL <- [
    'host'::'localhost',
    'dbtype'::'MySQL',
    'database'::'smartgrid_demandprofiles', // it may be a null string
    'port'::'3306',
    'user'::'smartgrid',
    'passwd'::'smartgrid'];
    
    init {
            create agentDB number: 1;
			ask agentDB{
				do get_household_profiles_ids;	
			}
			create house number: num_houses ;
			create transformer number: num_transformers;
			create powerline number: num_lines;
			create generator number: num_generator;
			do build_graph;
    }
    
    action build_graph {
	  	general_graph <- graph([]);
	  	loop gn over: generator {
	  		loop pl over: powerline {
	  			create edge_agent with: [shape::link({gn.my_x, gn.my_y}::{pl.my_x, pl.my_y})] returns: edges_created;
	  			add edge:(gn::pl)::first(edges_created) to: general_graph;
	  			ask(powerline(pl)){
	  				my_generator_index <- generator(gn).my_index;
	  			}
	  		}
	  		
	  	}
	  	
	  	loop pl_2 over: powerline {
	  		ask pl_2{
	  			do get_my_transformers;	
	  		}
	  		loop tr over: (pl_2.my_transformers) {
	  			create edge_agent with: [shape::link({pl_2.my_x, pl_2.my_y}::{tr.my_x, tr.my_y})] returns: edges_created;
	  			add edge:(pl_2::tr)::first(edges_created) to: general_graph;
	  			ask(transformer(tr)){
	  				my_powerline_index <- powerline(pl_2).my_index;
	  			}
	  		}
	  	} 
	  	
	  	loop tr_2 over: transformer{
	  		ask tr_2{
	  			do get_my_houses;	
	  		}
	  		loop hs over: (tr_2.my_houses) {
	  			create edge_agent with: [shape::link({tr_2.my_x, tr_2.my_y}::{hs.my_x, hs.my_y})] returns: edges_created;
	  			add edge:(tr_2::hs)::first(edges_created) to: general_graph;
	  			ask house(hs){
	  				my_transformer_index <- transformer(tr_2).my_index;
	  			}
	  		}
	  	} 
	  	//write general_graph;
	 }
	 
	reflex restart_power_and_time{
	 	totalenergy_smart <- 0.0;
	 	totalenergy_nonsmart <- 0.0;
	 	
	 	loop gn over: generator {
	 		gn.demand_smart  <- 0.0;
	 		gn.demand_nonsmart  <- 0.0;
	 	}
	 	
	 	loop pl over: powerline {
	 		pl.demand_smart  <- 0.0;
	 		pl.demand_nonsmart  <- 0.0;
	 	}
	 	
	 	loop tr over: transformer {
	 		tr.demand_smart  <- 0.0;
	 		tr.demand_nonsmart  <- 0.0;
	 	}
	 	if( time_step = ( cycle_length + 1 ))
	 	{
	 		do halt;
	 	}
	 	else
	 	{
	 		time_step <- time_step + 1;
	 	}
 	}	  
}

//AgentDB
species agentDB parent: AgentDB { 
	float get_coordinate_x (int radius, int index, float degree){
		return ((radius *(cos(index*degree))) + (grid_width/4));
	}
	
	float get_coordinate_y (int radius, int index, float degree){
		return ((radius *(sin(index*degree))) + (grid_height/4));
	}
	
	action check_db_connection{
		if (debug_agentdb = 1)
		{
	    	if (self testConnection(params:MySQL)){
	        	write "Connection is OK" ;
			}else{
	        	write "Connection is false" ;
			}
		}
    }
	
	action get_household_profiles_ids
	{
		do connect (params: MySQL);
		
		list<list> profileMin <- list<list> ( self select(select:"SELECT min(id_household_profile) FROM household_profiles;"));
		min_household_profile_id <- int (profileMin[2][0][0]);
		
		list<list> profileMax <- list<list> ( self select(select:"SELECT max(id_household_profile) FROM household_profiles;"));
		max_household_profile_id <- int (profileMax[2][0][0]);
	} 
	
	action recursive_combinations (int r, list<int> elements, list<int> combination, int device_id, int type)
    {
    	int length_elements <- length(elements);
    	
    	if (r = 1)
		{
			loop i from: 0 to: (length_elements - 1)
			{
				list<int> new_combination <- [];
				new_combination <- new_combination + combination;
				new_combination <- new_combination + elements[i];
								
				switch type{
					match 0 //type house
					{
						add new_combination to: house(device_id).all_combinations;
					}
					match 1 //type transfomer
					{
						add new_combination to: transformer(device_id).all_combinations;
					}
					match 2 //type powerline
					{
						add new_combination to: powerline(device_id).all_combinations;
					}
					match 3 //type generator
					{
						add new_combination to: generator(device_id).all_combinations;	
					}
				}
				
			}  			
		}
    	else{	
	    	loop i from: 0 to: (length_elements - r)    
	    	{
    			list<int> new_combination <- [];
    			new_combination <- new_combination + combination;
    			new_combination <- new_combination + elements[i]; 
    			
    			list<int> new_elements <- [];
    			loop j from: i+1 to: (length_elements - 1)
    			{
    				add	elements[j] to: new_elements;
    			} 
    			
    			do recursive_combinations( r-1, new_elements, new_combination, device_id, type );
	    	} 
    	}
    }
	
	init{
		do check_db_connection;
	}
}

//House
species house parent: agentDB {
    int house_size <- 4;
    int my_index <- house index_of self;
    int my_transformer_index;
    int houseprofile <- my_index + 1; //rnd(max_household_profile_id - min_household_profile_id) + min_household_profile_id; //598
    int num_appliances;
    float smart_budget; 
    list<int> pending_smart_appliances <- [];
    list<list> combinations_power_sum_tick;
	list<list> combinations_bids_sum_tick;
	list<float> combinations_bids_sum;
	list<float> combinations_power_sum;
    list<list> all_combinations;
    int better_combination_index;
    float better_combination_bid;
    float total_bid <- 0.0;
    float total_power;
    bool enough_budget <- true;
    
    list<int> my_smart_appliances <- [];
    list<list> list_appliances_db;
    file my_icon <- file("../images/House.gif");
    float demand_smart <- 0.0;
    float demand_nonsmart <- 0.0;
    
    float my_x <- get_coordinate_x(radius_house, my_index, degree_house);
    float my_y <- get_coordinate_y(radius_house, my_index, degree_house);
    
    float degree_appliance;
    int priority_to_assign;
            
    aspect base {
		draw sphere(house_size) color: rgb('blue') at: {my_x , my_y, 0 } ;
	}
	
	aspect icon {
        draw my_icon size: house_size at: {my_x , my_y, 0 } ;
    }
	
	action get_my_appliances{
		ask agentDB{
			myself.list_appliances_db <- list<list> (self select(select:"SELECT DISTINCT a.id_appliance, a.appliance_description FROM appliances a JOIN appliances_profiles ap ON a.id_appliance = ap.id_appliance WHERE ap.id_household_profile = "+ myself.houseprofile +" AND a.isSmart = 1;"));	
		}
		
		num_appliances <- length( (list_appliances_db[2]) );
		degree_appliance <- (360 / (num_appliances + 1) ); //+1 because of other loads
		list<int> priorities <- []; 
		smart_budget <- (rnd(10)/10 + 13) * num_appliances;
		
		loop i from: 0 to: (num_appliances - 1){
			add (i+1) to: priorities;
		}
		
 		loop i from: 1 to: num_appliances{
 			create smart_appliance number: 1 returns: appliance_created;
 			
 			priority_to_assign <- one_of(priorities);
 			remove priority_to_assign from: priorities;   

			ask appliance_created{
				appliance_id <- int (myself.list_appliances_db[2][i-1][0]);
				appliance_name <- string (myself.list_appliances_db[2][i-1][1]);
				priority <- myself.priority_to_assign;
				houseprofile <-  myself.houseprofile;
				do get_power_day;
			}
 	  	}
 	  	
 	  	loop ap over: (members of_species smart_appliance){
 	  		add ap.my_appliance_index to: my_smart_appliances;
 	  		//add ap.my_appliance_index to: pending_smart_appliances;	
 	  	}
 	  	
 	  	create other_loads number: 1 returns: other_loads_created;
 	  	ask other_loads_created{
 	  		houseprofile <-  myself.houseprofile;
 	  		do get_power_day;
 	  	}
 	  	
	}
	 
	reflex get_demand{
		if (debug_house = 1)
		{
			write("house: " + my_index + " transformer: " + my_transformer_index + " house demand_smart: " + demand_smart);
			write("house: " + my_index + " transformer: " + my_transformer_index + " house demand_nonsmart: " + demand_nonsmart);
		}
		
		//for this step the house only has the sum of other loads demand 
		transformer(my_transformer_index).demand_nonsmart <- transformer(my_transformer_index).demand_nonsmart + demand_nonsmart;
		transformer(my_transformer_index).demand_smart <- transformer(my_transformer_index).demand_nonsmart + demand_smart;
		
		do combinatorial_auction;
		pending_smart_appliances <- [];
		
		if (time_step = (cycle_length + 1) and print_results = 1)
		{
			int powerline_index <- transformer(my_transformer_index).my_powerline_index;
			write("SMARTBUDGET;Powerline" + powerline_index + ";Transformer" + my_transformer_index + ";House" + my_index + ";" + smart_budget);
			write("ENOUGHBUDGET;Powerline" + powerline_index + ";Transformer" + my_transformer_index + ";House" + my_index + ";" + enough_budget);
		}
	}
	
	action combinatorial_auction{
    	do get_combinations;
    	do get_combinations_sum;
    	do remove_exceeded_combinations;
    	do get_best_combination;
    }
    
	action get_combinations{
		all_combinations <- [];
		list<int> combination <- [];
		list<int> elements <- pending_smart_appliances;
		int num_elements <- length(elements);
		if (num_elements > 0){
			loop i from:1 to: num_elements{
				do recursive_combinations(i, elements, combination, my_index, 0);	
			}
			if (debug_house = 1)
			{
				write("House: " + my_index + " elements: " + elements);
				write("House: " + my_index + " combinations: " + all_combinations);
			}
		}
		else if (debug_house = 1)
		{
			write("House: " + my_index + " no more pending appliances" + " remaining smart_budget: " + smart_budget);
		}
	}
    
    action get_combinations_sum{
    	combinations_power_sum_tick <- [];
    	combinations_bids_sum_tick <- [];
    	
		int combination_index <- -1;
		loop comb over: all_combinations{
			add [] to: combinations_power_sum_tick;
			add [] to: combinations_bids_sum_tick;
			combination_index <- combination_index + 1;
			
			int max_index_container <- -1;
			loop ap over: comb{
				int num_rows <- length(smart_appliance(ap).power);
		  		if (num_rows > 0)
		  		{
			  		loop i from: 0 to: num_rows - 1 {
			  			if (max_index_container < i)
			  			{
			  				add smart_appliance(ap).power[i] to: combinations_power_sum_tick[combination_index];
			  				add smart_appliance(ap).energybid[i] to: combinations_bids_sum_tick[combination_index];
			  				max_index_container <- max_index_container + 1; 
			  			}
			  			else
			  			{
			  				combinations_power_sum_tick[combination_index][i] <- float(combinations_power_sum_tick[combination_index][i]) + (smart_appliance(ap).power[i]);
			  				combinations_bids_sum_tick[combination_index][i] <- float(combinations_bids_sum_tick[combination_index][i]) + (smart_appliance(ap).energybid[i]);
			  			}
			  		}
		  		}
			}
		}
		if (debug_house = 1)
		{
			write("House: " + my_index + " combinations_power_sum_tick: " + combinations_power_sum_tick);
			write("House: " + my_index + " combinations_bids_sum_tick: " + combinations_bids_sum_tick);
    	}
    }
    
    action remove_exceeded_combinations
    {
    	list<int> exceeded_combinations <- [];
    	int length_combinations <- length(all_combinations);
    	combinations_bids_sum <- [];
    	combinations_power_sum <- [];
    	
    	if (length_combinations > 0)
    	{
	    	loop comb from: 0 to: length_combinations - 1
	    	{
	    		float sum_bid <- sum( list<float>(combinations_bids_sum_tick[comb]) );
	    		float sum_power <- sum( list<float>(combinations_power_sum_tick[comb]) ); 
	    		if(debug_house = 1)
	    		{
	    			write("House: " + my_index + " comb: " + comb + " sum_bid: " + sum_bid + " smart_budget: " + smart_budget);
	    		}
	    		if (sum_bid > smart_budget)
	    		{
	    			add 0 to: combinations_bids_sum;
	    			add 0 to: combinations_power_sum;
	    			add comb to: exceeded_combinations;
	    			combinations_power_sum_tick[comb] <- [];
		    		combinations_bids_sum_tick[comb]<- [];
	    		}
	    		else
	    		{
	    			add sum_bid to: combinations_bids_sum;
	    			add sum_power to: combinations_power_sum;
	    		}
	    	}
	    	
	    	int length_exceeded_comb <- length(exceeded_combinations);
	    	if (length_exceeded_comb >= length_combinations)
	    	{
	    		enough_budget <- false;
	    	}
	    	
			if (debug_house = 1)
			{    	
	    		write("House: " + my_index + " exceeded combinations: " + exceeded_combinations);
	    		write("House: " + my_index + " in competence combinations: " + combinations_power_sum_tick);
	    	}
    	}
    }
    
    action get_best_combination
    {
    	list<float> combinations_coef <- [];
    	
    	int length_combinations <- length(combinations_bids_sum);
    	if (length_combinations > 0)
    	{
	    	loop comb from: 0 to: length_combinations - 1
	    	{
	    		if (combinations_power_sum[comb] != 0)
	    		{
	    			add combinations_bids_sum[comb] / combinations_power_sum[comb] to: combinations_coef;
	    		}
	    		else
	    		{
	    			add 0 to: combinations_coef;
	    		}
	    	}
	    	
	    	if (debug_house = 1){ write("House: " + my_index + "combinations_coef: " + combinations_coef); }
	    	
	    	float min_coeficient <- min(combinations_coef where (each > 0.0));
    	
	    	if (min_coeficient > 0)
	    	{
		    	int min_coeficient_index <- combinations_coef last_index_of min_coeficient;
		    	
		    	better_combination_bid <- combinations_bids_sum[min_coeficient_index];
		    	better_combination_index <- min_coeficient_index;
		    	
		    	total_bid <- combinations_bids_sum[better_combination_index];
				total_power <- combinations_power_sum[better_combination_index];
		    	
		    	if (debug_house = 1)
				{
					write("House: " + my_index + " combinations_bids_sum: " + combinations_bids_sum);
		    		write("House: " + my_index + " better_combination_index: " + better_combination_index + " better_combination: " + better_combination_bid );
				}
			}
			else{
				better_combination_bid <- 0.0;
				better_combination_index <- -1;
				total_bid <- 0.0;
				total_power <- 0.0;
			}
		}
		else
		{
			better_combination_bid <- 0.0;
			better_combination_index <- -1;
			total_bid <- 0.0;
			total_power <- 0.0;
		}
    }

	action assign_power
    {
    	//reducir presupuesto
    	if (debug_house = 1)
		{
    		write("**ASSIGNED POWER House: " + my_index + "previous smart_budget: " + smart_budget + " spent money: " + total_bid + " final budget: " + (smart_budget - total_bid) );
    	}
    	smart_budget <- smart_budget - total_bid;
    	
    	loop ap over: all_combinations[better_combination_index]
    	{
    		smart_appliance(ap).got_energy <- true;
    	}
    	
    }
	
	init{
		do get_my_appliances;
		if (debug_house = 1)
		{
			write("house_index: " + my_index + " house_profile: " + houseprofile);
		}
	}
	
//Other loads (subspecies of house)
	species other_loads parent: agentDB {
		int appliance_size <- 1;
		int houseprofile;
		int my_appliance_index <- house(host).num_appliances;
		float my_appliance_x <- house(host).my_x + (radius_appliance *(cos(my_appliance_index*degree_appliance))); 
		float my_appliance_y <- house(host).my_y + (radius_appliance *(sin(my_appliance_index*degree_appliance)));
		file my_icon <- file("../images/Appliance.gif") ;
		list<list> energy;
		float current_demand;
		float current_power;
		
		reflex getdemand{
			current_power <- (float (energy[2][time_step-1][1]));
			current_demand <- current_power;

			house(host).demand_nonsmart <- 0.0;
		 	house(host).demand_nonsmart <- house(host).demand_nonsmart + current_demand;
		 	totalenergy_nonsmart <- totalenergy_nonsmart + current_demand;

			if (print_results = 1){
			 	int transfomer_index <- house(host).my_transformer_index;
				int powerline_index <- transformer(transfomer_index).my_powerline_index;
			 	write("" + time_step + ";NONSMARTPOWER;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";NonSmartAppliance" + my_appliance_index + ";" +current_power);
				write("" + time_step + ";NONSMARTMONEY;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";NonSmartAppliance" + my_appliance_index + ";" + (current_power > 0 ? base_price : 0.0));
			}
			
		}
			
		aspect appliance_icon {
			draw sphere(appliance_size) color: rgb("blue") at:{my_appliance_x, my_appliance_y, 0};
    	}
    	
    	action get_power_day{
    		ask agentDB{
				myself.energy <- list<list> (self select(select:"SELECT SUM(energy) energy, SUM(power) power, time FROM appliances_profiles WHERE id_household_profile = "+myself.houseprofile+" AND id_appliance NOT IN (SELECT id_appliance FROM appliances WHERE isSmart = 1) GROUP BY time ORDER BY time;"));	
			}
    	}	
	}	

//Smart Appliances  (subspecies of house)
	species smart_appliance  parent: agentDB {
		int appliance_size <- 1;
		int my_appliance_index <- smart_appliance index_of self;
		float my_appliance_x <- house(host).my_x + (radius_appliance *(cos(my_appliance_index*degree_appliance))); 
		float my_appliance_y <- house(host).my_y + (radius_appliance *(sin(my_appliance_index*degree_appliance)));
		file my_icon <- file("../images/Appliance.gif") ;
		string appliance_name;
		int appliance_id;
		int houseprofile;
		list<list> energyandpower;
		list<float> energybid;
		list<float> energy;
		list<float> power;
		float current_demand;
		int priority; //the higher the number the higher the priority
		bool got_energy <- false;
		bool zero_power <- false;
		bool enough_time <- true;
		int energy_index <- 0;
		int length_energy;
		
	    reflex getdemand{
	    	int transfomer_index <- house(host).my_transformer_index;
			int powerline_index <- transformer(transfomer_index).my_powerline_index;
	    	
	    	if (got_energy = true and length_energy > 0 and energy_index < length_energy)
	    	{
	    		if (debug_house = 1)
				{
	    			write ("house_index: " + my_index + " appliance_index: " + my_appliance_index + " demand: " + power[energy_index]);
			 	}
			 	
			 	current_demand <- power[energy_index];
			 	
			 	house(host).demand_smart <- 0.0;
			 	house(host).demand_smart <- house(host).demand_smart + current_demand;
			 	totalenergy_smart <- totalenergy_smart + current_demand;
			 	
			 	if (print_results = 1){
				 	write("" + time_step + ";SMARTPOWER;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";" +power[energy_index]);
				 	write("" + time_step + ";SMARTMONEY;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";" +energybid[energy_index]);
			 	}
			 	
			 	energy_index <- energy_index + 1;
	    	}
	    	else 
	    	{
	    		if (print_results = 1){
		    		write("" + time_step + ";SMARTPOWER;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";0.0");
				 	write("" + time_step + ";SMARTMONEY;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";0.0");
			 	}
			 	
	    		if ( got_energy = false and zero_power = false) {
		    		if ( /*enough_budget = true and */ ((cycle_length) - time_step) <= length_energy ){
		    			enough_time <- false;
		    		}
		    		if (enough_time = true) {
		    			add my_appliance_index to: house(host).pending_smart_appliances;
		    			do calculate_bid;
		    		}
		    	}
	    	
	    	}
	    	
	    	if(time_step = (cycle_length + 1) and print_results = 1)
	    	{
	    		
	    		if (zero_power = true)
	    		{
	    			write("GOTENERGY;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";true");
	    			write("ENOUGHBUDGETAPP;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";true");
	    			write("ENOUGHTIME;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";true");
	    		}
	    		else
	    		{
	    			write("GOTENERGY;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";" + ((got_energy = true) ? "true" : "false"));
	    			write("ENOUGHBUDGETAPP;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";" + ((got_energy = false and enough_time = true) ? "false" : "true"));
	    			write("ENOUGHTIME;Powerline" + powerline_index + ";Transformer" + transfomer_index + ";House" + my_index + ";SmartAppliance" + my_appliance_index + ";" + ((enough_time = true) ? "true" : "false"));
	    		}
	    		
	    	}
		}
		
		aspect appliance_base {
			draw sphere(appliance_size) color: rgb('purple') at:{my_appliance_x, my_appliance_y, 0};
		}
		
		aspect appliance_icon {
			if(got_energy = true)
			{
				draw sphere(appliance_size) color: rgb("red") at:{my_appliance_x, my_appliance_y, 0};
			}
			else if(zero_power = true)
			{
				draw sphere(appliance_size) color: rgb("purple") at:{my_appliance_x, my_appliance_y, 0};
			}
			else if(enough_budget = false)
			{
				draw sphere(appliance_size) color: rgb("cyan") at:{my_appliance_x, my_appliance_y, 0};
			}
			else if(enough_time = false)
			{
				draw sphere(appliance_size) color: rgb("green") at:{my_appliance_x, my_appliance_y, 0};
			}
			else
			{
				draw sphere(appliance_size) color: rgb("gray") at:{my_appliance_x, my_appliance_y, 0};
			}

    	}
    	
    	action get_power_day{
    		ask agentDB{
				myself.energyandpower <- list<list> (self select(select:"SELECT energy, power FROM appliances_profiles WHERE id_appliance = "+myself.appliance_id+" AND id_household_profile = "+myself.houseprofile+" AND power != 0 ORDER BY time;"));
			}
			do get_energy_power_bid;
    	}
    	
    	action get_energy_power_bid{
    		int num_rows <- length( (energyandpower[2]) );
    		if (num_rows > 0){
	    		loop i from: 0 to: (num_rows - 1){
	    			add (float(energyandpower[2][i][0])) to: energy;
	    			add (float(energyandpower[2][i][1])) to: power;
	    		}
    		}
    		else{
    			zero_power <- true;
    		}
    		length_energy <- length(energy);
    		do calculate_bid;
    	}
    	
    	action calculate_bid{
    		energybid <- [];
    		if (length_energy > 0){
    			loop i from: 0 to: (length_energy - 1){
    				add (energy[i] * base_price * priority * (rnd(5)/10 + 0.5)) to: energybid;	
    			}
    		}
    	}
	}
}

//Transformers
species transformer parent: agentDB {
    int transformer_size <- 4;
    int my_index <- transformer index_of self;
    int my_powerline_index;
    list<house> my_houses <- [];
    file my_icon <- file("../images/Transformer.gif") ;
    float demand_smart;
    float demand_nonsmart;
    list<float> available_nonsmart_power_per_tick;
    list<float> available_smart_power_per_tick;
    list<list> combinations_power_sum_tick;
	list<list> combinations_bids_sum_tick;
	list<float> combinations_bids_sum;
	list<list> all_combinations;
    int better_combination_index;
    float better_combination_bid;
    
    float my_x <- get_coordinate_x(radius_transformer, my_index, degree_transformer);
    float my_y <- get_coordinate_y(radius_transformer, my_index, degree_transformer);
	
	float distance <- ceil((radius_house-radius_transformer)/cos(degree_house)) + 1;
	
	init{
		loop i from: 0 to: cycle_length{
			add transformer_power_capacity_nonsmart to: available_nonsmart_power_per_tick;
			add transformer_power_capacity_smart to: available_smart_power_per_tick; 
		}
	}
	
    aspect base {
		draw sphere(transformer_size) color: rgb('green') at: {my_x , my_y, 0 } ;
	}
	
	aspect icon {
        draw my_icon size: transformer_size at: {my_x , my_y, 0 } ;
    }
	
	action get_my_houses{
		loop hs over: (species(house)) {
			if ( sqrt( (hs.my_x - my_x)^2 + (hs.my_y - my_y)^2 ) <= distance )
			{
				add hs to: my_houses;
			} 
    	}
    }
    
    reflex get_demand{
    	if (debug_transformer = 1)
		{
			write("transformer: " + my_index + " powerline: " + my_powerline_index + " demand_nonsmart: " + demand_nonsmart);
			write("transformer: " + my_index + " powerline: " + my_powerline_index + " demand_smart: " + demand_smart);
		}
		
		do combinatorial_auction;
		
    	powerline(my_powerline_index).demand_nonsmart <- powerline(my_powerline_index).demand_nonsmart + demand_nonsmart;
    	powerline(my_powerline_index).demand_smart <- powerline(my_powerline_index).demand_smart + demand_smart;
    	
    	if (print_results = 1){
    		write("" + time_step + ";Transformer" + my_index + ";exceed_flag_smart;" + ( demand_smart  - transformer_power_capacity_smart ) );
    		write("" + time_step + ";Transformer" + my_index + ";exceed_flag_nonsmart;" + ( demand_nonsmart - transformer_power_capacity_nonsmart ) );
    	}
    }
    
    action combinatorial_auction{
    	do get_combinations;
    	do get_combinations_sum;
    	do remove_exceeded_combinations;
    	do get_best_combination;
    }
    
	action get_combinations{
		all_combinations <- [];
		list<int> combination <- [];
		list<int> elements <- [];
		
		loop hs over: list<int>(my_houses)
		{
			if house(hs).better_combination_index != -1
			{
				add hs to: elements;
			}
		}
		
		int num_elements <- length(elements);
		if (num_elements > 0){
			loop i from:1 to: num_elements{
				do recursive_combinations(i, elements, combination, my_index, 1);	
			}
			if (debug_transformer = 1)
			{
				write("Transformer: " + my_index + " elements: " + elements);
				write("Transformer: " + my_index + " combinations: " + all_combinations);
			}
		}
		else if (debug_transformer = 1)
		{
			write("Transformer: " + my_index + " no more pending houses");
		}
	}
    
    action get_combinations_sum{
    	combinations_power_sum_tick <- [];
    	combinations_bids_sum_tick <- [];
    	
		int combination_index <- -1;
		loop comb over: all_combinations{
			add [] to: combinations_power_sum_tick;
			add [] to: combinations_bids_sum_tick;
			combination_index <- combination_index + 1;
			
			int max_index_container <- -1;
			loop hs over: comb{
		  		int hs_better_combination_index <- house(hs).better_combination_index;
		  		int num_rows <- length(house(hs).combinations_power_sum_tick[hs_better_combination_index]);
		  		if (num_rows > 0)
		  		{
			  		loop i from: 0 to: num_rows - 1 {
			  			if (max_index_container < i)
			  			{
			  				add house(hs).combinations_power_sum_tick[hs_better_combination_index][i] to: combinations_power_sum_tick[combination_index];
			  				add house(hs).combinations_bids_sum_tick[hs_better_combination_index][i] to: combinations_bids_sum_tick[combination_index];
			  				max_index_container <- max_index_container + 1; 
			  			}
			  			else
			  			{
			  				combinations_power_sum_tick[combination_index][i] <- float(combinations_power_sum_tick[combination_index][i]) + float(house(hs).combinations_power_sum_tick[hs_better_combination_index][i]);
			  				combinations_bids_sum_tick[combination_index][i] <- float(combinations_bids_sum_tick[combination_index][i]) + float(house(hs).combinations_bids_sum_tick[hs_better_combination_index][i]);
			  			}
			  		}
		  		}
			}
		}
		if (debug_transformer = 1)
		{
			write("Transformer: " + my_index + " combinations_power_sum_tick: " + combinations_power_sum_tick);
			write("Transformer: " + my_index + " combinations_bids_sum_tick: " + combinations_bids_sum_tick);
    	}
    }
    
    action remove_exceeded_combinations
    {
    	list<int> exceeded_combinations <- [];
    	int length_combinations <- length(all_combinations);
    	if (length_combinations > 0)
    	{
	    	loop comb from: 0 to: length_combinations - 1
	    	{
	    		int length_powerlist <- length(combinations_power_sum_tick[comb]);
	    		if (length_powerlist > 0)
	    		{ 
	    			bool exceeds <- false;
		    		int i <- 0;
		    		loop while: (exceeds = false) and (i < length_powerlist)
		    		{
		    			if (time_step < cycle_length)
		    			{
			    			if (float(combinations_power_sum_tick[comb][i]) > available_smart_power_per_tick[time_step + i])
			    			{
			    				exceeds <- true;
			    				add comb to: exceeded_combinations;
			    			}
						}
		    			i <- i + 1;
		    		}
		    		if (exceeds = true) //erases power list and bid list
		    		{
		    			combinations_power_sum_tick[comb] <- [];
		    			combinations_bids_sum_tick[comb]<- [];
		    		}
		    	}
	    	}
			if (debug_transformer = 1)
			{    	
	    		write("Transformer: " + my_index + " exceeded combinations: " + exceeded_combinations);
	    		write("Transformer: " + my_index + " in competence combinations: " + combinations_power_sum_tick);
	    	}
    	}
    }
    
    action get_best_combination
    {
    	combinations_bids_sum <- [];
    	int length_bidlist <- length(combinations_bids_sum_tick);
    	if (length_bidlist > 0)
    	{
	    	loop comb from: 0 to: length_bidlist-1
	    	{
	    		add sum(list<float>(combinations_bids_sum_tick[comb])) to: combinations_bids_sum;
	    	}
    		better_combination_bid <- max(combinations_bids_sum);
    		if (better_combination_bid > 0.0)
	    	{
		    	better_combination_index <- combinations_bids_sum last_index_of better_combination_bid;
		    	
		    	if (debug_transformer = 1)
				{
					write("Transformer: " + my_index + " combinations_bids_sum: " + combinations_bids_sum);
		    		write("Transformer: " + my_index + " better_combination_index: " + better_combination_index + " better_combination: " + better_combination_bid );
				}
			}
			else {
    			better_combination_index <- -1;
			}
    	}
    	else
    	{
    		better_combination_bid <- 0.0;
    		better_combination_index <- -1;
    	}
    }
    
    action assign_power
    {
    	if (better_combination_bid > 0){
	    	int length_rows <- length(combinations_power_sum_tick[better_combination_index]);
	    	if ( length_rows > 0)
	    	{
		    	loop i from: 0 to: length_rows - 1
		    	{
		    		available_smart_power_per_tick[time_step + i] <- available_smart_power_per_tick[time_step + i] + float(combinations_power_sum_tick[better_combination_index][i]);
		    	}
	    	}
	    	
	    	loop hs over: all_combinations[better_combination_index]
	    	{
	    		ask house(hs){
	    			do assign_power;
	    		}	
	    	}    
    	}
    }
     
}

//Power lines
species powerline parent: agentDB {
    int lines_size <- 7;
    int my_index <- powerline index_of self;
    int my_generator_index;
	list<transformer> my_transformers <- [];
    file my_icon <- file("../images/PowerLines.gif") ;
    float demand_smart;
    float demand_nonsmart;
    list<float> available_nonsmart_power_per_tick;
    list<float> available_smart_power_per_tick;
    list<list> combinations_power_sum_tick;
	list<list> combinations_bids_sum_tick;
	list<float> combinations_bids_sum;
	list<list> all_combinations;
	int better_combination_index;
    
    float my_x <- get_coordinate_x(radius_lines, my_index, degree_lines);
    float my_y <- get_coordinate_y(radius_lines, my_index, degree_lines);
    
    float distance <- ceil((radius_transformer-radius_lines)/cos(degree_transformer)) + 1;
    
    init{
		loop i from: 0 to: cycle_length{
			add powerline_power_capacity_nonsmart to: available_nonsmart_power_per_tick;
			add powerline_power_capacity_smart to: available_smart_power_per_tick; 
		}
	}
    
    aspect base {
		draw sphere(lines_size) color: rgb('yellow') at: {my_x , my_y, 0 } ;
	}
	
	aspect icon {
        draw my_icon size: lines_size at: {my_x , my_y, 0 } ;
    }
	
	action get_my_transformers{
		loop tr over: (species(transformer)) {
			if ( sqrt( (tr.my_x - my_x)^2 + (tr.my_y - my_y)^2 ) <= distance )
			{
				add tr to: my_transformers;
			} 
    	}
    }
    
    reflex get_demand{
    	if (debug_powerline = 1)
		{
			write("powerline: " + my_index + " generator: " + my_generator_index + " demand_smart: " + demand_smart);
			write("powerline: " + my_index + " generator: " + my_generator_index + " demand_nonsmart: " + demand_nonsmart);
		}
		
		do combinatorial_auction;
		
    	generator(my_generator_index).demand_smart <- generator(my_generator_index).demand_smart + demand_smart;
    	generator(my_generator_index).demand_nonsmart <- generator(my_generator_index).demand_nonsmart + demand_nonsmart;
    	
    	if (print_results = 1){
    		write("" + time_step + ";Powerline" + my_index + ";exceed_flag_smart;" + ( demand_smart - powerline_power_capacity_smart) );
    		write("" + time_step + ";Powerline" + my_index + ";exceed_flag_nonsmart;" + ( demand_nonsmart - powerline_power_capacity_nonsmart) );
    	}
    }
    
    action combinatorial_auction{
    	do get_combinations;
    	do get_combinations_sum;
    	do remove_exceeded_combinations;
    	do get_best_combination;
    }
    
	action get_combinations{
		all_combinations <- [];
		list<int> combination <- [];
		list<int> elements <- [];
		
		loop tr over: list<int>(my_transformers)
		{
			if transformer(tr).better_combination_index != -1
			{
				add tr to: elements;
			}
		}
		int num_elements <- length(elements);
		if (num_elements > 0){
			loop i from:1 to: num_elements{
				do recursive_combinations(i, elements, combination, my_index, 2);	
			}
			if (debug_powerline = 1)
			{
				write("Powerline: " + my_index + " elements: " + elements);
				write("Powerline: " + my_index + " combinations: " + all_combinations);
			}
		} else if (debug_powerline = 1)
		{
			write("Powerline: " + my_index + " no more pending transformers");
		}
	}
    
    action get_combinations_sum{
    	combinations_power_sum_tick <- [];
    	combinations_bids_sum_tick <- [];
    	
		int combination_index <- -1;
		loop comb over: all_combinations{
			add [] to: combinations_power_sum_tick;
			add [] to: combinations_bids_sum_tick;
			combination_index <- combination_index + 1;
			
			int max_index_container <- -1;
			loop tr over: comb{
				int transf_better_combination_index <- transformer(tr).better_combination_index;
				int num_rows <- length(transformer(tr).combinations_power_sum_tick[transf_better_combination_index]);
				
		  		if (num_rows > 0)
		  		{
		  			loop i from: 0 to: num_rows - 1 {
			  			if (max_index_container < i)
			  			{
			  				add transformer(tr).combinations_power_sum_tick[transf_better_combination_index][i] to: combinations_power_sum_tick[combination_index];
			  				add transformer(tr).combinations_bids_sum_tick[transf_better_combination_index][i] to: combinations_bids_sum_tick[combination_index];
			  				max_index_container <- max_index_container + 1; 
			  			}
			  			else
			  			{
			  				combinations_power_sum_tick[combination_index][i] <- float(combinations_power_sum_tick[combination_index][i]) + float(transformer(tr).combinations_power_sum_tick[transf_better_combination_index][i]);
			  				combinations_bids_sum_tick[combination_index][i] <- float(combinations_bids_sum_tick[combination_index][i]) + float(transformer(tr).combinations_bids_sum_tick[transf_better_combination_index][i]);
			  			}
			  		}
		  		}
			}
		}
		
		if (debug_powerline = 1)
		{
			write("Powerline: " + my_index + " combinations_power_sum_tick: " + combinations_power_sum_tick);
			write("Powerline: " + my_index + " combinations_bids_sum_tick: " + combinations_bids_sum_tick);
    	}
    }
    
    action remove_exceeded_combinations
    {
    	list<int> exceeded_combinations <- [];
    	int length_combinations <- length(all_combinations);
    	if (length_combinations > 0)
    	{
	    	loop comb from: 0 to: length_combinations - 1
	    	{
	    		int length_powerlist <- length(combinations_power_sum_tick[comb]);
	    		if (length_powerlist > 0)
	    		{ 
	    			bool exceeds <- false;
		    		int i <- 0;
		    		loop while: (exceeds = false) and (i < length_powerlist)
		    		{
		    			if (time_step < cycle_length)
		    			{
			    			if (float(combinations_power_sum_tick[comb][i]) > available_smart_power_per_tick[time_step + i])
			    			{
			    				exceeds <- true;
			    				add comb to: exceeded_combinations;
			    			}
		    			}
		    			i <- i + 1;
		    		}
		    		if (exceeds = true) //erases power list and bid list
		    		{
		    			combinations_power_sum_tick[comb] <- [];
		    			combinations_bids_sum_tick[comb]<- [];
		    		}
		    	}
	    	}
	    	if (debug_powerline = 1)
			{
		    	write("Powerline: " + my_index + " exceeded combinations: " + exceeded_combinations);
		    	write("Powerline: " + my_index + " in competence combinations: " + combinations_power_sum_tick);
	    	}
    	}
    }
    
    action get_best_combination
    {
    	combinations_bids_sum <- [];
    	int length_bidlist <- length(combinations_bids_sum_tick);
    	if (length_bidlist > 0)
    	{
	    	loop comb from: 0 to: length_bidlist-1
	    	{
	    		add sum(list<float>(combinations_bids_sum_tick[comb])) to: combinations_bids_sum;
	    	}
	    	
	    	float better_combination <- max(combinations_bids_sum);
	    	if (better_combination > 0.0)
	    	{
		    	better_combination_index <- combinations_bids_sum last_index_of better_combination;
		    	
		    	if (debug_powerline = 1)
				{
					write("Powerline: " + my_index + " combinations_bids_sum: " + combinations_bids_sum);
		    		write("Powerline: " + my_index + " better_combination_index: " + better_combination_index + " better_combination: " + better_combination );
		    	}
			}
			else {
				better_combination_index <- -1;
			}
    	}
    	else {
    		better_combination_index <- -1;
    	}
    }
    
    action assign_power
    {
    	int length_rows <- length(combinations_power_sum_tick[better_combination_index]);
    	if ( length_rows > 0)
    	{
	    	loop i from: 0 to: length_rows - 1
	    	{
	    		available_smart_power_per_tick[time_step + i] <- available_smart_power_per_tick[time_step + i] + float(combinations_power_sum_tick[better_combination_index][i]);
	    	}
    	}
    	
    	loop tr over: all_combinations[better_combination_index]
    	{
    		ask transformer(tr){
    			do assign_power;
    		}	
    	}
    }
}

//Generator
species generator parent: agentDB {
	int generator_size <- 10;
	int my_index <- generator index_of self;
	list<powerline> my_lines <- []; 
	float my_x <- (grid_width/4);
    float my_y <- (grid_height/4);
    file my_icon <- file("../images/PowerPlant.gif") ;
    float demand_smart;
    float demand_nonsmart;
    list<float> available_nonsmart_power_per_tick;
    list<float> generated_smart_power_per_tick;
    list<float> sold_smart_power_per_tick;
    list<list> combinations_power_sum_tick;
	list<list> combinations_bids_sum_tick;
	list<float> combinations_bids_sum;
	list<list> all_combinations;
	int better_combination_index;
	
	//production fuction - period production - smart variables
	list<list> powerproductionperiods_list_smart;
	list<float> powerproductionperiods_smart;
	int production_period_smart <- 3;
	
	//production function - max production - smart variables
	list<list> powerproductionmax_list_smart;
	float maxpowerproduction_smart;
	
	//production fuction - period production - non smart variables
	list<list> powerproductionperiods_list_nonsmart;
	list<float> powerproductionperiods_nonsmart;
	int production_period_nonsmart <- 3;
	
	//production function - max production - non smart variables
	list<list> powerproductionmax_list_nonsmart;
	float maxpowerproduction_nonsmart;
	
       
	aspect base {
		draw sphere(generator_size) color: rgb('red') at: {my_x , my_y , 0 } ;			
	}
	
	aspect icon {
        draw my_icon size: generator_size at: {my_x , my_y, 0 } ;
    }
    
    action recalculate_available_power{
    	if (time_step < cycle_length){
	    	loop i from: (time_step + 1) to: cycle_length{
				available_nonsmart_power_per_tick[i] <- generator_current_production_nonsmart;
				generated_smart_power_per_tick[i] <- generator_current_production_smart;
			}
		}
    }
    
    action production_function_period_smart{
		int num_rows <- length( (powerproductionperiods_list_smart) );
		if (num_rows > 0){
			int index <- round(floor(floor((time_step-1)/60)/(24/production_period_smart)));
			generator_current_production_smart <- float(powerproductionperiods_list_smart[2][index][1]);
			do recalculate_available_power;
		}
    }
    
    action production_function_max_smart{
		int num_rows <- length( (powerproductionmax_list_smart) );
		if (num_rows > 0){
			generator_current_production_smart <- float(powerproductionmax_list_smart[2][0][0]);
			do recalculate_available_power;
		}
    } 
    
    action production_function_max_nonsmart{
		int num_rows <- length( (powerproductionmax_list_nonsmart) );
		if (num_rows > 0){
			generator_current_production_nonsmart <- float(powerproductionmax_list_nonsmart[2][0][0]);
			do recalculate_available_power;
		}
    } 
    
    action production_function_period_nonsmart{
		int num_rows <- length( (powerproductionperiods_list_nonsmart) );
		if (num_rows > 0){
			int index <- round(floor(floor((time_step-1)/60)/(24/production_period_nonsmart)));
			generator_current_production_nonsmart <- float(powerproductionperiods_list_nonsmart[2][index][1]);
			do recalculate_available_power;
		}
    }
    
    action price_cosine{
    	//base_price <- ( -0.25 * cos( (360 * time_step) /cycle_length ) ) + 1.25; //base_price between 1 and 1.5, cosine func
    	base_price <- ( (price_cosine_bound * -1) * cos( (360 * time_step) /cycle_length ) ) + price_cosine_base; 
    }
    
    action price_constant{
    	base_price <- price_constant;
    }
    
    reflex get_demand{
    	if (debug_generator = 1)
		{
			write("smart_power_capacity: " + generated_smart_power_per_tick[time_step]);
			write("generator: " + my_index + " demand_smart: " + demand_smart);
			write("generator: " + my_index + " demand_nonsmart: " + demand_nonsmart);
		}
		
		if (print_results = 1){
			write("" + time_step + ";base_price;" + base_price);
			write("" + time_step + ";power_excess_smart;" + power_excess_smart);
			write("" + time_step + ";power_excess_nonsmart;" + power_excess_nonsmart);
		}
		
		do combinatorial_auction;
    }
    
    reflex production_and_price{
     	power_excess_nonsmart <- generator_current_production_nonsmart - demand_nonsmart;
     	power_excess_smart <- generator_current_production_smart - demand_smart;
	
		if (production_function_smart = "Max"){
			do production_function_max_smart;	
		}
		else if (production_function_smart = "Period"){
			do production_function_period_smart;
		}

		if (production_function_nonsmart = "Max"){
			do production_function_max_nonsmart;	
		}
		else if (production_function_nonsmart = "Period"){
			do production_function_period_nonsmart;
		}
		
		if (price_function = "Cosine"){
			do price_cosine;
		}
		else if (price_function = "Constant"){
			do price_constant;
		}
		
    }
    
    action combinatorial_auction{
    	do get_combinations;
    	do get_combinations_sum;
    	do remove_exceeded_combinations;
    	do get_best_combination;
    	do assign_power;
    }
    
	action get_combinations{
		all_combinations <- [];
		list<int> combination <- [];
		list<int> elements <- [];
		
		loop pl over: list<int>(my_lines)
		{
			if powerline(pl).better_combination_index != -1
			{
				add pl to: elements;
			}
		}
		
		int num_elements <- length(elements);
		if (num_elements > 0){
			loop i from:1 to: num_elements{
				do recursive_combinations(i, elements, combination, my_index, 3);	
			}
			if (debug_generator = 1)
			{
				write("Generator: " + my_index + " elements: " + elements);
				write("Generator: " + my_index + " combinations: " + all_combinations);
			}
		}else if (debug_generator = 1)
		{
			write("Generator: " + my_index + " no more pending powerlines");
		}
	}
    
    action get_combinations_sum{
    	combinations_power_sum_tick <- [];
    	combinations_bids_sum_tick <- [];
    	
		int combination_index <- -1;
		loop comb over: all_combinations{
			add [] to: combinations_power_sum_tick;
			add [] to: combinations_bids_sum_tick;
			combination_index <- combination_index + 1;
			
			int max_index_container <- -1;
			loop pl over: comb{
				int pl_better_combination_index <- powerline(pl).better_combination_index;
				int num_rows <- length(powerline(pl).combinations_power_sum_tick[pl_better_combination_index]);
				
		  		if (num_rows > 0)
		  		{
		  			loop i from: 0 to: num_rows - 1 {
			  			if (max_index_container < i)
			  			{
			  				add powerline(pl).combinations_power_sum_tick[pl_better_combination_index][i] to: combinations_power_sum_tick[combination_index];
			  				add powerline(pl).combinations_bids_sum_tick[pl_better_combination_index][i] to: combinations_bids_sum_tick[combination_index];
			  				max_index_container <- max_index_container + 1; 
			  			}
			  			else
			  			{
			  				combinations_power_sum_tick[combination_index][i] <- float(combinations_power_sum_tick[combination_index][i]) + float(powerline(pl).combinations_power_sum_tick[pl_better_combination_index][i]);
			  				combinations_bids_sum_tick[combination_index][i] <- float(combinations_bids_sum_tick[combination_index][i]) + float(powerline(pl).combinations_bids_sum_tick[pl_better_combination_index][i]);
			  			}
			  		}
		  		}
			}
		}
		
		int length_combs <- length(combinations_power_sum_tick);
		if (length_combs > 0){
			list<float> sum_comb <- [];
			loop i from: 0 to: length_combs - 1{
				add sum(list<float>(combinations_power_sum_tick[i])) to: sum_comb;
			}
		}
		
		if (debug_generator = 1)
		{
			write("Generator: " + my_index + " combinations_power_sum_tick: " + combinations_power_sum_tick);
			write("Generator: " + my_index + " combinations_bids_sum_tick: " + combinations_bids_sum_tick);
    	}
    }
    
    action remove_exceeded_combinations
    {
    	list<int> exceeded_combinations <- [];
    	int length_combinations <- length(all_combinations);
    	if (length_combinations > 0)
    	{
	    	loop comb from: 0 to: length_combinations - 1
	    	{
	    		int length_powerlist <- length(combinations_power_sum_tick[comb]);
	    		if (length_powerlist > 0)
	    		{ 
	    			bool exceeds <- false;
		    		int i <- 0;
		    		loop while: (exceeds = false) and (i < length_powerlist)
		    		{
		    			if (time_step < cycle_length)
		    			{
			    			if (float(combinations_power_sum_tick[comb][i]) > (generated_smart_power_per_tick[time_step + i] - sold_smart_power_per_tick[time_step + i]))
			    			{
			    				exceeds <- true;
			    				add comb to: exceeded_combinations;
			    			}
		    			}
		    			i <- i + 1;
		    		}
		    		if (exceeds = true) //erases power list and bid list
		    		{
		    			combinations_power_sum_tick[comb] <- [];
		    			combinations_bids_sum_tick[comb]<- [];
		    		}
		    	}
	    	}
	    	
	    	if (debug_generator = 1)
			{
		    	write("Generator: " + my_index + " exceeded combinations: " + exceeded_combinations);
		    	write("Generator: " + my_index + " in competence combinations: " + combinations_power_sum_tick);
	    	}
    	}
    }
    
    action get_best_combination
    {
    	combinations_bids_sum <- [];
    	int length_bidlist <- length(combinations_bids_sum_tick);
    	if (length_bidlist > 0)
    	{
	    	loop comb from: 0 to: length_bidlist-1
	    	{
	    		add sum(list<float>(combinations_bids_sum_tick[comb])) to: combinations_bids_sum;
	    	}
	    	
	    	float better_combination <- max(combinations_bids_sum);
	    	if (better_combination > 0.0)
	    	{
		    	better_combination_index <- combinations_bids_sum last_index_of better_combination;
		    	
		    	if (debug_generator = 1)
				{
					write("Generator: " + my_index + " combinations_bids_sum: " + combinations_bids_sum);
		    		write("Generator: " + my_index + " better_combination_index: " + better_combination_index + " better_combination: " + better_combination );
		    	}
	    	}
	    	else{
	    		better_combination_index <- -1;
	    	}
    	}
    	else {
    		better_combination_index <- -1;
    	}
    }
    
    action assign_power
    {
    	if (better_combination_index != -1)
    	{
	    	int length_rows <- length(combinations_power_sum_tick[better_combination_index]);
	    	if (length_rows > 0)
	    	{
		    	loop i from: 0 to: length_rows - 1
		    	{
		    		sold_smart_power_per_tick[time_step + i] <- sold_smart_power_per_tick[time_step + i] + float(combinations_power_sum_tick[better_combination_index][i]);
		    	}
	    	}
	    	
	    	loop pl over: all_combinations[better_combination_index]
	    	{
	    		ask powerline(pl){
	    			do assign_power;
	    		}	
	    	}
    	}
    } 	
	
	init {
		loop pl over: (species(powerline)) {
			add pl to: my_lines; 
    	}
    	
    	loop i from: 0 to: cycle_length{
			add generator_current_production_nonsmart to: available_nonsmart_power_per_tick;
			add generator_current_production_smart to: generated_smart_power_per_tick; 
			add 0 to: sold_smart_power_per_tick;
		}
		
		ask agentDB{
			myself.powerproductionperiods_list_smart <- list<list> (self select(select:"select hour(a.time) div (24/"+myself.production_period_smart+") period, max(power) power from ( select time, sum(power) power from appliances_profiles where id_appliance in (select id_appliance from appliances where isSmart = 1) group by time ) a group by hour(a.time) div (24/"+myself.production_period_smart+");"));
			myself.powerproductionmax_list_smart <- list<list> (self select(select:"select max(power) power from ( select time, sum(power) power from appliances_profiles where id_appliance in (select id_appliance from appliances where isSmart = 1) group by time ) a ;"));
			
			myself.powerproductionperiods_list_smart <- list<list> (self select(select:"select hour(a.time) div (24/"+myself.production_period_nonsmart+") period, max(power) power from ( select time, sum(power) power from appliances_profiles where id_appliance not in (select id_appliance from appliances where isSmart = 1) group by time ) a group by hour(a.time) div (24/"+myself.production_period_nonsmart+");"));
			myself.powerproductionmax_list_nonsmart <- list<list> (self select(select:"select max(power) power from ( select time, sum(power) power from appliances_profiles where id_appliance not in (select id_appliance from appliances where isSmart = 1) group by time ) a ;"));
		}
	}
}

//Graph
species edge_agent {
    aspect base {
            draw shape color: rgb('black');
    }
}

//Land
grid land width: grid_width height: grid_height neighbours: 4 {
	rgb color <- rgb('white'); 
}

//Experiment
experiment test type: gui {
    //parameter "Production periods: " var: production_period min: 1 max: 24 category: "Power generation" ;
    parameter "Debug House: " var: debug_house min: 0 max: 1 category: "General configuration" ;
    parameter "Debug Transformer: " var: debug_transformer min: 0 max: 1 category: "General configuration" ;
    parameter "Debug Powerline: " var: debug_powerline min: 0 max: 1 category: "General configuration" ;
    parameter "Debug Generator: " var: debug_generator min: 0 max: 1 category: "General configuration" ;
    parameter "Print results: " var: print_results min: 0 max: 1 category: "General configuration" ;
    
    parameter "Smart Production Function: " var: production_function_smart among:["Max","Period"] category: "Power Generation configuration" ; 
    parameter "Non-Smart Production Function: " var: production_function_nonsmart among:["Max","Period"] category: "Power Generation configuration" ;
    
    parameter "Price Function: " var: price_function among:["Cosine","Constant"] category: "Price function configuration" ;
    parameter "Constant Power price: " var: price_constant  min: 1.0 max: 10.0 category: "Price function configuration" ;
    parameter "Cosine Power price base: " var: price_cosine_base  min: 0.0 max: 10.0 category: "Price function configuration" ;
    parameter "Cosine Power price bound: " var: price_cosine_bound  min: 0.0 max: 10.0 category: "Price function configuration" ;
  
    output {
            display main_display type: opengl {
            		grid land;
                    species house aspect: icon{
						species smart_appliance aspect: appliance_icon;
						species other_loads aspect: appliance_icon;
					}
                    species transformer aspect: icon;
                    species powerline aspect: icon;
                    species generator aspect: icon;
                    species edge_agent aspect: base;
            }
            
            display smartVsnonsmart_display {
  					chart "Total demand" type: series {
  						data "smart demand" value: totalenergy_smart color: rgb('red') ;
  						data "non-smart demand" value: totalenergy_nonsmart color: rgb('blue') ;
  						data "total demand" value: (totalenergy_smart + totalenergy_nonsmart) color: rgb('purple') ;
  						data "non-smart production" value: generator_current_production_nonsmart color: rgb('cyan');
  						data "smart production" value: generator_current_production_smart color: rgb('orange');
  						data "total production" value: (generator_current_production_nonsmart + generator_current_production_smart) color: rgb('black');
					}
			}
			/*
		    display house_chart_display {
					chart "House demand" type: series {
						loop hs over: house {
  							data "house" + hs + " demand" value: house(hs).demand color: rnd_color(255) ;
  						}
					}
    		}
    		display transformer_chart_display {
					chart "Transformer demand" type: series {
						loop tr over: transformer {
  							data "transformer" + tr + " demand" value: transformer(tr).demand color: rnd_color(255) ;
  						}
					}
    		}
    		display powerline_chart_display {
					chart "Powerlines demand" type: series {
						loop pl over: powerline {
  							data "Powerline" + pl + " demand" value: powerline(pl).demand color: rnd_color(255) ;
  						}
					}
    		}
    		
    		display powerexcess_chart_display {
					chart "Power excess" type: series {
						data "power excess" value: power_excess color: rgb('red') ;
					}
    		}*/
	}
}
