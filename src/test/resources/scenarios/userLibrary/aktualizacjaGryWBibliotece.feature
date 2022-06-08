#language: pl
Funkcja: Aktualizacja gry w bibliotece użytkownika

  Szablon scenariusza: : Pomyślna aktualizacja gry
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | id | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status   | played |
      | 1  | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED    | true   |
      | 2  | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |        | WISHLIST | false  |
    Kiedy zaktualizuję grę o id 2
      | rating | <rating> |
      | status | <status> |
      | played | <played> |
    Wtedy gra powinna zostać zaktualizowana pomyślnie
    Przykłady:
      | rating | status | played |
      | 8.0    | OWNED  | true   |

  Szablon scenariusza: : Ocena gry spoza dopuszczalnego zakresu
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | id | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status   | played |
      | 1  | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED    | true   |
      | 2  | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |        | WISHLIST | false  |
    Kiedy zaktualizuję grę o id 1
      | rating | <rating> |
      | status | <status> |
      | played | <played> |
    Wtedy powinien zostać zwrócony błąd BAD_REQUEST
    I komunikat błędu "Rating must be between 0 and 10"
    Przykłady:
      | rating | status | played |
      | 11     |        |        |
      | -1     |        |        |

  Scenariusz: Gra nie jest dodana do biblioteki
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | id | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status   | played |
      | 1  | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED    | true   |
      | 2  | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |        | WISHLIST | false  |
    Kiedy zaktualizuję grę o id 3
      | rating | 8.5  |
      | status |      |
      | played | true |
    Wtedy powinien zostać zwrócony błąd NOT_FOUND
    I komunikat błędu "Game had not been added to library"