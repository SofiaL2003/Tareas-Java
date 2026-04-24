public class ManipuladorStrings {

    public static String invertir(String s) {
        StringBuilder sb = new StringBuilder(s);
        sb.reverse();
        return sb.toString();
    }

    public static boolean esPalindromo(String s) {
        String sLC = s.toLowerCase().replaceAll(" ", "");
        boolean esPalindromo = sLC.equals(invertir(sLC));
        return esPalindromo;
    }

    public static int contarVocales(String s) {
        int count = 0;
        String vocales = "aeiouAEIOU";
        char[] chars = s.toCharArray();
        for (char c : chars){
            if (vocales.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }

    public static String construirPiramide(int niveles) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= niveles; i++) {
            sb.append(" ".repeat(niveles - i));
            sb.append("*".repeat(2*i - 1));
            sb.append("\n");
        


            // TODO: agregar espacios (niveles - i)
            // TODO: agregar asteriscos (2*i - 1)
            // TODO: agregar salto de linea
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Invertir 'Hola Mundo': "
                         + invertir("Hola Mundo"));
        System.out.println("'Anita lava la tina' es palindromo: "
                         + esPalindromo("Anita lava la tina"));
        System.out.println("Vocales en 'Murcielago': "
                         + contarVocales("Murcielago"));
        System.out.println("Piramide de 5 niveles:");
        System.out.println(construirPiramide(5));
    }
}