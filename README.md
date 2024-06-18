Link do repo z Back-Endem
https://github.com/Knutek1/PK-REST-API-Backend

Ogólnie dużo rzeczy nie zrobiłem:
- w Back-end mam tylko 10 endpointów (6 do AirTable i 4 do AirVisual) i nie zrobiłem zapytania PUT,
- nie zrobiłem logów do Bazy danych
- nie zrobiłem testów i wzorców projektowych.
Chciałem robić jeszcze drugą tabelę w AirTable, gdzie na podstawie rekordów z tabeli "Pomiary" byłyby jakieś rekordy tworzone z obliczeniami/statystyką.
Nie umiem wyrzucić odpowiedzi w Json "options": {} mimo zapisków NOT_EMPTY.
Scheduler jest we Frontendzie - zapewne powinno się to odbywać przez Backend, ze względu na zbyt małą ilość czasu do końca projektu nie zostawiłem to we frontendzie bo praktycznie wszystkie dane były przygotowane tak jak do manualnego wysyłania danych przez GUI. 
Niestety na to wszystko zabrakło mi czasu.
Czasami wyrzucało 429 Too Many Requests jak zbyt szybko zmieniałem endpointy i routy.
Czasami za pierwszym razem nie widzi zmienionego w programie @Route, konieczne jest wtedy ponowne uruchomienie.
Chyba się za dużo na Front-endzie jednak skupiłem i nie wiem czy czasami za dużo tam logiki nie umieściłem. Niby bazuje kod z Front-endu na Back-Endzie, ale jednak to Front-end zabrał mi spokojnie z 70% czasu (chciałem robić te CheckBoxy do wyboru itp.)

widoki we frontendzie:
http://localhost:8081/states
http://localhost:8081/cities
http://localhost:8081/nearest_city
http://localhost:8081/city_data
http://localhost:8081/records

W razie czego dane do AirTable
Id bazy:
app0XjQShEXDY8r9C

Id tabeli:
tbl7JvRcnZcrSf1ib
