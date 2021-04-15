# PortableDictionaryOSGi
Group project for Software Architecture Assignment 01   
Topic: **PortableDictionary With OSGi**
<pre>
Scenario 01: Dictionary usage  
  Producer: Word Dictionary  
  Consumer: Addnew, edit, getall, remove  
Scenario 02: Translation  
  Producer: Translator + Word Dictionary  
  Consumer: Translation, mapping words  
Scenario 03 (Changed): Simple ChatBot  
  Producer: ChatBotService + Word Dictionary  
  Consumer: ChatBotUser  
</pre>

### Task List:
- [x] WordDictionary
   - [x] English Dictionary
   - [ ] Sinhala Dictionary
   - [x] Dictionary Exception handling
   - [ ] Interaction Terminal for Dictionary inputs
      - [x] English Dictionary handling
      - [ ] Sinhala Dictionary handling 
   - [x] Save words as preferences using gson
- [ ] Sentence Translator
   - [x] English to Sinhala concept
   - [ ] English Word to Sinhala Word
   - [ ] English Sentence to Sinhala Sentence
   - [ ] Save translated words as preferences using gson
   - [ ] Run Experiments 
- [ ] Word Translator
   - [ ] English to Sinhala
   - [x] Interaction Terminal
- [ ] English Language Sentence Checker:Failed DeadEND
- [x] Simple ChatBot
   - [x] Predefine Words and Structures   
   - [x] Save user preferences  
   - [x] Generate Meaningful reply for given input  
   - [x] Multithread  
   - [x] Use Dictionary services  
   - [ ] Experiment, Identify Strengths and Weaknesses 
- [ ] Add Apache Felix framework
- [ ] Add OSGi Activator classes
- [ ] Add Manifest files
- [ ] Generate jar files
- [ ] Create batch script to robocopy classes to {folder}
- [ ] Create batch script to auto generate jar files
### Allocation:  
|Allocation|contributor|
|-|-|
|English Dictionary|[Amanda](https://github.com/amandaaaim)|
|Sinhala Dictionary|[Deshani](https://github.com/DeshaniMAWD)|
|Translator|[Vidura](https://github.com/vidurasathsara99)|
|Simple Chatbot|Me|

### Initial Project Architecture:  
```bash
├───bin
│       gson-2.8.6.jar
└───src
    ├───sentencechecker
    ├───testclasses
    │   ├───translators
    │   │       SpanishWordToFrenchWordTest.java
    │   │
    │   └───worddict
    │           DictionaryTest.java
    │
    ├───translator
    │   │   SpanishWordToFrenchWord.java
    │   │
    │   └───service
    │           SentenceTranslator.java
    │           WordTranslator.java
    │
    └───worddict
        │   FrenchDictionary.java
        │   SpanishDictionary.java
        │
        ├───commons
        │       DictionaryException.java
        │       WordData.java
        │
        └───service
                WordDictionary.java
```
