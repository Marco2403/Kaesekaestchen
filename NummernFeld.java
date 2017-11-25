import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Die Klasse Nummernfeld ist für das Eingeben von Zahlen nötig.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */

public class NummernFeld extends TextField
{
  /**
   * Gibt den Inhalt als eine Integer Zahl zurück
   */
  public int getInt()
  {
    Integer i = new Integer(getText());
    return i.intValue();
  }
  /**
   * Prüft, ob eine ein- oder zweistellige Zahl eingegeben wurde
   */
  public boolean istZahl()
  {
      String text=getText();
      if(text.length()==1)
      {
            if(istZiffer(text.charAt(0),0)==true)return true;
            else return false;
      }
      if(text.length()==2)
      {
            if(istZiffer(text.charAt(0),0)==true&istZiffer(text.charAt(1),1)==true)return true;
            else return false;
      }
      else return false;
  }
  /**
   * Prüft, ob es sich bei einem Zeichen um eine Ziffer handelt.
   * Falls diese sich an erster Selle befindet ist eine "0" nicht möglich.
   */
  private boolean istZiffer(char zeichen, int stelle)
    {
        if(stelle==0)
        {
            switch(zeichen)
            {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return true;

                default:
                    return false;
            }
        }
        else
        {
            switch(zeichen)
            {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return true;

                default:
                    return false;
            }
        }
    }

}