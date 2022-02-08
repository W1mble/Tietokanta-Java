### Tietokanta-Java

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)

Javalla toteutettu yksinkertainen ohjelma, joka käyttää tietokantaa. 

Tietokantaan ja muihin tarvittaviin tiedostoihin linkki on README:n lopussa.
Tietokanta on SQLite 3 -muodossa ja sitä voi selailla vaikka DB Browser for SQLite:lla.


## Esivalmistelut

* *JDBC -ajuri pitää lisätä projektin luokkapolkuun. Eclipsessä se onnistuu näin:*
     - Oikeaklikkaa projektia
     - Java Built Path
     - Libraries
     - Classpath
     - Add external jar

* *Tietokanta eli kurssit.db pitää olle projektin juurihakemistossa.*

### Käyttö
Konsoliin kirjoitetaan haluttua toimintoa vastaava numero. Toiminnot ovat:

1. Valitse vuosi, mistä etsitään opintopisteiden kokonaismäärä
      - Esimerkiksi 2004 (2000-2019)
2. Kirjoita opiskelijan nimi kenen opintosuoritukset tulostetaan
      - Esimerkiksi Simo Heino
      - Tulostuu laajuus, kurssin koodi, päiväys ja kurssin arvosana
3. Syötä kurssin nimi mistä näytetään keskiarvo
      - Esimerkiksi TKT2473
4. Opettajien määrä, ketkä ovat antaneet eniten opintopisteitä"
      - Esimerkiksi 2 (1-100)
6. Lopeta ohjelman suoritus
     


### Vaatimukset
* **Java 15**
* **kurssit.db** https://www.dropbox.com/s/kioulwiz3z82o17/kurssit.db?dl=0
*  **sqlite-jdbc-3.32.3.2.jar** https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
