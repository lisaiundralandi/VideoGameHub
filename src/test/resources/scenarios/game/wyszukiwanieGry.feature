#language: pl
#noinspection NonAsciiCharacters
Funkcja: Wyszukiwanie gier pasujących do kryteriów

  Szablon scenariusza: Wyszukanie wielu gier
    Zakładając, że istnieją następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |
    Kiedy wyszukam gry pasujące do kryteriów
      | title            | <tytuł>      |
      | creator          | <twórca>     |
      | publisher        | <wydawca>    |
      | platform         | <platforma>  |
      | yearOfPublishing | <rokWydania> |
      | category         | <kategoria>  |
    Wtedy powinna zostać zwrócona lista gier
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |
    Przykłady:
      | tytuł | twórca | wydawca | platforma | rokWydania | kategoria |
      |       |        |         | PS5       |            |           |
      |       |        |         |           | 2022       |           |


  Szablon scenariusza: Wyszukanie jednej gry
    Zakładając, że istnieją następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |
    Kiedy wyszukam gry pasujące do kryteriów
      | title            | <tytuł>      |
      | creator          | <twórca>     |
      | publisher        | <wydawca>    |
      | platform         | <platforma>  |
      | yearOfPublishing | <rokWydania> |
      | category         | <kategoria>  |
    Wtedy powinna zostać zwrócona lista gier
      | title                   | creator  | publisher                      | platform | yearOfPublishing | ageRating | category | description                        | addedBy |
      | Horizon: Forbidden West | Guerilla | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji    | Continuation of Horizon: Zero Dawn | admin   |
    Przykłady:
      | tytuł                   | twórca   | wydawca                        | platforma | rokWydania | kategoria |
      | Horizon: Forbidden West |          |                                | PS5       |            |           |
      |                         | Guerilla |                                |           |            |           |
      |                         |          | Sony Interactive Entertainment |           |            |           |
      |                         |          |                                |           |            | akcji     |


  Szablon scenariusza: Brak gier pasujących do kryteriów
    Zakładając, że istnieją następujące gry
      | title                   | creator      | publisher                      | platform | yearOfPublishing | ageRating | category   | description                        | addedBy |
      | Elden Ring              | FromSoftware | Bandai Namco                   | PS5      | 2022             | 16, M     | souls-like | You died                           | admin   |
      | Horizon: Forbidden West | Guerilla     | Sony Interactive Entertainment | PS5      | 2022             | 16, M     | akcji      | Continuation of Horizon: Zero Dawn | admin   |
    Kiedy wyszukam gry pasujące do kryteriów
      | title            | <tytuł>      |
      | creator          | <twórca>     |
      | publisher        | <wydawca>    |
      | platform         | <platforma>  |
      | yearOfPublishing | <rokWydania> |
      | category         | <kategoria>  |
    Wtedy powinna zostać zwrócona lista gier
      | title | creator | publisher | platform | yearOfPublishing | ageRating | category | description | addedBy |
    Przykłady:
      | tytuł | twórca | wydawca | platforma | rokWydania | kategoria |
      |       |        |         | PS4       |            |           |

