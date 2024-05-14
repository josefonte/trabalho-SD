-module(file_manager).
-export([start/0,
        add_file/3,
        remove_file/2,
        rate_file/3]).



start()-> register(?MODULE,spawn(fun() -> loop(#{}) end)).



%funções de interface

rpc(Request)->
    ?MODULE ! {Request,self()},
    receive {Res, ?MODULE} -> Res end.

add_file(Album,Name, Descricao) -> rpc({add_file,Album,Name,Descricao}).


remove_file(Album,Name) -> rpc({remove_file,Album,Name}).

rate_file(Album,Name,Rate) -> rpc({rate_file,Album,Name,Rate}).




%processo servidor
loop(Map) -> 
    receive
        {{create_album,Album},From} ->
            case maps:find(Album,Map) of
                error -> 
                    From ! {ok, ?MODULE},
                    loop(maps:put(Album,#{},Map));
                _ ->
                    From ! {album_exists, ?MODULE},
                    loop(Map)
            end;
        {{get_album,Album},From} ->
            case maps:find(Album,Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok,Files} ->
                    From ! {ok, Files},
                    loop(Map)
            end;
        {{add_file,Album, Name},From} ->
            % ir buscar map daquele album
            case maps:find(Album,Map) of
                error -> 
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok,Files} ->
                    case maps:find(Name, Files) of
                        error ->
                            From ! {ok, ?MODULE},
                            NewFiles = maps:put(Name,#{},Files),
                            loop(maps:put(Album,NewFiles,Map));
                        {ok,_} ->
                            From ! {filename_exists, ?MODULE},
                            loop(Map)
                    end
            end;
        {{remove_file, Album,Name},From} ->
            case maps:find(Album, Map) of
                error ->
                    % Album não existe
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, Files} ->
                    case maps:find(Name, Files) of
                        {ok, _} ->
                            NewMap = maps:put(Album, maps:remove(Name, Files), Map),
                            From ! {ok, ?MODULE},
                            loop(NewMap);
                        _ ->
                            % Ficheiro não existe
                            From ! {file_not_found, ?MODULE},
                            loop(Map)
                        end
            end;
        {{rate_file,Album, Name,Rate,Username},From} ->
            case maps:find(Album, Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, Files} ->
                    case maps:find(Name, Files) of
                        {ok, Rates} ->
                            case maps:find(Username, Rates) of
                                false ->
                                    NewRates = maps:put(Username, Rate, Rates),
                                    NewFiles = maps:put(Name,  NewRates, Files),
                                    NewMap = maps:put(Album, NewFiles, Map),
                                    From ! {ok, ?MODULE},
                                    loop(NewMap);
                                _ ->
                                    From ! {file_already_rated, ?MODULE},
                                    loop(Map)
                            end;
                        _ ->
                            From ! {file_not_found, ?MODULE},
                            loop(Map)
                    end
            end
    end.