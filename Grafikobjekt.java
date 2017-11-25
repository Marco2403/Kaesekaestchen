import java.awt.*;
/**
 * Die Klasse Verbindung gehört zum Interface Objekt.
 * Sie entspricht der Verbindung zwischen zwei Punkten in Käsekästchen.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */
 class Grafikobjekt extends Canvas
{
       /**
       * objektauswahl=0 -> Punkt
       * objektauswahl=1 -> Verbindungwagrecht
       * objektauswahl=2 -> Verbindungsenkrecht
       * objektauswahl=3 -> Blaues erobertes Feld
       * objektauswahl=4 -> Rotes erobertes Feld
       */
       int objektauswahl;
       Grafikobjekt(int objektauswahl)
       {
            this.objektauswahl =objektauswahl;
       }
       /**
       * Die Methode paint wird beim erzeugen eines Graphikobjekts aufgerufen.
       * Es zeichnet je nach Nummer der objektauswahl das jeweilige Objekt.
       */
       public void paint(Graphics g)
       {
              if(objektauswahl==0)
              {
                int groesse;
                groesse = getHeight();
                g.setColor(Color.BLACK);
                g.fillOval(0, 0, groesse, groesse);
              }
              if(objektauswahl==1)
              {
                int groesse;
                groesse = getWidth();
                g.setColor(Color.BLACK);
                g.drawLine(0,0,groesse,0);
              }
              if(objektauswahl==2)
              {
                int groesse;
                groesse = getHeight();
                g.setColor(Color.BLACK);
                g.drawLine(0,0,0,groesse);
              }
              if(objektauswahl==3)
              {
                int groesse;
                groesse = getHeight();
                g.setColor(Color.BLUE);
                g.fillOval(0, 0, groesse, groesse);
              }
              if(objektauswahl==4)
              {
                int groesse;
                groesse = getHeight();
                g.setColor(Color.RED);
                g.fillOval(0, 0, groesse, groesse);
              }
       }
}