create table if not exists `text_history_record`
(
   `id` integer primary key autoincrement,
   `cid` text,
   `content` text,
   `create_time` datetime
);

create index if not exists idx_cid on text_history_record (cid);
