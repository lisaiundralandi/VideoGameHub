#language: pl
Funkcja: Dodawanie gry do biblioteki

  Scenariusz: Gra pomyślnie dodana do biblioteki
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I istnieje następująca gra
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Kiedy dodam grę do biblioteki
    Wtedy gra zostanie pomyślnie dodana do biblioteki

  Scenariusz: Próba dodania nieistniejącej gry
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    Kiedy dodam grę do biblioteki
    Wtedy powinien zostać zwrócony błąd NOT_FOUND
    I komunikat błędu "Game does not exist"

  Scenariusz: Użytkownik nie jest zalogowany
    Zakładając, że nie jestem zalogowana
    I istnieje następująca gra
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Kiedy dodam grę do biblioteki
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED
    I komunikat błędu "You need to login"