import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by fuyun on 3/03/15.
 */
public class HelloWorldServer {
    public static char result[][]=new char[20][20];

    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {

                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        String request = exchange.getQueryString();
                        if (!request.isEmpty())
                            exchange.getResponseSender().send(PDC(request));
                    }
                }).build();
        server.start();
    }
    public static void decode(int x, int y, String s){
        int n=(int)Math.sqrt((double)s.length()+1.0);
        for (int i=0;i<n;i++)
            result[x][y+i]=s.charAt(i);
        for (int i=1;i<n;i++)
            result[x+i][y+n-1]=s.charAt(i + n - 1);
        for (int i=1;i<n;i++)
            result[x+n][y+n-i]=s.charAt(2*n-2+i);
        for (int i=1;i<n-1;i++)
            result[x+n-i][y+n]=s.charAt(3*n-3+i);
        if (4*n-5>0)
            decode (x+1,y+1,s.substring(4*n-5));
        else return;
    }
    public static String PDC(String requestSentence){
        //C to I
        String[] p=requestSentence.split("=|&");

        String request=p[3];
        decode(0,0,request);
        int n=(int)Math.sqrt((double)request.length()+1.0);

        String s=null;
        for (int i=0;i<n;i++)
            for (int j=0;j<n;j++)
                s+=result[i][j];
        char parts[]=s.toCharArray();

        //char[]tokens=request.toCharArray();
        //System.out.println(tokens.length);
        //char parts[] = {tokens[0],tokens[1],tokens[2],tokens[5],tokens[8],tokens[7],tokens[6],tokens[3],tokens[4]};

        //XY to Y
        BigInteger x = new BigInteger("8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");
        BigInteger xy = new BigInteger(p[1]);
        int y = xy.divide(x).intValue();
        //Y to Z
        int z = 1 + (y % 25);

        //I and Z to M
        char[]array = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        for(int i=0;i<s.length();i++){
            for(int j=0;j<array.length;j++){
                if(parts[i]==array[j]){
                    if(j>z-1){
                        parts[i]=array[j-z];
                        break;
                    }
                    else{
                        parts[i]=array[j+z];
                        break;
                    }
                }
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar gc = GregorianCalendar.getInstance();
        Calendar calendar = new GregorianCalendar();
        TimeZone tz = TimeZone.getTimeZone("EST");
        dateFormat.setTimeZone(tz);
        String response = new String(parts);
        String output = "Wajueji,5706-6361-2189,2725-1307-3825,8597-5462-8868"+"\n"+dateFormat.format(new Date())+"\n"+response;
        return output;
    }
}
