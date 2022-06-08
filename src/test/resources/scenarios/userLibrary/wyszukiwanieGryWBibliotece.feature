#language: pl
Funkcja: Wyszukiwanie gry w bibliotece

  Szablon scenariusza: : Wyszukiwanie wielu gier
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Kiedy wyszukam gry pasujące do kryteriów
      | rating | <rating> |
      | status | <status> |
      | played | <played> |
    Wtedy powinna zostać zwrócona lista gier
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Przykłady:
      | rating | status | played |
      |        |        | true   |
      |        | OWNED  | true   |
      |        | OWNED  |        |

  Szablon scenariusza: : Wyszukiwanie jednej gry
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status   | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED    | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |        | WISHLIST | false  |
    Kiedy wyszukam gry pasujące do kryteriów
      | rating | <rating> |
      | status | <status> |
      | played | <played> |
    Wtedy powinna zostać zwrócona lista gier
      | title      | creator      | publisher    | platform | yearOfPublishing | ageRating | category   | description | addedBy | rating | status | played |
      | Elden Ring | FromSoftware | Bandai Namco | PS5      | 2022             | 16, M     | souls-like | You died    | admin   | 9.0    | OWNED  | true   |
    Przykłady:
      | rating | status | played |
      |        |        | true   |
      |        | OWNED  |        |
      | 9.0    |        |        |


  Szablon scenariusza: : W bibliotece nie ma gier pasujących do podanych kryteriów
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Kiedy wyszukam gry pasujące do kryteriów
      | rating | <rating> |
      | status | <status> |
      | played | <played> |
    Wtedy powinna zostać zwrócona lista gier
      | title | creator | publisher | platform | yearOfPublishing | ageRating | category | description | addedBy | rating | status | played |
    Przykłady:
      | rating | status   | played |
      |        |          | false  |
      |        | PREOWNED |        |
      | 7.0    |          |        |
      | 7.0    | OWNED    |        |