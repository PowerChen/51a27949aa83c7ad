%% Feel free to use, reuse and abuse the code in this file.

%% @doc GET echo handler.
-module(web_handler).

-export([init/2]).
	
init(Req, Opts) ->
	Method = cowboy_req:method(Req),
	error_logger:info_msg("web_handler init ~p ~n",[Method]),
	Req2 = method_router(Method,Req),
	{ok, Req2, Opts}.

method_router(<<"GET">>,Req) ->
	case catch cowboy_req:match_qs([pt], Req) of
		#{pt := Pt} ->
			ok;
		_other ->
			Pt = undefined
	end,
	handler_get(Pt, Req);
method_router(<<"POST">>,Req) ->
	HasBody = cowboy_req:has_body(Req),
	handler_post(HasBody, Req);
method_router(_, Req) ->
	cowboy_req:reply(405, Req).
	
	
% GET 处理	
handler_get(Pt, Req) ->
	web_echo(Pt, Req).
	
% POST 处理
handler_post(true, Req) ->
	{ok, PostVals, Req2} = cowboy_req:body_qs(Req),
	Pt = proplists:get_value(<<"pt">>, PostVals),
	case Pt of
		<<"1">> -> % 登入注册
			% 传入 手机唯一编号 （IMEM / 手机号码）
			JsonArr = [{obj,[{<<"pt">>,1},{<<"info">>,<<"login success">>}]}],
			web_echo(rfc4627:encode(JsonArr), Req2);
		<<"2">> -> % 充值
			% 订单号，状态，充值钻石数
			JsonArr = [{obj,[{<<"pt">>,2},{<<"info">>,<<"rechage success">>}]}],
			web_echo(rfc4627:encode(JsonArr), Req2);
		<<"3">> -> % 查库返回json串 eg:[{"pt":3},{"id":1,"name":"poewr"},{"id":2,"name":"ting"}]
			Result = emysql:execute(db_pool,<<"select * from user">>),
			JSON = emysql:as_json(Result),
			JsonArr0 = [{obj,J}||J <- JSON],
			JsonArr = [{obj,[{<<"pt">>,3}]}|JsonArr0],
			web_echo(rfc4627:encode(JsonArr), Req2);
		_Other ->
			error_logger:info_msg("unknown pt number : ~p ~n",[Pt]),
			cowboy_req:reply(400, [], <<"unknown pt parameter.">>, Req)
	end;
handler_post(false, Req) ->
	cowboy_req:reply(400, [], <<"Missing body.">>, Req).

%% web 返回
web_echo(undefined, Req) ->
	cowboy_req:reply(400, [], <<"Missing pt parameter.">>, Req);
web_echo(Pt, Req) ->
	cowboy_req:reply(200, [
		{<<"content-type">>, <<"text/plain; charset=utf-8">>}
	], Pt, Req).