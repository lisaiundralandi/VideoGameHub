#language: pl
Funkcja: Tworzenie konta użytkownika

  Scenariusz: Poprawne tworzenie użytkownika
    Kiedy podam login "tester" i hasło "Password123!" i potwierdzenie hasła "Password123!"
    Wtedy użytkownik powinien zostać poprawnie zarejestrowany

  Scenariusz: Podanie niepoprawnego hasła
    Kiedy podam login "testerBadPassword" i hasło "Password123!" i potwierdzenie hasła "Password123"
    Wtedy powinien zostać zwrócony błąd BAD_REQUEST
    I komunikat błędu "Passwords do not match"

  Szablon scenariusza: Podanie hasła niespełniającego wymagań
    Kiedy podam login "testerBadPassword" i hasło "<hasło>" i potwierdzenie hasła "<hasło>"
    Wtedy powinien zostać zwrócony błąd BAD_REQUEST
    I komunikat błędu "<komunikat>"
    Przykłady:
      | hasło        | komunikat                             |
      | Pas2!        | Password too short                    |
      | password222! | Password has no upper case characters |
      | PASSWORD222! | Password has no lower case characters |
      | Password!    | Password has no digits                |
      | Password2    | Password has no special characters    |

  Scenariusz: Podanie loginu, który już istnieje
    Zakładając, że użytkownik z loginem "testerBadLogin" już istnieje
    Kiedy podam login "testerBadLogin" i hasło "Password123!" i potwierdzenie hasła "Password123!"
    Wtedy powinien zostać zwrócony błąd BAD_REQUEST
    I komunikat błędu "Login is already taken"
