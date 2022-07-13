
package calculadora;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
/**
 * 
 * @author Israel Pérez
 * La imagen calc.jpg debe estar en la misma carpeta que el código
 * 
 */

public class Calculadora {
    
    
    public static void main(String[] args) {
        
        Marco marco = new Marco();
    }
    
}
/**
 * 
 * Marco principal de la app
 */

class Marco extends JFrame implements ActionListener{
    
    JFrame marco = new JFrame();
    JPanel ps = new JPanel();
    PanelIzquierdo pi = new PanelIzquierdo();
    PanelInferior pin = new PanelInferior();
    PanelCentral pc = new PanelCentral();
    PanelDerecho pd = new PanelDerecho();
    
    public Marco(){
    
        iniciarComponentes();
        
    }
    
    public void iniciarComponentes(){
       
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        marco.setSize(500,300);
        int y = (int)(pantalla.getHeight() - marco.getHeight())/2;
        int x = (int)(pantalla.getWidth() - marco.getWidth())/2;
        marco.setLocation(x, y);
        marco.setTitle("Calculadora");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.getContentPane().setLayout(new BorderLayout());
        ps.setPreferredSize(new Dimension(500,100));
        ps.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("calculadora/calc.jpg"))));
        marco.getContentPane().add(ps, BorderLayout.NORTH);
        marco.getContentPane().add(pi, BorderLayout.WEST);
        marco.getContentPane().add(pin, BorderLayout.SOUTH);
        marco.getContentPane().add(pc, BorderLayout.CENTER);
        marco.getContentPane().add(pd, BorderLayout.EAST);
        inicializarBotones();
        marco.setVisible(true);
        
    }
    
    private void inicializarBotones() {
        for (int i = 0; i < pc.botones.length; i++) {
            pc.botones[i].addActionListener(pin);
        }
        pc.botones[10].setEnabled(false);
        pd.dec.addActionListener(this);
        pd.dec.addActionListener(pin);
        pi.suma.addActionListener(pin);
        pi.resta.addActionListener(pin);
        pi.producto.addActionListener(pin);
        pi.division.addActionListener(pin);
        pd.result.addActionListener(pin);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JCheckBox){
            if(pc.botones[10].isEnabled()){
                pc.botones[10].setEnabled(false);
            } else {
                pc.botones[10].setEnabled(true);
            }
        }
    }
}

/**
 * Panel izquierdo
 * 
 */

class PanelIzquierdo extends JPanel{
    
    JRadioButton suma = new JRadioButton("Suma", true);
    JRadioButton resta = new JRadioButton("Resta");
    JRadioButton producto = new JRadioButton("Producto");
    JRadioButton division = new JRadioButton("División");
    ButtonGroup grupoOperadores = new ButtonGroup();

    public PanelIzquierdo(){
        this.setLayout(new GridLayout(0,1));
        this.setPreferredSize(new Dimension(80,200));
        grupoOperadores.add(suma);
        grupoOperadores.add(resta);
        grupoOperadores.add(producto);
        grupoOperadores.add(division);
        this.add(suma);
        this.add(resta);
        this.add(producto);
        this.add(division);
    }
}

/**
 * Panel inferior, 
 * 
 */
class PanelInferior extends JPanel implements ActionListener{
    
    private JLabel etiOperando1 = new JLabel("");
    private JLabel etiOperador = new JLabel();
    private JLabel etiOperando2 = new JLabel();
    private Logica logica = new Logica(); //Control del estado de la app
    private boolean decimal = false; // control del decimal
    
    public PanelInferior(){
        this.setBorder(BorderFactory.createTitledBorder("Resultado"));
        this.add(etiOperando1);
        this.add(etiOperador);
        this.add(etiOperando2);
        this.setPreferredSize(new Dimension(500,50));
        this.setBackground(Color.white);
        
    }
    /**
     * Resetea las etiquetas y el estado
     */
    public void resetearEtiquetas(){
        
        this.etiOperando1.setText("");
        this.etiOperador.setText("");
        this.etiOperando2.setText("");
        logica.setEstado(0);
    
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object botonPulsado = ae.getSource();
        // se chequea que botón se pulsa
        if(botonPulsado instanceof JButton){
            String boton = ((JButton)botonPulsado).getText();
            if(boton.equals("Reset")){
                this.resetearEtiquetas();
            }else if(boton.equals("Resultado") && logica.getEstado() == 2
                    && this.etiOperando2.getText()!=""){
                logica.setOper1(Double.parseDouble(this.etiOperando1.getText()));
                logica.setOper2(Double.parseDouble(this.etiOperando2.getText()));
                if(decimal){
                    double resultado = logica.operar(this.etiOperador.getText());
                    this.resetearEtiquetas();
                    this.etiOperando1.setText(Double.toString(resultado));
                    logica.setEstado(3);
                } else {
                    int resultado = (int)logica.operar(this.etiOperador.getText());
                    this.resetearEtiquetas();
                    this.etiOperando1.setText(Integer.toString(resultado));
                    logica.setEstado(3);
                }
            // según el estado de la aplicación los botones operan de manera diferente    
            } else if(!boton.equals("Resultado")) {
                switch(logica.getEstado()){
                    case 0:
                        if(!boton.equals(".")){
                            this.etiOperando1.setText("");
                            this.etiOperando1.setText(this.etiOperando1.getText()+boton);
                            this.logica.setEstado(1);
                        } else {
                            this.etiOperando1.setText("0");
                            this.etiOperando1.setText(this.etiOperando1.getText()+boton);
                            this.logica.setEstado(1);
                        }
                        break;
                    case 1:
                        if(boton.equals(".") && this.etiOperando1.getText().contains(".")){
                            break;
                        }
                        this.etiOperando1.setText(this.etiOperando1.getText()+boton);
                        break;
                    case 2:
                         if(boton.equals(".") && this.etiOperando2.getText().contains(".")){
                            break;
                        }
                        this.etiOperando2.setText(this.etiOperando2.getText()+boton);
                        break;
                    case 3:
                        this.etiOperando1.setText("");
                        this.etiOperando1.setText(this.etiOperando1.getText()+boton);
                        logica.setEstado(1);
                        break;
                    default:
                        break;
                }      
            }
        // check del tipo de operación
        }else if(ae.getSource() instanceof JRadioButton && (logica.getEstado() == 1 
                || logica.getEstado()== 3 || logica.getEstado()== 2) ){
            String boton = ((JRadioButton)botonPulsado).getText();
            if(this.etiOperando2.getText()==""){
                switch(boton){
                case "Suma":
                    logica.setEstado(2);
                    
                    this.etiOperador.setText("+");
                    break;
                case "Resta":
                    logica.setEstado(2);
                    this.etiOperador.setText("-");
                    break;
                case "Producto":
                    logica.setEstado(2);
                    this.etiOperador.setText("*");
                    break;
                case "División":
                    logica.setEstado(2);
                    this.etiOperador.setText("/");
                    break;
                default:
                    break;
                 }
            }
        // check encargado de verificar si está en modo decimal            
        } else if(ae.getSource() instanceof JCheckBox){ 
            
            JCheckBox x = ((JCheckBox)ae.getSource());
            decimal = x.isSelected();
        }   
        
    }

}

/**
 * Panel Central
 * Los botones están generados por un for
 */
class PanelCentral extends JPanel{
    
    JButton[] botones = new JButton[12];
    
    public PanelCentral(){
        for (int i = 0; i < botones.length; i++) {
            
            if(i<9){
                botones[i] = new JButton((Integer.toString(i+1)));
            } else if (i == 10){
                botones[i] = new JButton(".");
            } else if (i == 9){
                botones[i] = new JButton("0");
            } else{
                botones[i] = new JButton("Reset");
            }
        }
        this.setLayout(new GridLayout(4,3));
        this.setPreferredSize(new Dimension(320,200));
        for(JButton b: botones){
        
            this.add(b);
        }
    }
}

/**
 * Panel Derecho
 * 
 */
class PanelDerecho extends JPanel{
    JCheckBox dec = new JCheckBox("Decimales");
    JButton result = new JButton("Resultado");
    
    public PanelDerecho(){
        this.setLayout(new GridLayout(0,1));
        result.setPreferredSize(new Dimension(100,60));
        this.setPreferredSize(new Dimension(100,200));
        this.add(dec);
        this.add(result);
    }
}

/**
 * Clase encargada de realizar las operaciones
 * 
 */
class Logica{
    
    private int estado; // Controla el estado de la calculadora
    private double oper1; 
    private double oper2;
    
    //Constructor
    public Logica(){
    
        this.estado = 0;
    }
    
    //Setters
    public void setOper1(double oper1) {
        this.oper1 = oper1;
    }

    public void setOper2(double oper2) {
        this.oper2 = oper2;
    }
    
    public void setEstado(int est){
        this.estado = est;
    }
    
    //Getters
    public int getEstado(){
        return this.estado;
    }
    
    
    /**
     * 
     * @param operacion recibe el tipo de operación
     * @return devuelve el resultado de la operación
     */
    public double operar(String operacion){
        double resultado = 0;
        switch(operacion){
            case "+":
                resultado = this.oper1 + this.oper2;
                break;
            case "-":
                resultado = this.oper1 - this.oper2;
                break;
            case "*":
                resultado = this.oper1 * this.oper2;
                break;
            case "/":
                if(this.oper2 != 0){
                    resultado = this.oper1 / this.oper2;
                } else {
                    JOptionPane.showMessageDialog(null, "No se puede dividir por cero.", 
                    "División por cero", JOptionPane.ERROR_MESSAGE);
                    
                }
                
                break;
            default:
                break;
        }
        return resultado;
    }
}


