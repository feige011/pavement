

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ExcelRead {
    //    private static void readExcel() throws Exception {
//        File xlsFile = new File("D:\\QQ下载\\z(1).txt");
//        // 获得工作簿对象
//        Workbook workbook = Workbook.getWorkbook(xlsFile);
//        // 获得所有工作表
//        Sheet[] sheets = workbook.getSheets();
//        // 遍历工作表
//        if (sheets != null) {
//            for (Sheet sheet : sheets) {
//                // 获得行数
//                int rows = sheet.getRows();
//                // 获得列数
//                int cols = sheet.getColumns();
//                // 读取数据
//                for (int row = 0; row < rows; row++) {
//                    for (int col = 0; col < cols; col++) {
//                        Cell cell = sheet.getCell(col, row);
//                        System.out.print(cell.getContents() + " ");
//                    }
//                    System.out.println();
//                }
//            }
//        }
//        workbook.close();
//    }
//    public String getValues(String filePath )
//    {
//        int a=0;
//        String values = null;
//        try{
//            // 创建对Excel工作簿文件的引用
//            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
//            // 创建对工作表的引用。
//            // 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
//            HSSFSheet sheet = workbook.getSheet("Sheet1");
//            // 也可用getSheetAt(int index)按索引引用，
//            // 在Excel文档中，第一张工作表的缺省索引是0，
//            // 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
//            // 读取左上端单元
//            a=sheet.getLastRowNum();
//            System.out.println(a);
//            for(int j=1;j<=a;j++)
//            {
//                HSSFRow row = sheet.getRow(j);
//                System.out.println("-----------------------第"+j+"行数据----------------");
//                for(int i = 0;i<row.getLastCellNum();i++)
//                {
//                    HSSFCell cell = row.getCell(i);
//                    //输出单元内容，cell.getStringCellValue()就是取所在单元的值
//                    values = cell.getStringCellValue();
//                    System.out.println("单元格内容是： " + values);
//                }
//            }
//        }catch(Exception e) {
//            System.out.println("已运行xlRead() : " + e );
//        }
//        return values;
//    }
    public static void main(String args[]) throws IOException {
//        String filePath="E:\\TestPageCjtvPara.xls";
//        FileInputStream xlsFile = new FileInputStream("D:\\QQ下载\\z(1).txt");
//        xlsFile.read();
        String s = "";
        String s1 = "";
        try {
            String encoding = "utf-8";
            File file = new File("D:\\QQ下载\\z(1).txt");
            File file1 = new File("D:\\QQ下载\\v(1).txt");

            if (file1.isFile() && file1.exists()) { //判断文件是否存在
                InputStreamReader read1 = new InputStreamReader(
                        new FileInputStream(file1), encoding);//考虑到编码格式
                BufferedReader bufferedReader1 = new BufferedReader(read1);
                String lineTxt1 = null;
//                InputStream in = new FileInputStream(file1);

                while ((lineTxt1 = bufferedReader1.readLine()) != null) {
//                    System.out.write(tempByte, 0, byteread);
//                    s.append(tempByte,0,byteread);
                    s1 = s1 + lineTxt1;
//                    System.out.println("分===========================隔==============================符");
//                    System.out.println(lineTxt);
                }

            }

            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
//                char[] tempByte = new char[1024];
//                int byteread = 0;
//                InputStream in = new FileInputStream(file);

                while ((lineTxt = bufferedReader.readLine()) != null) {
//                    System.out.write(tempByte, 0, byteread);
//                    s.append(tempByte,0,byteread);
                    s = s + lineTxt;
                    System.out.println("分===========================隔==============================符");
//                    System.out.println(lineTxt);
                }

//                System.out.println(s);
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        String str[] = s.split(",");
        Double []a = new Double[13000];
        for (int i = 0; i < str.length; i++) {
            a[i]=Double.parseDouble(str[i]);
        }
        for (int i = 0; i < str.length; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
//        System.out.println(s1);
        String str1[] = s1.split(",");
        Double []a1 = new Double[13000];
        for (int i = 0; i < str1.length; i++) {
            a1[i]=Double.parseDouble(str1[i]);
        }
        for (int i = 0; i <str1.length; i++) {
            System.out.print(a1[i] + " ");
        }
        System.out.println();
        Butterworth butterworth=new Butterworth();
        double[] d = new double[12000];
        double[] e = new double[12000];
        double[] f = new double[12000];
        try{
            IirFilterCoefficients iirFilterCoefficients;
//            System.out.println("666666666666666666");
            iirFilterCoefficients= IirFilterDesignExstrom.design(FilterPassType.lowpass, 5,10.0/50.0,13.0/50.0);
//            System.out.println("777777777777777777");
            e=iirFilterCoefficients.a;
            f=iirFilterCoefficients.b;
            System.out.println("!!!!!");
            d=butterworth.IIRFilter(a,e,f);
            System.out.println("@@@@@@");
            for(int i=0;i<500;i++){
                System.out.println("$$$d[i]="+d[i]+" a1[i]="+a1[i]);
            }
            gaussion gau=new gaussion();
            List<Integer> list= gau.show(d,a1);
            System.out.println(list.size());
            for(int i=0;i<list.size();i++){
                System.out.print(list.get(i)+" ");
            }
        }catch (Exception e1){
            System.out.println(e1.getMessage());
        }



//        int zheng=0;
//        for(int i=0;i<s.length();i++){
//            if((s[i] >= 'a') && (s[i] <= 'z'))
//        }
//        ExcelRead er = new ExcelRead();
//        er.getValues(filePath);
    }
}

