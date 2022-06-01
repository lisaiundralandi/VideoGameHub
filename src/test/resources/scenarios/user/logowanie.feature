#language: pl
#noinspection NonAsciiCharacters
Funkcja: Logowanie

  Scenariusz: Poprawne logowanie
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje
    Kiedy podam login "tester" i hasło "Password123!"
    Wtedy użytkownik powinien zostać poprawnie zalogowany

  Szablon scenariusza: Podanie niepoprawnego hasła lub loginu
    Zakładając, że użytkownik z loginem "tester" i hasłem "Password123!" istnieje
    Kiedy podam login "<login>" i hasło "<hasło>"
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED
    Przykłady:
      | login   | hasło        |
      | tester  | Password123  |
      | tester1 | Password123! |
      | tester1 | Password123  |
