import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    //???
    Map<String, ArrayList<PageEntry>> indexForSearch = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        try {
            if (pdfsDir.isDirectory()) {
                //Читаем файлы из каталога
                for (File file : pdfsDir.listFiles()) {
                    var doc = new PdfDocument(new PdfReader(new FileInputStream(file)));
                    int numberOfPages = doc.getNumberOfPages();
                    var fileName = file.getName();

                    //читаем страницы из файла, переводим текст в low и составляем индекс страницы
                    for (int i = 1; i <= numberOfPages; i++) {
                        PdfPage page = doc.getPage(i);
                        var text = PdfTextExtractor.getTextFromPage(page).toLowerCase();
                        if (text.length() > 0) {
                            var words = text.split("\\P{IsAlphabetic}+");
                            HashMap<String, PageEntry> indexOfPage = new HashMap<>();

                            for (String word : words) {
                                if (indexOfPage.containsKey(word)) {
                                    indexOfPage.get(word).setCount(indexOfPage.get(word).getCount() + 1);
                                } else {
                                    indexOfPage.put(word, new PageEntry(fileName, i, 1));
                                }
                            }
                            // добавляем информацию о словах в индекс каталога
                            for (String wordKey : indexOfPage.keySet()) {
                                ArrayList<PageEntry> p = indexForSearch.get(wordKey);
                                if (p != null) {
                                    p.add(indexOfPage.get(wordKey));
                                } else {
                                    p = new ArrayList<>();// почему не работало без этой строчки?
                                    p.add(indexOfPage.get(wordKey));
                                }
                                indexForSearch.put(wordKey, p);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("Что-то пошло не так");
        }
  //      System.out.printf("Индекс из %s слов составлен\n", indexForSearch.size());
    }

    @Override
    public List<PageEntry> search(String word) {
        if (indexForSearch.get(word.toLowerCase()) != null) {
            return indexForSearch.get(word.toLowerCase()).stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
