%% cowboy web server
-module(web_server).
-behaviour(application).

%% API.
-export([start/0,startL/0,start/2]).
-export([stop/1]).

%% API.
% 远程服务器用
start() ->
	start(1).
% 本地测试用
startL() ->
	start(2).
start(T) ->
	ok = ensure_started(log4erl),
	log4erl:conf("../logs/log4erl.conf"),
	error_logger:add_report_handler(error_logger_log4erl_h),
	ok = ensure_started(ranch),
	ok = ensure_started(crypto),
	ok = ensure_started(rfc4627_jsonrpc),
	ok = ensure_started(cowlib),
	ok = ensure_started(cowboy),
	ok = ensure_started(emysql),
	case T of
		1  ->
			% 数据库内网IP
			emysql:add_pool(db_pool, 1,"root", "12345", "10.190.142.16", 3306,"drillboy", utf8);
		2  ->
			% 数据库公网IP
			emysql:add_pool(db_pool, 1,"root", "12345", "203.195.175.39", 3306,"drillboy", utf8)
	end,
	web_db:init(),
	ok = ensure_started(web_server),
	error_logger:info_msg("web server started ! ~n",[]).
	
	
ensure_started(APP) ->
	case application:start(APP) of
		ok -> 
			ok;
		{error, {already_started, APP}} ->
			ok
	end.
		
	
	
start(_Type, _Args) ->
	Dispatch = cowboy_router:compile([
		{'_', [
			{"/", web_handler, []}
		]}
	]),
	{ok, _} = cowboy:start_http(http, 100, [{port, 8080}], [
		{env, [{dispatch, Dispatch}]}
	]),	
	web_server_sup:start_link().

stop(_State) ->
	error_logger:remove_report_handler(error_logger_log4erl_h),
	ok.
