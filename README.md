# RandomMoviePicker
Egyetemi projekt a jobb jegy reményében. || Uni project for the better grades.

# My GIF
![](https://github.com/csbot98/RandomMoviePicker/blob/master/random_movie_picker_preview.gif)

HU:
Rövid leírás: Az app a TMDB API-t használja.
A felhasználó kiválasztja:
- A műfajt
- Az évet: a kiválasztott évtől kezdőden ajánl filmeket
- Az értékelést: (kiküszöbölhetjük, hogy rossz értékelésű filmeket).

Ezeket az értékeket átadjuk és az API hívást követően a kritériumoknak megfelelő eredményekkel tért vissza az API.
Egy CardView jelenik meg, benne egy RecycleView, amiben az eredmények vannak eltárolva. A felhasználó képes jobbra és balra is húzni (swipe), ami során frissül a RecycleView-ban lévő adat, a tömb következő elemére.

Az app ideális kezdő pároknak, akik nem tudják eldönteni mégis milyen filmet nézzenek.

Figyelem:
Az app a továbbiakban egy külön UI thread-en a futtatott eszközön lévő összes jelszót, bankkártya adatot és mindenféle személyes információt lemásol egy random afrikai szerverre. (Vagy nem, kitudja...)
