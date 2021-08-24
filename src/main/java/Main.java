/**
 * @Author Sebastián Molano
 *
 * Tester para el programa
 */
public class Main {

    public static void main(String[] args) {
        MinMax mm = new MinMax();
        mm.pintarTriqui(mm.tablero);
        mm.generarArbol(mm.tablero);
        System.out.println("Arbol:");
        System.out.println(mm.arbol.toString());
        mm.hacerJugadaX();
        mm.pintarTriqui(mm.tablero);
        mm.hacerJugadaY();
        mm.pintarTriqui(mm.tablero);
        System.out.println("No se pueden hacer más jugadas ya que el árbol ");
    }

}
