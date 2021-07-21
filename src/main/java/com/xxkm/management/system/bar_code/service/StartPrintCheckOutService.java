package com.xxkm.management.system.bar_code.service;

import com.xxkm.management.system.bar_code.service.impl.StartPrintCheckOut;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.print.*;
import java.util.ArrayList;
import java.util.Date;


public interface StartPrintCheckOutService extends Printable {




    /*

     * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，

     * 1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号

     */

    public int print(Graphics g, PageFormat pf, int page)

            throws PrinterException;



    /* 打印指定页号的具体文本内容 */

    public void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) ;




    /* 将打印目标文本按页存放为字符串数组 */

    public ArrayList[] getDrawText(ArrayList list) ;


    public void printText2Action(ArrayList list) ;

}
