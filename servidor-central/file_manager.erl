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

% loop through the files that exist and verify if they are in the new list
                    % if they are check if the ratings for that user exist and if they do update them
                    % if the file is not on the new list remove it from the map
                    % the files that are on the new list but not on the old list are added to the map
add_files(OldFiles, Files, User) ->
    NewFiles = maps:fold(
        fun(Name, Ratings, Acc) ->
            case maps:find(Name, Files) of
                {ok, UserRating} ->
                    % File exists in the new list
                    case maps:find(User, Ratings) of
                        {ok, _} ->
                            maps:put(Name, Ratings, Acc);
                        error ->
                            case UserRating of
                                "null" ->
                                    maps:put(Name, Ratings, Acc);
                                _ ->
                                    case string:to_integer(UserRating) of
                                        {error, _} ->
                                            maps:put(Name, Ratings, Acc);
                                        {UserRatingInt, _} ->
                                            NewRatings = maps:put(User, UserRatingInt, Ratings),
                                            maps:put(Name, NewRatings, Acc)
                                    end
                            end
                    end;
                error ->
                    Acc
            end
        end,
        #{},
        OldFiles
    ),
    maps:fold(
        fun(Name, Rating, Acc) ->
            case maps:find(Name, OldFiles) of
                error ->
                    case Rating of
                        "null" ->
                            maps:put(Name,#{}, Acc);
                        _ ->
                            case string:to_integer(Rating) of
                                {error, _} ->
                                    maps:put(Name,#{}, Acc);
                                {RatingInt, _} -> maps:put(Name,#{User=>RatingInt}, Acc)
                            end
                    end;
                _ ->
                    Acc  
            end        
        end,
        NewFiles,
        Files
    ).



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

        {{update_album, Album, Files, User}, From} ->
            case maps:find(Album, Map) of
                error ->
                    From ! {album_not_found, ?MODULE},
                    loop(Map);
                {ok, OldFiles} ->
                    io:format("OldFiles: ~p~n", [OldFiles]),
                    NewFiles = add_files(OldFiles, Files, User),
                    io:format("NewFiles: ~p~n", [NewFiles]),
                    NewMap = maps:put(Album, NewFiles, Map),
                    From ! {ok, ?MODULE},
                    loop(NewMap)
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