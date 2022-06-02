#language: pl
Funkcja: Zmiana hasła

  Scenariusz: Poprawna zmiana hasła
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje i jest zalogowany
    Kiedy wykonam zmianę hasła podając stare hasło "Password123!", nowe hasło "Password123!!" i potwierdzenie hasła "Password123!!"
    I podam login "tester" i hasło "Password123!!"
    Wtedy użytkownik powinien zostać poprawnie zalogowany

  Scenariusz: Próba zalogowania się starym hasłem
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje i jest zalogowany
    Kiedy wykonam zmianę hasła podając stare hasło "Password123!", nowe hasło "Password123!!" i potwierdzenie hasła "Password123!!"
    I podam login "tester" i hasło "Password123!"
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED

  Scenariusz: Podanie nieprawidłowego starego hasła
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje i jest zalogowany
    Kiedy wykonam zmianę hasła podając stare hasło "Password123", nowe hasło "Password123!!" i potwierdzenie hasła "Password123!!"
    Wtedy powinien zostać zwrócony błąd FORBIDDEN