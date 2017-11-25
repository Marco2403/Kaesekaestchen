 /**
 * Die Klasse Controller ist f�r die Kommunikation zwischen
 * Spiellogik und Grafikoberfl�che verantwortlich.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */

 public class Controller
 {

   Spiellogik spiellogik;
   Grafikoberflaeche grafikoberflaeche;

   Controller(int breite,int l�nge,int schwierigkeit,Grafikoberflaeche g)
   {
      spiellogik=new Spiellogik(breite,l�nge,schwierigkeit);
      grafikoberflaeche=g;
      spiellogik.g=grafikoberflaeche;
   }
   void analyse()
   {
      spiellogik.analyseAnzeige();
   }
   void spielen()
   {
      spiellogik.spielen();
   }
   void neuesSpiel(int xK,int yK,String text,int sg)
   {
       grafikoberflaeche=new Grafikoberflaeche(xK,yK,text,sg);
   }
   void Verbindungeinfuegen(int x,int y)
   {
       spiellogik.Verbindungeinfuegen(x,y);
   }
 }