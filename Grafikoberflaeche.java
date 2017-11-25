import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Die Klasse Grafikoberflaeche führt Spiel Käsekästchen aus
 * und zeigt die Spielzüge grafisch an.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */

public class Grafikoberflaeche extends Frame
{


  // Anfang Attribute
  private Controller controller;
  private JButton[][] Buttons;
  public Grafikobjekt[][]Spielfeld;
  private int w;
  private int s;
  private int schwierigkeitsgrad;
  //Fensterobjekte
  Panel cp = new Panel(null);
  private Button computerzugbutton = new Button();
  private Button analysebutton = new Button();
  private Button neubotton = new Button();
  private Label xKaestchenlabel = new Label();
  private Label yKaestchenlabel = new Label();
  private Label schwierigkeitslabel = new Label();
  private NummernFeld yint = new NummernFeld();
  private NummernFeld xint = new NummernFeld();
  private NummernFeld schwierigkeitsgradint = new NummernFeld();
  public Label Anmerkunglabel = new Label();
  public Label Spielstandlabel = new Label();
  private Label zustandslabel1 = new Label();
  private Label zustandslabel2 = new Label();
  private Label zustandslabel3 = new Label();
  private Label [][] analyselabels;

  //beide Variablen werden für die Verbindungeinfügen - Buttons benutzt
  int xkb;
  int ykb;
  //Breite und Länge in Kästchen; beide Variablen werden für den Button
  //"Neues Spiel gleicher Größe" benutzt
  int xKästchen;
  int yKästchen;
// Ende Attribute

   /**
   * Initaliesierung der Grafischen Oberfläche
   */
  public Grafikoberflaeche(int breite,int länge,String title,int schwierigkeit)
  {
     super(title);
     s=länge*2+1;
     w=breite*2+1;
     controller=new Controller(breite,länge,schwierigkeit,this);
     xKästchen=breite;
     yKästchen=länge;
     xkb=0;
     ykb=0;
     schwierigkeitsgrad=schwierigkeit;
     Buttons= new JButton[w][s] ;
     Spielfeld = new Grafikobjekt[w][s];
     analyselabels= new Label [w][s];
    // Frame-Initialisierung
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent evt) { dispose(); }
    });
    //Automatische Größenanpassung des Frames
    int frameWidth = 796;
    int frameHeight = 582;
    if(xKästchen>15)
    {
       int xk=xKästchen-15;
       frameWidth = frameWidth+xk*32;
    }
    if(yKästchen>14)
    {
       int yk=yKästchen-14;
       frameHeight = frameHeight+yk*32;
    }
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    add(cp);
  // Anfang Komponenten

    //Computerzug Buttons
    computerzugbutton.setBounds(8, 120, 235, 33);
    computerzugbutton.setLabel("Computerzug");
    computerzugbutton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {

        controller.spielen();
      }
    });
    cp.add(computerzugbutton);
    analysebutton.setBounds(8, 168, 235, 33);
    analysebutton.setLabel("Analyse");
    analysebutton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        controller.analyse();
      }
    });
    cp.add(analysebutton);
    //Komponenten für den Button "Neues Spiel"
    xKaestchenlabel.setBounds(48, 272, 79, 20);
    xKaestchenlabel.setText("x-Kästchen");
    xKaestchenlabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(xKaestchenlabel);
    yKaestchenlabel.setBounds(48, 304, 79, 20);
    yKaestchenlabel.setText("y-Kästchen");
    yKaestchenlabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(yKaestchenlabel);
    xint.setBounds(168, 264, 41, 28);
    xint.setText("");
    cp.add(xint);
    yint.setBounds(168, 304, 41, 28);
    yint.setText("");
    cp.add(yint);
    schwierigkeitslabel.setBounds(16, 344, 135, 20);
    schwierigkeitslabel.setText("Schwierigkeit(0-2)");
    schwierigkeitslabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    schwierigkeitslabel.setAlignment(Label.CENTER);
    cp.add(schwierigkeitslabel);
    schwierigkeitsgradint.setBounds(168, 344, 41, 28);
    schwierigkeitsgradint.setText("");
    cp.add(schwierigkeitsgradint);
    //Neues Spiel Button: Man muss nur die Kästchen ausfüllen, deren Wert man ändern möchte
    neubotton.setBounds(8, 384, 227, 25);
    neubotton.setLabel("Neues Spiel");
    neubotton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        dispose();
        int xK;
        int yK;
        int sg;
        if(xint.istZahl()==true)xK=xint.getInt();
        else xK=xKästchen;
        if(yint.istZahl()==true)yK=yint.getInt();
        else yK=yKästchen;
        if(schwierigkeitsgradint.istZahl()==true)sg=schwierigkeitsgradint.getInt();
        else sg=schwierigkeitsgrad;
        controller.neuesSpiel(xK,yK,"Käsekästchen",sg);
      }
    });
    cp.add(neubotton);
    //Anzeige Labels
    Spielstandlabel.setBounds(544, 16, 220, 20);
    Spielstandlabel.setText("Spielstand: 0:0");
    Spielstandlabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    Spielstandlabel.setAlignment(Label.CENTER);
    cp.add(Spielstandlabel);
    Anmerkunglabel.setBounds(316, 16, 219, 20);
    Anmerkunglabel.setText("Spieler Blau fängt an");
    Anmerkunglabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    Anmerkunglabel.setBackground(Color.WHITE);
    Anmerkunglabel.setAlignment(Label.CENTER);
    cp.add(Anmerkunglabel);
    zustandslabel1.setAlignment(Label.CENTER);
    zustandslabel1.setBounds(8, 20, 235, 20);
    zustandslabel1.setText("x-Kastchen:"+xKästchen);
    zustandslabel1.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    zustandslabel1.setVisible(true);
    cp.add(zustandslabel1);
    zustandslabel2.setAlignment(Label.CENTER);
    zustandslabel2.setBounds(8, 48, 235, 20);
    zustandslabel2.setText("y-Kastchen:"+yKästchen);
    zustandslabel2.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    cp.add(zustandslabel2);
    zustandslabel3.setBounds(8, 80, 233, 20);
    zustandslabel3.setText("Schwierigkeit:"+schwierigkeit);
    zustandslabel3.setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
    zustandslabel3.setAlignment(Label.CENTER);
    cp.add(zustandslabel3);
  // Ende Komponenten

    setResizable(true);
    setVisible(true);

    Punkteerzeugen();
    erlaubteVerbindungenfuellen();
  }

  // Anfang Methoden


    /**
     * Diese Methode wird nur durch den Konstructor aufgerufen
     * und füllt die Spielfeldmatrix mit Punkten.
     */
    private void Punkteerzeugen()
    {

        for (int i=0;i<w;i=i+2)
        {
            for (int j=0;j<s;j=j+2)
            {
                Spielfeld[i][j] = new Grafikobjekt(0);
                Spielfeld[i][j].setLocation(i*16+8+260, j*16+40+20);
                Spielfeld[i][j].setVisible(true);
                Spielfeld[i][j].setSize(8, 8);
                cp.add(Spielfeld[i][j]);
            }
        }
    }
    /**
     * Alle möglichen Verbindungen werden erzeugt ,
     * in der Grafikoberflaeche Matrix gespeichert und vorerst auf unsichtbar gesetzt.
     * Außerdem wird für jede Verbindung ein Button, zum einfügen/sichtbarmachen dieser,
     * erzeugt und in der Buttons Matrix gespeichert.
     * Diese Methode wird nur durch den Konstructor aufgerufen.
     */
    private void erlaubteVerbindungenfuellen()
    {
        int i;
        for (i=0;i<s;i++)
        {
            for (int j=1;j<w;j=j+2)
            {
                Spielfeld[j][i] = new Grafikobjekt(1);
                Spielfeld[j][i].setLocation(j*16+5+260, i*16+43+20);
                Spielfeld[j][i].setVisible(false);
                Spielfeld[j][i].setSize(16, 5);
                cp.add(Spielfeld[j][i]);
                xkb=j;ykb=i;
                Buttons[j][i]=new JButton();
                Buttons[j][i].setBounds(j*16+5+260, i*16+43+20, 16, 5);
                Buttons[j][i].addActionListener(new ActionListener() {
                int x=xkb;int y=ykb;
                public void actionPerformed(ActionEvent evt) {
                       controller.Verbindungeinfuegen(x,y);
                }
                });
                cp.add(Buttons[j][i]);
            }
            i=i+1;
            if(i<s)
            {
                for (int j=0;j<w;j=j+2)
                {
                    Spielfeld[j][i] = new Grafikobjekt(2);
                    Spielfeld[j][i].setLocation(j*16+11+260,i*16+37+20);
                    Spielfeld[j][i].setVisible(false);
                    Spielfeld[j][i].setSize(5, 16);
                    cp.add(Spielfeld[j][i]);
                    xkb=j;ykb=i;
                    Buttons[j][i]=new JButton();
                    Buttons[j][i].setBounds(j*16+11+260, i*16+37+20, 5, 16);
                    Buttons[j][i].addActionListener(new ActionListener() {
                    int x=xkb;int y=ykb;
                    public void actionPerformed(ActionEvent evt) {
                           controller.Verbindungeinfuegen(x,y);
                    }
                    });
                    cp.add(Buttons[j][i]);
                }
            }
        }

    }
    /**
    * Der Spielstand wird auf den neuen Wert gesetzt.
    */
    void setzeSpielstand(int blau, int rot)
    {
         Spielstandlabel.setText("Spielstand "+blau+" : "+rot);
    }
    /**
    * Das Anmerkungslabel erhält seinen neuen Wert.
    */
    void setzeAnmerkung(String Text)
    {
         Anmerkunglabel.setText(Text);
    }
    /**
    * Die Farbe eines Grafikobjekts wird gesetzt
    */
    void setzeFarbe(int x,int y,String farbe)
    {
         if(farbe.equals("blau"))
         {
             Spielfeld[x][y].setBackground(Color.BLUE);
         }
         else if(farbe.equals("rot"))
         {
             Spielfeld[x][y].setBackground(Color.RED);
         }
    }
    /**
    * Die Sichtbarkeit eines Grafikobjekts wird geändert
    */
    void setzeSichtbarkeit(int x,int y,boolean sichtbar)
    {
         Spielfeld[x][y].setVisible(sichtbar);
    }
    /**
    * Gibt den Inhalt des Anmerkungslabels
    */
    String gibAnmerkung()
    {
         return Anmerkunglabel.getText();
    }
    /**
    * Ein Punkt in der Farbe des Spielers wird in das eroberte Feld gesetzt.
    */
    void erobertesFeldeinfuegen(int x,int y,int objektauswahl)
    {
         Spielfeld[x][y] = new Grafikobjekt(objektauswahl);
         Spielfeld[x][y].setLocation(x*16+268, y*16+60);
         Spielfeld[x][y].setVisible(true);
         Spielfeld[x][y].setSize(10, 10);
         cp.add(Spielfeld[x][y]);
    }
    /**
    * Ein Analyselabel mit der Zahl der Gewinne für blau an einer Stelle wird eingefügt.
    */
   void analyselabelseinfuegen(int x,int y, int nummer,boolean wagerecht)
   {
       if(wagerecht==true)
       {
          analyselabels[x][y]=new Label();
          analyselabels[x][y].setBounds(x*16+263, y*16+30+20, 16, 12);
          analyselabels[x][y].setText(""+nummer);
          analyselabels[x][y].setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
          cp.add(analyselabels[x][y]);
       }
       else
       {
          analyselabels[x][y]=new Label();
          analyselabels[x][y].setBounds(x*16+255, y*16+35+20, 16, 12);
          analyselabels[x][y].setText(""+nummer);
          analyselabels[x][y].setFont(new Font("MS Sans Serif", Font.PLAIN, 13));
          cp.add(analyselabels[x][y]);
       }
   }
   /**
    * Alle Analyselabels werden entfernt.
    */
   void analyselabelsentfernen()
   {
       int i;
        for (i=0;i<s;i++)
        {
            for (int j=1;j<w;j=j+2)
            {
                if(analyselabels[j][i]!=null)cp.remove(analyselabels[j][i]);
                analyselabels[j][i]=null;
            }
            i=i+1;
            if(i<s)
            {
                for (int j=0;j<w;j=j+2)
                {
                   if(analyselabels[j][i]!=null)cp.remove(analyselabels[j][i]);
                   analyselabels[j][i]=null;
                }
            }
        }

   }
    /**
    * Main Methode.
    */
   public static void main(String[] args)
   {
     new Grafikoberflaeche(2,2,"Käsekästchen",2);
   }

}

