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
        
        for (int i = 0; i < words.size(); i++) {
            words.get(i).set_tags(dB_connection.get_tags(words.get(i).get_word()));
        }
        
        chunker.process_string(words);
    }

}
