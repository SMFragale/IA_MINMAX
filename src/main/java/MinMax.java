import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Author Sebastian Molano, representa el juego de triqui utilizando el algoritmo de triqui utilizando una IA MINMAX
 */
public class MinMax {

    /*Representa un estado asociado a su respectiva heurística*/
    private class EstadoE {
        int heurística;
        int[][] estado;

        public EstadoE(int heurística, int[][] estado) {
            this.heurística = heurística;
            this.estado = estado;
        }
        public int getHeurística() {
            return heurística;
        }
        public int[][] getEstado() {
            return estado;
        }

        @Override
        public String toString() {
            StringBuilder ret  = new StringBuilder("EstadoE{" +
                    "heurística=" + heurística);
            ret.append(", estado= ");
            for(int i = 0; i < estado.length; i++) {
                ret.append("[");
                for(int j = 0; j < estado[i].length; j++) {
                    ret.append(" ").append(estado[i][j]).append(" ");
                }
            ret.append("] ");
            }
            ret.append('}');
            return ret.toString();
        }
    }

    TreeNode<EstadoE> arbol;

    /*
    El tablero representa el estado por medio de una matriz. El número 0 en la matriz representa un espacio vacío, el
    número -1 representa el jugador "O" y el número 1 representa el jugador "X"
    */
    int[][] tablero = { //Coordenadas x,y  primero 0,0
            {0,0,0},
            {0,0,0},
            {0,0,0}
    };

    public MinMax() {
        generarArbol(tablero);
    }

    /*
    Devuelve un arreglo con los posibles sucesores (Estados posibles sucesores) del estado pasado como parámetro
    */
    public static ArrayList<int[][]> sucesores(int[][] tablero, int player) {
        ArrayList<int[][]> sucesores = new ArrayList<>();
        for(int i = 0; i < tablero.length; i++) {
            for(int j = 0; j < tablero[i].length; j++) {
                if(tablero[i][j] == 0) {
                    int[][] suc = copia(tablero);
                    suc[i][j] = player;
                    sucesores.add(suc);
                }
            }
        }
        return sucesores;
    }


    /*Utiliza el algoritmo MinMax para hacer la jugada de la IA (jugador X) Itera sobre el arbol de estados posibles y
    elige en este caso el estado que tenga la mayor heurística MAX*/
    public void hacerJugadaX() {
        if(arbol == null) {
            System.out.println("Se acabó");
            return;
        }
        ArrayMultiTreeNode<EstadoE> nodo = (ArrayMultiTreeNode<EstadoE>) arbol.root();
        Collection<? extends TreeNode<EstadoE>> subs = nodo.subtrees();
        EstadoE estado = new EstadoE(-100, emptyMatrix());
        TreeNode<EstadoE> nuevoArbol = null;
        for (Iterator<? extends TreeNode<EstadoE>> iterator = subs.iterator(); iterator.hasNext();) {
            TreeNode<EstadoE> es = iterator.next();
            //X es máximo
            if(es.data().heurística > estado.heurística) {
                estado = es.data();
                nuevoArbol = es;
            }
        }
        arbol = nuevoArbol;
        this.tablero = estado.estado;
    }

    public void hacerJugadaY() {
        if(arbol == null) {
            System.out.println("Se acabó");
            return;
        }
        ArrayMultiTreeNode<EstadoE> nodo = (ArrayMultiTreeNode<EstadoE>) arbol;
        Collection<? extends TreeNode<EstadoE>> subs = nodo.subtrees();
        EstadoE estado = new EstadoE(100, emptyMatrix());
        for (Iterator<? extends TreeNode<EstadoE>> iterator = subs.iterator(); iterator.hasNext();) {
            TreeNode<EstadoE> es = iterator.next();
            //Y es mínimo
            if(es.data().heurística < estado.heurística) {
                estado = es.data();
            }
        }
        this.tablero = estado.estado;
    }

    /*Devuelve un estado copia del estado pasado como parámetro*/
    private static int[][] copia(int[][] tablero) {
        int[][] copia = new int[3][3];
        for(int i = 0; i < tablero.length; i++) {
            System.arraycopy(tablero[i], 0, copia[i], 0, tablero[i].length);
        }
        return copia;
    }

    /*
    Genera el árbol para el jugador de la IA (X) (solo los primeros 2 niveles)
    */
    public void generarArbol(int[][] tablero) {
        arbol = new ArrayMultiTreeNode<>(new EstadoE(0, tablero));
        //Genera dos ramas del árbol
        ArrayList<int[][]> estadosPosibles = sucesores(tablero, 1);
        for (int[][] estadosPosible : estadosPosibles) {
            //Agrega la heurística estado al arbol (primero del jugador X)
            ArrayMultiTreeNode<EstadoE> nodo = new ArrayMultiTreeNode<>(new EstadoE(heuristica(estadosPosible), estadosPosible));
            arbol.add(nodo);
            for(int[][] estadosPosible2 : sucesores(estadosPosible, -1)) {
                //Agrega la heurística estado al arbol (ahora del jugador O) (Segundo nivel del árbol)
                nodo.add(new ArrayMultiTreeNode<>(new EstadoE(heuristica(estadosPosible2), estadosPosible2)));
            }
        }
    }

    /* Possible wins. Devuelve la cantidad de posibles caminos de victoria para el jugador @player (O -> -1, X -> 1) en
    * el estado actual*/
    private static int heuristicaP(int[][] tablero, int player) {
        int caminos = 0;
        //Fila 1
        if((tablero[0][0] == player || tablero[0][0] == 0) && (tablero[0][1] == player || tablero[0][1] == 0) && (tablero[0][2] == player || tablero[0][2] == 0) ) {
            caminos++;
        }
        //Fila 2
        if((tablero[1][0] == player || tablero[1][0] == 0) && (tablero[1][1] == player || tablero[1][1] == 0) && (tablero[1][2] == player || tablero[1][2] == 0) ) {
            caminos++;
        }
        //Fila 3
        if((tablero[2][0] == player || tablero[2][0] == 0) && (tablero[2][1] == player || tablero[2][1] == 0) && (tablero[2][2] == player || tablero[2][2] == 0) ) {
            caminos++;
        }
        //Columna 1
        if((tablero[0][0] == player || tablero[0][0] == 0) && (tablero[1][0] == player || tablero[1][0] == 0) && (tablero[2][0] == player || tablero[2][0] == 0) ) {
            caminos++;
        }
        //Columna 2
        if((tablero[0][1] == player || tablero[0][1] == 0) && (tablero[1][1] == player || tablero[1][1] == 0) && (tablero[2][1] == player || tablero[2][1] == 0) ) {
            caminos++;
        }
        //Columna 3
        if((tablero[0][2] == player || tablero[0][2] == 0) && (tablero[1][2] == player || tablero[1][2] == 0) && (tablero[2][2] == player || tablero[2][2] == 0) ) {
            caminos++;
        }
        //Diagonal 1
        if((tablero[0][0] == player || tablero[0][0] == 0) && (tablero[1][1] == player || tablero[1][1] == 0) && (tablero[2][2] == player || tablero[2][2] == 0) ) {
            caminos++;
        }
        //Diagonal 2
        if((tablero[0][2] == player || tablero[0][2] == 0) && (tablero[1][1] == player || tablero[1][1] == 0) && (tablero[2][0] == player || tablero[2][0] == 0) ) {
            caminos++;
        }
        return caminos;
    }

    /*Heurística del estado actual*/
    public int heuristica(int[][] tablero) {
        return heuristicaP(tablero, 1) - heuristicaP(tablero, -1);
    }

    private int[][] emptyMatrix() {
        return new int[][]{
                {0,0,0},
                {0,0,0},
                {0,0,0}
        };
    }

    /*
              c1   c2  c3
    diagonal1\ |   |   |  /diagonal2
    fila 1 ->|   |   |   |
             |---|---|---|
    fila 2 ->|   |   |   |
             |---|---|---|
    fila 3 ->|   |   |   |
    */
    public void pintarTriqui(int[][] tablero) {
        System.out.println("           c1   c2  c3");
        System.out.println("diagonal1\\ |   |   |  /diagonal2");
        for(int i = 0; i < tablero.length; i++) {
            System.out.print("         ");
            for(int j = 0; j < tablero[i].length; j++) {
                System.out.print("| ");
                if(tablero[i][j] == 0) System.out.print("  ");
                else if(tablero[i][j] == -1) System.out.print("O ");
                else System.out.print("X ");
                if(j == tablero[i].length-1)
                    System.out.print("|");
            }
            System.out.println();
            if(i != tablero.length-1)
                System.out.println("         |---|---|---|");
        }
    }
}
