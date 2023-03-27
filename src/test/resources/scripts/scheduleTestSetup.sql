insert into theme
(id, is_admin, name, owner_id)
values (1, true, '가족과', '1');

insert into plan
(id, owner_id, days, night, place_id)
values
(1, '1', 2, 1, '1');

insert into plan_theme
(plan_id, theme_id)
values (1,1);
