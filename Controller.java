 /**
 * Die Klasse Controller ist für die Kommunikation zwischen
 * Spiellogik und Grafikoberfläche verantwortlich.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */

 public class Controller
 {

   Spiellogik spiellogik;
   Grafikoberflaeche grafikoberflaeche;

   Controller(int breite,int länge,int schwierigkeit,Grafikoberflaeche g)
   {
      spiellogik=new Spiellogik(breite,länge,schwierigkeit);
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