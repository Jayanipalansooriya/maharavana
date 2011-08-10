/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import com.example.chunked_checker.Chunker;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author hp
 */
public class Manager {

     private Tagger tagger;
     private Messenger my_console;

     private Chunker chunker;

     private DB_connection dB_connection;

     public Manager(){
         my_console = new Messenger("", this);

         dB_connection = new DB_connection(my_console);
         tagger = new Tagger(my_console, dB_connection);
         chunker = new Chunker(my_console,tagger, dB_connection);
     }

     private ArrayList<Word>  get_word_arr_without_srt_end(String input_string){
         //try{
            //StringTokenizer st = new StringTokenizer(input_string,". ",false);
            StringTokenizer st = new StringTokenizer(input_string);

            ArrayList<Word> words=new ArrayList<Word>();

            int i=1;

            while (st.hasMoreTokens())
                {
                        String nxt_tkn = st.nextToken();
                        //words.add(new Word(nxt_tkn, tagger.read_db(nxt_tkn)));
                        //!! as reading form db: tags for nxt_tkn are not added to the word here

                        words.add(new Word(nxt_tkn, i));
                        //out_txt += nxt_tkn + tagger.read_db(nxt_tkn) ;// +"[" + ((ArrayList) hash_table.get(nxt_tkn)).get(0).toString()+"]"+" ";

                        i++;
                }

            return words;
     }

     private ArrayList<Word>  get_word_arr(String input_string){
            ArrayList<Word> words = get_word_arr_without_srt_end(input_string);

            Word srtn_word =  new Word(Word.strt_word, 0);
            srtn_word.add_tag(Word.strt_tag);
            words.add(0, srtn_word);

            Word endn_word =  new Word(Word.end_word, words.size());
            endn_word.add_tag(Word.end_tag);
            words.add(endn_word);

            return words;
     }

    public void proceess_string(String input_string){
        //String out_txt = "";

        ArrayList<Word> words = get_word_arr(input_string);

        tagger.check_grammer_errors(words);
    }

    public void chunk_and_proceess_string(String input_string){
        ArrayList<Word> words = get_word_arr(input_string);

        if(isAquestion(words))
             words = getModifiedTokenList(words);

        for (int i = 0; i < words.size(); i++) {
            words.get(i).set_tags(dB_connection.get_tags(words.get(i).get_word()));
        }


        if (isOrPattern(words))
               words=checkOrPattern(words);

        chunker.process_string(words);
    }

  public  ArrayList<Word> getModifiedTokenList(ArrayList<Word> list){
        ArrayList<Word> tokenList =list;
        boolean b = false;
        int size =tokenList.size();
        String lastWord = tokenList.get(size-1).get_word();
        int lastWordLenght = lastWord.length();
        if (lastWord.endsWith("ද?")) {
                b = true;
                 if (  lastWordLenght == 2) {
                     tokenList.remove(size-1);
                     tokenList.add(new Word(tokenList.get(size-2).get_word()+"ය"));
                }
                 else if( lastWordLenght > 2){
                     String modifiedLastWord = lastWord.substring(0, lastWordLenght-3);
                     tokenList.remove(size-1);
                     tokenList.add(new Word(modifiedLastWord));
                 }
        }
        else if(lastWord.endsWith("ද")){
            System.out.println("question mark is needed to be the sentence gramaticallly correct");

                 b = true;
                 if (  lastWordLenght == 1) {
                     tokenList.remove(size-1);
                     tokenList.add(new Word(tokenList.get(size-2).get_word()+"ය"));
                }
                 else if( lastWordLenght > 1){
                     String modifiedLastWord = lastWord.substring(0, lastWordLenght-2);
                     tokenList.remove(size-1);
                     tokenList.add(new Word(modifiedLastWord));
                 }
        }
        return tokenList;
    }

     public  boolean isAquestion(ArrayList<Word> list){
        boolean b = false;
        int size =list.size();
        String lastWord = list.get(size-1).get_word();
        if (lastWord.endsWith("ද?") || lastWord.endsWith("ද")) {
                b = true;
        }
        return b;
    }

    private boolean isOrPattern(ArrayList<Word> words) {
        ArrayList<Word> wordsWithoutAdjAdv=new ArrayList<Word>();
        int lastOrIndex=0;
        int orCount=0;
       // throw new UnsupportedOperationException("Not yet implemented");
      wordsWithoutAdjAdv= removeAdjAdv(words);
        for (int i=wordsWithoutAdjAdv.size();i>0;i--){
            for(int j=0;j<wordsWithoutAdjAdv.get(i).num_tags();j++){

                if(wordsWithoutAdjAdv.get(i).get_tag(j)=="OR"){
                   if(orCount==0)
                       lastOrIndex=i;


                    orCount++;

            }
            }
            if(orCount*2==(lastOrIndex-1))
                return true;
            else
                return false;

        }

        return false;
    }

    private ArrayList<Word> removeAdjAdv(ArrayList<Word> words) {
      //  throw new UnsupportedOperationException("Not yet implemented");
         for (int i=0;i<words.size();i++){
            for(int j=0;j<words.get(i).num_tags();j++){

                if((words.get(i).get_tag(j)=="JJ") ||(words.get(i).get_tag(j)=="RB") )
                    words.remove(i);


            }

        }
         return words;
    }

    private ArrayList<Word> checkOrPattern(ArrayList<Word> words) {
        ArrayList<Word> wordsWithoutAdjAdv=new ArrayList<Word>();
        ArrayList<Word> filteredWords=new ArrayList<Word>();
        int lastOrIndex=0;
        int orCount=0;
       // throw new UnsupportedOperationException("Not yet implemented");
      wordsWithoutAdjAdv= removeAdjAdv(words);
        for (int i=wordsWithoutAdjAdv.size();i>0;i--){
            for(int j=0;j<wordsWithoutAdjAdv.get(i).num_tags();j++){

                if(wordsWithoutAdjAdv.get(i).get_tag(j)=="OR"){
                   if(orCount==0)
                       lastOrIndex=i;


                    orCount++;

            }
            }
            wordsWithoutAdjAdv.remove(lastOrIndex);
        for (int k=(lastOrIndex-1);k<wordsWithoutAdjAdv.size();k++){
            //filteredWords.add(k, wordsWithoutAdjAdv.get(k));
             filteredWords.add( wordsWithoutAdjAdv.get(k));
        }

    }
return filteredWords;


}
}
