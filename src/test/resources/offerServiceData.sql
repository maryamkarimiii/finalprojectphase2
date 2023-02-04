insert into service (name)
values ('service1');

insert into sub_service (base_amount, description, name, service_id)
values (1200.0, 'none', 'subService1', 1);

insert into customer (email, first_name, last_name, password, phone_number, role, username)
values ('maryam12@gmail.com', 'maryam', 'karimi', '123456Mk', '09100321487', 'CUSTOMER', 'maryam12@gmail.com');

insert into expert (email, first_name, last_name, password, phone_number, role, username,
                    expert_registration_status, total_score)
values ('maryam12@gmail.com', 'maryam', 'karimi', '123456Mk', '09100321487', 'EXPERT', 'maryam12@gmail.com',
        'CONFIRMED',
        1.5);

insert into order_table (address, description, order_status, price, tracking_number, work_date,
                         customer_id, expert_id, sub_service_id)
values ('none','none','WAITING_FOR_OFFER',1300.0,'123456',CAST('11/08/2023' as date),1,1,1);