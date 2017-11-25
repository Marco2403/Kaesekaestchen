import java.util.Random;

/**
 * Die Klasse Spiellogik ist für den Spielablauf verantwortlich.
 *
 * @author (Marco Volkert)
 * @version (Seminararbeit)
 */

 public class Spiellogik
{
  //allgemeine Attribute
  public boolean[][]Spielfeldan; //Spielfeld auf dem die Analyse durchgeführt wird.
  public boolean[][]Spielfeldan2; //Spielfeld auf dem eine Analyse
                                       //während der Analyse durchgeführt wird.
  private int w;
  private int s;
  private boolean blauistdran ;
  private int rot;
  private int blau;
  public int verbindungsanzahl;
  public int schwierigkeitsgrad;
  private Random generator;
  Grafikoberflaeche g;
  //Hilfsattribute für spielen()
  private int feVxk;  //finde eroberungs Verbindung x Koordinate
  private int feVyk;  //finde eroberungs Verbindung y Koordinate
  private int fuVxk;  //finde ungefährliche Verbindung x Koordinate
  private int fuVyk;  //finde ungefährliche Verbindung y Koordinate
  private int fwVxk;  //finde weniger üble Verbindung x Koordinate
  private int fwVyk;  //finde weniger üble Verbindung y Koordinate
  private int faVxk;  //finde analyse Verbindung x Koordinate
  private int faVyk;  //finde analyse Verbindung y Koordinate
//  Hilfsattribute für die Analyse
  private int[] ax;
  private int[] ay;
  private int ebene;
  private int blauanalyse;
  private boolean blauistdrananalyse ;
  private int[][] analyse;
  private int [] stellenanzahl;
  private boolean [] blauistdrananalysematrix;
  private boolean schonanalysiert;
  private int anzahlungefährlicherverbindungen;
  private int analysex;
  private int analysey;
  
   /**
   * Initaliesierung aller Variablen und Matrixen.
   */
   public Spiellogik(int breite,int länge,int schwierigkeit)
  {
         s=länge*2+1;
         w=breite*2+1;
         Spielfeldan= new boolean[w][s];
         Spielfeldan2= new boolean[w][s];
         blauistdran=true;
         feVxk=0;
         feVyk=0;
         fuVxk=0;
         fuVyk=0;
         fwVxk=0;
         fwVyk=0;
         faVxk=0;
         faVyk=0;
         generator= new Random();
         verbindungsanzahl=(länge+1)*breite+(breite+1)*länge;
         ax=new int[verbindungsanzahl];
         ay=new int[verbindungsanzahl];
         schwierigkeitsgrad=schwierigkeit;
         schonanalysiert = false;

   }
    /**
     * Diese Methode wird beim drücken eines Einfüge Buttons aufgerufen.
     * Der Button übermittelt dieser Methode seine Position,
     * sodass an dieser Stelle eine Verbindung eingefügt werden kann,
     * somit hat der user weniger Arbeit.
     *
     * Desweiteren fängt die Methode mit dem Spieler Blau an und
     * wechselt automatisch zwischen den Spielern.
     * Wenn ein Kästchen geschlossen wird, wird dieses in der Farbe des Spielers markiert.
     */
    void Verbindungeinfuegen(int XKoordinate, int YKoordinate)
    {
        int x=XKoordinate;
        int y=YKoordinate;
         if(feldwurdeerobert(x,y,g.Spielfeld[x][y].objektauswahl)==true)
         {
             einfügeneinerEroberungsverbindung(x,y);
         }
         else
         {
             einfügeneinernormalenVerbindung(x,y);
         }
         schonanalysiert = false;
         g.analyselabelsentfernen();
    }
    private void einfügeneinerEroberungsverbindung(int x, int y)
    {
         g.setzeSichtbarkeit(x,y,true);
         Spielfeldan[x][y]=true;
         Felderoberteinfuegen(x,y,g.Spielfeld[x][y].objektauswahl,blauistdran);
         if(blauistdran==true) g.setzeFarbe(x,y,"blau");
         else g.setzeFarbe(x,y,"rot");
         zähleeroberteFelder();
         g.setzeSpielstand(blau, rot);
         if(Spielfeldistvoll())Gewinnerausgeben();
         else if(blauistdran==true) g.setzeAnmerkung("Spieler Blau ist nochmal dran");
         else g.setzeAnmerkung("Spieler Rot ist nochmal dran");
    }
    private void einfügeneinernormalenVerbindung(int x, int y)
    {
       g.setzeSichtbarkeit(x,y,true);
       Spielfeldan[x][y]=true;
       if(blauistdran==true)blauistdran=false;
       else blauistdran=true;
       if(blauistdran==false)g.setzeFarbe(x,y,"blau");
       else g.setzeFarbe(x,y,"rot");
       g.setzeSpielstand(blau, rot);
        if(blauistdran==true)g.setzeAnmerkung("Spieler Blau ist jetzt dran");
       else g.setzeAnmerkung("Spieler Rot ist jetzt dran");
    }
    /**
     * Der Computer gibt seinen eigenen Spielzug ab.
     * Dabei verfolgt er vier Devisen:
     * - Wenn ein Feld geschlossen werden kann, schließe es und mach noch einen Zug,
     *   sofern kein speciales Ende nötig ist.
     * - Ansonsten hinterlass kein Feld das der Gegner im nächsten Zug schließen könnte.
     * - Wenn keins der beiden geht, überlass dem Gegner möglichst wenig Felder.
     * - Führe eine kommplet Analyse durch, wenn dies möglich und nötig ist.
     */
    void spielen()
    {

           findeeroberungsVerbindung();
           if(!(feVxk==0&feVyk==0))
           {
               int x=feVxk;
               int y=feVyk;
               if(specialendnötig(analyseMöglich())==false)
               {
                 einfügeneinerEroberungsverbindung(x,y);
                 feVxk=0;
                 feVyk=0;
                 schonanalysiert = false;
                 g.analyselabelsentfernen();
                 this.spielen();
               }
               else if(analyseMöglich()==true)
               {
                      g.setzeAnmerkung("Bitte warten");
                      analyse();
                      if(feldwurdeerobert(faVxk,faVyk,g.Spielfeld[faVxk][faVyk].objektauswahl)==true)
                     {
                         einfügeneinerEroberungsverbindung(faVxk,faVyk);
                     }
                     else
                     {
                         einfügeneinernormalenVerbindung(faVxk,faVyk);
                     }
                      faVxk=0;
                      faVyk=0;
                      feVxk=0;
                      feVyk=0;
               }
               else
               {
                   findespecialendVerbindung();
                   if(!(fwVxk==0&fwVyk==0))
                   {
                      einfügeneinernormalenVerbindung(fwVxk,fwVyk);
                      feVxk=0;
                      feVyk=0;
                      fwVxk=0;
                      fwVyk=0;
                   }
                   else
                   {
                       einfügeneinerEroberungsverbindung(x,y);
                       feVxk=0;
                       feVyk=0;
                       schonanalysiert = false;
                       g.analyselabelsentfernen();
                       this.spielen();
                   }
               }
           }
           else if(analyseMöglich()==true)
           {
                  g.setzeAnmerkung("Bitte warten");
                  analyse();
                  if(feldwurdeerobert(faVxk,faVyk,g.Spielfeld[faVxk][faVyk].objektauswahl)==true)
                 {
                     einfügeneinerEroberungsverbindung(faVxk,faVyk);
                 }
                 else
                 {
                     einfügeneinernormalenVerbindung(faVxk,faVyk);
                 }
                  faVxk=0;
                  faVyk=0;
           }
           else
           {
                findeungefährlicheVerbindung();
                 if(!(fuVxk==0&fuVyk==0))
                 {
                    einfügeneinernormalenVerbindung(fuVxk,fuVyk);
                    fuVxk=0;
                    fuVyk=0;
                  }
                  else
                  {
                     findewenigeruebleVerbindung();
                     if(!(fwVxk==0&fwVyk==0))
                     {
                        einfügeneinernormalenVerbindung(fwVxk,fwVyk);
                        fwVxk=0;
                        fwVyk=0;
                     }
                }
           }
           schonanalysiert = false;
           g.analyselabelsentfernen();
    }
    /**
     * Diese Methode gibt aus, ob eine Analyse durchführbar ist
     * und berüktsichtigt dabei den schwierigkeitsgrad
     */
    private boolean analyseMöglich()
    {
        if(gibAnzahlfreieVerbindungen()>1)
        {
          if(schwierigkeitsgrad>=2)
          {
             findeungefährlicheVerbindung();
             if(fuVxk==0&fuVyk==0)
             {
                if(verbindungsanzahl<50)return true;
                else if(gibAnzahlfreieVerbindungen()<=24)return true;
                else return false;
             }
             else if(gibAnzahlfreieVerbindungen()<=13)
             {
               fuVxk=0;fuVyk=0;return true;
             }
             else if(verbindungsanzahl<15&anzahlungefährlicherverbindungen<5)
             {
               fuVxk=0;fuVyk=0;return true;
             }
             else if(verbindungsanzahl<25&anzahlungefährlicherverbindungen<4)
             {
               fuVxk=0;fuVyk=0;return true;
             }
             else if(verbindungsanzahl<37&anzahlungefährlicherverbindungen<3)
             {
               fuVxk=0;fuVyk=0;return true;
             }
             else if(verbindungsanzahl<45&anzahlungefährlicherverbindungen<2)
             {
               fuVxk=0;fuVyk=0;return true;
             }
             else return false;
          }
          else return false;
        }
        else return false;
    }
    /**
     * Führt eine komplette Analyse aus,
     * sofern analyseAnzeige() dies nicht schon gemacht hat.
     * Wenn dies der Fall ist wertet sie nur die schon gemachte Analyse aus.
     */
    private void analyse()
    {
        if(schonanalysiert==false)
        {
          int ebenen=gibAnzahlfreieVerbindungen();
          int stellen=gibAnzahlfreieVerbindungen();
          analyse= new int[ebenen][stellen];
          stellenanzahl= new int[ebenen];
          for(int i=0;i<ebenen;i++)
          {
             stellenanzahl[i]=-1;
          }
          blauistdrananalyse=blauistdran;
          blauistdrananalysematrix= new boolean [ebenen];
          ax=new int[gibAnzahlfreieVerbindungen()];
          ay=new int[gibAnzahlfreieVerbindungen()];
          ebene=-1;
          leereAnalyseMatrix();
          spielenanalyse();
        }
        werteanalyseaus();
        leereSpielfeldan();
        leereAnalyseMatrix();
    }
    /**
     * Führt eine Analyse durch und gibt diese Grafisch aus.
     */
    void analyseAnzeige()
    {
        if(analyseMöglich())
        {
            if(schonanalysiert==true)
            {
               g.setzeAnmerkung("Schon analysiert");
            }
            else
            {
                String anmerkungalt=g.gibAnmerkung();
                g.setzeAnmerkung("Bitte warten");
                int ebenen=gibAnzahlfreieVerbindungen();
                int stellen=gibAnzahlfreieVerbindungen();
                analyse= new int[ebenen][stellen];
                stellenanzahl= new int[ebenen];
                for(int i=0;i<ebenen;i++)
                {
                   stellenanzahl[i]=-1;
                }
                blauistdrananalyse=blauistdran;
                blauistdrananalysematrix= new boolean [ebenen];
                ax=new int[gibAnzahlfreieVerbindungen()];
                ay=new int[gibAnzahlfreieVerbindungen()];
                ebene=-1;
                leereAnalyseMatrix();
                spielenanalyse();
                zeigeanalyse();
                schonanalysiert = true;
                g.setzeAnmerkung(anmerkungalt);
                leereSpielfeldan();
            }
        }
        else g.setzeAnmerkung("Analyse nicht möglich");
    }
    /**
     * Diese Methode führt von einer Verbindung ausgehend alle möglichen Spielablaufe durch
     * und zahlt die dabei die Anzahl der Siege.
     */
    private void spielenanalyse()
    {
       ebene++;
       stellenanzahl[ebene]=-1;
       feVxk=0;
       feVyk=0;
       findeeroberungsVerbindung();
       int x= feVxk;
       int y= feVyk;
       if(!(feVxk==0&feVyk==0)&&specialendnötig(true)==false)
       {
           Spielfeldan[x][y]=true;
           ax[ebene]=x;
           ay[ebene]=y;
           stellenanzahl[ebene]++;
           blauistdrananalysematrix[ebene]=blauistdrananalyse;
           if(blauistdrananalyse==true)setzeeroberung(x,y);
           if(ebene==0)
           {
             analysex=x;analysey=y;
           }
           feVxk=0;
           feVyk=0;
           x=0;
           y=0;
           if(Spielfeldistvoll()==false)
            {
               spielenanalyse();
            }
            else
            {
                 analyse[ebene][stellenanzahl[ebene]]=blauanalyse;
                 analyse[ebene-1][stellenanzahl[ebene-1]]=blauanalyse;
                 
            }
       }
       else
       {
            for (y=0;y<s;y++)
            {
                for(x=0;x<w;x++)
                {
                    if(g.Spielfeld[x][y]!=null&Spielfeldan[x][y]==false)
                    {
                        if(g.Spielfeld[x][y].isVisible()==false)
                        {
                            Spielfeldan[x][y]=true;
                            ax[ebene]=x;
                            ay[ebene]=y;
                            stellenanzahl[ebene]++;
                            blauistdrananalysematrix[ebene]=blauistdrananalyse;
                            if(feldwurdeerobert(x,y,g.Spielfeld[x][y].objektauswahl)==true)
                            {
                               if(blauistdrananalysematrix[ebene]==true)setzeeroberung(x,y);
                            }
                            else
                            {
                               if(blauistdrananalyse==true)blauistdrananalyse=false;
                               else blauistdrananalyse=true;
                            }
                            if(Spielfeldistvoll()==false)
                            {
                               spielenanalyse();
                            }
                            else
                            {
                                 analyse[ebene][stellenanzahl[ebene]]=blauanalyse;
                                 analyse[ebene-1][stellenanzahl[ebene-1]]=blauanalyse;
                                 x=w;y=s;
                            }
                        }
                    }
                }
            }
        }
        if(0<ebene)
        {
           if(Spielfeldan[ax[ebene]][ay[ebene]]==true)
            {
               if(blauistdrananalysematrix[ebene]==true)
               {
                  if(feldwurdeerobert(ax[ebene],ay[ebene],
                  g.Spielfeld[ax[ebene]][ay[ebene]].objektauswahl)==true)
                  setzeeroberungzurück(ax[ebene],ay[ebene]);
               }
               Spielfeldan[ax[ebene]][ay[ebene]]=false;
            }
            ax[ebene]=0;
            ay[ebene]=0;
            if(blauistdrananalysematrix[ebene-1]==true)
            {
               if(feldwurdeerobert(ax[ebene-1],ay[ebene-1],
               g.Spielfeld[ax[ebene-1]][ay[ebene-1]].objektauswahl)==true)
               setzeeroberungzurück(ax[ebene-1],ay[ebene-1]);
            }
            Spielfeldan[ax[ebene-1]][ay[ebene-1]]=false;
            ax[ebene-1]=0;
            ay[ebene-1]=0;
            if(Spielfeldistvoll()==false)
            {
                 if(blauistdrananalysematrix[ebene]==true)
                 {
                    analyse[ebene-1][stellenanzahl[ebene-1]]=findeMaximalwerteinerMatrix(ebene);
                 }
                 else
                 {
                    analyse[ebene-1][stellenanzahl[ebene-1]]=findeMinimalwerteinerMatrix(ebene);
                 }
            }
            blauistdrananalyse=blauistdrananalysematrix[ebene-1];
            ebene--;
        }
    }
    /**
     *  Diese Methode zählt die Anzahl der eroberten Felder hoch,
     *  wenn ein Feld während der Analyse erobert wurde.
     */
    private void setzeeroberung(int x,int y)
    {
        if(g.Spielfeld[x][y].objektauswahl==1)
        {
            if(0<x&1<y&y<s-2)
            {
                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true&
                 Spielfeldan[x][y-2]==true)blauanalyse++;
                 if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true&
                 Spielfeldan[x][y+2]==true)blauanalyse++;
            }
            else blauanalyse++;

        }
        else
        {
            if(0<y&1<x&x<w-2&y<s-1)
            {
                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true&
                 Spielfeldan[x+2][y]==true)blauanalyse++;
                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true&
                 Spielfeldan[x-2][y]==true)blauanalyse++;
            }
            else blauanalyse++;
        }
    }
    /**
     *  Diese Methode setzt die Eroberung während der Analyse zurück,
     *  damit ein neuer Weg ausprobiert werden kann.
     */
    private void setzeeroberungzurück(int x,int y)
    {
         if(g.Spielfeld[x][y].objektauswahl==1)
        {
            if(0<x&1<y&y<s-2)
            {
                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true&
                 Spielfeldan[x][y-2]==true)blauanalyse--;
                 if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true&
                 Spielfeldan[x][y+2]==true)blauanalyse--;
            }
            else blauanalyse--;

        }
        else
        {
            if(0<y&1<x&x<w-2&y<s-1)
            {
                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true&
                 Spielfeldan[x+2][y]==true)blauanalyse--;
                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true&
                 Spielfeldan[x-2][y]==true)blauanalyse--;
            }
            else blauanalyse--;
        }
    }
    /**
     *  Diese Metode leert die Analyse Martix vollständig
     */
    private void leereAnalyseMatrix()
    {
       int ebenen=gibAnzahlfreieVerbindungen();
       int stellen=gibAnzahlfreieVerbindungen();
       for(int i=0;i<ebenen;i++)
       {
         for(int j=0;j<stellen;j++)
         {
           analyse[i][j]=-1;
         }
       }
    }
    /**
     *  Diese Metode leert eine Ebene der Analyse Martix
     */
    private void leereAnalyseMatrixeinerEbene(int e)
    {
       int stellen=gibAnzahlfreieVerbindungen();
       for(int j=0;j<stellen;j++)
       {
         analyse[e][j]=-1;
       }
    }
    /**
     * Diese Metode sucht nach dem Minimalwert in einer Ebene der Analyse Matrix.
     */
    private int findeMinimalwerteinerMatrix(int e)
    {
         int minwert=1000;
        for (int i=0;i<=stellenanzahl[e];i++)
        {
            if(analyse[e][i]<minwert)minwert=analyse[e][i];
        }
        leereAnalyseMatrixeinerEbene(e);
        return minwert;
    }
    /**
     * Diese Metode sucht nach dem Maximalwert in einer Ebene der Analyse Matrix
     */
    private int findeMaximalwerteinerMatrix(int e)
    {
        int maxwert=-1;
        for (int i=0;i<=stellenanzahl[e];i++)
        {
            if(analyse[e][i]>maxwert)maxwert=analyse[e][i];
        }
        leereAnalyseMatrixeinerEbene(e);
        return maxwert;
    }
    /**
     * Wertet die Analyse aus und gibt die Koordinaten einer der besten Verbindung aus.
     */
    private void werteanalyseaus()
    {
        int nummer=0;
        int probenummer=0;
        int[] nummern = new int[stellenanzahl[0]+1];
        int j=0;
        if(blauistdran==true)
        {
           int maxwert=-1;
           for (int i=0;i<=stellenanzahl[0];i++)
           {
              if(analyse[0][i]>maxwert)
              {
                  maxwert=analyse[0][i];
                  nummern[0]=i;
              }
           }
           for (int i=0;i<=stellenanzahl[0];i++)
           {
              if(analyse[0][i]==maxwert)
              {
                  nummern[j]=i;
                  j++;
              }
           }
           
        }
        else
        {
            int minwert=1000;
            for (int i=0;i<=stellenanzahl[0];i++)
           {
              if(analyse[0][i]<minwert)
              {
                  minwert=analyse[0][i];
                  nummern[0]=i;
              }
           }
           for (int i=0;i<=stellenanzahl[0];i++)
           {
              if(analyse[0][i]==minwert)
              {
                  nummern[j]=i;
                  j++;
              }
           }
        }
        if(j!=0)
        {
            int g= generator.nextInt(j);
            nummer=nummern[g];
        }
         for (int y=0;y<s;y++)
        {
            for(int x=0;x<w;x++)
            {
                if(g.Spielfeld[x][y]!=null)
                {
                    if(g.Spielfeld[x][y].isVisible()==false)
                    {
                        if(probenummer==nummer)
                        {
                            faVxk=x;faVyk=y;
                        }
                        probenummer++;
                    }
                }
            }
        }
    }
    /**
     * Zeigt die Gewinne, die die Analyse erechnet hat,
     * neben den jeweiligen Verbindungen an.
     */
    private void zeigeanalyse()
    {
        int[] gewinn = new int[stellenanzahl[0]+1];
        int j=0;
        for (int i=0;i<=stellenanzahl[0];i++)
        {
           gewinn[j]=analyse[0][i];
           j++;
        }
        j=0;
        if(!(analysex==0&analysey==0))
        {
           g.analyselabelseinfuegen(analysex,analysey,gewinn[j],
           g.Spielfeld[analysex][analysey].objektauswahl==1);
           analysex=0;analysey=0;
        }
        else
        {
            for (int y=0;y<s;y++)
            {
                for(int x=1;x<w;x=x+2)
                {
                    if(g.Spielfeld[x][y]!=null)
                    {
                       if(g.Spielfeld[x][y].isVisible()==false)
                       {
                           g.analyselabelseinfuegen(x,y,gewinn[j],true);
                           j++;
                       }
                    }
                }
                y++;
                if(y<s)
                {
                    for (int x=0;x<w;x=x+2)
                    {
                        if(g.Spielfeld[x][y]!=null)
                        {
                           if(g.Spielfeld[x][y].isVisible()==false)
                           {
                                g.analyselabelseinfuegen(x,y,gewinn[j],false);
                               j++;
                           }
                        }
                    }
                }
            }
        }
    }
    /**
     * Diese Methode sucht nach Verbindungen in der Spielfeldan-Matrix mit denen ein Feld geschlossen
     * und somit "erobert" werden kann.
     * Die X-Koordinate speichert es in feVxk und die Y-Koordinate speichert es in feVyk.
     */
    private void findeeroberungsVerbindung()
    {
        for (int y=0;y<s;y++)
        {
            for(int x=1;x<w;x=x+2)
            {
                if(g.Spielfeld[x][y]!=null)
                {
                    if(Spielfeldan[x][y]==false&g.Spielfeld[x][y].isVisible()==false)
                    {
                        if(y==0)
                        {
                             if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true&
                             Spielfeldan[x][y+2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                        }
                        else if(y==s-1)
                        {
                             if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true&
                             Spielfeldan[x][y-2]==true){feVxk=x;feVyk=y;x=w;y=s;}

                        }
                        else
                        {
                             if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true&
                             Spielfeldan[x][y-2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                             else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true&
                             Spielfeldan[x][y+2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                        }
                    }
                }
            }
            y++;
            if(y<s-1)
            {
                for (int x=0;x<w;x=x+2)
                {
                    if(g.Spielfeld[x][y]!=null)
                    {
                        if(Spielfeldan[x][y]==false&g.Spielfeld[x][y].isVisible()==false)
                        {
                            if(x==0)
                            {
                                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true&
                                 Spielfeldan[x+2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                            else if(x==w-1)
                            {
                                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true&
                                 Spielfeldan[x-2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                            else
                            {
                                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true&
                                 Spielfeldan[x+2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                                 else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true&
                                 Spielfeldan[x-2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * Diese Methode sucht nach Verbindungen in der Spielfeldan2 Matrix mit denen ein Feld geschlossen
     * und somit "erobert" werden kann.
     * Die X-Koordinate speichert es in feVxk und die Y-Koordinate speichert es in feVyk.
     * Sie wird von findewenigeruebleVerbindung() aufgerufen.
     */
    private void findeeroberungsVerbindungan2()
    {
        for (int y=0;y<s;y++)
        {
            for(int x=1;x<w;x=x+2)
            {
                 if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false&
                    Spielfeldan2[x][y]==false)
                    {
                        if(y==0)
                        {
                             if(Spielfeldan2[x-1][y+1]==true&Spielfeldan2[x+1][y+1]==true&
                             Spielfeldan2[x][y+2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                        }
                        else if(y==s-1)
                        {
                             if(Spielfeldan2[x-1][y-1]==true&Spielfeldan2[x+1][y-1]==true&
                             Spielfeldan2[x][y-2]==true){feVxk=x;feVyk=y;x=w;y=s;}

                        }
                        else
                        {
                             if(Spielfeldan2[x-1][y-1]==true&Spielfeldan2[x+1][y-1]==true&
                             Spielfeldan2[x][y-2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                             else if(Spielfeldan2[x-1][y+1]==true&Spielfeldan2[x+1][y+1]==true&
                             Spielfeldan2[x][y+2]==true){feVxk=x;feVyk=y;x=w;y=s;}
                        }
                     }
                 }
            }
            y++;
            if(y<s-1)
            {
                for (int x=0;x<w;x=x+2)
                {
                    if(g.Spielfeld[x][y]!=null)
                    {
                         if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false&
                         Spielfeldan2[x][y]==false)
                         {
                            if(x==0)
                            {
                                 if(Spielfeldan2[x+1][y-1]==true&Spielfeldan2[x+1][y+1]==true&
                                 Spielfeldan2[x+2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                            else if(x==w-1)
                            {
                                 if(Spielfeldan2[x-1][y-1]==true&Spielfeldan2[x-1][y+1]==true&
                                 Spielfeldan2[x-2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                            else
                            {
                                 if(Spielfeldan2[x+1][y-1]==true&Spielfeldan2[x+1][y+1]==true&
                                 Spielfeldan2[x+2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                                 else if(Spielfeldan2[x-1][y-1]==true&Spielfeldan2[x-1][y+1]==true&
                                 Spielfeldan2[x-2][y]==true){feVxk=x;feVyk=y;x=w;y=s;}
                            }
                         }
                    }
                }
            }
        }
    }
    /**
     * Die Eingabewerte sind die Koordinaten einer neuen Verbindung
     * und ein int Wert objektauswahl, mit dessen Hilfe bestimmt wird,
     * ob die Verbindung senkrecht oder wagerecht ist.
     * Die Methode prüft dann, ob durch diese neue Verbindung
     * ein Kästchen geschlossen wird, und
     * gibt einen booleanschen Wert aus.
     */
     private boolean feldwurdeerobert(int i,int j,int objektauswahl)
    {
        if(objektauswahl==1)
        {
            if(j==0)
            {
                 if(Spielfeldan[i-1][j+1]==true&Spielfeldan[i+1][j+1]==true&
                 Spielfeldan[i][j+2]==true)return true;
                 else return false;

            }
            else if(j==s-1)
            {
                 if(Spielfeldan[i-1][j-1]==true&Spielfeldan[i+1][j-1]==true&
                 Spielfeldan[i][j-2]==true)return true;
                 else return false;

            }
            else if(0<i&1<j&j<s-2)
            {
                 if(Spielfeldan[i-1][j-1]==true&Spielfeldan[i+1][j-1]==true&
                 Spielfeldan[i][j-2]==true)return true;
                 else if(Spielfeldan[i-1][j+1]==true&Spielfeldan[i+1][j+1]==true&
                 Spielfeldan[i][j+2]==true)return true;
                 else return false;
            }
            else return false;
        }
        else
        {
            if(i==0&j>0&j<s-1)
            {
                 if(Spielfeldan[i+1][j-1]==true&Spielfeldan[i+1][j+1]==true&
                 Spielfeldan[i+2][j]==true)return true;
                 else return false;
            }
            else if(i==w-1&0<j&j<s-1)
            {
                 if(Spielfeldan[i-1][j-1]==true&Spielfeldan[i-1][j+1]==true&
                 Spielfeldan[i-2][j]==true)return true;
                 else return false;
            }
            else if(0<j&1<i&i<w-2&j<s-1)
            {
                 if(Spielfeldan[i+1][j-1]==true&Spielfeldan[i+1][j+1]==true&
                 Spielfeldan[i+2][j]==true)return true;
                 else if(Spielfeldan[i-1][j-1]==true&Spielfeldan[i-1][j+1]==true&
                 Spielfeldan[i-2][j]==true)return true;
                 else return false;
            }
            else return false;
        }
    }
    /**
     * Diese Methode sucht nach Verbindungen,
     * die es dem Gegner nicht alzu einfach machen Felder hinzu zu gewinnen.
     * Alle möglichen X-Koordinaten speichert es in der fuVxkm Matrix
     * und alle möglichen Y-Koordinaten speichert es in der fuVykm Matrix.
     * Daraufhin sucht sich ein Zufallsgenerator eine X-Koordinate
     * und die dazugehörige Y-Koordinate heraus.
     * Die X-Koordinate wird in fuVxk speichert und die Y-Koordinate wird in fuVyk speichert.
     */
    private void findeungefährlicheVerbindung()
    {
        int i=0;
        int [] fuVxkm= new int[verbindungsanzahl];
        int [] fuVykm= new int[verbindungsanzahl];
        for (int y=0;y<s;y++)
        {
            for(int x=1;x<w;x=x+2)
            {
                if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false)
                    {
                      if(y==0)
                      {
                           if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true);
                           else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x][y+2]==true);
                           else if(Spielfeldan[x+1][y+1]==true&Spielfeldan[x][y+2]==true);
                           else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                      }
                      else if(y==s-1)
                      {
                           if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true);
                           else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x][y-2]==true);
                           else if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x][y-2]==true);
                           else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                      }
                      else if(0<y&1<y&y<s-2)
                      {
                           if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x+1][y-1]==true);
                           else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x][y-2]==true);
                           else if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x][y-2]==true);
                           else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x+1][y+1]==true);
                           else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x][y+2]==true);
                           else if(Spielfeldan[x+1][y+1]==true&Spielfeldan[x][y+2]==true);
                           else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                      }
                   }
                }
            }
            y++;
            if(y<s)
            {
                for (int x=0;x<w;x=x+2)
                {
                    if(g.Spielfeld[x][y]!=null)
                    {

                       if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false)
                       {
                            if(x==0&y>0&y<s-1)
                            {
                                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true);
                                 else if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+2][y]==true);
                                 else if(Spielfeldan[x+1][y+1]==true&Spielfeldan[x+2][y]==true);
                                 else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                            }
                            else if(x==w-1&0<y&y<s-1)
                            {
                                 if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true);
                                 else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-2][y]==true);
                                 else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x-2][y]==true);
                                 else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                            }
                            else if(0<y&1<x&x<w-2&y<s-1)
                            {
                                 if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+1][y+1]==true);
                                 else if(Spielfeldan[x+1][y-1]==true&Spielfeldan[x+2][y]==true);
                                 else if(Spielfeldan[x+1][y+1]==true&Spielfeldan[x+2][y]==true);
                                 else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-1][y+1]==true);
                                 else if(Spielfeldan[x-1][y-1]==true&Spielfeldan[x-2][y]==true);
                                 else if(Spielfeldan[x-1][y+1]==true&Spielfeldan[x-2][y]==true);
                                 else {fuVxkm[i]=x;fuVykm[i]=y;i++;}
                            }
                       }
                    }
                }
            }
        }
        anzahlungefährlicherverbindungen=i-1;
        if(i!=0)
        {
            int j= generator.nextInt(i);
            fuVxk=fuVxkm[j];
            fuVyk=fuVykm[j];
        }

    }
    /**
     * Diese Methode sucht nach Verbindungen,
     * die dem Gegner moglichst wenige Felder hinzugewinnen lassen.
     * Die X-Koordinate speichert es in fwVxk und die Y-Koordinate speichert es in fwVyk.
     */
    private void findewenigeruebleVerbindung()
    {
        int analyse=1000;
        boolean ringgefunden=false;
        boolean ketteunter3gefunden=false;
        for (int y=0;y<s;y++)
        {
            for(int x=0;x<w;x++)
            {
                if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false)
                    {
                        int z=0;
                        Spielfeldan[x][y]=true;
                        for(int i=0;i<1000;i++)
                        {
                            findeeroberungsVerbindung();
                            if(feVxk==0&feVyk==0)
                            {
                                leereSpielfeldan();
                                if(blauanalyse>z&ketteunter3gefunden==false)
                                {
                                    if(ringgefunden==false)
                                    {
                                        ringgefunden=true;
                                        analyse=z;
                                        fwVxk=x;
                                        fwVyk=y;
                                    }
                                    else if(z<analyse)
                                    {
                                        analyse=z;
                                        fwVxk=x;
                                        fwVyk=y;
                                    }
                                }
                                else
                                {
                                   if(z<=3)
                                   {
                                      ketteunter3gefunden=true;
                                      analyse=z;
                                      fwVxk=x;
                                      fwVyk=y;
                                   }
                                   else if(z<analyse&ketteunter3gefunden==false&ringgefunden==false)
                                   {
                                    analyse=z;
                                    fwVxk=x;
                                    fwVyk=y;
                                   }
                                }
                                blauanalyse=0;
                                feVxk=0;
                                feVyk=0;
                                i=1000;
                            }
                            else
                            {
                                Spielfeldan[feVxk][feVyk]=true;
                                setzeeroberung(feVxk,feVyk);
                                z++;
                                feVxk=0;
                                feVyk=0;
                            }
                        }
                    }
                }
            }
        }

    }
    /**
     * Diese Methode prüft, ob durch die eingefügte Verbindung ein Feld geschlossen wird,
     * und fügt das eroberte Feld an der jeweiligen Stelle ein.
     * Sie wird von spielen() und Verbindungeinfuegen() aufgerufen.
     */
    private void Felderoberteinfuegen(int i,int j,int objektauswahl,boolean istblau)
    {
        int objektauswahl2;
        int x1=0;
        int y1=0;
        int x2=0;
        int y2=0;
        if(istblau==true)objektauswahl2=3;
        else objektauswahl2=4;
        if(objektauswahl==1)
        {
            if(j==0)
            {
                 if(g.Spielfeld[i-1][j+1].isVisible()==true&g.Spielfeld[i+1][j+1].isVisible()==true&
                 g.Spielfeld[i][j+2].isVisible()==true){x1=i;y1=j+1;}
            }
            else if(j==s-1)
            {
                 if(g.Spielfeld[i-1][j-1].isVisible()==true&g.Spielfeld[i+1][j-1].isVisible()==true&
                 g.Spielfeld[i][j-2].isVisible()==true){x1=i;y1=j-1;}
            }
            else if(0<i&1<j&j<s-2)
            {
                 if(g.Spielfeld[i-1][j-1].isVisible()==true&g.Spielfeld[i+1][j-1].isVisible()==true&
                 g.Spielfeld[i][j-2].isVisible()==true){x1=i;y1=j-1;}
                 if(g.Spielfeld[i-1][j+1].isVisible()==true&g.Spielfeld[i+1][j+1].isVisible()==true&
                 g.Spielfeld[i][j+2].isVisible()==true){x2=i;y2=j+1;}
            }
        }
        else
        {
            if(i==0&j>0&j<s-1)
            {
                 if(g.Spielfeld[i+1][j-1].isVisible()==true&g.Spielfeld[i+1][j+1].isVisible()==true&
                 g.Spielfeld[i+2][j].isVisible()==true){x1=i+1;y1=j;}
            }
            else if(i==w-1&0<j&j<s-1)
            {
                 if(g.Spielfeld[i-1][j-1].isVisible()==true&g.Spielfeld[i-1][j+1].isVisible()==true&
                 g.Spielfeld[i-2][j].isVisible()==true){x1=i-1;y1=j;}
            }
            else if(0<j&1<i&i<w-2&j<s-1)
            {
                 if(g.Spielfeld[i+1][j-1].isVisible()==true&g.Spielfeld[i+1][j+1].isVisible()==true&
                 g.Spielfeld[i+2][j].isVisible()==true){x1=i+1;y1=j;}
                 if(g.Spielfeld[i-1][j-1].isVisible()==true&g.Spielfeld[i-1][j+1].isVisible()==true&
                 g.Spielfeld[i-2][j].isVisible()==true){x2=i-1;y2=j;}
            }
        }
        //Eroberte Felder werden eingefügt
        if(x1>0&y1>0)
        {
         g.erobertesFeldeinfuegen(x1,y1,objektauswahl2);
        }
        if(x2>0&y2>0)
        {
         g.erobertesFeldeinfuegen(x2,y2,objektauswahl2);
        }
    }
    /**
     * Diese Methode zählt, wie viele Felder jeweils von blau und rot erobert wurden,
     * und speichert diese in den Variablen blau und rot.
     * Sie wird von spielen() aufgerufen.
     */
    private void zähleeroberteFelder()
    {
        blau=0;
        rot=0;
         for (int y=1;y<s;y=y+2)
         {
             for(int x=1;x<w;x=x+2)
             {
                 if(x>0&y>0&x<w-1&y<s-1)
                 {
                     if(g.Spielfeld[x][y]!=null)
                         {
                            if(g.Spielfeld[x][y].objektauswahl==3)blau++;
                            else rot++;
                         }
                 }
             }
         }

    }
    /**
     * Diese Methode gibt aus, ob ein Specialend möglich ist.
     * Darunter vertstehe ich, dass darauf verzichtet wird ein Feld zu schließen,
     * um später mehr Felder zu bekommen.
     */
    private boolean specialendnötig(boolean vonanalyseaufgerufen)
    {
       Spielfeldan2mitSpielfeldansynchronisieren();
       fuVxk=0;;fuVyk=0;
       if(vonanalyseaufgerufen==false)findeungefährlicheVerbindung();
       if(zähleverbundeneEroberungsverbindungen()>1&
       schwierigkeitsgrad>0&fuVxk==0&fuVyk==0)
       {
             feVxk=0;
             feVyk=0;
             findeeroberungsVerbindungan2();
             Spielfeldan2[feVxk][feVyk]=true;
             feVxk=0;
             feVyk=0;
             findeeroberungsVerbindungan2();
             if(feVxk==0&feVyk==0)
             {
                 Spielfeldan2mitSpielfeldansynchronisieren();
                 return false;
             }
             else
             {
                 Spielfeldan2mitSpielfeldansynchronisieren();
                 feVxk=0;
                 feVyk=0;
                 if(specialendmöglich(1)==true)return true;
                 else if(specialendmöglich(2)==true)
                 {
                    feVxk=0;
                    feVyk=0;
                    findeeroberungsVerbindung();
                    Spielfeldan2[feVxk][feVyk]=true;
                    Spielfeldan[feVxk][feVyk]=true;
                    int x=feVxk;
                    int y=feVyk;
                    feVxk=0;
                    feVyk=0;
                    if(specialendmöglich(1)==false)
                    {
                      Spielfeldan[x][y]=false;
                      Spielfeldan2[x][y]=false;
                      return true;
                    }
                    else
                    {
                      Spielfeldan[x][y]=false;
                      Spielfeldan2[x][y]=false;
                      return false;
                    }
                 }
                 else return false;
             }
       }
       else return false;
    }
    /**
     * Diese Metode gibt die Anzahl der zusammenhängenden Verbindungsreihen aus.
     * Diese kommen zustande,
     * wenn man durch das Setzten einer Verbindung mehere Male erneurt ziehen darf.
     */
    private int zähleverbundeneEroberungsverbindungen()
    {
        int a=0;
        feVxk=0;
        feVyk=0;
        for(int i=0;i<1000;i++)
        {
            findeeroberungsVerbindungan2();
            if(feVxk==0&feVyk==0)
            {
                a=a+1;
                i=1000;
            }
            else
            {
                Spielfeldan2[feVxk][feVyk]=true;
            }
            feVxk=0;
            feVyk=0;
        }
        for (int y=0;y<s;y++)
        {
            for(int x=0;x<w;x++)
            {
                if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false&
                    Spielfeldan2[x][y]==false)
                    {
                            int z=0;
                            Spielfeldan2[x][y]=true;
                            for(int i=0;i<1000;i++)
                            {
                                findeeroberungsVerbindungan2();
                                if(feVxk==0&feVyk==0)
                                {
                                    a=a+1;
                                    i=1000;
                                }
                                else
                                {
                                    Spielfeldan2[feVxk][feVyk]=true;
                                }
                                feVxk=0;
                                feVyk=0;
                            }
                     }
                 }
            }
        }
        Spielfeldan2mitSpielfeldansynchronisieren();
        return a;

    }
    /**
     * Diese Methode prüft ob ein Specialend möglich ist.
     * Es kann nämlich unter Umständen nicht möglich sein,
     * auf das Erobern eines Kästchen zu verzichten,
     * ohne dass man dem Gegner zu viele Verbindungen überlässt.
     */
    private boolean specialendmöglich(int a)
    {
        int analyse=1000;
        for (int y=0;y<s;y++)
        {
            for(int x=0;x<w;x++)
            {
                if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false&Spielfeldan[x][y]==false)
                    {
                         if(feldwurdeerobert(x,y,g.Spielfeld[x][y].objektauswahl)==false)
                         {
                              int z=0;
                              Spielfeldan2[x][y]=true;
                              for(int i=0;i<1000;i++)
                              {
                                  findeeroberungsVerbindungan2();
                                  if(feVxk==0&feVyk==0)
                                  {
                                      Spielfeldan2mitSpielfeldansynchronisieren();
                                      if(z<analyse)
                                      {
                                          analyse=z;
                                      }
                                      feVxk=0;
                                      feVyk=0;
                                      i=1000;
                                  }
                                  else
                                  {
                                      Spielfeldan2[feVxk][feVyk]=true;
                                      z++;
                                      feVxk=0;
                                      feVyk=0;
                                  }
                              }
                         }
                     }
                }
            }
        }
        if(analyse==a)return true;
        else return false;
    }
    /**
     * Diese Methode sucht nach Verbindungen,
     * die dem Gegner ein paar Kästchen schenken.
     * Die X-Koordinate speichert es in fwVxk und die Y-Koordinate speichert es in fwVyk.
     */
    private void findespecialendVerbindung()
    {
        int analyse=1000;
        for (int y=0;y<s;y++)
        {
            for(int x=0;x<w;x++)
            {
                if(g.Spielfeld[x][y]!=null)
                {

                    if(g.Spielfeld[x][y].isVisible()==false)
                    {

                       if(feldwurdeerobert(x,y,g.Spielfeld[x][y].objektauswahl)==false)
                       {
                            int z=0;
                            Spielfeldan[x][y]=true;
                            for(int i=0;i<1000;i++)
                            {
                                findeeroberungsVerbindung();
                                if(feVxk==0&feVyk==0)
                                {
                                    leereSpielfeldan();
                                    if(z<analyse)
                                    {
                                        analyse=z;
                                        fwVxk=x;
                                        fwVyk=y;
                                    }
                                    feVxk=0;
                                    feVyk=0;
                                    i=1000;
                                }
                                else
                                {
                                    Spielfeldan[feVxk][feVyk]=true;
                                    z++;
                                    feVxk=0;
                                    feVyk=0;
                                }
                            }
                       }
                    }
                }
            }
        }

    }
    /**
     * Setzt die Spielfeldan Matrix auf den Inhalt vom Spielfeld zurück.
     */
    private void leereSpielfeldan()
    {
        int i;
        for (i=0;i<s;i++)
        {
            for (int j=1;j<w;j=j+2)
            {
                if(g.Spielfeld[j][i].isVisible()==true)Spielfeldan[j][i]=true;
                else Spielfeldan[j][i]=false;
            }
            i++;
            if(i<s)
            {
                   for (int j=0;j<w;j=j+2)
                   {
                      if(g.Spielfeld[j][i].isVisible()==true)Spielfeldan[j][i]=true;
                      else Spielfeldan[j][i]=false;
                   }
            }
        }
    }
    /**
     * Diese Methode setzt den Inhalt der Spielfeldan2-Matrix
     * auf den Inhalt der Spielfeldan-Matrix zurück.
     */
    private void Spielfeldan2mitSpielfeldansynchronisieren()
    {
        for (int x=0;x<w;x++)
        {
            for (int y=0;y<s;y++)
           {
                Spielfeldan2[x][y]=Spielfeldan[x][y];
           }
        }
     }
     /**
     * Gibt die Anzahl der noch freien Verbindungen aus.
     */
    private int gibAnzahlfreieVerbindungen()
    {
        int a=0;
        int i;
        for (i=0;i<s;i++)
        {
            for (int j=1;j<w;j=j+2)
            {
                if(g.Spielfeld[j][i]!=null){if (g.Spielfeld[j][i].isVisible()==false)a=a+1;}
            }
            i++;
            if(i<s)
            {
                for (int j=0;j<w;j=j+2)
                {
                    if(g.Spielfeld[j][i]!=null){if (g.Spielfeld[j][i].isVisible()==false)a=a+1;}
                }
            }
        }
        return a;
    }
     /**
     * Diese Methode prüft ob die Spielfeldan-Matrix voll ist
     * und gibt dies mit einem boolischen Wert aus.
     */
    private boolean Spielfeldistvoll()
    {
        int i;
        for (i=0;i<s;i++)
        {
            for (int j=1;j<w;j=j+2)
            {
                if(Spielfeldan[j][i]==false)return false;
            }
            i++;
            if(i<s)
            {
                for (int j=0;j<w;j=j+2)
                {
                    if(Spielfeldan[j][i]==false)return false;
                }
            }
        }
        return true;
    }
    /**
     * Diese Methode prüft welcher Spieler mehr Felder erobert hat und gibt den Gewinner aus.
     * Wird nur ausgeführt wenn das  Spielfeld voll ist.
     * Sie wird von spielen() aufgerufen.
     */
    private void Gewinnerausgeben()
    {
        if(rot<blau)g.setzeAnmerkung("Spieler Blau hat gewonnen");
        else if(blau<rot)g.setzeAnmerkung("Spieler Rot hat gewonnen");
        else g.setzeAnmerkung("Unentschieden");
    }
}