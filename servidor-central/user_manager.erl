-module(user_manager).
-export([start/0,
        add_user/2,
        remove_user/2,
        loop/1]).



start()-> register(?MODULE,spawn(fun() -> loop(#{}) end)).



%funções de interface

rpc(Request)->
    ?MODULE ! {Request,self()},
    receive {Res, ?MODULE} -> Res end.

add_user(Album,Username) -> rpc({add_user,Album,Username}).


remove_user(Album,Username) -> rpc({remove_user,Album,Username}).




%processo servidor
loop(Map) ->
    receive
        {{create_album,Album,Username},From} ->
            case maps:find(Album,Map) of
                error ->
                    From ! {ok, ?MODULE},
                    NewSet = sets:from_list([Username]),
                    loop(maps:put(Album,NewSet,Map));
                _ ->
                    From ! {album_exists, ?MODULE},
                    loop(Map)
            end;

        {{verify_user,Album,User},From} ->
            case maps:find(Album,Map) of
                {ok,Users} ->
                    case sets:is_element(User, Users) of
                        true ->
                            From ! {ok, ?MODULE},
                            loop(Map);
                        false ->
                            From ! {user_not_found, ?MODULE},
                            loop(Map)
                    end;
                _ ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map)
            end;

        {{add_user,Album, Username},From} ->
            case maps:find(Album,Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok,Users} ->
                    case sets:is_element(Username, Users) of
                        false ->
                            From ! {ok, ?MODULE},
                            NewUsers = sets:add_element(Username, Users),
                            loop(maps:put(Album,NewUsers,Map));
                        true ->
                            From ! {username_exists, ?MODULE},
                            loop(Map)
                    end
            end;

        {{remove_user, Album,Username},From} ->
            case maps:find(Album, Map) of
               {ok, Users} ->
                   case sets:is_element(Username, Users) of
                       true ->
                           NewMap = maps:put(Album, sets:del_element(Username, Users), Map),
                           From ! {ok, ?MODULE},
                           loop(NewMap);
                       _ ->
                           From ! {username_not_found, ?MODULE},
                           loop(Map)
                   end;
               _ ->
                   From ! {album_not_found, ?MODULE},
                   loop(Map)
            end;

        {{update_album, Album, Users}, From} ->
            case maps:find(Album, Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, _ } ->
                    % fazer a verificacao com os utilizadores no login manager
                    % sets:is_subset(Users, ExistingUsers) of>
                    From ! {ok, ?MODULE},
                    NewMap = maps:put(Album, Users, Map),
                    loop(NewMap)
            end;

        {{get_album_users, Album}, From} ->
            case maps:find(Album, Map) of
                {ok, Users} ->
                    From ! {ok, Users, ?MODULE},
                    loop(Map);
                _ ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map)
            end

    end.
