package com.xxkm.management.system.bar_code.service.impl;

import com.xxkm.management.system.bar_code.service.StartPrintCheckOutService;
import org.springframework.stereotype.Service;

import java.awt.Color;

import java.awt.Font;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import java.awt.print.Book;

import java.awt.print.PageFormat;

import java.awt.print.Paper;

import java.awt.print.Printable;

import java.awt.print.PrinterException;

import java.awt.print.PrinterJob;

import java.util.ArrayList;

import java.util.Date;
import java.util.Map;

import javax.print.PrintService;

import javax.print.PrintServiceLookup;

@Service
public class StartPrintCheckOut implements StartPrintCheckOutService {


    public int PAGES;

    //public String printStr = "";

    //public Area area;

    ArrayList rslist;

    public int printFlag = 0;

    //private double Y = 0.0;



    /*

     * Graphic指明打印的图形环境；PageFormat指明打印页格式（页面大小以点为计量单位，

     * 1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）；page指明页号

     */

    public int print(Graphics g, PageFormat pf, int page)

            throws PrinterException {


        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.black); // 设置打印颜色为黑色


        if (page >= PAGES) // 当打印页号大于需要打印的总页数时，打印工作结束

            return Printable.NO_SUCH_PAGE;

        g2.translate(pf.getImageableX(), pf.getImageableY());// 转换坐标，确定打印边界

        drawCurrentPageText(g2, pf, page); // 打印当前页文本


        return Printable.PAGE_EXISTS; // 存在打印页时，继续打印工作

    }



    /* 打印指定页号的具体文本内容 */

    public void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {

        System.out.println(pf.getPaper().getHeight() + "   "

                + pf.getPaper().getWidth());

        ArrayList list = getDrawText(rslist)[page];// 获取当前页的待打印文本内容

        //CheckOutVo vo = (CheckOutVo) list.get(0);

        // 获取默认字体及相应的尺寸


        Font f = new Font("新宋体", Font.PLAIN, 15);

        Font f1 = new Font("新宋体", Font.PLAIN, 13);

        Font f2 = new Font("黑体", Font.PLAIN, 8);

        Font f4 = new Font("新宋体", Font.PLAIN, 10);

        g2.setFont(f1);

        int height = 50;

        System.out.println("list_size=" + list.size());

        g2.setFont(f4);

        g2.rotate(Math.PI, pf.getWidth()/2, pf.getHeight()/2);//以A4纸的中心旋转360度（取PageFormat宽和高的一半得到旋转中心点）

        for (int j = 0; j < list.size(); j++) {

            //CheckOutVo vo1 = (CheckOutVo) list.get(j);

            //g2.drawString(vo1.getMenu_name() + "", 2, height - 5);

            Map<String, Object> vo1= (Map<String, Object>) list.get(j);

            height += 5;

            g2.drawString(vo1.get("entity_name") + "", 10, height );

            g2.drawString(vo1.get("entity_type") + "", 65, height );

            g2.drawString(vo1.get("office_name") + "", 115, height );

            height += 15;

            System.out.println("height=" + height);

            //sum += vo1.getPrice_sum();

        }

        //g2.drawLine((int)(90.00 + (list.size() * 35)), 35, 135, 35);

        //Y+=20;

        System.out.println("最后打印的高度 height : " + height);

    }



    /* 将打印目标文本按页存放为字符串数组 */

    public ArrayList[] getDrawText(ArrayList list) {

        ArrayList[] mtlist = new ArrayList[PAGES];// 根据页数初始化数组

        mtlist[0] = list;

        return mtlist;

    }

    public void printText2Action(ArrayList list) {

        printFlag = 0; // 打印标志清零


        if (list.size() > 0) // 当打印内容不为空时

        {

            this.rslist = list;

            System.out.println("开始打印..............");

            System.out.println("本次打印数为：" + list.size());


            PAGES = 1; // 获取打印总页数

            // 通俗理解就是书、文档

            Book book = new Book();

            // 设置成竖打

            PageFormat pf = new PageFormat();

            pf.setOrientation(PageFormat.PORTRAIT);

            // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。

            Paper p = new Paper();


            p.setSize(595, 842);// 纸张大小A4为595×842

            System.out.println("print paper Height : " + p.getHeight());

            p.setImageableArea(0, 0, 595, 842);// A4(595 X

            // 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72

            pf.setPaper(p);

            // 把 PageFormat 和 Printable 添加到书中，组成一个页面

            book.append(this, pf, PAGES);


           /* CheckOutVo vo = (CheckOutVo) rslist.get(0);


            int printid = vo.getIsprintall();*/

            PrintService[] services = PrintServiceLookup.lookupPrintServices(

                    null, null);


            String printname="Microsoft XPS Document Writer";

            //printname="Canon iR2525/2530 UFRII LT";

            //CheckOutDao dao = new CheckOutDao();

            //String printname = dao.selectprintname(printid);

            PrintService services1 = null;

            System.out.println("length:=" + services.length);

            for (int i = 0; i < services.length; i++) {

                System.out.println("name=" + services[i].getName());

                if (printname.equals(services[i].getName())) {

                    services1 = services[i];

                    System.out.println("正在运行的打印机名称：" + printname);

                }

            }


            // 获取打印服务对象

            PrinterJob job = PrinterJob.getPrinterJob();


            // 设置打印类


            job.setPageable(book);

            System.out.println("print pf.getHeight() : " + pf.getHeight());


            try {

                System.out.println("service:" + services1);

                job.setPrintService(services1);

            } catch (PrinterException e1) {

                // TODO Auto-generated catch block

                e1.printStackTrace();

            }


            try {

                // 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印


                job.print();


            } catch (PrinterException e) {

                e.printStackTrace();

            }

        } else {

            // 如果打印内容为空时，提示用户打印将取消

            // JOptionPane.showConfirmDialog(null,

            // "Sorry, Printer Job is Empty, Print Cancelled!",

            // "Empty", JOptionPane.DEFAULT_OPTION,

            // JOptionPane.WARNING_MESSAGE);

            // System.out.println("打印内容为空！");

        }

    }


}
