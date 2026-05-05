# Spring Boot BlackJack API

Backendowa aplikacja typu REST API umożliwiająca grę w Blackjacka, zbudowana przy użyciu frameworka Spring Boot. Projekt zarządza stanem graczy oraz trwającymi grami w pamięci operacyjnej (In-Memory).

## 🚀 Technologie
* **Java**: 25
* **Framework**: Spring Boot 4.0.6
* **Budowanie projektu**: Maven (z wykorzystaniem Maven Wrapper)
* **Zależności**: Spring Web MVC

## 🎮 Mechanika Gry
Logika gry została zaimplementowana w serwisach i modelach domeny:
* **Punktacja**: Karty od 2 do 10 mają wartość nominalną, figury (Walet, Dama, Król) są warte 10 punktów, a As jest wart 11 punktów, chyba że suma przekracza 21 – wtedy jego wartość zmienia się na 1 punkt.
* **Gracz**: Rozpoczyna z saldem 1000.00. Może stawiać zakłady, dobierać karty (Hit) lub pasować (Stand).
* **Krupier (Dealer)**: Dobiera karty automatycznie do momentu osiągnięcia minimum 17 punktów.
* **Wygrane**: Wygrana gracza wypłaca mnożnik 2.00. W przypadku remisu (TIE), zakład jest zwracany.

## 📡 Dokumentacja API

### Zarządzanie Graczem (`PlayerController`)
* **`POST /api/players`**: Tworzy nowego gracza z unikalnym identyfikatorem i początkowym saldem.
* **`DELETE /api/players/{playerId}`**: Usuwa gracza oraz kończy jego aktywne sesje gier.

### Rozgrywka (`GameController`)
* **`POST /api/games/start`**: Rozpoczyna nową partię. 
    * Wymagane parametry: `playerId`, `betAmount`.
* **`POST /api/games/{gameId}/hit`**: Gracz dobiera kolejną kartę. Jeśli suma przekroczy 21, następuje automatyczna przegrana.
* **`POST /api/games/{gameId}/stand`**: Gracz kończy ruch. Krupier dobiera karty, a system rozstrzyga wynik gry.

## 🏗️ Struktura Projektu
* **`model/`**: Zawiera definicje kart, talii (`Deck`), osób (`Player`, `Dealer`) oraz logikę zliczania punktów.
* **`service/`**: Obsługuje logikę biznesową – `GameService` zarządza przebiegiem partii, a `PlayerService` portfelem gracza.
* **`dto/`**: Zawiera `GameResponceRecord`, który maskuje ukrytą kartę krupiera podczas trwania gry.

## 🛠️ Uruchomienie
Aby uruchomić aplikację, skorzystaj z dołączonego wrappera Maven:

```bash
# Systemy Linux/macOS
./mvnw spring-boot:run

# System Windows
mvnw.cmd spring-boot:run
