package cn.mrzhqiang.randall.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class JsoupUnitTest {
    public static final SimpleDateFormat DATE_NORMAL = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    public static final SimpleDateFormat DATE_HMS = new SimpleDateFormat("HH-mm-ss_SSS", Locale.getDefault());

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://haowanba.com");
        Document home = Jsoup.parse(url, (int) TimeUnit.SECONDS.toMillis(10));
//        System.out.println(home);
        String title = home.title();
        System.out.println(title);

        Element body = home.body();
        Elements bodyList = body.children();
//        System.out.println(bodyList);
        Element first = bodyList.first();
        createFileByString(title, parseNode(first));
    }

    public static String parseNode(Node node) {
        StringBuilder contentSb = new StringBuilder();
        String nodeName = node.nodeName();

        switch (nodeName) {
            case "p":
                for (Node child : node.childNodes()) {
                    contentSb.append(parseNode(child));
                }
                return contentSb.toString();
            case "a":
                Element a = (Element) node;
                return "[" + a.text() + "]" + "(" + a.absUrl("href") + ")";
            case "img":
                return "![img.png]" + "(" + node.absUrl("src") + ")";
            case "br":
                return "\r\n   \r\n";
            case "#text":
                return node.toString();
        }

        return "";
    }

    public static void createFileByString(String title, String content) throws IOException {
        Date now = new Date();
        File file = new File(".\\wml2md\\" + DATE_NORMAL.format(now));
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file, title + DATE_HMS.format(now) + ".md");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }

}
