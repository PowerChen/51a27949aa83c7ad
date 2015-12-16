{application,web_server,
             [{description,"Cowboy Web Server."},
              {vsn,"1"},
              {modules,[web_db,web_handler,web_server,web_server_sup]},
              {registered,[web_server_sup]},
              {applications,[kernel,stdlib,cowboy]},
              {mod,{web_server,[]}},
              {env,[]}]}.
