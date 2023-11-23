/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.epsevg.prop.lab.c4;

/**
 *
 * @author bernat
 */
public class Prova {
    public static void main(String[] args) {
        Tauler t = new Tauler(8);
        t.afegeix(0, 1);
      t.afegeix(0, 1);
      t.afegeix(0, 1);
      t.afegeix(0, 1);
        
        
        t.pintaTaulerALaConsola();
        int color = 1; // Color del jugador actual
        float heuristica = calculaHeu(t, color);
        System.out.println("Puntuación de la heurística: " + heuristica);
        
    }
    // Metodes privats per evaluar l'heuristica del tauler
       /*
        Calcula la puntuació heuristica total del tauler per al color donat.
        El parametre tauler és el tauler en joc.
        El parametre color és el color del jugador  per al qual calculem l'heurística.
        El valor de retorn és la puntuació del tauler per al color donat. 
    */
    private static float calculaHeu( Tauler tauler,  int color) {
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
    private static int contarConsecutives( Tauler tauler,  int fila,  int columna,  int filaDir,  int columnaDir,  int color) {
        int consecutives = 0;
        int total = 0;

        for (int i = fila, j = columna; i >= 0 && i < tauler.getMida() && j >= 0 && j < tauler.getMida() && total < 4; i += filaDir, j += columnaDir) {
            if (tauler.getColor(i, j) == inverteix(color)) {
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
    private static float puntuarPosicio( Tauler tauler,  int fila,  int columna,  int color) {
        float puntuacio = 0.0f;

        for (int filaDir = -1; filaDir <= 1; ++filaDir) {
            for (int columnaDir = -1; columnaDir <= 1; ++columnaDir) {
                if (filaDir != 0 || columnaDir != 0) {
                    int contarConsecutives = contarConsecutives(tauler, fila, columna, filaDir, columnaDir, tauler.getColor(fila, columna));
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
    private static int inverteix( int color) {
        return -color;
    }
}
/*
Fila
7  ->  0 0 0 0 0 0 0 0
6  ->  0 0 0 0 0 0 0 0
5  ->  0 0 0 0 0 0 0 0
4  ->  0 0 0 0 0 0 0 0
3  ->  0 0 0 0 0 0 0 0
2  ->  0 0 0 0 0 0 0 0
1  ->  0-1 0 0 0 0 0 0
0  ->  0-1 0 0 0 0 0 1

       0 1 2 3 4 5 6 7   Columna

*/
