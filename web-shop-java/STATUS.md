# Checklista för Java Enterprise-laborationen

Presentation är servlet och JSP. Service innehåller programmets regler. DAO läser från och skriver till databasen.

## Betyg 3

### Produktkatalog

#### Presentation

- [x] Visa aktiva produkter.

#### Service

- [x] Hämta produktkatalogen.

#### DAO

- [x] Läsa produkter och lagerstatus från databasen.

### Varukorg

#### Presentation

- [x] Lägga till produkter, visa varukorgen och ta bort produkter.
- [x] Visa ett meddelande när antal justeras mot lagersaldot.

#### Service

- [x] Hantera varukorgens innehåll.
- [x] Justera antal mot lagersaldot när varukorgen öppnas.

#### DAO

- [x] Läsa produkt och lagerstatus vid tillägg och justering.

### Inloggning

#### Presentation

- [x] Skapa formulär och sidor för registrering, inloggning och utloggning.

#### Service

- [x] Hantera registrering, lösenord och inloggning.
- [x] Lagra inloggad användare i sessionen.

#### DAO

- [x] Spara användare och hämta dem via användarnamn eller id.
- [x] Kontrollera om användarnamn och e-post redan används.

### Trelagersarkitektur

- [x] Använda presentation, service och DAO tydligt i hela webbshoppen.

## Betyg 4

### Lagerstatus

- [x] Visa lagerstatus för produkter.

### Beställningar

#### Presentation

- [x] Låta kunden skicka varukorgen som en beställning.
- [x] Visa bekräftelse efter lyckad beställning.

#### Service

- [x] Kontrollera beställningen och samordna order och lager.
- [x] Tömma varukorgen först när beställningen har sparats.

#### DAO

- [x] Spara beställning och orderrader i en databastransaktion.
- [x] Minska lagersaldo i samma transaktion.
- [x] Rulla tillbaka ändringarna om beställningen misslyckas.

### Användaradministration och roller

#### Presentation

- [ ] Visa funktioner för att administrera användare.

#### Service

- [ ] Hantera användarroller: kund, admin och lagerpersonal.
- [ ] Kontrollera roller innan skyddade funktioner körs.

#### DAO

- [x] Läsa användare och ändra roll eller aktiv status.

## Betyg 5

### Produkt- och kategoriadministration

#### Presentation

- [ ] Skapa formulär för att lägga till och ändra produkter och kategorier.

#### Service

- [ ] Kontrollera indata och hantera produkt- och kategoriregler.

#### DAO

- [ ] Spara och läsa produkter och kategorier.
- [ ] Uppdatera lager för produkter.

### Lagerarbete

#### Presentation

- [ ] Visa beställningar för lagerpersonal.
- [ ] Låta lagerpersonal markera beställningar som packade.

#### Service

- [ ] Kontrollera vilka beställningar lagerpersonal får packa.

#### DAO

- [ ] Läsa beställningar och spara deras status.

### Behörighet och MVC

- [ ] Kontrollera behörighet för alla administrations- och lagerfunktioner.
- [ ] Använda en tydlig MVC-struktur för hela webbshoppen.

## Inför redovisningen

- [ ] Testa programmet och dokumentera lösningen.
- [ ] Ta fram ett klassdiagram.
- [ ] Dela det privata kodförrådet med läraren och den opponerande gruppen.
