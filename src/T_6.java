import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by dragon on 2017/12/28.
 */
public class T_6 {
    public static void main(String[] args) {
        System.out.print(convert("ABCD", 2));
    }

    public static String convert(String s, int numRows) {
        if (s.length()==0  ){
            return "";
        }
        if (numRows==1){
            return s;
        }



        //该字母是每项中的第几项？
        int row = 0;
        int column = 0;
        int item = numRows - 2 + numRows, width = ((s.length() / item) + 1) * (numRows - 1);
        char result[][] = new char[numRows][width];

        char sc[] = s.toCharArray();

        for (int i = 0; i < sc.length; i++) {
            //非正常的竖线列
            if (i % item >= numRows) {
                column += 1;
//                row = row - (item - i % item);
                row--;
                result[row][column] = sc[i];

                if (row == 1) {
                    column++;
                    row--;
                }

                continue;
            }
            result[row][column] = sc[i];
            if (numRows - i % item == 1) {
                continue;
            }
            row += 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < width; j++) {
                if (result[i][j] != '\0') {
                    sb.append(result[i][j]);
                }
                //System.out.print(result[i][j]);
            }
//            System.out.println();
        }


        return sb.toString();
    }

}
