#language: pl
#noinspection NonAsciiCharacters
Funkcja: Dodawanie gry

  Scenariusz: Gra dodana pomyślnie
    Zakładając, że użytkownik z loginem "tester" istnieje i jest zalogowany
    Kiedy dodam grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy gra zostanie dodana pomyślnie
    I szczegóły gry to
      | id               | to, co zwrócone podczas dodawania |
      | title            | Elden Ring                        |
      | creator          | FromSoftware                      |
      | publisher        | Bandai Namco                      |
      | platform         | PS5                               |
      | yearOfPublishing | 2022                              |
      | ageRating        | 16, M                             |
      | category         | souls-like                        |
      | description      | You died!                         |
      | addedBy          | tester                            |

  Scenariusz: Brak wymaganego pola
    Zakładając, że użytkownik z loginem "tester" istnieje i jest zalogowany
    Kiedy dodam grę
      | title            |              |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy powinien zostać zwrócony błąd BAD_REQUEST
    I komunikat błędu "Validation failed for object='gameRequest'. Error count: 1"

  Scenariusz: Próba dodania gry będąc niezalogowanym
    Zakładając, że nie jestem zalogowana
    Kiedy dodam grę
      | title            | Elden Ring   |
      | creator          | FromSoftware |
      | publisher        | Bandai Namco |
      | platform         | PS5          |
      | yearOfPublishing | 2022         |
      | ageRating        | 16, M        |
      | category         | souls-like   |
      | description      | You died!    |
    Wtedy powinien zostać zwrócony błąd UNAUTHORIZED
    I komunikat błędu "You need to login"