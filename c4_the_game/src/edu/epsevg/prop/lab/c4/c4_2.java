/*
    La clase c4_2.java implementa un nou jugador autonom per al joc conecta 4.
    L'estrategia del jugador es basa en l'avaluació de l'heuristica de les posicions del tauler utilitzant un algorisme minimax.
    Tambè compta amb la possibilitat d'aplicar la poda alpha-beta per millorar la seva eficiencia.
 */
package edu.epsevg.prop.lab.c4;

/*
    @author marc i oriol
*/


public class c4_2 implements Jugador, IAuto {
    private  String nom;           // Nom del jugador.
    private  int profunditat;      // Profunditat màxima a la que arribara la cerca.   
    boolean ab;                              // Indica si utilitzem poda alpha-beta o no.
    
    /*
        Constructor de la clase c4_2
        El parametre p és la profunditat a la que arribarà la cerca.
        El parametre ab és si utilitzem alpha-beta o no.
    */
    public c4_2( int p,  boolean ab) {
        this.nom = "c4_2";
        this.profunditat= p;
        this.ab = ab;
    }
    
    // Metodes privats per evaluar l'heuristica del tauler
       /*
        Calcula la puntuació heuristica total del tauler per al color donat.
        El parametre tauler és el tauler en joc.
        El parametre color és el color del jugador  per al qual calculem l'heurística.
        El valor de retorn és la puntuació del tauler per al color donat. 
    */
    private float calculaHeu( Tauler tauler,  int color) {
        float puntuacio = 0.0f;

        for (int i = tauler.getMida() - 1; i >= 0; --i) {
            for (int j = 0; j < tauler.getMida(); ++j) {
                puntuacio += puntuarPosicio(tauler, i, j, color);

                // Aumentar la prioridad para las piezas en las 3 casillas centrales
                if (j >= tauler.getMida() / 2 - 1 && j <= tauler.getMida() / 2 + 1) {
                    puntuacio += 10 * tauler.getColor(i, j) * color;  
                }
            }
        }

        return puntuacio;
    }
    /*
        Conta les fiches consecutives en una direcció específica des de la posició donada.
        El parametre tauler és el tauler en joc.
        El parametre fila és la fila on comencem.
        El parametre columna és la columna de inici.
        El parametre filaDir és la direcció de la fila.
        El parametre columnaDir és la dirreció de la columna.
        El parametre color és el color de les fitxes a contar.
        El valor retornat és el nombre de fitxes consecutives en la direcció donada.
    */
    private int contarConsecutives( Tauler tauler,  int fila,  int columna,  int filaDir,  int columnaDir,  int color) {
        int consecutives = 0;
        int total = 0;

        for (int i = fila, j = columna; i >= 0 && i < tauler.getMida() && j >= 0 && j < tauler.getMida() && total < 4; i += filaDir, j += columnaDir) {
            if (tauler.getColor(i, j) == this.inverteix(color)) {
                break;
            }
            if (tauler.getColor(i, j) == color) {
                consecutives++;
            }
            total++;
        }



        return consecutives;
    }
    /*
        Retorna la heurista de una posició fila, columna mirant totes les direcciones quantes fitxes consecutives té
        El parametre tauler és el tauler en joc.
        El parametre fila és la fila de la posició que evaluem.
        El parametre columna és la columna de la posició que evaluem.
        El parametre color és el color de la fitxa que evaluem.
        El valor de retorn és la puntuació heuristica de la posició que evaluem.
    */
    private float puntuarPosicio( Tauler tauler,  int fila,  int columna,  int color) {
        float puntuacio = 0.0f;

        for (int filaDir = -1; filaDir <= 1; ++filaDir) {
            for (int columnaDir = -1; columnaDir <= 1; ++columnaDir) {
                if (filaDir != 0 || columnaDir != 0) {
                     int contarConsecutives = this.contarConsecutives(tauler, fila, columna, filaDir, columnaDir, tauler.getColor(fila, columna));
                    if (contarConsecutives == 2) {
                        puntuacio += tauler.getColor(fila, columna) * color;
                    } else if (contarConsecutives == 3) {
                        puntuacio += 10 * tauler.getColor(fila, columna) * color;
                    } else if (contarConsecutives >= 4) {
                        puntuacio += 100 * tauler.getColor(fila, columna) * color;
                    }
                }
            }
        }

        return puntuacio;
    }
    
    /*
        Inverteix el color donat
        El parametre color és el color a invertir.
        El color retornat és el color ja invertit.
    */
    private int inverteix( int color) {
        return -color;
    }

    

    
    
 
    /*
        Realitza la evaluació recursiva de les possibles tirades utilitzan l'algorisme minimax.
        Aplica  la poda alpha-beta si esta habilitada.
        El parametre tauler és el tauler en joc.
        El parametre color és el color del jugador actual.
        El parametre columna és la columna de la ultima jugada.
        El parametre prof és la profunditat actual a l'arbre de tirades.
        El parametre alpha és el valor alpha per la poda alpha-beta.
        El parametre beta és el valor beta per la poda alpha-beta.
        El valor retornat és la millor puntuació heurística per la jugada actual.     
    */
    private float CalculaRes( Tauler tauler,  int color,  int columna,  int prof, float alpha,  float beta) {
        if (prof == 0 || !tauler.espotmoure() || tauler.solucio(columna, this.inverteix(color))) {

            return this.calculaHeu(tauler, color);

        } else {
            for (int i = 0; i < tauler.getMida(); ++i) {
                 Tauler tauler2 = new Tauler(tauler);
                if (tauler2.movpossible(i)) {
                    tauler2.afegeix(i, color);

                    if (ab) {  // Realizar la poda alfa-beta solo si ab es true
                        alpha = Math.max(alpha, -this.CalculaRes(tauler2, this.inverteix(color), i, prof - 1, -beta, -alpha));
                        if (beta <= alpha) {
                            break;
                        }
                    } else {
                        // Sin poda alfa-beta
                        alpha= Math.max(alpha, -this.CalculaRes(tauler2, this.inverteix(color), i, prof - 1, -beta, -alpha));

                    }
                }
            }
        }
        return alpha;
    }
      
    //Metodes publics per jugar.
      
    /*
        Realitza el moviment del jugador al tauler. Crida al metodes privats per tal de fer la millor tirada possible.
        El parametre tauler és el tauler en joc.
        El parametre color és el color del jugador.
        El valor retornat és la columna on es vol realitzar el moviment.
    */
    @Override
    public int moviment( Tauler tauler,  int color) {
        float max = -10000.0f;
        int columna = 0;
        for (int i = 0; i < 8; ++i) {
            
            
            
            Tauler tauler2 = new Tauler(tauler);
            if (tauler2.movpossible(i)) {
                tauler2.afegeix(i, color);
                 float res = -this.CalculaRes(tauler2, this.inverteix(color), i, this.profunditat - 1, -10000.0f, 10000.0f);
                if (res > max) {
                    columna = i;
                    max = res;
                }
            }
        }
        return columna;
    }
       
    /*
        Retorna el nom 
    */
    @Override
    public String nom() {
        return nom;
    }
}