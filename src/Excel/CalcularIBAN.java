/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author xiann
 */
public class CalcularIBAN {

    public ArrayList<ArrayList<String>> aList;
    public int numFilas;

    public CalcularIBAN() {

    }

    public ArrayList<ArrayList<String>> editarIBAN(int numFilas, ArrayList<ArrayList<String>> aList) {
        this.aList = aList;
        this.numFilas = numFilas;
        Element root = new Element("cuentas");
        Document doc = new Document();

        for (int i = 1; i < numFilas; i++) {
            if (!aList.get(i).get(9).equals("%")) {
                String nuevoCCC = ComprobarCCC(aList.get(i).get(9));
                if (!nuevoCCC.equals("true")) {
                 //   System.out.println("I= " + i);
                    ArrayList<String> aux2 = aList.get(i);
                    String oldCCC = aList.get(i).get(9);
                    String newIBAN = IBANaPartirDeCCC(nuevoCCC, aList.get(i).get(10));
                    aux2.set(11, newIBAN);
                    aux2.set(9, nuevoCCC);
                    aList.set(i, aux2);
                    xmlBuilder(aList.get(i), i, root, doc, oldCCC, newIBAN);

                } else {
                    ArrayList<String> aux2 = aList.get(i);

                    String newIBAN = IBANaPartirDeCCC(aList.get(i).get(9), aList.get(i).get(10));
                    aux2.set(11, newIBAN);
                    aList.set(i, aux2);
                }
            }

        }

        doc.setRootElement(root);
        XMLOutputter outter = new XMLOutputter();
        outter.setFormat(Format.getPrettyFormat());
        try {
            outter.output(doc, new FileWriter(new File("src/resources/erroresCCC.xml")));
        } catch (IOException ex) {
            Logger.getLogger(ExcelFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aList;

    }

    private void xmlBuilder(ArrayList<String> array, int i, Element root, Document doc, String CCCError, String newIBAN) {

        Element elemTrabajador = new Element("cuenta");
        elemTrabajador.setAttribute("id", "" + i);
        //TODO:ARREGLAR ESTO
        Element hijoNombre = new Element("Nombre");
        hijoNombre.addContent((!array.get(4).equals("%") ? array.get(4) : "\u200e"));
        Element hijoApellido1 = new Element("PrimerApellido");

        hijoApellido1.addContent((!array.get(5).equals("%") ? array.get(5) : "\u200e"));
        Element hijoApellido2 = new Element("SegundoApellido");

        hijoApellido2.addContent((!array.get(6).equals("%") ? array.get(6) : "\u200e"));

        Element hijoEmpresa = new Element("Empresa");
        hijoEmpresa.addContent((!array.get(1).equals("%") ? array.get(1) : "\u200e"));

        Element hijoCodigoCuentaErroneo = new Element("CodigoDeCuentaErroneo");
        hijoCodigoCuentaErroneo.addContent(CCCError);

        Element hijoNuevoIBAN = new Element("NuevoIBAN");
        hijoNuevoIBAN.addContent(newIBAN);

        elemTrabajador.addContent(hijoNombre);
        elemTrabajador.addContent(hijoApellido1);
        elemTrabajador.addContent(hijoApellido2);
        elemTrabajador.addContent(hijoEmpresa);
        elemTrabajador.addContent(hijoCodigoCuentaErroneo);
        elemTrabajador.addContent(hijoNuevoIBAN);

        root.addContent(elemTrabajador);

    }

    public String ComprobarCCC(String CCC) {
        if (CCC.length() == 20) {

            int primeraParte[] = new int[10];
            int segundaParte[] = new int[10];
            int codigoControl1 = 0;
            int codigoControl2 = 0;

            primeraParte[0] = 0;
            primeraParte[1] = 0;
            for (int i = 0; i < 20; i++) {
                if (i < 8) {
                    primeraParte[i + 2] = Character.getNumericValue(CCC.charAt(i));

                }
                if (i == 8) {
                    codigoControl1 = Character.getNumericValue(CCC.charAt(i));
                }
                if (i == 9) {
                    codigoControl2 = Character.getNumericValue(CCC.charAt(i));
                }
                if (i > 9) {
                    segundaParte[i - 10] = Character.getNumericValue(CCC.charAt(i));
                }
            }
//            System.out.print("PRIMEROS 10:  ");
//            for (int i = 0; i < 10; i++) {
//                System.out.print(primeraParte[i] + " ");
//            }
//            System.out.print("  ");
//            System.out.print(" Los Dígitos de control: " + codigoControl1 + codigoControl2);
//            System.out.print("  LOS ULTIMOS 10: ");
//            for (int i = 0; i < 10; i++) {
//                System.out.print(segundaParte[i] + " ");
//            }
//            System.out.println("");

            int sumatorio = (primeraParte[0] * 1) + (primeraParte[1] * 2) + (primeraParte[2] * 4) + (primeraParte[3] * 8) + (primeraParte[4] * 5)
                    + (primeraParte[5] * 10) + (primeraParte[6] * 9) + (primeraParte[7] * 7) + (primeraParte[8] * 3) + (primeraParte[9] * 6);

            sumatorio = sumatorio % 11;
            sumatorio = 11 - sumatorio;
            sumatorio = sumatorio == 11 ? 0 : sumatorio;
            sumatorio = sumatorio == 10 ? 1 : sumatorio;

            int sumatorio2 = (segundaParte[0] * 1) + (segundaParte[1] * 2) + (segundaParte[2] * 4) + (segundaParte[3] * 8) + (segundaParte[4] * 5)
                    + (segundaParte[5] * 10) + (segundaParte[6] * 9) + (segundaParte[7] * 7) + (segundaParte[8] * 3) + (segundaParte[9] * 6);

            sumatorio2 = sumatorio2 % 11;
            sumatorio2 = 11 - sumatorio2;
            sumatorio2 = sumatorio2 == 11 ? 0 : sumatorio2;
            sumatorio2 = sumatorio2 == 10 ? 1 : sumatorio2;

            if (sumatorio == codigoControl1 && sumatorio2 == codigoControl2) {
                return "true";
            } else {
                return CCC.substring(0, 8) + sumatorio + sumatorio2 + CCC.substring(10, 20);
            }

        }

        return "%";

    }

    public static boolean verificaIBAN(String IBAN, String CCC, String country) {
        //completamente inutil
        int resto;
        int CCCnum;//CCC en formato numerico
        int numberCountry[] = returnNumberOfLetter(country);
        CCC = CCC + numberCountry[0] + "" + numberCountry[1];//Anadimos ES
        String digControl = IBAN.substring(2, 4);
        CCC = CCC + digControl;//Anadimos dig cont

        CCCnum = Integer.parseInt(CCC);
        resto = CCCnum % 97;

        //Si el resto es 1->true, sino->false
        return resto == 1;

    }

    public static String IBANaPartirDeCCC(String CCC, String country) {
        //Numero con el que calcularemos la regla 97-10
        StringBuilder calculo = new StringBuilder("");
        StringBuilder IBAN = new StringBuilder("");
        BigInteger restobig;
        int resto, control;
        int numberCountry[] = returnNumberOfLetter(country);
        calculo.append(CCC);
        calculo.append(numberCountry[0]);
        calculo.append(numberCountry[1]);
        calculo.append("00");
        //Lo pasamos a Integer, Big porque no se podia otra cosa
        BigInteger calculonum = new BigInteger(calculo.toString());
        //Modelo 97-10
        restobig = calculonum.mod(new BigInteger("97"));
        //Ya podemos asar el resto a int porue es mas pequeno
        resto = restobig.intValue();
        control = 98 - resto;
        String controlEnLetra;
        controlEnLetra = control < 10 ? "0" + control : "" + control;

        //Unimos los campos en el iban
        IBAN.append(country);
        IBAN.append(controlEnLetra);
        IBAN.append(CCC);

        return IBAN.toString();
    }

    public static int[] returnNumberOfLetter(String country) {
        int ret[] = new int[2];

        for (int i = 0; i < 2; i++) {
            switch (country.charAt(i)) {
                case 'A':
                    ret[i] = 10;
                    break;
                case 'B':
                    ret[i] = 11;
                    break;
                case 'C':
                    ret[i] = 12;
                    break;
                case 'D':
                    ret[i] = 13;
                    break;
                case 'E':
                    ret[i] = 14;
                    break;
                case 'F':
                    ret[i] = 15;
                    break;
                case 'G':
                    ret[i] = 16;
                    break;
                case 'H':
                    ret[i] = 17;
                    break;
                case 'I':
                    ret[i] = 18;
                    break;
                case 'J':
                    ret[i] = 19;
                    break;
                case 'K':
                    ret[i] = 20;
                    break;
                case 'L':
                    ret[i] = 21;
                    break;
                case 'M':
                    ret[i] = 22;
                    break;
                case 'N':
                    ret[i] = 23;
                    break;
                case 'O':
                    ret[i] = 24;
                    break;
                case 'P':
                    ret[i] = 25;
                    break;
                case 'Q':
                    ret[i] = 26;
                    break;
                case 'R':
                    ret[i] = 27;
                    break;
                case 'S':
                    ret[i] = 28;
                    break;
                case 'T':
                    ret[i] = 29;
                    break;
                case 'U':
                    ret[i] = 30;
                    break;
                case 'V':
                    ret[i] = 31;
                    break;
                case 'W':
                    ret[i] = 32;
                    break;
                case 'X':
                    ret[i] = 33;
                    break;
                case 'Y':
                    ret[i] = 34;
                    break;
                case 'Z':
                    ret[i] = 35;
                    break;

            }
        }
        return ret;
    }
}
