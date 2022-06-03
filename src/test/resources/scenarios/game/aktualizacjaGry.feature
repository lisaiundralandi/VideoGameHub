#language: pl
Funkcja: Aktualizacja szczegółów gry

  Scenariusz: Szczegóły gry zaktualizowane pomyślnie
    Zakładając, że administrator z loginem "admin" istnieje i jest zalogowany
    I dodam grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died     |
    Kiedy zaktualizuję grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy gra zostanie zaktualizowana pomyślnie
    I szczegóły gry to
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |

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
      | description      | You died     |
    Kiedy zaktualizuję grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy powinien zostać zwrócony błąd FORBIDDEN
    I komunikat błędu "Access prohibited"

  Scenariusz: Próba aktualizacji nieistniejącej gry
    Zakładając, że administrator z loginem "admin" istnieje i jest zalogowany
    Kiedy zaktualizuję grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy powinien zostać zwrócony błąd NOT_FOUND