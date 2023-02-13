insert into theme
(id, is_admin, name, owner_id)
values (1, true, '가족과', '1');

insert into schedule
(id, owner_id, days, night, place_id)
values
(1, '1', 2, 1, '1');

insert into schedule_theme
(schedule_id, theme_id)
values (1,1);

insert into schedule
(id, owner_id, days, night, place_id)
values
(2, '1', 3, 2, '2');

insert into schedule_theme
(schedule_id, theme_id)
values (2,1);