import java.io.*;
import java.util.*;

//=====================================================================
class DictEntry3 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; // number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry3() {
        postingList = new HashSet<Integer>();
    }
}

// =====================================================================
class Index3 {

    // --------------------------------------------
    Map<Integer, String> sources; // store the doc_id and the file name
    HashMap<String, DictEntry3> index; // THe inverted index
    Set<Integer> allFiles = new HashSet<Integer>();

    Index3() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry3>();
        allFiles = new HashSet<Integer>();
    }

    // ---------------------------------------------
    public void printPostingList(HashSet<Integer> hset) {
        Iterator<Integer> it2 = hset.iterator();
        while (it2.hasNext()) {
            System.out.print(it2.next() + ", "
                    + "HI");
        }
        System.out.println("");
    }

    public void printDictionary() {
        // Iterator it = index.entrySet().iterator();
        // while (it.hasNext()) {
        // Map.Entry pair = (Map.Entry) it.next();
        // DictEntry3 dd = (DictEntry3) pair.getValue();
        // System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" +
        // dd.term_freq + "> =--> ");
        // //it.remove(); // avoids a ConcurrentModificationException
        // printPostingList(dd.postingList);
        // }
        System.out.println("------------------------------------------------------");
        System.out.println("*****    Number of terms = " + index.size());
        System.out.println("------------------------------------------------------");

    }

    // -----------------------------------------------
    public HashSet<Integer> buildIndex(String[] files) {
        HashSet<Integer> Files = new HashSet<>();
        int num = 0;

        int i = 0;
        for (String fileName : files) {
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                Files.add(num);
                num++;
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry3());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; // set doc freq to the number of doc that contain the term
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        // set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }
                // printDictionary();
            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
        return Files;
    }

    // --------------------------------------------------------------------------
    // query inverted index
    // takes a string of terms as an argument
    public String find(String phrase) {
        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        for (String word : words) {
            res.retainAll(index.get(word).postingList);
        }
        if (res.size() == 0) {
            System.out.println("Not found");
            return "";
        }
        // String result = "Found in: \n";
        for (int num : res) {
            result += "\t" + sources.get(num) + "\n";
        }
        return result;
    }

    // ---------------------------------------------------------------------------
    HashSet<Integer> Union(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();
        ;
        Iterator<Integer> itP1 = pL1.iterator();
        Iterator<Integer> itP2 = pL2.iterator();
        int docId1 = 0, docId2 = 0;
        // INTERSECT ( p1 , p2 )
        // 1 answer ← {}
        // answer =
        // 2 while p1 != NIL and p2 != NIL
        if (itP1.hasNext())
            docId1 = itP1.next();
        if (itP2.hasNext())
            docId2 = itP2.next();

        while (itP2.hasNext() && itP2.hasNext()) {

            // 3 do if docID ( p 1 ) = docID ( p2 )
            // if (docId1 == docId2) {
            // 4 then ADD ( answer, docID ( p1 ))
            // 5 p1 ← next ( p1 )
            // 6 p2 ← next ( p2 )
            answer.add(docId1);
            answer.add(docId2);
            docId1 = itP1.next();
            docId2 = itP2.next();
        }
        answer.add(docId1);
        answer.add(docId2);
        // 7 else if docID ( p1 ) < docID ( p2 )
        // 8 then p1 ← next ( p1 )
        // else if (docId1 < docId2) {
        // if (itP1.hasNext())
        // docId1 = itP1.next();
        // else
        // return answer;

        return answer;
    }

    // ----------------------------------------------------------------------------
    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();
        ;
        // p1 = {0,1}
        // p2={1}
        Iterator<Integer> itP1 = pL1.iterator();
        Iterator<Integer> itP2 = pL2.iterator();
        int docId1 = 0, docId2 = 0;
        // INTERSECT ( p1 , p2 )
        // 1 answer ← {}
        // answer =
        // 2 while p1 != NIL and p2 != NIL
        if (itP1.hasNext())
            docId1 = itP1.next(); // docId1=1
        if (itP2.hasNext())
            docId2 = itP2.next();// docId2=0

        while (itP1.hasNext() && itP2.hasNext()) {

            // 3 do if docID ( p 1 ) = docID ( p2 )
            if (docId1 == docId2) {
                // 4 then ADD ( answer, docID ( p1 ))
                // 5 p1 ← next ( p1 )
                // 6 p2 ← next ( p2 )
                answer.add(docId1);
                docId1 = itP1.next();
                docId2 = itP2.next();
            } // 7 else if docID ( p1 ) < docID ( p2 )
              // 8 then p1 ← next ( p1 )
            else if (docId1 < docId2) {
                if (itP1.hasNext())
                    docId1 = itP1.next();

                else
                    return answer;

            } else {
                // 9 else p2 ← next ( p2 )
                if (itP2.hasNext())
                    docId2 = itP2.next();
                else
                    return answer;
            }
        }
        if (docId1 == docId2) {
            answer.add(docId1);
        }
        // 10 return answer
        return answer;
    }

    // -----------------------------------------------------------------------
    // allfile{1,2,3,4,5}
    // result={}, pl={2,3,5}
    //
    public HashSet<Integer> Not(Set<Integer> s2) {
        HashSet<Integer> result = new HashSet<Integer>();
        HashSet<Integer> temp = new HashSet<Integer>();
        temp.addAll(buildIndex(new String[] {

                "109.txt",
                "100.txt",
                "101.txt",
                "102.txt",
                "103.txt",
                "104.txt",
                "105.txt",
                "106.txt",
                "107.txt",
                "108.txt",
                "500.txt",
                "501.txt",
                "502.txt",
                "503.txt",
                "504.txt",
                "505.txt",
                "506.txt",
                "507.txt",
                "508.txt",
                "509.txt",
                "510.txt",
                "511.txt",
                "512.txt",
                "513.txt",
                "514.txt",
                "515.txt",
                "516.txt",
                "518.txt",
                "519.txt",
                "520.txt",
                "521.txt",
                "522.txt",
                "523.txt",
                "524.txt",
                "525.txt",
                "526.txt",
                "527.txt",
                "1.txt",
                "2.txt",
                "3.txt",
                "4.txt",

        }));
        temp.removeAll(s2);
        for (int i : temp) {
            result.add(i);
        }
        return result;
    }

    // ------------------------------------------------------------------------

    public HashSet<Integer> find_01(String phrase) { // 2 term phrase 2 postingsLists
        HashSet<Integer> result = new HashSet<Integer>();
        String[] words = phrase.split("\\W+");
        // 1- get first posting list
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // printPostingList(pL1);
        // 2- get second posting list
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);
        if (words[2].equalsIgnoreCase("not")) {
            HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[3].toLowerCase()).postingList);
        }
        // printPostingList(pL2);
        // 3- apply the algorithm

        if (words[1].equalsIgnoreCase("and"))
            result = intersect(pL1, pL2);
        else if (words[1].equalsIgnoreCase("or"))
            result = Union(pL1, pL2);
        /*
         * if (words[1].equalsIgnoreCase("not"))
         * result = Not(pL2);
         */

        // System.out.println(answer);
        // printPostingList(answer);
        // result = "Found in: \n";
        // for (int num : answer) {
        // //System.out.println("\t" + sources.get(num));
        // result += "\t" + sources.get(num) + "\n";
        // }
        return result;
    }
    // -----------------------------------------------------------------------

    public String find_02(String phrase) { // 3 lists

        String result = "";
        String[] words = phrase.split("\\W+");
        HashSet<Integer> pL1 = new HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);
        // printPostingList(pL1);
        HashSet<Integer> pL2 = new HashSet<Integer>(index.get(words[1].toLowerCase()).postingList);
        // printPostingList(pL2);
        HashSet<Integer> answer1 = intersect(pL1, pL2);
        // printPostingList(answer1);
        HashSet<Integer> pL3 = new HashSet<Integer>(index.get(words[2].toLowerCase()).postingList);
        // printPostingList(pL3);
        HashSet<Integer> answer2 = intersect(pL3, answer1);
        // printPostingList(answer2);

        // result = "Found in: \n";
        for (int num : answer2) {
            // System.out.println("\t" + sources.get(num));
            result += "\t" + sources.get(num) + "\n";
        }
        return result;

    }
    // -----------------------------------------------------------------------

    public void find_03(String phrase) { // any mumber of terms non-optimized search
        HashSet<Integer> result = new HashSet<Integer>();
        HashSet<String> finalResult = new HashSet<String>();
        finalResult = null;
        result = null;
        String[] words = phrase.split("\\W+");
        int len = words.length;
        // HashSet<Integer> res = new
        // HashSet<Integer>(index.get(words[0].toLowerCase()).postingList);

        /*
         * if (words[0].equalsIgnoreCase("not")) {
         * result = Not(index.get(words[1]).postingList);
         * }
         */
        /*
         * for (int i = 1; i < len; i++) {
         * if (words[i].equalsIgnoreCase("not")) {
         * notFreq[z] = i;
         * // store new posList in not index and will ignore the next index
         * index.get(words[i].toLowerCase()).postingList = Not(index.get(words[i +
         * 1].toLowerCase()).postingList);
         * index.get(words[i + 1].toLowerCase()).postingList.clear();
         * z = z + 1;
         * }
         * }
         */

        for (int i = 1; i < len; i += 2) {
            if (words[i].equalsIgnoreCase("and")) {
                result = intersect(index.get(words[i - 1].toLowerCase()).postingList,
                        index.get(words[i + 1].toLowerCase()).postingList);
            } else if (words[i].equalsIgnoreCase("or")) {
                result = Union(index.get(words[i - 1].toLowerCase()).postingList,
                        index.get(words[i + 1].toLowerCase()).postingList);
            } else if (words[i].equalsIgnoreCase("andnot")) {
                HashSet<Integer> res2 = new HashSet<>();
                result = Not(index.get(words[i + 1].toLowerCase()).postingList);
                result = intersect(result, res2);
            } /*
               * else {
               * System.out.println("Error");
               *
               * }
               */
        }
        /*
         * if (!result.isEmpty())
         * for ( auto i : result)
         * System.out.println("");
         */

        for (int num : result) {
            System.out.println("DocID = " + sources.get(num) + "\n");
        }

    }

}

// =====================================================================
public class InvertedIndex {

    public static void main(String args[]) throws IOException {
        Index3 index = new Index3();
        String phrase;

        /**/
        index.buildIndex(new String[] {
                "109.txt",
                "100.txt",
                "101.txt",
                "102.txt",
                "103.txt",
                "104.txt",
                "105.txt",
                "106.txt",
                "107.txt",
                "108.txt",
                "500.txt",
                "501.txt",
                "502.txt",
                "503.txt",
                "504.txt",
                "505.txt",
                "506.txt",
                "507.txt",
                "508.txt",
                "509.txt",
                "510.txt",
                "511.txt",
                "512.txt",
                "513.txt",
                "514.txt",
                "515.txt",
                "516.txt",
                "518.txt",
                "519.txt",
                "520.txt",
                "521.txt",
                "522.txt",
                "523.txt",
                "524.txt",
                "525.txt",
                "526.txt",
                "527.txt",
                "508.txt",
                "509.txt",
                "1.txt",
                "2.txt",
                "3.txt",
                "4.txt",
                // change it to your path e.g. "c:\\tmp\\100.txt
        });
        /**/

        /**/
        // index.find_01("agile cost");
        // index.compare("and agile"); o1
        // System.out.println(" result = " +index.find_02("different system agile"));
        index.find_03("mariam or cats");
        // System.out.println(index.find_03("not cats"));

        // index.compare("different system should results are in cost and can only
        // computing elements");
        // do {
        // System.out.println("Print search phrase: ");
        // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        // phrase = in.readLine();
        // System.out.println(index.find(phrase));
        // } while (!phrase.isEmpty());
    }
}
