#language: pl
Funkcja: Usuwanie gry z biblioteki użytkownika

  Scenariusz: Pomyślne usunięcie gry z biblioteki
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | id | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | 1  | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | 2  | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Kiedy usunę grę o id 1
    Wtedy gra zostanie usunięta pomyślnie

  Scenariusz: Gra o podanym id nie jest dodana do biblioteki
    Zakładając, że jestem zalogowana jako użytkownik z loginem "tester"
    I w bibliotece użytkownika są następujące gry
      | id | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy | rating | status | played |
      | 1  | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   | 9.0    | OWNED  | true   |
      | 2  | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   | 8.0    | OWNED  | true   |
    Kiedy usunę grę o id 4
    Wtedy powinien zostać zwrócony błąd NOT_FOUND