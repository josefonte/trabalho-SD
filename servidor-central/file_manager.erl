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
                {ok,Files} ->
                    From ! {ok, Files,?MODULE},
                    loop(Map);
                _ ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map)
            end;

        {{get_files,Album},From} ->
            case maps:find(Album,Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok,Files} ->
                    NewFiles = maps:fold(
                        fun(Name, Rates, Acc) ->
                            Average = case maps:size(Rates) of
                                0 -> 0; % Return 0 if Rates is empty
                                _ -> lists:sum(maps:values(Rates))/maps:size(Rates)
                            end,
                            maps:put(Name, Average, Acc)
                        end,
                        #{},
                        Files
                    ),
                    From ! {ok, NewFiles, ?MODULE},
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
            case string:to_integer(Rate) of
                {error, _} ->
                    % Conversion failed, Reason contains the error reason
                    From ! {number_format_error, ?MODULE},
                    loop(Map);
                {RateInt, _} ->
                    case maps:find(Album, Map) of
                        error ->
                            From ! {album_not_found, ?MODULE},
                            loop(Map);
                        {ok, Files} ->
                            case maps:find(Name, Files) of
                                {ok, Rates} ->
                                    case maps:find(Username, Rates) of
                                        error ->
                                            NewRates = maps:put(Username, RateInt, Rates),
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
            end;

        {{update_album, Album, AllFiles}, From} ->
            case maps:find(Album, Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, OldFiles} ->
                    io:format("file_manager: OldFiles: ~p~n", [OldFiles])
                    % io:format("file_manager: AllFiles: ~p~n", [AllFiles]),
                    % NewFiles = update_all_files(OldFiles, AllFiles),
                    % io:format("file_manager: NewFiles: ~p~n", [NewFiles]),
                    % NewMap = maps:put(Album, NewFiles, Map),
                    % From ! {ok, ?MODULE},
                    % loop(NewMap)
            end;

        {{get_album_files, Album, User}, From} ->
            case maps:find(Album, Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, Files} ->
                    FilteredFiles = maps:map(
                        fun(_File, Ratings) ->
                            case maps:find(User, Ratings) of
                                {ok, Rating} ->
                                    case is_integer(Rating) of
                                        true -> integer_to_list(Rating);
                                        false -> "null"
                                    end;
                                error -> "null"
                            end
                        end, Files),
                    From ! {ok, FilteredFiles, ?MODULE},
                    loop(Map)
            end

    end.
