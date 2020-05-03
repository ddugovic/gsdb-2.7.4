
set -x

java ListRecords "tournament"      "Colmar*"      > psn/Colmar
java ListRecords "tournament"      "*Den Haag*"   > psn/Den_Haag
java ListRecords "tournament"      "*Euro*"       > psn/European_Championships
java ListRecords "tournament"      "*First*"      > psn/First_40_moves
java ListRecords "tournament"      "*German*"     > psn/German_Open
java ListRecords "tournament"      "*inga*"       > psn/Ginga
java ListRecords "tournament"      "*unisen*"     > psn/Junisen
java ListRecords "tournament"      "*Belgian*"    > psn/Belgian_Championships
java ListRecords "tournament"      "*Kio*"        > psn/Kio
java ListRecords "tournament"      "*Kisei*"      > psn/Kisei
java ListRecords "tournament"      "*Meijin*"     > psn/Meijin
java ListRecords "tournament"      "*Verkouille*" > psn/Memorial_Verkouille
java ListRecords "tournament"      "*Nakabisha*"  > psn/Nakabisha_Examples
java ListRecords "tournament"      "*Zen?Nihon*"  > psn/Zen_Nihon_Pro
java ListRecords "tournament"      "*Nijmegen*"   > psn/Nijmegen
java ListRecords "tournament"      "*Dutch*"      > psn/Dutch_Championships
java ListRecords "tournament"      "*Oi*"         > psn/Oi
java ListRecords "tournament"      "*Oza*"        > psn/Oza
java ListRecords "tournament"      "*Rikai*"      > psn/Rikai_Sittard
java ListRecords "tournament"      "*Ryu?O*"      > psn/Ryu-O
java ListRecords "proam"           "*Amateur"     > psn/Amateur_games
java ListRecords "proam"           "*Professional"> psn/Professional_games
java ListRecords "date"            "1992*"        > psn/Games_from_1992
java ListRecords "date"            "1993*"        > psn/Games_from_1993
java ListRecords "date"            "1994*"        > psn/Games_from_1994
java ListRecords "date"            "1995*"        > psn/Games_from_1995
java ListRecords "date"            "1996*"        > psn/Games_from_1996
java ListRecords "date"            "1997*"        > psn/Games_from_1997
java ListRecords "date"            "1998*"        > psn/Games_from_1998
