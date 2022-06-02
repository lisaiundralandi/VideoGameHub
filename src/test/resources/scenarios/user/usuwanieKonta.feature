#language: pl

Funkcja: Usuwanie konta

  Scenariusz: Poprawne usunięcie konta
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje i jest zalogowany
    Kiedy wyślę żądanie usunięcia konta
    I podam login "tester" i hasło "Password123!"
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED

  Scenariusz: Próba usunięcia konta nie będąc zalogowanym
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje
    Kiedy wyślę żądanie usunięcia konta
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED

