Group No: 14
Project Title: Spelling and Grammar Checker for Sinhala.





> Project Proposal.
> Spelling & Grammar Checker for Sinhala.













Supervisors: 	Professor Gihan Dias
> Dr. Sanath Jayasena
Members.
Name
Index number
Akila Gunarathna
070147T
Nirasha Katugampala
070233E
A.G.P Ambegoda
070023K
H.A.D.W Bandara
070044B

Overview.
Sinhala is the main language used in Sri Lanka. With the increased use of Information Technology, the typical Sri Lankans tend to use software up to a significant extent in their day to day operations. A major problem that has occurred was the lack of software support for Sinhala language. Most of the people prefer to do their own work in Sinhala using sophisticated software. Most of the Government Departments still need their documents written in Sinhala. Due to this emerging requirement so many people have contributed to free and open source world by designing Sinhala tools for open source software. Due to the high cost of commercial software most of the Sri Lankans have started use Free and Open Source software. As a result the use of Open Office has been increased. Open office has typing support for Sinhala. Recently some people have developed Sinhala spell checkers for open office. With a careful scrutiny we observed that these spell checkers also need to be developed further to increase their accuracy and efficiency. Still Sinhala language does not have a perfect spell checker.
Other than spell checking, a Grammar checker for Sinhala has become a vital requirement in today's world. There does not exist a Sinhala grammar checker yet. This might be due to the complexity of the Sinhala language as well as due to the difficulty in designing a Grammar checker using the existing technology. All the application Programming Interfaces that have been used for grammar checking support Western languages. So we decided to develop a Sinhala Grammar checker for Open Office platform using one of the existing Application Programming Interfaces.
Our aim in short is to improve the localization support in Open Office.

Problem Definition.
When we are adding a base word to the current Sinhala dictionary tool it will automatically complete the set of words based on the stem and the POS category (noun, verb .. etc).This has been done by using a set of tables which define what should be appended to the word according to the POS category of the base word. Thereby if the corresponding suffix or the prefix that should be added is not defined in that table it will not generate all the words.
Apart from that the addition of suffixes is not accurate. Each and every suffix can not be added to each and every word. It should be meaningful. The existing dictionary generates words which are not meaningful. So we need to improve it up to a significant extent.
However the existing dictionary also needs to be improved further by adding more words related to different fields.
The existing grammar checkers for Open Office are as follows.

1. After the Deadline, a grammar checker for English, with OpenOffice?.org integration
2. Language tool,  a style and grammar checker with OpenOffice?.org integration, for English, French, German, Polish, Dutch, and other languages

The existing grammar checkers do not support Sinhala. This has become a major problem for the users who deal with Sinhala official documents. There is a high probability of making a grammar mistake. So the need of a Sinhala grammar checker has become vital.
The above grammar checkers support mainly the style of grammar where Subject Verb Object order is preserved. Sinhala is different to this, the basic order is Subject Object Verb. So we need to alter the inbuilt rules of the grammar checker we are using for the development.
Above grammar checkers use a program called a Parts Of Speech tagger, which is responsible for identifying the Parts Of speech tag of the word. (Whether it is a noun, verb, adjective etc). It is really difficult to build a POS tagger for Sinhala. Most of the researchers are still conducting their research on this area, but they do not appear to have built a successful POS tagger yet. So it is one of the major challenges we are facing as at now.It is vital to have POS tagger as a supportive tool for the grammar checker. So a major research will have to be done in terms of developing the POS tagger.
Then we need to develop grammar rules and implement them using a suitable API.
Project Objectives
The objective of the project is to enhance the local language support in Open Office. This project mainly consists of 2 main parts.

1. To develop the existing Sinhala spell checker: We need to have a complete Sinhala spell checker. So we would improve one of the existing Sinhala spell checkers. We plan to add more dictionaries for the existing spell checker and to develop more spelling rules for it. We would extend it to have many dictionaries specially designed for advanced Sinhala , simple language(day to day use), IT terminology etc. We are still researching on the possible dictionaries which we can make available with our product.
2. To develop a Sinhala grammar checker: The grammar checker will comprise of a POS tagger and a grammar checker which gets the support from the POS tagger. This would be a new development for open source field and we would be the pioneers in doing a Sinhala Grammar checker.

Our Goal.
Our goal is to be the pioneers in developing a Grammar checker for Sinhala. Since we are going to initiate it, the future generation can improve it further.

Deliverables.

An enhanced version of the existing Sinhala spell checker developed by Mr. Laknath from Faculty Of IT, University Of Moratuwa.
A basic Sinhala Grammar checker with an attached POS tagger.
Scope.
Part 1- Spell checker.
We will be further developing the existing spell checker tool that was developed by Mr. Laknath Buddika. In the existing Sinhala spell checker tool ,there is an option page which contains tables for various parts of speech (currently for verbs, nouns, adverbs, adjectives). These tables define what should be appended to a base word depending on the ending letter sets (condition) after removing some letters (strip) and weather it is a suffix or a prefix (option), in order to get a compound word. The existing set of rules is not a completed set. In order to find additional rules we should learn Sinhala grammar. And we should compare the existing rules set to find new rules that can be added to the spell checker tool.

Part 2
We would develop a simple POS tagger which would identify the POS tag of each word and assign a tag. Then we would develop a Sinhala Grammar checker based on an exisitng Application Programming Interface. This would support only for the basic grammar styles in Sinhala.

Methodology.
We decided to use the spell checker developed by Mr. Laknath for further improvement. He has used java as the programming language and Hunspell the base for the spell checker.
Hunspell is a spell checker and morphological analyzer designed for languages with rich morphology and complex word compounding or character encoding and can use UTF-8 encoded dictionaries. It uses two files to check grammar. The **.dic files contain all the base words of a language with a corresponding set of flags separated using a “/’” form the word. This set of flags will define special flags (prefixes and suffixes and the letters that should be replaced with) that can be found the**.aff file. The two files can be used to create all the words of the language.




Example of Hunspell:
Aff file
SET UTF-8
TRY esianrtolcdugmphbyfvkwzESIANRTOLCDUGMPHBYFVKWZ’
REP 2
REP f ph REP ph f
PFX A Y 1
PFX A 0 re .
SFX B Y 2
SFX B 0 ed [^y]
SFX B y ied y
In the above Aff file there are two affix classes, A and B.
Dic file
hello
try/B
work/AB
Depending on above two file the possible set of words are:
“hello", “try", “tried", “work", “worked", “rework", “reworked".
We will be using “Language Tool” as the Application Programming platform for our development of the grammar checker. It is written in java language. We would study the existing grammar checkers for different languages and study how they had been developed. Then we would try to design the Sinhala grammar checker based on the knowledge we would gain from them.
The language tool works as follows. First it splits a text in to the sentences. Then each sentence is split in to words and a POS tag is attached to each word. Then the analyzed text is matched against a set of built-in rules and against the rules loaded from a particular file. We can define new rules in the grammar.xml file. So we need to use XML technology as well. Rules which cannot be defined through the XML need to be defined using java classes. We need to do changes to the java source code when we need to add new language to the language tool. So we will have to link all these technologies together to make this project a success.

POS tagger development.
AS a part of this project we need develop a Parts Of Speech tagger. It would identify the relevant POS category of the word and will assign that tag to the word. We need to develop a certain set of rules to identify the POS category. According to the research we have done so far we identified the need to develop a mathematical model based on Baye's Theorem. The tag with the highest probability will be assigned to the word. Currently we are researching whether to use Hidden Markov Models as the statistical model for this. The development of a POS tagger involves a significant amount of research in terms of language as well as in computing.
We need to research on the underlying technologies as well as Sinhala language to make the project effective. We were instructed to read some Sinhala grammar books in order to gain a sound understanding of Sinhala Spelling and Grammar.
This is a draft POS tagger.



This is a Tamil POS tagger. It tags the relevent POS tag to each word.







Project Plan.

