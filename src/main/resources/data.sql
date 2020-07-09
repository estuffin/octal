insert into oct_users 
	select * from (select 1 user_id, 'test@test.com' email, systimestamp create_date, null update_date, 0 login_count, null last_login_date, null curr_login_date, null curr_ip, null last_ip, null do_api_key)  
	 where 1 > (select count(*) 
	              from oct_users 
	             where user_id = 1);