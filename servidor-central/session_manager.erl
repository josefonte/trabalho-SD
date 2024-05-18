-module(session_manager).
-export([start/0, loop/2]).


start() -> register(?MODULE, spawn(fun() -> loop([], #{}) end)).


add_files(OldFiles, NewFiles, User) ->
    maps:fold(fun(File, Rating, Acc) ->
                    update_file(Acc, File, Rating, User) end, OldFiles, NewFiles).

update_file(OldFiles, File, Rating, User) ->
    case maps:is_key(File, OldFiles) of
        true ->
            OldFileRatings = maps:get(File, OldFiles),
            case maps:is_key(User, OldFileRatings) of
                true ->
                    % Verify if value is null and if is update it
                    case maps:get(User, OldFileRatings) of
                        "null" ->
                            NewRatings = maps:put(User, Rating, OldFileRatings),
                            maps:put(File, NewRatings, OldFiles);
                        _ ->
                            % continue with the old value
                            OldFiles
                    end;
                false ->
                    NewRatings = maps:put(User, Rating, OldFileRatings),
                    maps:put(File, NewRatings, OldFiles)
            end;
        false ->
            NewRatings = #{User => Rating},
            maps:put(File, NewRatings, OldFiles)
    end.


loop(Users, Albums) ->
    receive
        {Request, From} ->
            {Msg, UsersNextState, AlbumsNextState} = handle(Request, From, Users, Albums),
            From ! {Msg, ?MODULE},
            loop(UsersNextState, AlbumsNextState)
    end.

handle({session_join, User}, _, Users, Albums) ->
    case lists:member(User, Users) of
        false ->
            {ok, [User | Users], Albums};
        true ->
            {user_exists, Users, Albums}
    end;

handle({session_leave, User}, _, Users, Albums) ->
    case lists:member(User, Users) of
        true ->
            List = lists:delete(User, Users),
            io:format("session_manager: UsersOnSession: ~p~n", [List]),
            {ok, List, Albums};
            % {ok, lists:delete(User, Users), Albums};
        false ->
            {user_not_found, Users, Albums}
    end;

handle({is_last_user, User}, _, Users, Albums) ->
    io:format("session_manager: UsersOnSession: ~p~n", [Users]),
    case length(Users) of
        1 ->
            case lists:member(User, Users) of
                true ->
                    {ok, Users, Albums};
                false ->
                    {user_not_last, Users, Albums}
            end;
        _ ->
            {user_not_last, Users, Albums}
    end;

handle({update_ratings, User, Album, Files}, _, Users, Albums) ->
    case lists:member(User, Users) of
        true ->
            case maps:find(Album, Albums) of
                {ok, OldFiles} ->
                    io:format("session_manager: OldFiles: ~p~n", [OldFiles]),
                    NewFiles = add_files(OldFiles, Files, User),
                    io:format("session_manager: NewFiles: ~p~n", [NewFiles]),
                    NewAlbum = maps:put(Album, NewFiles, Albums),
                    {ok, Users, NewAlbum};
                error ->
                    NewFiles = add_files(#{}, Files, User),
                    io:format("session_manager: NewAlbum: ~p~n", [NewFiles]),
                    NewAlbum = maps:put(Album, NewFiles, Albums),
                    {ok, Users, NewAlbum}
            end;
        false ->
            {user_not_found, Users, Albums}
    end;

handle({update_and_get_all_files, User, Album, Files}, _, Users, Albums) ->
    case lists:member(User, Users) of
        true ->
            case maps:find(Album, Albums) of
                {ok, OldFiles} ->
                    io:format("session_manager: OldFiles: ~p~n", [OldFiles]),
                    NewFiles = add_files(OldFiles, Files, User),
                    io:format("session_manager: NewFiles: ~p~n", [NewFiles]),
                    NewAlbum = maps:put(Album, NewFiles, Albums),
                    {{ok, NewAlbum}, NewFiles, Users, NewAlbum};
                error ->
                    NewFiles = add_files(#{}, Files, User),
                    io:format("session_manager: NewAlbum: ~p~n", [NewFiles]),
                    NewAlbum = maps:put(Album, NewFiles, Albums),
                    {{ok, NewAlbum}, NewFiles, Users, NewAlbum}
            end;
        false ->
            {user_not_found, Users, Albums}
    end.


% handle({get_all_ratings, Album}, _, Users, Albums) ->
%     io:format("session_manager: get_all_ratings"),
%     case maps:find(Album, Albums) of
%         {ok, Files} ->
%             {{ok, Files}, Users, Albums};
%         error ->
%             {album_not_found, Users, Albums}
%     end.
