-module(server).
-import(user_manager, []).
-import(file_manager, []).
-import(login_manager, []).
-export([start/1, stop/1, server/1]).

start(Port) -> register(?MODULE, spawn(fun() -> server(Port) end)).

stop(Port) -> gen_tcp:close(Port).

server(Port) ->
    Result = gen_tcp:listen(Port, [binary, {packet, line}]),
    case Result of
        {ok, ListenSocket} ->
            user_manager:start(),
            file_manager:start(),
            login_manager:start(),
            acceptor(ListenSocket);

        {error, _} ->
            io:fwrite("Error starting server!\n")
    end.


acceptor(ListenSocket) ->
    case gen_tcp:accept(ListenSocket) of
        {ok, Socket} ->
            spawn(fun() -> acceptor(ListenSocket) end),
            userAuth(Socket,"");

        {error, closed} ->
            io:fwrite("Closed socket\n");

        {error, timeout} ->
            io:fwrite("Timeout\n");

        {error, system_limit} ->
            io:fwrite("Limit of socket reached\n");

        {error, _} ->
            io:fwrite("Error listening to socket\n")
    end.

verifyUser(Album,User) ->
    user_manager ! {{verify_user, Album, User}, self()},
    receive
        {ok, _} -> true;
        _ -> false
    end.



userAuth(Socket,User) ->
    receive
        {_, _, Data} ->
            CleanedData = re:replace(Data, "\\n|\\r", "", [global,{return,list}]),
            Info = string:split(CleanedData, ",", all),
            case Info of
                ["create_account", Username, Passwd] ->
                    login_manager ! {{create_account, Username, Passwd}, self()},
                    receive
                        {ok, _} ->
                            gen_tcp:send(Socket, "Account created\n"),
                            userAuth(Socket,Username);
                        {user_exists, _} ->
                            gen_tcp:send(Socket, "User already exists\n"),
                            userAuth(Socket,User)
                    end;

                ["close_account", Username, Passwd] ->
                    login_manager ! {{close_account, Username, Passwd}, self()},
                    receive
                        {ok, _} ->
                            gen_tcp:send(Socket, "Account closed\n"),
                            userAuth(Socket,"");
                        {invalid, _} ->
                            gen_tcp:send(Socket, "Invalid account\n"),
                            userAuth(Socket,User)
                    end;

                ["login", Username, Passwd] ->
                    login_manager ! {{login, Username, Passwd}, self()},
                    receive
                        {ok, _} ->
                            gen_tcp:send(Socket, "Logged in\n"),
                            userAuth(Socket,Username);
                        {invalid, _} ->
                            gen_tcp:send(Socket, "Invalid login\n"),
                            userAuth(Socket,User)
                    end;

                ["logout", Username] ->
                    login_manager ! {{logout, Username}, self()},
                    receive
                        {ok, _} ->
                            gen_tcp:send(Socket, "Logged out\n"),
                            userAuth(Socket,"");
                        {invalid, _} ->
                            gen_tcp:send(Socket, "Invalid logout\n"),
                            userAuth(Socket,User)

                    end;
                    
                ["online"] ->
                    login_manager ! {online, self()},
                    receive
                        {OnlineUsers, _} ->
                            gen_tcp:send(Socket, "Online users: " ++ string:join(OnlineUsers, ", ") ++ "\n")
                    end,
                    userAuth(Socket,User);
                ["create_album",Album] ->
                    case User of
                        "" ->
                            gen_tcp:send(Socket, "You must be logged in to create an album\n");
                        _ ->
                            file_manager ! {{create_album,Album},self()},
                            user_manager ! {{create_album,Album,User},self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "Album created\n");
                                {album_exists, _} ->
                                    gen_tcp:send(Socket, "Album already exists\n")
                            end,
                            receive
                                {ok, _} -> ok;
                                _ -> gen_tcp:send(Socket, "Error creating album\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["add_file", Album,File,Descricao] ->
                    case verifyUser(Album,User) of
                        false ->
                            gen_tcp:send(Socket, "You must be a user of the album to add a file\n");
                        true ->
                            file_manager ! {{add_file, Album,File,Descricao}, self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "File added\n");
                                {filename_exists, _} ->
                                    gen_tcp:send(Socket, "File name already exists in album\n");
                                {album_not_found, _} ->
                                    gen_tcp:send(Socket, "Album not found\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["remove_file",Album,File] ->
                    case verifyUser(Album,User) of
                        false ->
                            gen_tcp:send(Socket, "You must be a user of the album to remove a file\n");
                        true ->
                            file_manager ! {{remove_file, Album, File}, self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "File removed\n");
                                {file_not_found, _} ->
                                    gen_tcp:send(Socket, "File not found\n");
                                {album_not_found, _} ->
                                    gen_tcp:send(Socket, "Album not found\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["rate_file",Album,File,Rate] ->
                    case verifyUser(Album,User) of
                        false ->
                            gen_tcp:send(Socket, "You must be a user of the album to rate a file\n");
                        true ->
                            file_manager ! {{rate_file, Album, File, Rate}, self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "File rated\n");
                                {file_not_found, _} ->
                                    gen_tcp:send(Socket, "File not found\n");
                                {album_not_found, _} ->
                                    gen_tcp:send(Socket, "Album not found\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["add_user", Album,Username] ->
                    case verifyUser(Album,User) of
                        false ->
                            gen_tcp:send(Socket, "You must be a user of the album to add a user\n");
                        true ->
                            user_manager ! {{add_user, Album, Username}, self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "User added\n");
                                {username_exists, _} ->
                                    gen_tcp:send(Socket, "Username already exists in album\n");
                                {album_not_found, _} ->
                                    gen_tcp:send(Socket, "Album not found\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["remove_user", Album,Username] ->
                    case verifyUser(Album,User) of
                        false ->
                            gen_tcp:send(Socket, "You must be a user of the album to remove a user\n");
                        true ->
                            user_manager ! {{remove_user, Album, Username}, self()},
                            receive
                                {ok, _} ->
                                    gen_tcp:send(Socket, "User removed\n");
                                {username_not_found, _} ->
                                    gen_tcp:send(Socket, "Username not found in album\n");
                                {album_not_found, _} ->
                                    gen_tcp:send(Socket, "Album not found\n")
                            end
                    end,
                    userAuth(Socket,User);
                ["exit"] ->
                    gen_tcp:send(Socket, "Goodbye\n"),
                    gen_tcp:close(Socket);
                
                _ ->
                    gen_tcp:send(Socket, "Invalid command\n"),
                    userAuth(Socket,User)
            end
    end.



