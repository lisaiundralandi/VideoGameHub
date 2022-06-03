#language: pl
Funkcja: Usuwanie gry

  Scenariusz: Gra usunięta pomyślnie
    Zakładając, że administrator z loginem "admin" istnieje i jest zalogowany
    I dodam grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Kiedy usunę grę
    Wtedy gra zostanie usunięta pomyślnie

  Scenariusz: Użytkownik nie jest administratorem
    Zakładając, że użytkownik z loginem "tester" istnieje i jest zalogowany
    I dodam grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Kiedy usunę grę
    Wtedy powinien zostać zwrócony błąd FORBIDDEN
    I komunikat błędu "Access prohibited"

  Scenariusz: Gra nie istnieje
    Zakładając, że administrator z loginem "admin" istnieje i jest zalogowany
    Kiedy usunę grę
    Wtedy powinien zostać zwrócony błąd NOT_FOUND
    I komunikat błędu "Game does not exist"