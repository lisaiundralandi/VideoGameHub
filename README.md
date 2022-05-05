# VideoGameHub

REST API do zarządzania biblioteką gier.

## Funkcje

* Rejestracja i logowanie,
* Biblioteka gier,
* Biblioteka gier użytkownika,
* Wyszukiwanie gier,
* Ocenianie gier.

Projekt jest ciągle rozwijany, więc ta lista będzie rozszerzana.

## Jak uruchomić projekt?

VideoGameHub to aplikacja stworzona z wykorzystaniem frameworka Spring Boot. Oznacza to, że można ją uruchomić z poziomu
IntelliJ uruchamiając
klasę [VideoGameHubApplication](src/main/java/io/github/lisaiundralandi/VideoGameHubApplication.java).

Projekt można zbudować za pomocą narzędzia Maven. Należy uruchomić `mvn clean package`. Zbudowany zostanie plik jar,
który można uruchomić za pomocą polecenia `java -jar VideoGameHub.jar` (nazwa pliku może być inna, plik jar w
folderze `target`).

## Wymagana konfiguracja

Aplikacja używa bazy danych MySQL do zapisywania danych. Schemat bazy danych zostanie automatycznie stworzony.
W [application.properties](src/main/resources/application.properties) znajduje się domyślna konfiguracja połączenia (
URL `jdbc:mysql://localhost:3306/vgh` i użytkownik `vgh`). W konfiguracji brakuje hasła, które należy dostarczyć
używając zmiennej środowiskowej `DB_PASS`.

Aby użyć domyślnych ustawień, należy uruchomić serwer MySQL, stworzyć użytkownika `vgh` i bazę danych `vgh`. Ewentualnie
należy zmodyfikować plik `application.properties` tak, aby zawierał poprawną konfigurację.

## Dokumentacja API

Dostępna pod adresem `http://localhost:8080/swagger-ui/index.html` po uruchomieniu aplikacji. Kopia w formacie YAML
dostępna w repozytorium w pliku [api-docs.yaml](api-docs.yaml).

## Kolekcja Postman

WIP

