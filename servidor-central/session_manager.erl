-module(session_manager).
-export([start/0, loop/2]).


start() -> register(?MODULE, spawn(fun() -> loop([], #{}) end)).


loop(Users, Files) ->
    receive
        {Request, From} ->
            {Msg, UsersNextState, FilesNextState} = handle(Request, From, Users, Files),
            From ! {Msg, ?MODULE},
            loop(UsersNextState, FilesNextState)
    end.

handle({session_join, User}, _, Users, Files) ->
    case lists:member(User, Users) of
        false ->
            {ok, [User | Users], Files};
        true ->
            {user_exists, Users, Files}
    end;

handle({session_leave, User}, _, Users, Files) ->
    case lists:member(User, Users) of
        true ->
            {ok, lists:delete(User, Users), Files};
        false ->
            {user_not_found, Users, Files}
    end;

handle({is_last_user, User}, _, Users, Files) ->
    case length(Users) of
        1 ->
            case lists:member(User, Users) of
                true ->
                    {ok, Users, Files};
                false ->
                    {user_not_last, Users, Files}
            end;
        _ ->
            {user_not_last, Users, Files}
    end.
