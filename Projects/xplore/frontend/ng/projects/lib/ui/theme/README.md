# Xplore Tema
Här finns det ett generellt tema för Xplore, och en service som hanterar byten mellan olika teman.
När temat sätts skapas det en del css-variabler, som man använder för att stilsätta saker som inte tillhör Angular Material.
Css-variablerna som skapas utgår ifrån Material Designs standard, mer finns att läsa på Material Designs hemsida.
https://material.io/design/color/the-color-system.html#color-theme-creation


## Användning

För att kunna använda Xplore-temat med Layoutkomponenten, måste du göra följande:
  * Importera xp-theme.scss i projektets style.scss
  * Se till att projektet använder sig av css-variablerna som skapas.


## Variabler

  - **primary**: Primär färg, används på t.ex. knappar
  - **primary-variant**
  - **on-primary**: Färg för text/ikoner som används på primary
  - **accent**: Sekundär färg, används för t.ex. radio-knappar
  - **accent-variant**
  - **on-accent**: Färg för text/ikoner som används på accent
  - **background**: Bakgrundsfärg
  - **on-background**: Textfärg för t.ex. rubriker som ligger på bakgrunden
  - **surface**: Färg för t.ex. cards som ligger på bakgrunden, är ofta samma färg som bakgrunden
  - **on-surface**: Textfärg för surface