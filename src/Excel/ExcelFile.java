/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
//set global max_connections = 900;
 */
package Excel;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import es.unileon.xijaja.nominas.dao.CategoryDAO;
import es.unileon.xijaja.nominas.dao.CompanyDAO;
import es.unileon.xijaja.nominas.dao.PayrollDAO;
import es.unileon.xijaja.nominas.dao.WorkerDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.jdom2.Document;
import org.apache.poi.ss.usermodel.*;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ExcelFile {

    private int numFilas;
    private Workbook workbook;
    private Sheet sheet;
    private Sheet sheet2;
    private Sheet sheet3;
    private Sheet sheet4;
    private Sheet sheet5;
    private ArrayList<ArrayList<String>> aList;
    private ArrayList<Integer> retencion2;
    private HashMap<String, ArrayList<Integer>> categories;
    private HashMap<String, Double> retencion;
    private HashMap<String, Double> porcentajes;
    private HashMap<String, Double> trienios;
    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    private static DecimalFormat df2;
    private double extraDeJunio;
    private double extraDeDiciembre;

    public ExcelFile() {
        this.getExcelData();

    }

    /**
     * Guardamos en las estructuras de datos los datos de las diferentas hojas
     * del excel
     */
    private void getExcelData() {

        aList = new ArrayList<>();
        retencion2 = new ArrayList<>();
        categories = new HashMap<>();
        retencion = new HashMap<>();
        porcentajes = new HashMap<>();
        trienios = new HashMap<>();
        numFilas = 0;
        FileInputStream file = null;
        simbolos.setDecimalSeparator('.');
        df2 = new DecimalFormat("#####00.00", simbolos);
        try {

            file = new FileInputStream(new File(getClass().getResource("/resources/SistemasInformacionII.xlsx").toURI()));
            workbook = WorkbookFactory.create(file);

        } catch (IOException | URISyntaxException | EncryptedDocumentException e) {
            System.err.println("Error al leer el archivo excel");
        }

        DataFormatter dataFormatter = new DataFormatter();
        sheet = workbook.getSheetAt(0);

        //REllenamos el arraylsit con los datos de la pagina 1 del excel
        for (Row r : sheet) {
            numFilas++;
            ArrayList<String> aux = new ArrayList<String>();
            for (int i = 0; i < r.getLastCellNum(); i++) {
                if (r.getCell(i) == null) {
                    aux.add("%");
                } else {
                    Cell cell = r.getCell(i);
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (cellValue.equals("")) {
                        aux.add("%");
                    } else {
                        aux.add(cellValue);
                    }
                }

            }

            aList.add(aux);
        }

        //Guardamos los datos de la hoja 2
        sheet2 = workbook.getSheetAt(1);
        for (Row r : sheet2) {
            ArrayList<Integer> aux = new ArrayList<>();

            String key = null;

            for (int i = 0; i < r.getLastCellNum(); i++) {

                if (r.getRowNum() != 0) {
                    Cell cell = r.getCell(i);
                    if (i == 0) {
                        key = dataFormatter.formatCellValue(cell);
                    } else {

                        aux.add(Integer.parseInt(dataFormatter.formatCellValue(cell)));

                    }
                }

            }
            if (r.getRowNum() != 0) {

                categories.put(key, aux);
            }
        }

        //Guardamos los datos de la hoja 3
        sheet3 = workbook.getSheetAt(2);
        for (Row r : sheet3) {
            double aux = 0.0;

            String key = null;
            for (int i = 0; i < r.getLastCellNum(); i++) {

                if (r.getRowNum() != 0) {
                    Cell cell = r.getCell(i);
                    if (i == 0) {
                        key = dataFormatter.formatCellValue(cell);
                    } else {

                        String numToChange = dataFormatter.formatCellValue(cell);

                        aux = Double.parseDouble(numToChange.replace(',', '.'));
                    }
                }
            }
            if (r.getRowNum() != 0) {
                retencion2.add(Integer.parseInt(key));
                retencion.put(key, aux);
            }
        }

        //Guardamos los datos de la hoja 4
        sheet4 = workbook.getSheetAt(3);
        for (Row r : sheet4) {
            double aux = 0.0;

            String key = null;
            for (int i = 0; i < r.getLastCellNum(); i++) {

                Cell cell = r.getCell(i);
                if (i == 0) {
                    key = dataFormatter.formatCellValue(cell);

                } else {

                    String numToChange = dataFormatter.formatCellValue(cell);

                    aux = Double.parseDouble(numToChange.replace(',', '.'));
                }
            }
            porcentajes.put(key, aux);

        }

        //Guardamos los datos de la hoja 5
        sheet5 = workbook.getSheetAt(4);
        for (Row r : sheet5) {
            double aux = 0.0;
            String key = null;
            for (int i = 0; i < r.getLastCellNum(); i++) {

                if (r.getRowNum() != 0) {
                    Cell cell = r.getCell(i);
                    if (i == 0) {
                        key = dataFormatter.formatCellValue(cell);
                    } else {

                        String numToChange = dataFormatter.formatCellValue(cell);

                        aux = Double.parseDouble(numToChange.replace(',', '.'));
                    }
                }

            }
            if (r.getRowNum() != 0) {
                trienios.put(key, aux);
            }
        }
        for (int i = 1; i < aList.size(); i++) {
            ArrayList<String> aux = aList.get(i);
            if (!aList.get(i).get(1).equals("%")) {
                if (aux.get(3).length() != 10) {
                    aux.set(3, arreglarFecha(aux.get(3)));
                } else {
                    aux.set(3, aux.get(3));
                }

            }
            aList.set(i, aux);
        }
    }

    /**
     *
     * Editamos el excel
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void editExcel(String month, String year) throws FileNotFoundException, IOException {
        System.out.println("Se está actualizando el archivo EXCEL....");
        

        this.storeCorrectDNI();
        
        if (aList.get(2).get(11).equals("%")) {

            CalcularIBAN calculariban = new CalcularIBAN();
            aList = calculariban.editarIBAN(numFilas, aList);
        }

        for (int i = 1; i < aList.size(); i++) {
        
            ArrayList<String> a = aList.get(i);
            if (!(a.get(1).equals("%"))) {
                if ((a.get(8).equals("%"))) {
                    a = this.generateUser(a);
                    aList.set(i, a);
                }
            }

        }

        //QUITAR ESPACIOS VACIOS
        for (int i = 0; i < aList.size(); i++) {
            for (int j = 0; j < aList.get(i).size(); j++) {
                if (aList.get(i).get(j).equals("%")) {
                    ArrayList<String> aux2 = aList.get(i);
                    aux2.set(j, "");
                    aList.set(i, aux2);
                }
            }
        }
        calcularNóminas(month, year);
//VISUALIZAR EL ARRAY ANTES DE SUBIR
//        for (int i = 0; i < aList.size(); i++) {
//            for (int j = 0; j < aList.get(i).size(); j++) {
//                System.out.print(aList.get(i).get(j) + " ");
//            }
//            System.out.println("");
//        }

        //RELLENAR EXCEL
        for (int i = 0; i < numFilas; i++) {
      
            Row r = sheet.getRow(i);
            sheet.removeRow(r);
            sheet.createRow(i);

            r = sheet.getRow(i);
            for (int j = 0; j < aList.get(i).size(); j++) {
                r.createCell(j);
                r.getCell(j).setCellValue(aList.get(i).get(j));

            }//Ponemos el valor del array que debe tener
        }
        try (FileOutputStream outputStream = new FileOutputStream("src/resources/SistemasInformacionII.xlsx")) {
            workbook.write(outputStream);
        } catch (Exception e) {
            System.out.println("Error al escribir en el excel" + e.getMessage());
        }
        /*
        System.out.println("");
        System.out.println("------------------------------------------");
        System.out.println("    TODO HA FUNCIONADO CORRECTAMENTE");
        System.out.println("            EXCEL ACTUALIZADO      ");
        System.out.println("------------------------------------------");
        System.out.println("");
        */
        
    }

    /**
     * Metemos en la estructura de datos los DNI arreglados, y escribimos en el
     * archivo XML
     */
    private void storeCorrectDNI() {
        for (int i = 1; i < numFilas; i++) {
          

            if (!(aList.get(i).get(1).equals("%"))) {
                if (!(aList.get(i).get(7).equals("%"))) {
                    if (aList.get(i).get(7).length() == 9) {

                        //el metodo nos devuelve true si está bien, y el dni correcto si estaba mal
                        String DNIComprobado = checkDNI(aList.get(i).get(7));

                        //si no es correcto
                        if (!DNIComprobado.equals("true")) {
                            ArrayList<String> aux2 = aList.get(i);
                            aux2.set(7, DNIComprobado);
                            aList.set(i, aux2);
                        }
                    }
                }
            }
        }
        Element root = new Element("Trabajadores");
        Document doc = new Document();
        for (int i = 1; i < numFilas; i++) {
            if (aList.get(i).get(7).equals("%") && !aList.get(i).get(1).equals("%")) {
                xmlBuilder(aList.get(i), i + 1, root, doc);

            } else {
                String dni = aList.get(i).get(7);
                for (int j = 1; j < numFilas; j++) {
                    if (j != i && !aList.get(i).get(7).equals("%")) {
                        if (dni.equals(aList.get(j).get(7)) || dni.equals(aList.get(j).size() == 6)) {
                            xmlBuilder(aList.get(i), i + 1, root, doc);
                        }
                    }

                }
            }
        }

        doc.setRootElement(root);
        XMLOutputter outter = new XMLOutputter();
        outter.setFormat(Format.getPrettyFormat());
        try {
            outter.output(doc, new FileWriter(new File("src/resources/Errores.xml")));
        } catch (IOException ex) {
            Logger.getLogger(ExcelFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * Metodo auxiliar por el cual contruimos la estructura del archivo XML
     *
     * @param array
     * @param i
     * @param root
     * @param doc
     */
    private void xmlBuilder(ArrayList<String> array, int i, Element root, Document doc) {

        Element elemTrabajador = new Element("Trabajador");
        elemTrabajador.setAttribute("id", "" + i);

        Element hijoNombre = new Element("Nombre");
        hijoNombre.addContent((!array.get(4).equals("%") ? array.get(4) : "\u200e"));
        Element hijoApellido1 = new Element("PrimerApellido");

        hijoApellido1.addContent((!array.get(5).equals("%") ? array.get(5) : "\u200e"));
        Element hijoApellido2 = new Element("SegundoApellido");

        hijoApellido2.addContent((!array.get(6).equals("%") ? array.get(6) : "\u200e"));
        Element hijoCatedoria = new Element("Categoria");

        hijoCatedoria.addContent((!array.get(2).equals("%") ? array.get(2) : "\u200e"));

        Element hijoEmpresa = new Element("Empresa");
        hijoEmpresa.addContent((!array.get(1).equals("%") ? array.get(1) : "\u200e"));

        elemTrabajador.addContent(hijoNombre);
        elemTrabajador.addContent(hijoApellido1);
        elemTrabajador.addContent(hijoApellido2);
        elemTrabajador.addContent(hijoCatedoria);
        elemTrabajador.addContent(hijoEmpresa);
        root.addContent(elemTrabajador);

    }

    /**
     * Comprobamos que la letra del DNI es correcta o no
     *
     * @param dni
     * @return true si esta bien, el dni arreglado si no es correcto
     */
    private String checkDNI(String dni) {

        int dninumero = 0;//Solo los numeros del dni
        char digitoControl;//Letra del dni

        //Para los dni de extranjeros
        String nuevoDNI = "";
        String dniOriginal = dni;
        if (dni.charAt(0) == 'X') {
            nuevoDNI = "0" + dni.substring(1, dni.length());
            dni = nuevoDNI;

        } else if (dni.charAt(0) == 'Y') {
            nuevoDNI = "1" + dni.substring(1, dni.length());

            dni = nuevoDNI;
        } else if (dni.charAt(0) == 'Z') {
            nuevoDNI = "2" + dni.substring(1, dni.length());

            dni = nuevoDNI;
        }

        //Guardamos la letra del DNI
        digitoControl = dni.charAt(8);

        //Pasamos el dni a un int
        String dniSinLetra = dni.substring(0, dni.length() - 1);
        dninumero = Integer.parseInt(dniSinLetra);
        //Calculamos el resto/23
        int resto = dninumero % 23;
        char letra = ' ';
        switch (resto) {
            case 0:
                letra = 'T';
                break;
            case 1:
                letra = 'R';
                break;
            case 2:
                letra = 'W';
                break;
            case 3:
                letra = 'A';
                break;
            case 4:
                letra = 'G';
                break;
            case 5:
                letra = 'M';
                break;
            case 6:
                letra = 'Y';
                break;
            case 7:
                letra = 'F';
                break;
            case 8:
                letra = 'P';
                break;
            case 9:
                letra = 'D';
                break;
            case 10:
                letra = 'X';
                break;
            case 11:
                letra = 'B';
                break;
            case 12:
                letra = 'N';
                break;
            case 13:
                letra = 'J';
                break;
            case 14:
                letra = 'Z';
                break;
            case 15:
                letra = 'S';
                break;
            case 16:
                letra = 'Q';
                break;
            case 17:
                letra = 'V';
                break;
            case 18:
                letra = 'H';
                break;
            case 19:
                letra = 'L';
                break;
            case 20:
                letra = 'C';
                break;
            case 21:
                letra = 'K';
                break;
            case 22:
                letra = 'E';
                break;
        }

        //Si coincide la letra del dni con la que debe ser
        if (letra == digitoControl) {
            return "true";
        } else {

            String ret = dniOriginal.substring(0, dniOriginal.length() - 1);
            return ret + letra;
        }

    }

    private ArrayList<String> generateUser(ArrayList<String> aux) {

        String nombre = aux.get(4);
        String apellido1 = aux.get(5);
        String apellido2 = aux.get(6);
        String nombreEmpresa = aux.get(1);

        StringBuilder user = new StringBuilder();
        user.append(apellido1.toUpperCase().charAt(0));
        if (!apellido2.equals("%")) {
            user.append(apellido2.toUpperCase().charAt(0));
        }
        user.append(nombre.toUpperCase().charAt(0));
        String domain = "@" + nombreEmpresa + ".es";
        String number = numberToUser(user.toString(), domain);
        user.append(number);
        user.append(domain);
        aux.set(8, user.toString());
        return aux;

    }

    public String numberToUser(String userWithoutNumber, String domain) {
        int ret = 0;
        for (int i = 0; i < aList.size(); i++) {
            if (!aList.get(i).get(8).equals("%")) {
                if (userWithoutNumber.length() == 3) {
                    if (aList.get(i).get(8).substring(0, 3).equals(userWithoutNumber) && aList.get(i).get(8).substring(5, (aList.get(i).get(8).length())).equals(domain)) {
                        ret++;
                    }
                } else {
                    if (aList.get(i).get(8).substring(0, 2).equals(userWithoutNumber) && aList.get(i).get(8).substring(4, (aList.get(i).get(8).length())).equals(domain)) {
                        ret++;
                    }
                }
            }
        }
        if (ret < 10) {
            return "0" + ret;
        } else {
            return "" + ret;
        }
    }
    public void actualizarAList(){
         FileInputStream file2;
        Workbook workbook2;
         try {

            file2 = new FileInputStream(new File(getClass().getResource("/resources/SistemasInformacionII.xlsx").toURI()));
              workbook2 = WorkbookFactory.create(file2);
               DataFormatter dataFormatter = new DataFormatter();
               ArrayList<ArrayList<String>> aList2= new ArrayList<ArrayList<String>>();
        sheet = workbook2.getSheetAt(0);
         //REllenamos el arraylsit con los datos de la pagina 1 del excel
        for (Row r : sheet) { 
          
           
            ArrayList<String> aux = new ArrayList<String>();
            for (int i = 0; i < r.getLastCellNum(); i++) {
                if (r.getCell(i) == null) {
                    aux.add("%");
                } else {
                    Cell cell = r.getCell(i);
                    String cellValue = dataFormatter.formatCellValue(cell);
                    if (cellValue.equals("")) {
                        aux.add("%");
                    } else {
                        aux.add(cellValue);

                    }

                }

            }

            aList2.add(aux);
        }
         for (int i = 1; i < aList2.size(); i++) {
            ArrayList<String> aux = aList2.get(i);
            if (!aList2.get(i).get(1).equals("%")) {
                if (aux.get(3).length() != 10) {
                    aux.set(3, arreglarFecha(aux.get(3)));
                } else {
                    aux.set(3, aux.get(3));
                }

            }
            aList2.set(i, aux);
        }
        aList=aList2;
        
        } catch (IOException | URISyntaxException | EncryptedDocumentException e) {
            System.err.println("Error al leer el archivo excel");
        }
    }
    public void calcularNóminas(String month, String year) {
        System.out.println("Se empieza a calcular las nominas, exportandolas a PDF y subiendolas a la Base de Datos...");
       
       
        //actualizarAList();
       

        ArrayList<String> dataExport;
        boolean extraSinProrratear = false;
        double brutomes = 0.0;

        if (month.equals("06") || month.equals("12")) {
            extraSinProrratear = true;
        }

        for (int i = 1; i < aList.size(); i++) {
            

            // System.out.println("ID : " + i);
            ArrayList<String> workerArrayList = aList.get(i);
            if (!aList.get(i).get(1).equals("%") && !aList.get(i).get(1).equals("") && !aList.get(i).get(7).equals("%") && !aList.get(i).get(7).equals("")) {

                if ((year.equals(workerArrayList.get(3).substring(6, 10)) && Integer.valueOf(month) >= Integer.valueOf(workerArrayList.get(3).substring(3, 5)) || Integer.valueOf(year) > Integer.valueOf(workerArrayList.get(3).substring(6, 10)))) {

                    dataExport = new ArrayList<>();
                    Double numeroDePagas = 14.0;
                    double extraJunio = 6, extraDiciembre = 6;

                    if (workerArrayList.get(12).equals("SI")) {
                        numeroDePagas = 12.0;
                    }
                    dataExport.add(workerArrayList.get(1));//nombre empresa    0
                    dataExport.add(workerArrayList.get(0));//CIF empresa       1
                    dataExport.add(workerArrayList.get(2));//Categoria         2
                    dataExport.add("");//Bruto                                 3
                    dataExport.add(workerArrayList.get(3));//fecha de alta     4
                    dataExport.add(workerArrayList.get(11));//IBAN             5
                    dataExport.add(workerArrayList.get(4) + " " + workerArrayList.get(5) + " " + workerArrayList.get(6));//nombre y apellidos  6
                    dataExport.add(workerArrayList.get(7));//CIF/DNI   7
                    dataExport.add(nombreMes(month) + "/" + year);//Fecha 8
                    // dataExport.add("" + diasMes(month, year));//dias del mes                      9
                    dataExport.add("30");//dias del mes
                    dataExport.add("");//Salario Mes      10  
                    dataExport.add("");//Prorrateo Mes     11
                    dataExport.add("");//Complemento Mes   12
                    dataExport.add(""); // numero de trienios  13
                    dataExport.add("");//Antiguedad Mes    14
                    dataExport.add("");//Sobre que se calcula  15  
                    dataExport.add("");//Seguridad Social %    16  
                    dataExport.add("");//Seguridad Social  17
                    dataExport.add("");//Desempleo %   18
                    dataExport.add("");//Desempleo     19
                    dataExport.add("");//Cuota Formacion   %   20  
                    dataExport.add("");//Cuota Formacion   21
                    dataExport.add("");//IRPF %    22  
                    dataExport.add("");//IRPF  23
                    dataExport.add("");//Total deducciones 24
                    dataExport.add("");//Total Devengos    25
                    dataExport.add("");//Líquido a Percibir 26
                    dataExport.add("");//Coste empresario Base 27
                    dataExport.add("");//Contingencias Comunes %   28
                    dataExport.add("");//Contingencias Comunes 29
                    dataExport.add("");//Desempleo empresa %   30
                    dataExport.add("");//Desempleo empresa     31
                    dataExport.add("");//Cuota Formacion empresa%  32
                    dataExport.add("");//Cuota Formacion empresa   33
                    dataExport.add("");//Accidentes empresa %  34
                    dataExport.add("");//Accidentes empresa    35
                    dataExport.add("");//FOGASA empresa%   36
                    dataExport.add("");//FOGASA empresa    37
                    dataExport.add("");//TOTAL empresa    38
                    dataExport.add("");//COSTE TOTAL empresa   39
                    dataExport.add("");// SOBRE EL QUE SE CALCULA IRPF 40

                    ArrayList salario = categories.get(workerArrayList.get(2));
                    int totalTrienios = 0;
                    if (Integer.valueOf(month) > Integer.valueOf(workerArrayList.get(3).substring(3, 5))) {
                        totalTrienios = (Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) / 3;

                    } else {
                        totalTrienios = ((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) - 1) / 3;
                    }
                    dataExport.set(13, "" + totalTrienios);
                    if (totalTrienios == 0) {
                        dataExport.set(14, "0");
                    } else {
                        dataExport.set(14, "" + trienios.get("" + totalTrienios));
                    }
                    int totalTrieniosAnho = 0;

                    ArrayList<Integer> aux = categories.get(dataExport.get(2));
                    extraDeJunio = 0.0;
                    extraDeDiciembre = 0.0;
                    //CALCULO LAS 2 EXTRAS DEL AÑO
                    if (workerArrayList.get(12).equals("NO")) {
                        calcularExtras(Integer.parseInt(month), Integer.parseInt(year), Integer.valueOf(workerArrayList.get(3).substring(3, 5)), Integer.valueOf(workerArrayList.get(3).substring(6, 10)), aux);
                    }

                    double sueldoBaseMes = aux.get(0) / 14.0;
                    dataExport.set(10, "" + sueldoBaseMes);
                    double complementoMes = (aux.get(1) / 14.0);

                    dataExport.set(12, "" + complementoMes);
                    double prorrateo = 0.0;

                    double brutoAnual = 0.0;
                    if (year.equals(workerArrayList.get(3).substring(6, 10))) { // SI PIDE EL MISMO AÑO QUE EL PROPIO año DE ENTRADA, no hay trienio
                        //mismo año
                        //         System.out.println("PRUEBA 1-   - empezo a trabajar en el mismo año que pide");
                        int mesEntrada = Integer.valueOf(workerArrayList.get(3).substring(3, 5));
                        int mesesTrabajados = 12 - mesEntrada + 1;//para contar el que entra
                        extraDiciembre = 0;
                        extraJunio = 0;
                        if (mesEntrada <= 5) {
                            extraJunio = 6 - mesEntrada;
                            extraDiciembre = 6;
                        } else if (mesEntrada <= 11) {

                            extraDiciembre = 12 - mesEntrada;
                        }
                        if (numeroDePagas == 12.0) {
                            brutoAnual = ((aux.get(0) + aux.get(1)) / 12.0) * mesesTrabajados;
                            prorrateo = (sueldoBaseMes + complementoMes) / 6;

                        } else {
                            Double totalExtras = ((sueldoBaseMes + complementoMes) / 6) * extraJunio + ((sueldoBaseMes + complementoMes) / 6) * extraDiciembre;
                            brutoAnual = (sueldoBaseMes * mesesTrabajados) + (complementoMes * mesesTrabajados) + totalExtras;//calcular con esto irpf

                            //        System.out.println("EXTRA DICIEMBRE = " + prorrateoDiciembre + " EXTRA JUNIO= " + prorrateoJunio);
                        }

                    } else {
                        if (numeroDePagas == 12.0 && (Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10)) + 1) % 3 == 0 && Integer.valueOf(workerArrayList.get(3).substring(3, 5)) < 6) {
                            //    System.out.println("PRUEBA 2-   -Pide diciembre, y recibe trienio del anterior (solo 12 pagas)");
                            brutoAnual = aux.get(0) + aux.get(1);
                            if (totalTrienios == 0) {

                                brutoAnual += (trienios.get("" + (totalTrienios + 1)) / 6.0);
                            } else {
                                brutoAnual += (14.0 * trienios.get("" + (totalTrienios))) + ((trienios.get("" + (totalTrienios + 1)) - trienios.get("" + (totalTrienios))) / 6.0);
                            }
                            if (month.equals("12")) {
                                prorrateo = (sueldoBaseMes / 6.0) + (complementoMes / 6.0) + (trienios.get("" + (totalTrienios + 1)) / 6.0);

                            } else {
                                if (totalTrienios == 0) {
                                    prorrateo = (sueldoBaseMes / 6.0) + (complementoMes / 6.0);
                                } else {
                                    prorrateo = (sueldoBaseMes / 6.0) + (complementoMes / 6.0) + (trienios.get("" + (totalTrienios)) / 6.0);

                                }

                            }

                        } else if (Integer.valueOf(month) > Integer.valueOf(workerArrayList.get(3).substring(3, 5))) {
                            //   System.out.println("ES MAYOR EL MES DE ENTRADA");
                            //  System.out.println("año de entrada "+Integer.valueOf(workerArrayList.get(3).substring(6, 10))+" año que pide :"+Integer.valueOf(year));
                            //  System.out.println((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))));
                            // System.out.println((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) % 3 == 0);

                            if ((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) % 3 == 0) {
                                //      System.out.println("PRUEBA 3-BIEN   - Cambia de trienio este año, despues del mes que pide");
                                int mesEntrada = Integer.valueOf(workerArrayList.get(3).substring(3, 5)) + 1;
                                int trieniosAntiguos, trieniosNuevos;
                                if (mesEntrada <= 6) {
                                    trieniosAntiguos = mesEntrada - 1;
                                    trieniosNuevos = 15 - mesEntrada;

                                } else {
                                    trieniosAntiguos = mesEntrada;
                                    trieniosNuevos = 14 - mesEntrada;
                                }
                                if (numeroDePagas == 12.0) {
                                    if (mesEntrada < 6 && Integer.valueOf(month) < 6) {

                                    }
                                    prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6) + (trienios.get("" + (totalTrienios)) / 6);
                                }
                                if (totalTrienios == 1) {
                                    brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + totalTrienios));

                                } else {
                                    //     System.out.println(aux.get(0) + "" + aux.get(1) + "Trienios Antiguos a cobrar: " + trieniosAntiguos + " Trienios nuevos a conrar: " + trieniosNuevos);
                                    brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + totalTrienios)) + (trieniosAntiguos * trienios.get("" + (totalTrienios - 1)));
                                    double diferenciaNuevotrienio = trienios.get("" + (totalTrienios)) - (trienios.get("" + (totalTrienios + 1)));

                                }

                            } else {
                                //       System.out.println("PRUEBA 4-  mal  - Todo normal");

                                if (numeroDePagas == 12.0) {

                                    if (totalTrienios == 0) {
                                        prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6);
                                    } else {
                                        prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6) + (trienios.get("" + (totalTrienios)) / 6);

                                    }

                                }

                                if (totalTrienios == 0) {
                                    brutoAnual = aux.get(0) + aux.get(1);//calcular con esto irpf
                                } else {
                                    brutoAnual = aux.get(0) + aux.get(1) + ((trienios.get("" + totalTrienios)) * 14);//calcular con esto irpf
                                }

                            }
                        } else if (month.equals(workerArrayList.get(3).substring(3, 5))) {
                            //         System.out.println("ES IGUAL EL MES DE ENTRADA");
                            //         System.out.println("año de entrada "+Integer.valueOf(workerArrayList.get(3).substring(6, 10))+" año que pide :"+Integer.valueOf(year));

                            if ((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) % 3 == 0) {
                                //             System.out.println("PRUEBA 7-   - Mismo mes y cambia de trienio este año, despues del mes que pide");
                                int mesEntrada = Integer.valueOf(workerArrayList.get(3).substring(3, 5)) + 1;
                                int trieniosAntiguos, trieniosNuevos;
                                if (mesEntrada <= 6) {
                                    trieniosAntiguos = mesEntrada - 1;
                                    trieniosNuevos = 15 - mesEntrada;
                                } else {
                                    trieniosAntiguos = mesEntrada;
                                    trieniosNuevos = 14 - mesEntrada;
                                }
                                if (totalTrienios == 0) {
                                    brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + (totalTrienios + 1)));

                                } else {
                                    brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + (totalTrienios + 1))) + (trieniosAntiguos * trienios.get("" + totalTrienios));

                                }
                                if (numeroDePagas == 12.0) {

                                    prorrateo = (sueldoBaseMes + complementoMes + (trienios.get("" + (totalTrienios + 1)))) / 6.0;

                                }

                            } else {
                                //                System.out.println("PRUEBA 8- BIEN mal  - Todo normal");

                                if (numeroDePagas == 12.0) {

                                    if (totalTrienios == 0) {
                                        prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6);
                                    } else {
                                        prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6) + (trienios.get("" + (totalTrienios)) / 6);

                                    }

                                }

                                if (totalTrienios == 0) {
                                    brutoAnual = aux.get(0) + aux.get(1);//calcular con esto irpf
                                } else {
                                    brutoAnual = aux.get(0) + aux.get(1) + ((trienios.get("" + totalTrienios)) * 14);//calcular con esto irpf
                                }

                            }

                        } else if ((((Integer.valueOf(year) - Integer.valueOf(workerArrayList.get(3).substring(6, 10))) - 1)) % 3 == 0) {
                            //               System.out.println("PRUEBA 5-   - Cambia de trienio este año, antes del mes que pide");

                            //cuando el cambio de trienio se produce despues de pedir la nómina
                            int mesEntrada = Integer.valueOf(workerArrayList.get(3).substring(3, 5)) + 1;
                            int trieniosAntiguos, trieniosNuevos;
                            if (mesEntrada <= 6) {
                                trieniosAntiguos = mesEntrada - 1;
                                trieniosNuevos = 15 - mesEntrada;
                            } else {
                                trieniosAntiguos = mesEntrada;
                                trieniosNuevos = 14 - mesEntrada;
                            }
                            if (totalTrienios == 0) {
                                brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + (totalTrienios + 1)));

                            } else {
                                brutoAnual = aux.get(0) + aux.get(1) + (trieniosNuevos * trienios.get("" + (totalTrienios + 1))) + (trieniosAntiguos * trienios.get("" + totalTrienios));

                            }

                        } else {
                            //               System.out.println("PRUEBA 6-   - Genérico");

                            if (totalTrienios == 0) {
                                brutoAnual = aux.get(0) + aux.get(1);
                                if (numeroDePagas == 12.0) {

                                    prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6);
                                }

                            } else {
                                brutoAnual = aux.get(0) + aux.get(1) + (14 * trienios.get("" + (totalTrienios)));
                                if (numeroDePagas == 12.0) {

                                    prorrateo = (sueldoBaseMes / 6) + (complementoMes / 6) + (trienios.get("" + (totalTrienios)) / 6);
                                }
                            }

                        }
                    }

                    dataExport.set(3, "" + brutoAnual);
                    dataExport.set(11, "" + prorrateo);

                    double totalPositivoSueldo = 0.0;
                    if (numeroDePagas == 14.0) {
                        //   totalPositivoSueldo = (aux.get(0)+aux.get(1)+Double.parseDouble(dataExport.get(14))) / 14.0;

                        double calculoirpf = Double.parseDouble(dataExport.get(10)) + Double.parseDouble(dataExport.get(11)) + Double.parseDouble(dataExport.get(12)) + Double.parseDouble(dataExport.get(14));
                        totalPositivoSueldo = calculoirpf;
                   //     System.out.println("TOTAL POSITIVO SUELDO= "+ totalPositivoSueldo+ "irpf "+ calculoirpf);
                        dataExport.set(40, "" + calculoirpf);

                        if ((Integer.parseInt(workerArrayList.get(3).substring(3, 5)) < 6  || workerArrayList.get(3).substring(3, 5).equals(12) )  && extraDeJunio != 0.0) {
                            totalPositivoSueldo += extraDeJunio / 6.0;
                        } else if ((Integer.parseInt(workerArrayList.get(3).substring(3, 5)) >= 6 && Integer.parseInt(workerArrayList.get(3).substring(3, 5)) < 12 ) && extraDeDiciembre != 0) {
                            totalPositivoSueldo += extraDeDiciembre / 6.0;
                        } else {
                            totalPositivoSueldo += totalPositivoSueldo / 6.0;
                        }
                    } else {
                        totalPositivoSueldo = Double.parseDouble(dataExport.get(10)) + Double.parseDouble(dataExport.get(11)) + Double.parseDouble(dataExport.get(12)) + Double.parseDouble(dataExport.get(14));
                        dataExport.set(40, "" + totalPositivoSueldo);

                    }

                    dataExport.set(15, "" + totalPositivoSueldo);
                    dataExport.set(27, dataExport.get(15));

                    //IRPF
                    for (int i2 = 0; i2 < retencion2.size(); i2++) {

                        if (((int) Double.parseDouble(dataExport.get(3))) < retencion2.get(i2)) {
                            dataExport.set(22, "" + retencion.get("" + retencion2.get(i2)));
                            break;
                        }
                    }
                    // %
                    dataExport.set(16, "" + porcentajes.get("Cuota obrera general TRABAJADOR"));
                    dataExport.set(18, "" + porcentajes.get("Cuota desempleo TRABAJADOR"));
                    dataExport.set(20, "" + porcentajes.get("Cuota formación TRABAJADOR"));
                    dataExport.set(28, "" + porcentajes.get("Contingencias comunes EMPRESARIO"));
                    dataExport.set(30, "" + porcentajes.get("Desempleo EMPRESARIO"));
                    dataExport.set(32, "" + porcentajes.get("Formacion EMPRESARIO"));
                    dataExport.set(34, "" + porcentajes.get("Accidentes trabajo EMPRESARIO"));
                    dataExport.set(36, "" + porcentajes.get("Fogasa EMPRESARIO"));
                    //DEDUCCION
                    double aux2 = Double.parseDouble(dataExport.get(15)) * (Double.parseDouble(dataExport.get(16)) / 100);
                    dataExport.set(17, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(15)) * (Double.parseDouble(dataExport.get(18)) / 100);
                    dataExport.set(19, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(15)) * (Double.parseDouble(dataExport.get(20)) / 100);
                    dataExport.set(21, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(40)) * (Double.parseDouble(dataExport.get(22)) / 100);
                    dataExport.set(23, "" + aux2);

                    aux2 = (Double.parseDouble(dataExport.get(17)) + Double.parseDouble(dataExport.get(19)) + Double.parseDouble(dataExport.get(21)) + Double.parseDouble(dataExport.get(23)));
                    dataExport.set(24, "" + aux2);

                    aux2 = (Double.parseDouble(dataExport.get(10)) + Double.parseDouble(dataExport.get(11)) + Double.parseDouble(dataExport.get(12)) + Double.parseDouble(dataExport.get(14)));
                    dataExport.set(25, "" + aux2);

                    aux2 = (Double.parseDouble(dataExport.get(25)) - Double.parseDouble(dataExport.get(24)));
                    dataExport.set(26, "" + aux2);

                    aux2 = Double.parseDouble(dataExport.get(27)) * (Double.parseDouble(dataExport.get(28)) / 100);
                    dataExport.set(29, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(27)) * (Double.parseDouble(dataExport.get(30)) / 100);
                    dataExport.set(31, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(27)) * (Double.parseDouble(dataExport.get(32)) / 100);
                    dataExport.set(33, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(27)) * (Double.parseDouble(dataExport.get(34)) / 100);
                    dataExport.set(35, "" + aux2);
                    aux2 = Double.parseDouble(dataExport.get(27)) * (Double.parseDouble(dataExport.get(36)) / 100);
                    dataExport.set(37, "" + aux2);

                    aux2 = (Double.parseDouble(dataExport.get(29)) + Double.parseDouble(dataExport.get(31)) + Double.parseDouble(dataExport.get(33)) + Double.parseDouble(dataExport.get(35)) + Double.parseDouble(dataExport.get(37)));
                    dataExport.set(38, "" + aux2);

                    aux2 = (Double.parseDouble(dataExport.get(38)) + Double.parseDouble(dataExport.get(25)));
                    dataExport.set(39, "" + aux2);

                    WorkerDAO wDao = new WorkerDAO();
                    CategoryDAO cDao = new CategoryDAO();
                    CompanyDAO comDao = new CompanyDAO();
                    PayrollDAO pDao = new PayrollDAO();

                    //System.out.println("NOMBRE TRABAJADOR: " + dataExport.get(6) + "  " + dataExport.get(7));
                 
                    int existeCategoria = cDao.ExistCategory(dataExport.get(2));
                    if (existeCategoria==0) {
                        cDao.addCategory(dataExport.get(2),Double.valueOf(aux.get(0)),Double.valueOf(aux.get(1)),0);


                    }else{
                         cDao= new CategoryDAO();
                         cDao.addCategory(dataExport.get(2),Double.valueOf(aux.get(0)),Double.valueOf(aux.get(1)),existeCategoria);

                    }
                    int existeEmpresa = comDao.ExistCompany(dataExport.get(1));
               
                    if (existeEmpresa==0) {
                       comDao.addCompany(dataExport.get(1), dataExport.get(0),0);
                    }else{
                        comDao = new CompanyDAO();
                         comDao.addCompany(dataExport.get(1), dataExport.get(0),existeEmpresa);
                    }

                       int existeTrabajador = wDao.ExistWorker(workerArrayList.get(4), workerArrayList.get(5), workerArrayList.get(6), dataExport.get(7), dataExport.get(4));

                    if (existeTrabajador==0) {
                        wDao.addWorker(workerArrayList,0);
                    }else{
                        wDao = new WorkerDAO();
                        wDao.addWorker(workerArrayList,existeTrabajador);
                    }
                    
                    int existenomina = pDao.ExistPayRoll(month, year, wDao.getworker(workerArrayList.get(4), workerArrayList.get(5), workerArrayList.get(6), dataExport.get(7), dataExport.get(4)), Double.parseDouble(dataExport.get(25)), Double.parseDouble(dataExport.get(26)));
                    if (existenomina==0) {
                        pDao.addPayRoll(dataExport,workerArrayList,month,year,0);

                    }else{
                        pDao = new PayrollDAO();
                         pDao.addPayRoll(dataExport,workerArrayList,month,year,existenomina);
                    }
                    
                    
                    ArrayList<String> dataExportExtra = dataExport;
                    dataExport = rounded(dataExport);

                  //  imprimirNómina(dataExport);
                    try {
                        WritePDF(dataExport, false);
                    } catch (Exception FileNotFoundException) {
                        System.out.println("ERROR AL PASAR A PDF");
                    }
                      boolean hayExtra=false;
                    if (extraSinProrratear && workerArrayList.get(12).equals("NO")) {
                        if (month.equals("06")) {
                            //          System.out.println("EXTRAJUNIO VALE: " + extraJunio);
                            hayExtra=true;
                            dataExportExtra=extra(dataExportExtra, extraJunio);
                        } else if (month.equals("12")) {
                            //        System.out.println("EXTRADICIEMBRE VALE: " + extraDiciembre);
                              hayExtra=true;
                            dataExportExtra=extra(dataExportExtra, extraDiciembre);
                        }

                    }
                    if (hayExtra) {
                         wDao = new WorkerDAO();
                     cDao = new CategoryDAO();
                     comDao = new CompanyDAO();
                     pDao = new PayrollDAO();

                    //System.out.println("NOMBRE TRABAJADOR: " + dataExport.get(6) + "  " + dataExport.get(7));
                 
                     existeCategoria = cDao.ExistCategory(dataExportExtra.get(2));
                    if (existeCategoria==0) {
                        cDao.addCategory(dataExportExtra.get(2),Double.valueOf(aux.get(0)),Double.valueOf(aux.get(1)),0);


                    }else{
                         cDao= new CategoryDAO();
                         cDao.addCategory(dataExportExtra.get(2),Double.valueOf(aux.get(0)),Double.valueOf(aux.get(1)),existeCategoria);

                    }
                     existeEmpresa = comDao.ExistCompany(dataExportExtra.get(1));
               
                    if (existeEmpresa==0) {
                       comDao.addCompany(dataExportExtra.get(1), dataExportExtra.get(0),0);
                    }else{
                        comDao = new CompanyDAO();
                         comDao.addCompany(dataExportExtra.get(1), dataExportExtra.get(0),existeEmpresa);
                    }

                        existeTrabajador = wDao.ExistWorker(workerArrayList.get(4), workerArrayList.get(5), workerArrayList.get(6), dataExportExtra.get(7), dataExportExtra.get(4));

                    if (existeTrabajador==0) {
                        wDao.addWorker(workerArrayList,0);
                    }else{
                        wDao = new WorkerDAO();
                        wDao.addWorker(workerArrayList,existeTrabajador);
                    }
                    
                     existenomina = pDao.ExistPayRoll(month, year, wDao.getworker(workerArrayList.get(4), workerArrayList.get(5), workerArrayList.get(6), dataExportExtra.get(7), dataExportExtra.get(4)), Double.parseDouble(dataExportExtra.get(25)), Double.parseDouble(dataExportExtra.get(26)));
                    if (existenomina==0) {
                        pDao.addPayRoll(dataExportExtra,workerArrayList,month,year,0);

                    }else{
                        pDao = new PayrollDAO();
                         pDao.addPayRoll(dataExportExtra,workerArrayList,month,year,existenomina);
                    }
                   
                    
                    
                    }

                } else {
                }

            }
          
        }
      
        System.out.println("");
        System.out.println("--------------------------------------------------------------");
        System.out.println("|               TERMINADO DE EXPORTAR PDF                    |");
        System.out.println("--------------------------------------------------------------");
        System.out.println("|     TERMINADO DE SUBIR A LA BASE DE DATOS CORRECTAMENTE    |");
        System.out.println("--------------------------------------------------------------");
        System.out.println("");
             

                    
    }

    public void calcularExtras(int mesDeLaNomina, int anhoDeLaNomina, int mesInicioTrabajador, int AnhoInicioTrabajador, ArrayList<Integer> aux) {
        Double salarioSinprorrateoMes = (aux.get(0) + aux.get(1)) / 14.0;
        int anhoshastaJunio = 0, anhoshastaDiciembre = 0;
        if (mesInicioTrabajador > 6) {
            anhoshastaJunio = anhoDeLaNomina - (AnhoInicioTrabajador) - 1;
        } else {
            anhoshastaJunio = anhoDeLaNomina - AnhoInicioTrabajador;
        }
        if (mesInicioTrabajador == 12) {
            anhoshastaDiciembre = anhoDeLaNomina - (AnhoInicioTrabajador - 1);
        } else {
            anhoshastaDiciembre = anhoDeLaNomina - AnhoInicioTrabajador;
        }

        //             System.out.println("AÑOS DE DIFETENCIA CON Junio "+ anhoshastaJunio+ " hasta diciembre : "+ anhoshastaDiciembre);
        int trieniosJunio = anhoshastaJunio / 3;
        int trieniosDiciembre = anhoshastaDiciembre / 3;
        //            System.out.println("trienios junio "+ trieniosJunio+ " de diciembre : "+ trieniosDiciembre);
       

        if (trieniosJunio == 0) {
            extraDeJunio = salarioSinprorrateoMes;
        } else {
            extraDeJunio = salarioSinprorrateoMes + trienios.get("" + trieniosJunio);
        }
        if (trieniosDiciembre == 0) {
            extraDeDiciembre = salarioSinprorrateoMes;
        } else {
            extraDeDiciembre = salarioSinprorrateoMes + trienios.get("" + trieniosDiciembre);
        }
        if (anhoshastaJunio == -1) {
            extraDeJunio = 0.0;
        }
         if(mesDeLaNomina==12 &&(anhoDeLaNomina - AnhoInicioTrabajador+ 1) % 3 == 0 && mesInicioTrabajador < 6) {
             extraDeJunio = salarioSinprorrateoMes + trienios.get("" + (trieniosJunio+1));
        }
        //           System.out.println("EXTRA JUNIO: "+ extraDeJunio+ " EXTRA DE DICIEMBRE: "+ extraDeDiciembre);

    }

    public void WritePDF(ArrayList<String> dataExport, boolean extra) throws FileNotFoundException {
        String nombreApellido = "";
        for (int x = 0; x < dataExport.get(6).length(); x++) {
            if (dataExport.get(6).charAt(x) != ' ') {
                nombreApellido += dataExport.get(6).charAt(x);
            }
        }
        String fechaSinbarra = "";
        for (int x = 0; x < dataExport.get(8).length(); x++) {
            if (dataExport.get(8).charAt(x) != '/') {
                fechaSinbarra += dataExport.get(8).charAt(x);
            }
        }
        String ruta;
        if (extra) {
            fechaSinbarra = fechaSinbarra.substring(9, fechaSinbarra.length());
            ruta = "src/resources/nominas/" + dataExport.get(7) + nombreApellido + fechaSinbarra + "EXTRA.pdf";

        } else {
            ruta = "src/resources/nominas/" + dataExport.get(7) + nombreApellido + fechaSinbarra + ".pdf";
        }
        //   System.out.println("RUTA:  " + ruta);
        PdfWriter writer = new PdfWriter(ruta);
        //  PdfWriter writer = new PdfWriter("1.pdf");
        PdfDocument pdf = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf, PageSize.LETTER);

        Paragraph inicio = new Paragraph();
        inicio.add("Nomina " + dataExport.get(8));
        inicio.setTextAlignment(TextAlignment.CENTER);
        inicio.setFontSize(22);
        document.add(inicio);

        Paragraph empty = new Paragraph("");
        document.add(empty);

        com.itextpdf.layout.element.Table tabla1 = new com.itextpdf.layout.element.Table(2);
        tabla1.setWidth(500);
        Paragraph empresa = new Paragraph("Empresa: ");
        empresa.setTextAlignment(TextAlignment.LEFT);
        empresa.setFontSize(10);
        empresa.setBold();
        Paragraph nom = new Paragraph(dataExport.get(0));
        Paragraph cif = new Paragraph("CIF: " + dataExport.get(1));

        Paragraph dir1 = new Paragraph("Avenida de la facultad - 6");
        Paragraph dir2 = new Paragraph("24001 León");

        com.itextpdf.layout.element.Cell cell1 = new com.itextpdf.layout.element.Cell();
        cell1.setBorder(new SolidBorder(1));
        cell1.setWidth(250);
        cell1.setTextAlignment(TextAlignment.CENTER);
        cell1.add(empresa);
        cell1.add(nom);
        cell1.add(cif);
        cell1.add(dir1);
        cell1.add(dir2);

        tabla1.addCell(cell1);

        com.itextpdf.layout.element.Cell cell2 = new com.itextpdf.layout.element.Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.setPadding(10);
        cell2.setTextAlignment(TextAlignment.RIGHT);
        cell2.add(new Paragraph("IBAN: " + dataExport.get(5)));
        cell2.add(new Paragraph("Bruto anual: " + dataExport.get(3)));
        cell2.add(new Paragraph("Categoría: " + dataExport.get(2)));
        cell2.add(new Paragraph("Fecha de alta: " + dataExport.get(4)));
        tabla1.addCell(cell2);

        com.itextpdf.layout.element.Table tabla2 = new com.itextpdf.layout.element.Table(1);
        tabla2.setWidth(250);

        com.itextpdf.layout.element.Cell cell3 = new com.itextpdf.layout.element.Cell();

        cell3.setBorder(Border.NO_BORDER);
        cell3.setPaddingLeft(23);
        cell3.setPaddingTop(20);

        Paragraph empleado = new Paragraph("Empleado: ");
        empleado.setTextAlignment(TextAlignment.LEFT);
        empleado.setFontSize(11);
        empleado.setBold();
        cell3.add(empleado);
        cell3.add(new Paragraph(""));
        cell3.add(new Paragraph("   " + dataExport.get(6)));
        cell3.add(new Paragraph("DNI: " + dataExport.get(7)));
        cell3.add(new Paragraph("   Avenida de la facultad - 6"));
        cell3.add(new Paragraph("   24001 León"));
        cell3.setWidth(250);
        tabla2.addCell(cell3);
        tabla2.setFontSize(12);
        document.add(tabla1);
        document.add(tabla2);

        Paragraph barra = new Paragraph("____________________________________________________________________________");
        barra.setPaddingTop(5);
        barra.setPaddingBottom(5);

        Paragraph title = new Paragraph("____________________________________________________________________________\n"
                + " Conceptos                             Cantidad                           Devengo                        Deducción"
                + "\n____________________________________________________________________________");
        title.setBold();
        title.setFontSize(12);

        title.setHeight(55);

        document.add(title);
        float[] pointColumnWidths = {120F, 150F, 150F, 150F};
        com.itextpdf.layout.element.Table tablaSalario = new com.itextpdf.layout.element.Table(pointColumnWidths);
        tablaSalario.setBorder(Border.NO_BORDER);
        tablaSalario.setPaddingLeft(20);
        tablaSalario.setFontSize(8);

        /////////////////////////////////////SALARIO//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda1 = new com.itextpdf.layout.element.Cell();
        celda1.setBorder(Border.NO_BORDER);
        celda1.setPaddingLeft(20);
        celda1.add(new Paragraph("Salario Base "));
        celda1.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda1);

        com.itextpdf.layout.element.Cell celda2 = new com.itextpdf.layout.element.Cell();
        celda2.setBorder(Border.NO_BORDER);
        celda2.add(new Paragraph(dataExport.get(9)));
        celda2.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda2);

        com.itextpdf.layout.element.Cell celda3 = new com.itextpdf.layout.element.Cell();
        celda3.setBorder(Border.NO_BORDER);
        celda3.add(new Paragraph(dataExport.get(10)));
        celda3.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda3);

        com.itextpdf.layout.element.Cell celda4 = new com.itextpdf.layout.element.Cell();
        celda4.setBorder(Border.NO_BORDER);
        celda4.add(new Paragraph(""));
        celda4.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda4);
        /////////////////////////////////////PRORRATEO//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda5 = new com.itextpdf.layout.element.Cell();
        celda5.setBorder(Border.NO_BORDER);
        celda5.setPaddingLeft(20);
        celda5.add(new Paragraph("Prorrateo "));
        celda5.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda5);

        com.itextpdf.layout.element.Cell celda6 = new com.itextpdf.layout.element.Cell();
        celda6.setBorder(Border.NO_BORDER);
        celda6.add(new Paragraph(dataExport.get(9)));
        celda6.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda6);

        com.itextpdf.layout.element.Cell celda7 = new com.itextpdf.layout.element.Cell();
        celda7.setBorder(Border.NO_BORDER);
        celda7.add(new Paragraph(dataExport.get(11)));
        celda7.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda7);

        com.itextpdf.layout.element.Cell celda8 = new com.itextpdf.layout.element.Cell();
        celda8.setBorder(Border.NO_BORDER);
        celda8.add(new Paragraph(""));
        celda8.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda8);
        /////////////////////////////////////COMPLEMENTO//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda9 = new com.itextpdf.layout.element.Cell();
        celda9.setBorder(Border.NO_BORDER);
        celda9.setPaddingLeft(20);
        celda9.add(new Paragraph("Complemento "));
        celda9.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda9);

        com.itextpdf.layout.element.Cell celda10 = new com.itextpdf.layout.element.Cell();
        celda10.setBorder(Border.NO_BORDER);
        celda10.add(new Paragraph(dataExport.get(9)));
        celda10.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda10);

        com.itextpdf.layout.element.Cell celda11 = new com.itextpdf.layout.element.Cell();
        celda11.setBorder(Border.NO_BORDER);
        celda11.add(new Paragraph(dataExport.get(12)));
        celda11.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda11);

        com.itextpdf.layout.element.Cell celda12 = new com.itextpdf.layout.element.Cell();
        celda12.setBorder(Border.NO_BORDER);
        celda12.add(new Paragraph(""));
        celda12.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda12);
        /////////////////////////////////////ANTIGUEDAD//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda13 = new com.itextpdf.layout.element.Cell();
        celda13.setBorder(Border.NO_BORDER);
        celda13.setPaddingLeft(20);
        celda13.add(new Paragraph("Antigüedad "));
        celda13.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda13);

        com.itextpdf.layout.element.Cell celda14 = new com.itextpdf.layout.element.Cell();
        celda14.setBorder(Border.NO_BORDER);
        celda14.add(new Paragraph(dataExport.get(13)));
        celda14.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda14);

        com.itextpdf.layout.element.Cell celda15 = new com.itextpdf.layout.element.Cell();
        celda15.setBorder(Border.NO_BORDER);
        celda15.add(new Paragraph(dataExport.get(14)));
        celda15.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda15);

        com.itextpdf.layout.element.Cell celda16 = new com.itextpdf.layout.element.Cell();
        celda16.setBorder(Border.NO_BORDER);
        celda16.add(new Paragraph(""));
        celda16.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda16);
        /////////////////////////////////////CONTINGENCIAS//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda17 = new com.itextpdf.layout.element.Cell();
        celda17.setBorder(Border.NO_BORDER);
        celda17.setPaddingLeft(20);
        celda17.add(new Paragraph("Contingencias generales "));
        celda17.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda17);

        com.itextpdf.layout.element.Cell celda18 = new com.itextpdf.layout.element.Cell();
        celda18.setBorder(Border.NO_BORDER);
        celda18.add(new Paragraph(dataExport.get(16) + "% de " + dataExport.get(15)));
        celda18.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda18);

        com.itextpdf.layout.element.Cell celda19 = new com.itextpdf.layout.element.Cell();
        celda19.setBorder(Border.NO_BORDER);
        celda19.add(new Paragraph(""));
        celda19.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda19);

        com.itextpdf.layout.element.Cell celda20 = new com.itextpdf.layout.element.Cell();
        celda20.setBorder(Border.NO_BORDER);
        celda20.add(new Paragraph(dataExport.get(17)));
        celda20.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda20);
        /////////////////////////////////////DESEMPLEO//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda21 = new com.itextpdf.layout.element.Cell();
        celda21.setBorder(Border.NO_BORDER);
        celda21.setPaddingLeft(20);
        celda21.add(new Paragraph("Desempleo "));
        celda21.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda21);

        com.itextpdf.layout.element.Cell celda22 = new com.itextpdf.layout.element.Cell();
        celda22.setBorder(Border.NO_BORDER);
        celda22.add(new Paragraph(dataExport.get(18) + "% de " + dataExport.get(15)));
        celda22.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda22);

        com.itextpdf.layout.element.Cell celda23 = new com.itextpdf.layout.element.Cell();
        celda23.setBorder(Border.NO_BORDER);
        celda23.add(new Paragraph(""));
        celda23.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda23);

        com.itextpdf.layout.element.Cell celda24 = new com.itextpdf.layout.element.Cell();
        celda24.setBorder(Border.NO_BORDER);
        celda24.add(new Paragraph(dataExport.get(19)));
        celda24.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda24);
        /////////////////////////////////////Cuota Formacion//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda25 = new com.itextpdf.layout.element.Cell();
        celda25.setBorder(Border.NO_BORDER);
        celda25.setPaddingLeft(20);
        celda25.add(new Paragraph("Cuota formación "));
        celda25.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda25);

        com.itextpdf.layout.element.Cell celda26 = new com.itextpdf.layout.element.Cell();
        celda26.setBorder(Border.NO_BORDER);
        celda26.add(new Paragraph(dataExport.get(20) + "% de " + dataExport.get(15)));
        celda26.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda26);

        com.itextpdf.layout.element.Cell celda27 = new com.itextpdf.layout.element.Cell();
        celda27.setBorder(Border.NO_BORDER);
        celda27.add(new Paragraph(""));
        celda27.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda27);

        com.itextpdf.layout.element.Cell celda28 = new com.itextpdf.layout.element.Cell();
        celda28.setBorder(Border.NO_BORDER);
        celda28.add(new Paragraph(dataExport.get(21)));
        celda28.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda28);
        /////////////////////////////////////IRPF//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda29 = new com.itextpdf.layout.element.Cell();
        celda29.setBorder(Border.NO_BORDER);
        celda29.setPaddingLeft(20);
        celda29.add(new Paragraph("IRPF "));
        celda29.setTextAlignment(TextAlignment.LEFT);
        tablaSalario.addCell(celda29);

        com.itextpdf.layout.element.Cell celda30 = new com.itextpdf.layout.element.Cell();
        celda30.setBorder(Border.NO_BORDER);
        celda30.add(new Paragraph(dataExport.get(22) + "% de " + dataExport.get(40)));
        celda30.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda30);

        com.itextpdf.layout.element.Cell celda31 = new com.itextpdf.layout.element.Cell();
        celda31.setBorder(Border.NO_BORDER);
        celda31.add(new Paragraph(""));
        celda31.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda31);

        com.itextpdf.layout.element.Cell celda32 = new com.itextpdf.layout.element.Cell();
        celda32.setBorder(Border.NO_BORDER);
        celda32.add(new Paragraph(dataExport.get(23)));
        celda32.setTextAlignment(TextAlignment.CENTER);
        tablaSalario.addCell(celda32);

        document.add(tablaSalario);
        document.add(barra);

        float[] pointColumnWidths2 = {500F, 500F};
        com.itextpdf.layout.element.Table tablatotal = new com.itextpdf.layout.element.Table(pointColumnWidths2);
        tablatotal.setBorder(Border.NO_BORDER);
        tablatotal.setPaddingTop(1);
        tablatotal.setPaddingBottom(1);
        tablatotal.setPaddingLeft(20);

        tablatotal.setFontSize(8);

        com.itextpdf.layout.element.Cell celda33 = new com.itextpdf.layout.element.Cell();
        celda33.setBorder(Border.NO_BORDER);
        celda33.setPaddingLeft(20);
        celda33.add(new Paragraph("Total deducciones"));
        celda33.setTextAlignment(TextAlignment.LEFT);
        tablatotal.addCell(celda33);

        com.itextpdf.layout.element.Cell celda34 = new com.itextpdf.layout.element.Cell();
        celda34.setBorder(Border.NO_BORDER);
        celda34.setPaddingRight(55);
        celda34.add(new Paragraph(dataExport.get(24)));
        celda34.setTextAlignment(TextAlignment.RIGHT);
        tablatotal.addCell(celda34);

        com.itextpdf.layout.element.Cell celda35 = new com.itextpdf.layout.element.Cell();
        celda35.setBorder(Border.NO_BORDER);
        celda35.setPaddingLeft(20);
        celda35.add(new Paragraph("Total devengos"));
        celda35.setTextAlignment(TextAlignment.LEFT);
        tablatotal.addCell(celda35);

        com.itextpdf.layout.element.Cell celda36 = new com.itextpdf.layout.element.Cell();
        celda36.setBorder(Border.NO_BORDER);
        celda36.setPaddingRight(185);
        celda36.add(new Paragraph(dataExport.get(25)));
        celda36.setTextAlignment(TextAlignment.RIGHT);
        tablatotal.addCell(celda36);

        com.itextpdf.layout.element.Cell celda37 = new com.itextpdf.layout.element.Cell();
        celda37.setBorder(Border.NO_BORDER);
        celda37.setPaddingLeft(200);
        celda37.add(new Paragraph("Líquido a percibir"));
        celda37.setTextAlignment(TextAlignment.LEFT);
        tablatotal.addCell(celda37);

        com.itextpdf.layout.element.Cell celda38 = new com.itextpdf.layout.element.Cell();
        celda38.setBorder(Border.NO_BORDER);
        celda38.setPaddingRight(55);
        celda38.add(new Paragraph(dataExport.get(26)));
        celda38.setTextAlignment(TextAlignment.RIGHT);
        tablatotal.addCell(celda38);

        document.add(tablatotal);
        document.add(new Paragraph(""));

        com.itextpdf.layout.element.Table tablaBaseempresario = new com.itextpdf.layout.element.Table(2);
        tablaBaseempresario.setWidth(470);
        tablaBaseempresario.setPaddingTop(1);
        tablaBaseempresario.setPaddingBottom(1);
        tablaBaseempresario.setPaddingLeft(155);
        tablaBaseempresario.setPaddingRight(15);

        com.itextpdf.kernel.colors.Color colorgris = new DeviceRgb(68, 75, 87);
        tablaBaseempresario.setBorder(new SolidBorder(colorgris, 1));
        tablaBaseempresario.setFontColor(colorgris);
        tablaBaseempresario.setFontSize(8);

        com.itextpdf.layout.element.Cell celda39 = new com.itextpdf.layout.element.Cell();
        celda39.setBorder(Border.NO_BORDER);
        // celda39.setPaddingLeft(10);
        celda39.add(new Paragraph("Calculor empresario: BASE"));
        celda39.setTextAlignment(TextAlignment.LEFT);
        tablaBaseempresario.addCell(celda39);

        com.itextpdf.layout.element.Cell celda40 = new com.itextpdf.layout.element.Cell();
        celda40.setBorder(Border.NO_BORDER);
        celda40.add(new Paragraph(dataExport.get(27)));
        celda40.setTextAlignment(TextAlignment.RIGHT);
        tablaBaseempresario.addCell(celda40);

        document.add(tablaBaseempresario);

        float[] pointColumnWidths4 = {240F, 230F};
        com.itextpdf.layout.element.Table tablaempresario = new com.itextpdf.layout.element.Table(pointColumnWidths4);
        tablaempresario.setBorder(Border.NO_BORDER);
        tablaempresario.setPaddingLeft(20);
        tablaempresario.setPaddingRight(20);
        tablaempresario.setFontColor(colorgris);

        tablaempresario.setFontSize(8);

        /////////////////////////////////////Contingencias//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda41 = new com.itextpdf.layout.element.Cell();
        celda41.setBorder(Border.NO_BORDER);
        celda41.setPaddingLeft(20);
        celda41.add(new Paragraph("Contingencias comunes empresario " + dataExport.get(28) + "%"));
        celda41.setTextAlignment(TextAlignment.LEFT);
        tablaempresario.addCell(celda41);

        com.itextpdf.layout.element.Cell celda42 = new com.itextpdf.layout.element.Cell();
        celda42.setBorder(Border.NO_BORDER);
        celda42.add(new Paragraph(dataExport.get(29)));
        celda42.setTextAlignment(TextAlignment.RIGHT);
        tablaempresario.addCell(celda42);
        /////////////////////////////////////Desempleo//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda43 = new com.itextpdf.layout.element.Cell();
        celda43.setBorder(Border.NO_BORDER);
        celda43.setPaddingLeft(20);
        celda43.add(new Paragraph("Desempleo " + dataExport.get(30) + "%"));
        celda43.setTextAlignment(TextAlignment.LEFT);
        tablaempresario.addCell(celda43);

        com.itextpdf.layout.element.Cell celda44 = new com.itextpdf.layout.element.Cell();
        celda44.setBorder(Border.NO_BORDER);
        celda44.add(new Paragraph(dataExport.get(31)));
        celda44.setTextAlignment(TextAlignment.RIGHT);
        tablaempresario.addCell(celda44);
        /////////////////////////////////////Formacion//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda45 = new com.itextpdf.layout.element.Cell();
        celda45.setBorder(Border.NO_BORDER);
        celda45.setPaddingLeft(20);
        celda45.add(new Paragraph("Formacion " + dataExport.get(32) + "%"));
        celda45.setTextAlignment(TextAlignment.LEFT);
        tablaempresario.addCell(celda45);

        com.itextpdf.layout.element.Cell celda46 = new com.itextpdf.layout.element.Cell();
        celda46.setBorder(Border.NO_BORDER);
        celda46.add(new Paragraph(dataExport.get(33)));
        celda46.setTextAlignment(TextAlignment.RIGHT);
        tablaempresario.addCell(celda46);
        /////////////////////////////////////Accidentes de trabajo//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda47 = new com.itextpdf.layout.element.Cell();
        celda47.setBorder(Border.NO_BORDER);
        celda47.setPaddingLeft(20);
        celda47.add(new Paragraph("Accidentes de trabajo " + dataExport.get(34) + "%"));
        celda47.setTextAlignment(TextAlignment.LEFT);
        tablaempresario.addCell(celda47);

        com.itextpdf.layout.element.Cell celda48 = new com.itextpdf.layout.element.Cell();
        celda48.setBorder(Border.NO_BORDER);
        celda48.add(new Paragraph(dataExport.get(35)));
        celda48.setTextAlignment(TextAlignment.RIGHT);
        tablaempresario.addCell(celda48);
        /////////////////////////////////////FOGASA//////////////////////////////////////////
        com.itextpdf.layout.element.Cell celda49 = new com.itextpdf.layout.element.Cell();
        celda49.setBorder(Border.NO_BORDER);
        celda49.setPaddingLeft(20);
        celda49.add(new Paragraph("FOGASA " + dataExport.get(36) + "%"));
        celda49.setTextAlignment(TextAlignment.LEFT);
        tablaempresario.addCell(celda49);

        com.itextpdf.layout.element.Cell celda50 = new com.itextpdf.layout.element.Cell();
        celda50.setBorder(Border.NO_BORDER);
        celda50.add(new Paragraph(dataExport.get(37)));
        celda50.setTextAlignment(TextAlignment.RIGHT);
        tablaempresario.addCell(celda50);

        document.add(tablaempresario);
        document.add(barra);

        com.itextpdf.layout.element.Table tablatotalEmpresario = new com.itextpdf.layout.element.Table(2);
        tablatotalEmpresario.setWidth(470);
        tablatotalEmpresario.setPaddingTop(1);
        tablatotalEmpresario.setPaddingBottom(1);
        tablatotalEmpresario.setPaddingLeft(155);
        tablatotalEmpresario.setPaddingRight(15);

        tablatotalEmpresario.setFontColor(colorgris);
        tablatotalEmpresario.setFontSize(8);

        com.itextpdf.layout.element.Cell celda51 = new com.itextpdf.layout.element.Cell();
        celda51.setBorder(Border.NO_BORDER);

        celda51.add(new Paragraph("Total empresario"));
        celda51.setTextAlignment(TextAlignment.LEFT);
        tablatotalEmpresario.addCell(celda51);

        com.itextpdf.layout.element.Cell celda52 = new com.itextpdf.layout.element.Cell();
        celda52.setBorder(Border.NO_BORDER);
        celda52.add(new Paragraph(dataExport.get(38)));
        celda52.setTextAlignment(TextAlignment.RIGHT);
        tablatotalEmpresario.addCell(celda52);

        document.add(tablatotalEmpresario);
        document.add(new Paragraph(""));

        com.itextpdf.layout.element.Table tablacostetotal = new com.itextpdf.layout.element.Table(2);
        tablacostetotal.setWidth(500);
        tablacostetotal.setPaddingTop(1);
        tablacostetotal.setPaddingBottom(1);
        tablacostetotal.setPaddingLeft(30);
        tablacostetotal.setPaddingRight(15);

        com.itextpdf.kernel.colors.Color colorRojo = new DeviceRgb(255, 0, 0);
        tablacostetotal.setBorder(new SolidBorder(3));
        tablacostetotal.setFontColor(colorRojo);
        tablacostetotal.setFontSize(12);

        com.itextpdf.layout.element.Cell celda53 = new com.itextpdf.layout.element.Cell();
        celda53.setBorder(Border.NO_BORDER);

        celda53.add(new Paragraph("COSTE TOTAL TRABAJADOR:"));
        celda53.setTextAlignment(TextAlignment.LEFT);
        tablacostetotal.addCell(celda53);

        com.itextpdf.layout.element.Cell celda54 = new com.itextpdf.layout.element.Cell();
        celda54.setBorder(Border.NO_BORDER);
        celda54.add(new Paragraph(dataExport.get(39)));
        celda54.setTextAlignment(TextAlignment.RIGHT);
        tablacostetotal.addCell(celda54);

        document.add(tablacostetotal);

        document.close();

    }

    public void imprimirNómina(ArrayList<String> dataExport) {

        System.out.println("////////////////////////////////////////////////////////////////////////////");
        System.out.println("Empleado:  " + dataExport.get(6) + " DNI :" + dataExport.get(7));//nombre y apellidos  6

        System.out.print("Nombre de la empresa: " + dataExport.get(0));//nombre empresa    0
        System.out.println(" Con CIF : " + dataExport.get(1));//CIF empresa       1
        System.out.print("Categoria: " + dataExport.get(2));//Categoria         2
        System.out.print(" BRUTO ANUAL: " + dataExport.get(3));//Bruto                                 3
        System.out.println(" Fecha de alta: " + dataExport.get(4));//fecha de alta     4
        System.out.println("IBAN: " + dataExport.get(5));//IBAN             5
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("--------------------------NOMINA: " + dataExport.get(8) + "---------------------");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Conceptos            Cantidad         Imp.Unitario        Devengo         Dedución");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Salario Base:              " + dataExport.get(9) + "                            " + dataExport.get(10));
        System.out.println("Prorrateo:                 " + dataExport.get(9) + "                            " + dataExport.get(11));
        System.out.println("Complemento:               " + dataExport.get(9) + "                            " + dataExport.get(12));
        System.out.println("Antigüedad:                " + dataExport.get(13) + "                             " + dataExport.get(14));
        System.out.println("Contingencias generales:   " + dataExport.get(16) + "% de " + dataExport.get(15) + "                                   " + dataExport.get(17));
        System.out.println("Desempleo:                 " + dataExport.get(18) + "% de " + dataExport.get(15) + "                                   " + dataExport.get(19));
        System.out.println("Cuota Formacion            " + dataExport.get(20) + "% de " + dataExport.get(15) + "                                   " + dataExport.get(21));
        System.out.println("IRPF                       " + dataExport.get(22) + "% de " + dataExport.get(40) + "                                   " + dataExport.get(23));
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Total deducciones                                                            " + dataExport.get(24));
        System.out.println("Total devengos                                           " + dataExport.get(25));
        System.out.println("                            Líquido a percibir                               " + dataExport.get(26));
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Calculo empresario: BASE                                                    " + dataExport.get(27));
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Contingencias comunes empresario " + dataExport.get(28) + "%                                      " + dataExport.get(29));
        System.out.println("Desempleo " + dataExport.get(30) + "%                                                               " + dataExport.get(31));
        System.out.println("Formacion " + dataExport.get(32) + "%                                                               " + dataExport.get(33));
        System.out.println("Accidentes de trabajo   " + dataExport.get(34) + "%                                                 " + dataExport.get(35));
        System.out.println("FOGASA   " + dataExport.get(36) + "%                                                                " + dataExport.get(37));
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Total empresario:                                                           " + dataExport.get(38));
        System.out.println("------------------------------------------------------------------------------------");

        System.out.println("COSTE TOTAL TRABAJADOR                                                     " + dataExport.get(39));
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("");

    }

    public ArrayList<String> extra(ArrayList<String> dataExport, double mesesExtra) {

        String nuevoNombre = "Extra de " + dataExport.get(8);
        dataExport.set(8, nuevoNombre);//Fecha 8
        dataExport.set(10, "" + ((Double.parseDouble(dataExport.get(10))) / 6.0) * mesesExtra);
        dataExport.set(12, "" + ((Double.parseDouble(dataExport.get(12))) / 6.0) * mesesExtra);
        dataExport.set(15, "00.00");

        dataExport.set(17, "00.00");
        dataExport.set(19, "00.00");

        dataExport.set(21, "00.00");
        dataExport.set(23, "" + ((Double.parseDouble(dataExport.get(23))) / 6.0) * mesesExtra);
        dataExport.set(24, dataExport.get(23));//IRPF  23
        dataExport.set(25, "" + ((Double.parseDouble(dataExport.get(25))) / 6.0) * mesesExtra);
        double aux2 = (Double.parseDouble(dataExport.get(25)) - Double.parseDouble(dataExport.get(24)));
        dataExport.set(26, "" + aux2);

        dataExport.set(27, "00.00");

        dataExport.set(29, "00.00");

        dataExport.set(31, "00.00");

        dataExport.set(33, "00.00");

        dataExport.set(35, "00.00");

        dataExport.set(37, "00.00");

        dataExport.set(38, "00.00");
        dataExport.set(40, "" + ((Double.parseDouble(dataExport.get(40))) / 6.0) * mesesExtra);
        dataExport.set(39, dataExport.get(40));
        dataExport = rounded(dataExport);
        //imprimirNómina(dataExport);
        try {
            WritePDF(dataExport, true);
        } catch (Exception FileNotFoundException) {
            System.out.println("ERROR AL PASAR A PDF");
        }
        return dataExport;

    }

    public ArrayList<String> rounded(ArrayList<String> dataExport) {
        dataExport.set(3, "" + df2.format(Double.parseDouble(dataExport.get(3))));
        dataExport.set(10, "" + df2.format(Double.parseDouble(dataExport.get(10))));
        dataExport.set(11, "" + df2.format(Double.parseDouble(dataExport.get(11))));
        dataExport.set(12, "" + df2.format(Double.parseDouble(dataExport.get(12))));
        dataExport.set(14, "" + df2.format(Double.parseDouble(dataExport.get(14))));
        dataExport.set(15, "" + df2.format(Double.parseDouble(dataExport.get(15))));
        dataExport.set(17, "" + df2.format(Double.parseDouble(dataExport.get(17))));
        dataExport.set(19, "" + df2.format(Double.parseDouble(dataExport.get(19))));
        dataExport.set(21, "" + df2.format(Double.parseDouble(dataExport.get(21))));
        dataExport.set(23, "" + df2.format(Double.parseDouble(dataExport.get(23))));
        dataExport.set(24, "" + df2.format(Double.parseDouble(dataExport.get(24))));
        dataExport.set(25, "" + df2.format(Double.parseDouble(dataExport.get(25))));
        dataExport.set(26, "" + df2.format(Double.parseDouble(dataExport.get(26))));
        dataExport.set(27, "" + df2.format(Double.parseDouble(dataExport.get(27))));
        dataExport.set(29, "" + df2.format(Double.parseDouble(dataExport.get(29))));
        dataExport.set(31, "" + df2.format(Double.parseDouble(dataExport.get(31))));
        dataExport.set(33, "" + df2.format(Double.parseDouble(dataExport.get(33))));
        dataExport.set(35, "" + df2.format(Double.parseDouble(dataExport.get(35))));
        dataExport.set(37, "" + df2.format(Double.parseDouble(dataExport.get(37))));
        dataExport.set(38, "" + df2.format(Double.parseDouble(dataExport.get(38))));
        dataExport.set(39, "" + df2.format(Double.parseDouble(dataExport.get(39))));
        dataExport.set(40, "" + df2.format(Double.parseDouble(dataExport.get(40))));

        return dataExport;
    }

    public String arreglarFecha(String fecha) {

        String ret = "";
        String[] parts = fecha.split("/");
        if (Integer.parseInt(parts[1]) < 10) {
            ret += "0" + parts[1] + "/";

        } else {
            ret += parts[1] + "/";

        }
        if (Integer.parseInt(parts[0]) < 10) {
            ret += "0" + parts[0] + "/";

        } else {
            ret += parts[0] + "/";

        }
        if (Integer.parseInt(parts[2]) < 22) {
            ret += "20" + parts[2];

        } else {
            ret += "19" + parts[2];

        }

        return ret;

    }

    public String nombreMes(String month) {
        String mesString;
        //TODO COMPROBAR SI HACE FALTA EL 0
        switch (Integer.parseInt(month)) {
            case 1:
                mesString = "Enero";
                break;
            case 2:
                mesString = "Febrero";
                break;
            case 3:
                mesString = "Marzo";
                break;
            case 4:
                mesString = "Abril";
                break;
            case 5:
                mesString = "Mayo";
                break;
            case 6:
                mesString = "Junio";
                break;
            case 7:
                mesString = "Julio";
                break;
            case 8:
                mesString = "Agosto";
                break;
            case 9:
                mesString = "Septiembre";
                break;
            case 10:
                mesString = "Octubre";
                break;
            case 11:
                mesString = "Noviembre";
                break;
            case 12:
                mesString = "Diciembre";
                break;
            default:
                mesString = "Invalid month";
                break;
        }
        return mesString;
    }

}
