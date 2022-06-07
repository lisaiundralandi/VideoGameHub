#language: pl
Funkcja: Zwraca listę gier z biblioteki użytkownika

  Scenariusz: Poprawne zwrócenie listy gier
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Kiedy pobiorę listę gier z biblioteki
    Wtedy powinna zostać zwrócona lista gier
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |

  Scenariusz: Zwrócenie pustej listy w przypadku braku gier w bibliotece
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    Kiedy pobiorę listę gier z biblioteki
    Wtedy powinna zostać zwrócona lista gier
      | title | creator | publisher | platform | yearOfPublishing | ageRating | category | description | addedBy | rating | status | played |