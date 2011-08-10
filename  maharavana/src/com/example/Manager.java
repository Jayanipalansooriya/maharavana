/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import Catogory.GrammarCategory;
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

            //nirashas
            if(isAquestion(words))
            {
                System.out.println("a question");
                words = getModifiedTokenList(words);
            }
            //
            
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
        
        for (int i = 0; i < words.size(); i++) {
            words.get(i).set_tags(dB_connection.get_tags(words.get(i).get_word()));
        }


//ambe
        GrammarCategory gc= new GrammarCategory();

        if ( gc.isOrPattern((ArrayList<Word>)words.clone()))
              words=gc.checkOrPattern(words);
//
        
        chunker.process_string(words);
    }

    //nirashas
  public static  ArrayList<Word> getModifiedTokenList(ArrayList<Word> list){
        ArrayList<Word> tokenList =list;
        boolean b = false;
        int size =tokenList.size();
        String lastWord = tokenList.get(size-1).get_word();
        String beforeLastWord = tokenList.get(size-2).get_word();
        int lastWordLenght = lastWord.length();
        if (lastWord.endsWith("ද?")) {
                b = true;
                 if (  lastWordLenght == 2) {
                    // tokenList.remove(new Word("ද?"));
                     tokenList.remove(size-1);
                     if (!(beforeLastWord.endsWith("මි") ||beforeLastWord.endsWith("මු") || beforeLastWord.endsWith("යි") ||beforeLastWord.endsWith("හි")||beforeLastWord.endsWith("හු")) ) {
                          tokenList.remove(size-2);
                           tokenList.add(new Word(beforeLastWord+"ය"));
                     }
                }
                 else if( (lastWordLenght > 2) ){
                     String modifiedLastWord = lastWord.substring(0, lastWordLenght-2);
                     System.out.println("modified word"+modifiedLastWord);
                     if (!(modifiedLastWord.endsWith("මි") ||modifiedLastWord.endsWith("මු") || modifiedLastWord.endsWith("යි") ||modifiedLastWord.endsWith("හි")||beforeLastWord.endsWith("හු")) ) {
                          tokenList.remove(size-1);
                          tokenList.add(new Word(modifiedLastWord+"ය"));
                     }
                     else{
                          tokenList.remove(size-1);
                          tokenList.add(new Word(modifiedLastWord));
                     }
//                     tokenList.remove(size-1);
//                     tokenList.add(new Word(modifiedLastWord));
                 }
        }
        else if(lastWord.endsWith("ද")){
            System.out.println("question mark is needed to be the sentence gramaticallly correct");

                 b = true;
                 if (  lastWordLenght == 1) {
//                     tokenList.remove(size-1);
//                     tokenList.add(new Word(tokenList.get(size-2).get_word()+"ය"));
                      tokenList.remove(size-1);
                     if (!(beforeLastWord.endsWith("මි") ||beforeLastWord.endsWith("මු") || beforeLastWord.endsWith("යි") ||beforeLastWord.endsWith("හි")||beforeLastWord.endsWith("හු")) ) {
                            tokenList.remove(size-2);
                           tokenList.add(new Word(beforeLastWord+"ය"));
                     }
                }
                 else if( lastWordLenght > 1){
//                     String modifiedLastWord = lastWord.substring(0, lastWordLenght-2);
//                     tokenList.remove(size-1);
//                     tokenList.add(new Word(modifiedLastWord));
                       String modifiedLastWord = lastWord.substring(0, lastWordLenght-1);
                       System.out.println("modifiedLastWord"+modifiedLastWord);
                     if (!(modifiedLastWord.endsWith("මි") ||modifiedLastWord.endsWith("මු") || modifiedLastWord.endsWith("යි") ||modifiedLastWord.endsWith("හි")||beforeLastWord.endsWith("හු")) ) {
                          tokenList.remove(size-1);
                          tokenList.add(new Word(modifiedLastWord+"ය"));
                     }
                     else{
                          tokenList.remove(size-1);
                          tokenList.add(new Word(modifiedLastWord));
                     }
                 }
        }
        return tokenList;
    }


     public  boolean isAquestion(ArrayList<Word> list){
        boolean b = false;
        int size =list.size();
        String lastWord = list.get(size-1).get_word();
        if (lastWord.endsWith("??") || lastWord.endsWith("?")) {
                b = true;
        }
        return b;
    }

    ///////////////////////

}
