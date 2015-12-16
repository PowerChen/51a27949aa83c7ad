-module(web_db).

-export([init/0]).

init() ->
	emysql:execute(db_pool,get_sql(table_user)),
	emysql:execute(db_pool,get_sql(table_order)).

	
% 建表sql
get_sql(table_order) ->
<<"CREATE TABLE IF NOT EXISTS `order` (
  `order_id` varchar(64) NOT NULL COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `order_type` tinyint(1) NOT NULL COMMENT '订单类型(0:直接充,1：平台划帐,2:其它)',
  `order_platform` varchar(32) DEFAULT NULL COMMENT '运营充值平台',
  `phone_no` varchar(15) DEFAULT NULL COMMENT '电话号码',
  `phone_imem` varchar(15) NOT NULL COMMENT '手机唯一标识号',
  `gold` int(10) NOT NULL COMMENT '充值游戏内货币数:钻石',
  `create_time` int(10) NOT NULL COMMENT '订单时间',
  `status` tinyint(1) NOT NULL COMMENT '状态(0:失败,1:成功)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8">>;

get_sql(table_user) ->
<<"CREATE TABLE IF NOT EXISTS `user` (
  `guid` int(15) NOT NULL COMMENT '手机唯一标识imem',
  `phone_no` int(15) NOT NULL COMMENT '电话号码',
  `platform` varchar(20) DEFAULT NULL COMMENT '用户平台',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`guid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8">>.


