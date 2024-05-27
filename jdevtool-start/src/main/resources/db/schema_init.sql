create table if not exists `text_history_record`
(
   `id` integer primary key autoincrement,
   `cid` text,
   `content` text,
   `create_time` datetime
);

create index if not exists idx_cid on text_history_record (cid);

create table if not exists `note`
(
    `id` integer primary key autoincrement,
    `name` text,
    `type` integer,
    `content` text,
    `attrs` text,
    `create_time` datetime,
    `update_time` datetime
);

create index if not exists idx_name on note (`name`);

