BEGIN {
    header = "INSERT INTO team (id, route__id, team_number, name, start_date, contact, status) VALUES"
    id_start = 10000
    route = ENVIRON["route"]
    start_hour = ENVIRON["start"]
    print header
}

/^ *<tr>$/ {
    linemod=1
    number[1]=""
    team_number=""
    team[1]=""
    name=""
    phone[1]=""
    contact=""
}

linemod==2 {
    match($0, ">(.*)</td>", number)
    team_number = number[1]
    switch (team_number) {
        case /L.*/ : team_id = 1000 + strtonum(substr(team_number, 2)); break
        case /P.*/ : team_id = 2000 + strtonum(substr(team_number, 2)); break
        default :    team_id = team_number
    }
    id = id_start + team_id
    delta = 2 * (team_id % 100)
    hour = int(delta/60)
    min = delta % 60
    start_date = sprintf("2018-09-09 %02d:%02d:00",(start_hour + hour),min)
}

linemod==3 {
    match($0, "<td>(.*)</td>", team)
    name = team[1]
}

linemod==4 {
    match($0, "<td>(.*)</td>", phone)
    contact = phone[1]
}

/<\/tr>/ {
    print "(" id ", " route ", \"" team_number "\", \"" name "\", \"" start_date "\", \"" contact "\", " "\"1\"" "), "
}

{
    linemod++;
}

