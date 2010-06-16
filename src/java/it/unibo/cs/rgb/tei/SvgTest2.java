/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibo.cs.rgb.tei;
import java.io.*;
/**
 *
 * @author gine
 */
public class SvgTest2 {

       public static void main(String[] args){
           try {
               FileWriter outFile = new FileWriter("/home/gine/Desktop/svg1.xml");
              PrintWriter out = new PrintWriter(outFile);

              int rCircle = 40, nFontSize = 20, gFontSize =26;
              String nColourStroke = "#83adf7", gColourStroke = "red";
              String littleLetters="filtjr", mediumLetters="abcdeghnopqzsuvkzy0123456789", bigLetters="mw", gLetters="αβγδεζηθικλμνξοπρςτυφχψω";
              int xLitWordLoc =-1, xMedWordLoc =-5, xBigWordLoc =-7, xGWordLoc = -7, xWordLoc = 0;
              int yWordLoc = 5, yGWordLoc = 7;
              /* */
              //int xCirclePos=0, yCirclePos=0,xTextPos=0,yTextPos=0,xFirstPLinePos=0,yFirstPLinePos=0,xSecondPLinePos=0,ySecondPLinePos=0;
              int xCirclePos=100, yCirclePos=50;//,xFirstPLinePos=0,yFirstPLinePos=0,xSecondPLinePos=0,ySecondPLinePos=0;
              String id ="vva";
              /**/
              out.println("<html xmlns:svg=\"http://www.w3.org/2000/svg\">");
              out.println("<body>");
              out.println("<svg:svg width=\"1000\" height=\"1000\" version=\"1.1\" >");
              //TODO:if serio
              if (false) {
                xWordLoc=xGWordLoc;
                yWordLoc=yGWordLoc;
              } else {
                xWordLoc += howMuchMove(littleLetters,xLitWordLoc,id);
                xWordLoc += howMuchMove(mediumLetters,xMedWordLoc,id);
                xWordLoc += howMuchMove(bigLetters,xBigWordLoc,id);
              }

              int fontSize = 0;
              String colourStroke;
              //TODO: if serio
              if (true) {
                fontSize=nFontSize;
                colourStroke=nColourStroke;
              } else {
                fontSize=gFontSize;
                colourStroke=gColourStroke;
              }
              int xIdPos=xCirclePos+xWordLoc, yIdPos=yCirclePos+yWordLoc;
              String circleStyle="style=\"fill:none;fill-opacity:1;fill-rule:nonzero;stroke:"+colourStroke+";stroke-width:3;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-dashoffset:0;stroke-opacity:1",
                       textStyle="style=\"font-size:"+fontSize+"px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;fill:black;fill-opacity:1;stroke:none;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;font-family:Arial",
                       lineStyle="style=\"fill:none;fill-rule:evenodd;stroke:"+nColourStroke+";stroke-width:2.93415856;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1";

              String text="<svg:text id=\""+id+"\" x=\""+xIdPos+"\" y=\""+yIdPos+"\"  " + textStyle + "\">"+ id +"</svg:text>";
              String circle="<svg:circle id=\""+id+"\" cx=\""+xCirclePos+"\" cy=\""+yCirclePos+"\" r=\""+rCircle+"\" "+circleStyle+"\" />";//,
                     //line="<svg:line id=\""+id+"\" x1=\""+xFirstPLinePos+"\" y1=\""+yFirstPLinePos+"\" x2=\""+xSecondPLinePos+"\" y2=\""+ySecondPLinePos+"\" " +lineStyle+"\"/>";
        
              // Also could be written as follows on one line
              // Printwriter out = new PrintWriter(new FileWriter(args[0]));

              // Write text to file
//                          out.println("");

            out.println(circle);
            out.println(text);
            //out.println(line);
           out.println("</svg:svg>");
           out.println("</body>");
            out.println("</html>");
              out.close();
          } catch (IOException e){
              e.printStackTrace();
          }
      }

       public static int howMuchMove(String letters, int x, String id){
        int ret = 0;
        //System.out.print(letters + ": x:" + x + "id:" +id);
        for (int i=0; i<id.length();i++){
            if(letters.indexOf(id.charAt(i)) > -1 ) {
                    ret += x  ;
            }
            
        }
       
        return ret;
       }
  }
