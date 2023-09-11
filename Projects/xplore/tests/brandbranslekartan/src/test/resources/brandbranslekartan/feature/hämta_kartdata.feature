# language: sv
Egenskap: Hämta kartmaterial

  Man ska kunna hämta det data som skiktet Brandbränsleklassificerat är baserat på.
  Dels som raster, och dels i vektorformat. Antingen för ett helt län eller en enskild kommun.

  Scenariomall: Hämtning av data för <kommun> skogsmark
    # Givet att jag har öppnat dialogen för hämtning av kartmaterial för skogsmark
    När jag klickar på kommunen "<kommun>" och datatypen "<typ>" för kartan "<karta>"
    Så ska jag få hem en zip-fil med datat

    Exempel:
      | kommun    | typ    | karta     |
      | Stockholm | Raster | skogsmark |
      | Stockholm | Vektor | skogsmark |
      | Ronneby   | Raster | skogsmark |
      | Ronneby   | Vektor | skogsmark |

  Scenariomall: Hämtning av data för <kommun> öppen mark
    # Givet att jag har öppnat dialogen för hämtning av kartmaterial för öppen mark
    När jag klickar på kommunen "<kommun>" och datatypen "<typ>" för kartan "<karta>"
    Så ska jag få hem en zip-fil med datat

    Exempel:
      | kommun    | typ    | karta     |
      | Stockholm | Raster | oppenmark |
      | Stockholm | Vektor | oppenmark |
      | Ronneby   | Raster | oppenmark |
      | Ronneby   | Vektor | oppenmark |

  Scenariomall: Hämtning av data för <län> skogsmark
    # Givet att jag har öppnat dialogen för hämtning av kartmaterial för skogsmark
    När jag klickar på länet "<län>" och datatyp "<typ>" för kartan "<karta>"
    Så ska jag få hem en zip-fil med datat

    Exempel:
      | län        | typ    | karta     |
      | Stockholm  | Raster | skogsmark |
      | Stockholm  | Vektor | skogsmark |
      | Norrbotten | Raster | skogsmark |
      | Norrbotten | Vektor | skogsmark |

  Scenariomall: Hämtning av data för <län> öppen mark
    # Givet att jag har öppnat dialogen för hämtning av kartmaterial för öppen mark
    När jag klickar på länet "<län>" och datatyp "<typ>" för kartan "<karta>"
    Så ska jag få hem en zip-fil med datat

    Exempel:
      | län        | typ    | karta     |
      | Stockholm  | Raster | oppenmark |
      | Stockholm  | Vektor | oppenmark |
      | Norrbotten | Raster | oppenmark |
      | Norrbotten | Vektor | oppenmark |

