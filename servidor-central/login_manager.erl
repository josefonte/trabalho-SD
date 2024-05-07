-module(login_manager).
-export([start/0,loop/1, isUserOnline/2]).


start()-> register(?MODULE,spawn(fun() -> loop(#{}) end)).


loop(Users) ->
    receive
        {Request, From} ->
            {Msg, UsersNextState} = handle(Request, From, Users),
            From ! {Msg, ?MODULE},

            % io:format("Users: ~p~n~n", [UsersNextState]),

            loop(UsersNextState)
    end.

handle({create_account, Username, Passwd}, Pid, Users) ->
    case maps:find(Username, Users) of
        error ->
            HashedPasswd = crypto:hash(sha256, Passwd),
            {ok, maps:put(Username, {Pid, HashedPasswd, true}, Users)};
        _ ->
            {user_exists, Users}
    end;

handle({close_account, Username, Passwd}, _, Users) ->
    case maps:find(Username, Users) of
        {ok, {_, StoredHash, _}} ->
            HashedPasswd = crypto:hash(sha256, Passwd),
            case StoredHash == HashedPasswd of
                true ->
                    {ok, maps:remove(Username, Users)};
                false ->
                    {invalid, Users}
            end;
        _ ->
            {invalid, Users}
    end;

handle({login, Username, Passwd}, _, Users) ->
    case maps:find(Username, Users) of
        {ok, {Pid, StoredHash, false}} ->
            HashedPasswd = crypto:hash(sha256, Passwd),
            case StoredHash == HashedPasswd of
                true ->
                    {ok, maps:update(Username, {Pid, HashedPasswd, true}, Users)};
                false ->
                    {invalid, Users}
            end;
        _ ->
            {invalid, Users}
    end;

handle({logout, Username}, _, Users) ->
    case maps:find(Username, Users) of
        {ok, {Pid, Passwd, true}} ->
            {ok, maps:update(Username, {Pid, Passwd, false}, Users)};
        _ ->
            {invalid, Users}
    end;

handle(online, _, Users) ->
    OnlineUsers = maps:filter(fun(_, {_, _, true}) -> true; (_, _) -> false end, Users),
    OnlineUsernames = maps:keys(OnlineUsers),
    {OnlineUsernames, Users};

handle({verify_user, Username}, _, Users) ->
    case maps:find(Username, Users) of
        {ok, _} ->
            {ok, Users};
        _ ->
            {invalid, Users}
    end.


isUserOnline(Username, Users) ->
    case maps:find(Username, Users) of
        {ok, {_, true}} ->
            true;
        _ ->
            false
    end.
