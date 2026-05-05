# Spring Boot BlackJack 🃏

Aplikacja typu REST API realizująca logikę popularnej gry karcianej BlackJack (Oczko), zbudowana w oparciu o framework Spring Boot.

## 🚀 Technologie
* Java (Spring Boot)
* Maven

## ✨ Funkcjonalności (Zasady Gry)
Gra wspiera standardowe akcje gracza przy stole do Blackjacka:
* **Hit (Dobierz)** - Gracz dobiera kolejną kartę.
* **Stand (Czekaj)** - Gracz zatrzymuje swój obecny układ kart, a swoją turę rozpoczyna krupier (Dealer).
* **Double (Podwojenie)** - Gracz podwaja swoją stawkę początkową, dobiera **dokładnie jedną** dodatkową kartę, po czym jego tura automatycznie się kończy (przechodzi w stan Stand).
* **Rozstrzygnięcie gry** - Automatyczne porównanie wartości kart gracza i krupiera po zakończeniu tur obu stron, z uwzględnieniem "Blackjacka" (21 punktów z dwóch pierwszych kart) oraz "Bust" (przekroczenie 21 punktów).

## 🗂️ Struktura Projektu
Projekt wykorzystuje architekturę wielowarstwową:
* `controller` - Endpointy REST obsługujące akcje gracza (`PlayerController`) i stan gry (`GameController`).
* `service` - Główna logika biznesowa gry (`GameService`, `PlayerService`).
* `model` - Reprezentacja obiektów domenowych (`Card`, `Deck`, `Player`, `Dealer`, `Game`, `GameStatus` itd.).
* `dto` - Obiekty transferu danych (m.in. `GameResponseRecord`, `ErrorResponseRecord`).
* `exception` - Globalna obsługa błędów (`GlobalExceptionHandler`).

## 🌐 Endpointy API (REST)

Poniżej znajduje się lista dostępnych endpointów aplikacji:

### 🎮 Gra (`GameController`)
* `POST /api/game/start` - Rozpoczyna nową grę, przydziela początkowe karty dla gracza i krupiera.
* `GET /api/game/{gameId}` - Pobiera aktualny stan wybranej gry.
* `POST /api/game/{gameId}/hit` - Gracz podejmuje akcję "Hit" (dobiera kartę).
* `POST /api/game/{gameId}/stand` - Gracz podejmuje akcję "Stand" (kończy dobieranie kart, tura przechodzi na krupiera).
* `POST /api/game/{gameId}/double` - Gracz podejmuje akcję "Double" (dobiera ostatnią kartę i kończy turę).

### 👤 Gracz (`PlayerController`)
* `POST /api/players` - Tworzy nowego profilu gracza w systemie.
* `GET /api/players/{playerId}` - Pobiera informacje o danym graczu (np. jego statystyki lub balans konta).


## 🛠️ Uruchomienie lokalne

1. Sklonuj repozytorium:
   ```bash
   git clone <link-do-repo>
